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
import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for menubar.fxml view, injected via start
 * Controls the menu bar and tab switching, and injects the corresponding controllers
 * so they can have a shared model.
 *
 * @author Paul Batty
 */
public class MenubarController extends Controller {

    private BorderPane root;

    /**
     * Creates a new MenubarController
     *
     * @param model The shared model
     * @param root The root pane to switch in and out of
     */
    public MenubarController(Model model, BorderPane root) {
        super(model);
        this.root = root;
    }

    /**
     * switches the center pane to the header view
     */
    @FXML
    private void switchToHeader() {
        setCenterPane("view/fxml/header.fxml", new HeaderController(this.model));
    }

    /**
     * switches the center pane to the table view
     */
    @FXML
    private void switchToTableView() {
        setCenterPane("view/fxml/tableview.fxml", new TableViewController(this.model));
    }

    /**
     * switches the center pane to the visualisation view
     */
    @FXML
    private void switchToVisualisation() {
        setCenterPane("view/fxml/visualisation.fxml", new VisualisationController(this.model));
    }

    /**
     * switches the center pane to the log view
     */
    @FXML
    private void switchToLog() {
        setCenterPane("view/fxml/log.fxml", new LogController(this.model));
    }

    /**
     * switches the center pane to the sql editor view
     */
    @FXML
    private void switchToSqlEditor() {
        setCenterPane("view/fxml/sqleditor.fxml", new SqlEditorController(this.model));
    }

    /**
     * Opens a database
     */
    @FXML
    private void openDatabase() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Database");
            String path = fileChooser.showOpenDialog(null).getCanonicalPath();

            this.model.openDatabase(path, new Database(new BTree<String>(), new Metadata()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Oooops, Could not read that file!");
            alert.showAndWait();
        } catch (InvalidFileException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Oooops, That's not a valid database file");
            alert.showAndWait();
        }
    }

    /**
     * Switches to the pane, to the fxml file passed in
     *
     * @param fxmlPath path to the fxml file
     */
    private void setCenterPane(String fxmlPath, Controller controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
            loader.setController(controller);
            AnchorPane loadedPane = loader.load();

            root.setCenter(loadedPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
