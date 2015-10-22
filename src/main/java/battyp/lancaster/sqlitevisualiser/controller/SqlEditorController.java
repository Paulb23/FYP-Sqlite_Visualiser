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

import battyp.lancaster.sqlitevisualiser.Util.UiUtil;
import battyp.lancaster.sqlitevisualiser.model.Model;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for sqleditor.fxml
 *
 * @author Paul Batty
 */
public class SqlEditorController extends Controller {

    /**
     * To keep statement when tab switching
     */
    private static String saved_statment;

    /**
     * To keep result when tab switching
     */
    private static String saved_result;

    @FXML
    private TextArea sqleditor;

    @FXML
    private TextArea sqleditorreturn;

    /**
     * Creates a new Controller with the model set
     *
     * @param model The model to use
     */
    public SqlEditorController(Model model) {
        super(model);
    }

    /**
     * {@inheritDoc}
     */
    public void notifyObserver() {
        if (sqleditor.getText().equals("")) {
            if (saved_statment != null) {
                sqleditor.appendText(saved_statment);
            }
        }

        if (sqleditorreturn.getText().equals("")) {
            if (saved_result != null) {
                sqleditorreturn.appendText(saved_result);
            }
        }
    }

    @FXML
    private void save() {
        saved_statment = sqleditor.getText();
    }

    @FXML
    private void executeSql() {
        sqleditorreturn.clear();
        try {
            ResultSet result = model.getSqlExecutor().executeSql(sqleditor.getText());
            int cols = result.getMetaData().getColumnCount();
            while (result.next()) {
                for (int i = 1; i < cols; i++) {
                    sqleditorreturn.appendText(result.getString(i) + "\t");
                }
                sqleditorreturn.appendText("\r\n");
            }
            saved_result = sqleditorreturn.getText();
        } catch (SQLException e) {
            UiUtil.showExceptionError("Error Dialog",  "Oooops, Something went wring with that statement", e);
        }
    }
}
