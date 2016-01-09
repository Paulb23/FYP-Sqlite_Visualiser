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

package battyp.lancaster.sqlitevisualiser.app;

import battyp.lancaster.sqlitevisualiser.controller.Controller;
import battyp.lancaster.sqlitevisualiser.controller.MenubarController;
import battyp.lancaster.sqlitevisualiser.controller.SqlEditorController;
import battyp.lancaster.sqlitevisualiser.model.DefaultModel;
import battyp.lancaster.sqlitevisualiser.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * <h1> Sqlite Visualiser </h1>
 *
 * <p>
 * This class is the start of the JavaFx application.
 * It is designed to set up the front end of the application using the Model
 * created at the start.  If this model is non-existent the application will
 * use load the default model.
 *
 * <p>
 * This currently works by injecting a "master" like Controller into the
 * stage.
 * This controller will then inject the different views controller, in order to
 * provide the model, across the various views without having, to put
 * the model into global space.
 *
 * <p>
 * <b>NOTE:<b/> Make sure you have initialised the Model before starting the application.
 *
 * <p>
 * <b>NOTE:<b/> The "Master" controller for this application is MenubarController.
 *
 * @author Paul Batty
 * @see SqliteVisualisationApp
 * @see Model
 * @see battyp.lancaster.sqlitevisualiser.controller.Controller
 * @see MenubarController
 * @since 0.5
 */
public class SqliteVisualiser extends Application {

    private static final String FRAME_TITLE = "Sqlite Database Visualiser";

    private static final String ICON_16_PATH = "view/images/icons/icon16.png";
    private static final String ICON_24_PATH = "view/images/icons/icon24.png";
    private static final String ICON_32_PATH = "view/images/icons/icon32.png";
    private static final String ICON_64_PATH = "view/images/icons/icon64.png";
    private static final String ICON_128_PATH = "view/images/icons/icon128.png";

    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    private static final String MENUBAR_FXML_PATH = "view/fxml/menubar.fxml";
    private static final String SQLEXECUTOR_FXML_PATH = "view/fxml/sqleditor.fxml";

    private static final String CSS_PATH = "view/css/darktheme.css";

    private static final double LEFT_PANE_PERCENTAGE = 0.0f;
    private static final double CENTER_PANE_PERCENTAGE = 0.8f;
    private static final double RIGHT_PANE_PERCENTAGE = 1.0f;

    private static final int SQLEXECUTOR_PANE_NUMBER = 2;

    /* Has to be static else javafx wont be able to use it */
    private static Model MODEL;

    /**
     * Sets the Model to use throughout the program.
     * Make sure this is set before starting the javaFx Thread.
     *
     * <p>
     * This has to be static to allow it to be set before the application is started.
     *
     * @param model The model to use use when the application is launched.
     */
    protected static void setModel(Model model) {
        MODEL = model;
    }

    /**
     * Constructor.
     */
    public SqliteVisualiser() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        makeSureWeHaveAModel();
        setUpStage(primaryStage);
        primaryStage.show();
    }

    private void makeSureWeHaveAModel() {
        if (MODEL == null) {
            MODEL = new DefaultModel();
        }
    }

    public void setUpStage(Stage stage) throws IOException {
        stage.setOnCloseRequest(event -> MODEL.exitProgram()); /* Make sure we use a custom close to exit cleanly */
        stage.setTitle(FRAME_TITLE);
        loadIcons(stage);
        stage.setScene(createScene());
    }

    @SuppressWarnings("ConstantConditions")
    private void loadIcons(Stage stage) {
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource(ICON_16_PATH).toExternalForm()));
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource(ICON_24_PATH).toExternalForm()));
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource(ICON_32_PATH).toExternalForm()));
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource(ICON_64_PATH).toExternalForm()));
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource(ICON_128_PATH).toExternalForm()));
    }

    @SuppressWarnings("ConstantConditions")
    private Scene createScene() throws IOException {
        Scene scene = new Scene(createPanes(), FRAME_WIDTH, FRAME_HEIGHT);
        scene.getStylesheets().add(getClass().getClassLoader().getResource(CSS_PATH).toExternalForm());
        return scene;
    }

    private BorderPane createPanes() throws IOException {
        SplitPane splitPane = createSplitPanes();
        BorderPane root = new BorderPane();

        root.setTop(loadMenuBar(root, splitPane));
        root.setCenter(splitPane);

        Pane sqlExecutorPane = loadSqlExecutorPane();
        SplitPane.setResizableWithParent(sqlExecutorPane, false);
        splitPane.getItems().set(SQLEXECUTOR_PANE_NUMBER, sqlExecutorPane);

        return root;
    }

    private SplitPane createSplitPanes() {
        SplitPane splitPane = new SplitPane(new Pane(), new Pane(), new Pane());
        splitPane.setDividerPositions(LEFT_PANE_PERCENTAGE, CENTER_PANE_PERCENTAGE, RIGHT_PANE_PERCENTAGE);
        return splitPane;
    }

    private Pane loadMenuBar(BorderPane root, SplitPane splitPane) throws IOException {
        return loadFxmlAndController(new MenubarController(MODEL, root, splitPane), MENUBAR_FXML_PATH);
    }

    private Pane loadSqlExecutorPane() throws IOException {
        return loadFxmlAndController(new SqlEditorController(MODEL), SQLEXECUTOR_FXML_PATH);
    }

    private Pane loadFxmlAndController(Controller controller, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
        loader.setController(controller);
        return loader.load();
    }
}