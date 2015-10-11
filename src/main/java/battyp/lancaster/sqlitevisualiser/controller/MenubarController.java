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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for menubar.fxml view, injected via start
 * Controls the menu bar and tab switching, and injects the corresponding controllers
 * so they can have a shared model.
 *
 * @author Paul Batty
 */
public class MenubarController {

    private Model model;
    private BorderPane root;

    /**
     * Creates a new MenubarController
     *
     * @param model The shared model
     * @param root The root pane to switch in and out of
     */
    public MenubarController(Model model, BorderPane root) {
        this.model = model;
        this.root = root;
    }

    /**
     * switches the center pane to the header view
     */
    @FXML
    private void switchToHeader() {
        setCenterPane("view/fxml/header.fxml");
    }

    /**
     * switches the center pane to the table view
     */
    @FXML
    private void switchToTableView() {
        setCenterPane("view/fxml/tableview.fxml");
    }

    /**
     * switches the center pane to the visualisation view
     */
    @FXML
    private void switchToVisualisation() {
        setCenterPane("view/fxml/visualisation.fxml");
    }

    /**
     * switches the center pane to the log view
     */
    @FXML
    private void switchToLog() {
        setCenterPane("view/fxml/log.fxml");
    }

    /**
     * switches the center pane to the sql editor view
     */
    @FXML
    private void switchToSqlEditor() {
        setCenterPane("view/fxml/sqleditor.fxml");
    }

    /**
     * Switches to the pane, to the fxml file passed in
     *
     * @param fxmlPath path to the fxml file
     */
    private void setCenterPane(String fxmlPath) {
        try {
            URL pane = getClass().getClassLoader().getResource(fxmlPath);
            AnchorPane loadedPane = FXMLLoader.load(pane);

            root.setCenter(loadedPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
