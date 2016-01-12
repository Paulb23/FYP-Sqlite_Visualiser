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

import battyp.lancaster.sqlitevisualiser.model.Model;
import battyp.lancaster.sqlitevisualiser.util.UiUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * <h1> Table View Controller </h1>
 *
 * <p>
 * FXML Controller for the log tab, located in tableview.fxml.
 *
 * @author Paul Batty
 * @since 0.5
 */
public class TableViewController extends Controller {

    @FXML
    private ComboBox tableSelect;

    @FXML
    private TableView tableView;

    @FXML
    private TextArea schemaTextArea;

    /**
     * Constructor.
     *
     * @param model The model that this controller will use.
     */
    public TableViewController(Model model) {
        super(model);
    }

    @FXML
    private void updateView() {
        if (tableSelect.getValue() != null) {
            try {
                tableView.getItems().removeAll(tableView.getItems());
                tableView.getColumns().removeAll(tableView.getColumns());
                schemaTextArea.clear();

                this.model.getSqlExecutor().connect();
                    ResultSet rs = model.getSqlExecutor().executeSql("SELECT * FROM " + tableSelect.getValue());
                        addColumnsToTable(rs);
                        addDataToTable(rs);
                    rs.close();

                    showSchema();
                this.model.getSqlExecutor().disconnect();

            } catch (SQLException e) {
                UiUtil.showExceptionError("Error", "Error getting results!", e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addColumnsToTable(ResultSet rs) throws SQLException {
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            tableView.getColumns().addAll(col);
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
        tableView.setItems(data);
    }

    private void showSchema() throws SQLException {
        DatabaseMetaData databaseMetaData = this.model.getSqlExecutor().getDatabaseMetaData();
        ResultSet rs = model.getSqlExecutor().executeSql("SELECT * FROM sqlite_master WHERE tbl_name='" + tableSelect.getValue() +"'");
        schemaTextArea.appendText(rs.getString("sql"));
    }

    /**
     * {@inheritDoc}
     */
    public void notifyObserver() {
        if (model.isFileOpen()) {
            updateTableDropDown();
        }
    }

    private void updateTableDropDown() {
        try {
            this.model.getSqlExecutor().connect();
            ResultSet rs = model.getSqlExecutor().executeSql("SELECT * FROM sqlite_master WHERE type='table'");
            tableSelect.getItems().removeAll(tableSelect.getItems());
            while(rs.next()) {
                tableSelect.getItems().add(rs.getString("tbl_name"));
            }
            rs.close();
        this.model.getSqlExecutor().disconnect();
        } catch (SQLException e) {
            UiUtil.showExceptionError("Error", "Error getting tables!", e);
        } catch (ClassNotFoundException e) {
            UiUtil.showExceptionError("Error Dialog", "Oooops, Something wrong with the class path", e);
        } catch (FileNotFoundException e) {
            UiUtil.showExceptionError("Error Dialog", "Oooops, Could not find the database", e);
        }
    }
}
