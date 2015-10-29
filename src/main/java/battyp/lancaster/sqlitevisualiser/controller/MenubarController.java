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
import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <h1> Menubar Controller </h1>
 *
 * <p>
 * FXML Controller for the Header information tab, located in menubar.fxml.
 *
 * <p>
 * This is the "master" controller for this application and as such is created
 * at the very start and is injected with the model and root pane of the stage.
 *
 * <p>
 * This class is responsible for ensuring that all tabs / view have access to the
 * model. It does this by creating and injecting the corresponding controllers
 * when the tab / view is pressed / shown. this allows them all to have access
 * to the model so they can get the data needed.
 *
 * <p>
 * In addition to controlling the tab / view switching it as the name suggests
 * controls the menubar containing the open, exit and other such operations.
 *
 * @author Paul Batty
 * @see battyp.lancaster.sqlitevisualiser.app.SqliteVisualiser
 * @see Model
 * @see battyp.lancaster.sqlitevisualiser.model.filewatcher.FileWatcher
 * @since 0.5
 */
public class MenubarController extends Controller {

    private BorderPane root;
    private Controller currentController;

    @FXML
    private Button pauseButton;

    /**
     * Constructor.
     *
     * @param model The model that this controller will use.
     * @param root The root pane of the stage.
     */
    public MenubarController(Model model, BorderPane root) {
        super(model);
        this.root = root;

        /* register with the filewatcher so we get updates when the
        * database is modified.                                  */
        this.model.getFileWatcher().addObserver(this);
    }

    /**
     * Opens a database file.
     *
     * If a error is detected during the process a dialog is displayed to user.
     */
    @FXML
    private void openDatabase() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Database");
            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                this.model.openDatabase(file.getCanonicalPath(), new Database(new BTree<BTreeCell>(), new Metadata()));
                notifyObserver();
            }
        } catch (IOException e) {
            UiUtil.showExceptionError("Error Dialog", "Oooops, Could not read that file!", e);
        } catch (InvalidFileException e) {
            UiUtil.showExceptionError("Error Dialog", "Oooops, That's not a valid database file!", e);
        } catch (ClassNotFoundException e) {
            UiUtil.showExceptionError("Error Dialog", "Oooops, Error in Classpath!", e);
        } catch (SQLException e) {
            UiUtil.showExceptionError("Error Dialog", "Oooops, Could not connect to the database!", e);
        }
    }

    /**
     * Pauses or plays the live updater.
     */
    @FXML
    private void playOrPause() {
        if(this.model.getLiveUpdater().isUpdating()) {
            this.model.getLiveUpdater().stopUpdating();
            pauseButton.setText("Play");
        } else {
            this.model.getLiveUpdater().startUpdating();
            pauseButton.setText("Pause");
        }
    }

    /**
     * Gets the previous database.
     */
    @FXML
    private void getPrevious() {
        if (this.model.isFileOpen()) {
            this.model.getLiveUpdater().previousStep();
            notifyObserver();
        }
    }

    /**
     * Gets the previous database.
     */
    @FXML
    private void getNext() {
        if (this.model.isFileOpen()) {
            this.model.getLiveUpdater().nextStep();
            notifyObserver();
        }
    }

    /**
     * Quits the program safely.
     *
     * Injected at the start to handle the exiting.
     */
    @FXML
    public void exit() {
        this.model.getFileWatcher().terminate();
        System.exit(0);
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
     * {@inheritDoc}
     */
    public void notifyObserver() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currentController.notifyObserver();
            }
        });
    }

    /**
     * Switches the current view or Center pane.
     *
     * @param fxmlPath Path to the FXML file.
     * @param controller The controller responsible for the FXML file.
     */
    private void setCenterPane(String fxmlPath, Controller controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
            loader.setController(controller);
            AnchorPane loadedPane = loader.load();

            root.setCenter(loadedPane);

            this.currentController = controller;
            notifyObserver();
        } catch (IOException e) {
            UiUtil.showExceptionError("Error Dialog", "Oooops, Could not load that tab!", e);
        }
    }
}
