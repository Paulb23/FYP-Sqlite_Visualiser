/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Paul Batty
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package battyp.lancaster.sqlitevisualiser.controller;

import battyp.lancaster.sqlitevisualiser.util.UiUtil;
import battyp.lancaster.sqlitevisualiser.model.Model;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h1> Sql Editor Controller </h1>
 *
 * <p>
 * FXML Controller for the Sql Editor tab, located in sqleditor.fxml.
 *
 * <p>
 * This Class is designed to allow sql to be ran on the currently opened
 * database is does this by interacting with the SqlExecutor class.
 *
 *
 * @author Paul Batty
 * @see battyp.lancaster.sqlitevisualiser.model.sqlexecutor.SqlExecutor
 * @since 0.5
 */
public class SqlEditorController extends Controller {

    @FXML
    private TextArea sqleditor;

    @FXML
    private TextArea sqleditorreturn;

	@FXML
	private TableView sqlResultTable;

    /**
     * Constructor.
     *
     * @param model The model that this controller will use.
     */
    public SqlEditorController(Model model) {
        super(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObserver() {
    }

    @FXML
    private void executeSql() {
        if (this.model.isFileOpen()) {
            if (sqleditor.getText().equals("")) {
                return;
            }
            sqleditorreturn.clear();
            try {
                model.getSqlExecutor().connect();
                sqleditorreturn.setText(performSQl());
            } catch (SQLException e) {
                sqleditorreturn.setText(e.getMessage());
            } catch (ClassNotFoundException e) {
                UiUtil.showExceptionError("Error Dialog", "Oooops, Something wrong with the class path", e);
            } catch (FileNotFoundException e) {
                UiUtil.showExceptionError("Error Dialog", "Oooops, Could not find the database", e);
            }
        }
    }

    private String performSQl() throws SQLException {
        return (sqleditor.getText().startsWith("Select") || sqleditor.getText().startsWith("select")) ? performSelect() : performUpdate();
    }

    private String performSelect() throws SQLException {
		sqlResultTable.getItems().removeAll(sqlResultTable.getItems());
        sqlResultTable.getColumns().removeAll(sqlResultTable.getColumns());
        ResultSet result = model.getSqlExecutor().executeSql(sqleditor.getText());
        String output = "Selected.";

       /* int cols = result.getMetaData().getColumnCount();
        while (result.next()) {
            for (int i = 0; i < cols; i++) {
                output += (result.getString(i + 1) + "\t");
            }
            output += ("\r\n");
        }
		*/
			addColumnsToTable(result);
			addDataToTable(result);
        result.close();
        model.getSqlExecutor().disconnect(); // close connection as only select
        return output;
    }

    private String performUpdate() throws SQLException {
        model.getSqlExecutor().performUpdate(sqleditor.getText());
        this.model.getSqlExecutor().getDatabaseMetaData().getConnection().commit(); // leave connection open as updater will close it
        return "Database updated.\n";
    }


	private void addColumnsToTable(ResultSet rs) throws SQLException {
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue().get(j) == null) {
                        return new SimpleStringProperty("null");
                    }
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            sqlResultTable.getColumns().addAll(col);
        }
    }

    private void addDataToTable(ResultSet rs) throws SQLException  {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }
        sqlResultTable.setItems(data);
    }
}
