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
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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

    private static final int LEFT_PANE_NUMBER = 0;
    private static final int CENTER_PANE_NUMBER = 1;

    private static final double LEFT_DIVIDER_HIDDEN_VALUE = 0.0;
    private static final double LEFT_DIVIDER_SHOWN_VALUE = 0.2;

    private static final String HEADER_FXML_PATH = "view/fxml/header.fxml";

    private static final String TABLE_VIEW_LEFT_FXML_PATH = "view/fxml/tableviewleftpane.fxml";
    private static final String TABLE_VIEW_CENTER_FXML_PATH = "view/fxml/tableview.fxml";

    private static final String VISUALISATION_LEFT_FXML_PATH = "view/fxml/visulisationleftpane.fxml";
    private static final String VISUALISATION_CENTER_FXML_PATH = "view/fxml/visualisation.fxml";

    private static final String LOG_FXML_PATH = "view/fxml/log.fxml";

    private BorderPane root;
    private SplitPane splitPane;
    private Controller currentController;

    @FXML
    private Button pauseButton;

    @FXML
    private MenuItem playOrPauseMenuItem;

    /**
     * Constructor.
     *
     * @param model The model that this controller will use.
     * @param root The root pane of the stage.
     */
    public MenubarController(Model model, BorderPane root, SplitPane splitPane) {
        super(model);
        this.root = root;
        this.splitPane = splitPane;

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
            pauseButton.setId("playButton");
            playOrPauseMenuItem.setText("Play");
        } else {
            this.model.getLiveUpdater().startUpdating();
            pauseButton.setId("pauseButton");
            playOrPauseMenuItem.setText("Pause");
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
     */
    @FXML
    public void exit() {
        this.model.exitProgram();
    }

    /**
     * switches the center pane to the header view
     */
    @FXML
    private void switchToHeader() {
        clearLeftPane();
        setPane(HEADER_FXML_PATH, new HeaderController(this.model), CENTER_PANE_NUMBER);
    }

    /**
     * switches the center pane to the table view
     */
    @FXML
    private void switchToTableView() {
        TableViewController controller = new TableViewController(this.model);
        setPane(TABLE_VIEW_LEFT_FXML_PATH, controller, LEFT_PANE_NUMBER);
        setPane(TABLE_VIEW_CENTER_FXML_PATH, controller, CENTER_PANE_NUMBER);
    }

    /**
     * switches the center pane to the visualisation view
     */
    @FXML
    private void switchToVisualisation() {
        VisualisationController controller = new VisualisationController(this.model);
        setPane(VISUALISATION_LEFT_FXML_PATH, controller, LEFT_PANE_NUMBER);
        setPane(VISUALISATION_CENTER_FXML_PATH, controller, CENTER_PANE_NUMBER);
    }

    /**
     * switches the center pane to the log view
     */
    @FXML
    private void switchToLog() {
        clearLeftPane();
        setPane(LOG_FXML_PATH, new LogController(this.model), CENTER_PANE_NUMBER);
    }

    /**
     * {@inheritDoc}
     */
    public void notifyObserver() {
        Platform.runLater(() -> currentController.notifyObserver());
    }

    private void setPane(String fxmlPath, Controller controller, int paneNumber) {
        try {
            showPane(loadFXMLFile(fxmlPath, controller), paneNumber);
            this.currentController = controller;
        } catch (IOException e) {
            UiUtil.showExceptionError("Error Dialog", "Oooops, Could not load that tab!", e);
        }
    }

    private Pane loadFXMLFile(String fxmlPath, Controller controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
        loader.setController(controller);
        return loader.load();
    }

    private void showPane(Pane pane, int paneNumber) {
        SplitPane.setResizableWithParent(pane, false);
        double[] divider = this.splitPane.getDividerPositions();
        this.splitPane.getItems().set(paneNumber, pane);
        if (paneNumber == LEFT_PANE_NUMBER) {
            if(divider[LEFT_PANE_NUMBER] <= LEFT_DIVIDER_HIDDEN_VALUE) {
                divider[LEFT_PANE_NUMBER] = LEFT_DIVIDER_SHOWN_VALUE;
            }
            this.splitPane.setDividerPositions(divider);
        }
        this.splitPane.setDividerPositions(divider);
        notifyObserver();
    }

    private void clearLeftPane() {
        Pane pane = new Pane();
        SplitPane.setResizableWithParent(pane, false);
        this.splitPane.getItems().set(LEFT_PANE_NUMBER, pane);
        this.splitPane.getDividers().get(LEFT_PANE_NUMBER).setPosition(LEFT_DIVIDER_HIDDEN_VALUE);
    }
}
