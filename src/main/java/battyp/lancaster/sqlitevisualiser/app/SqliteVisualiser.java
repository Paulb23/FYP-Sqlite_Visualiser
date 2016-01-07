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

import battyp.lancaster.sqlitevisualiser.controller.MenubarController;
import battyp.lancaster.sqlitevisualiser.controller.SqlEditorController;
import battyp.lancaster.sqlitevisualiser.model.DefaultModel;
import battyp.lancaster.sqlitevisualiser.model.Model;
import battyp.lancaster.sqlitevisualiser.util.FileUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
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
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        if (MODEL == null) {
            MODEL = new DefaultModel();
        }
        BorderPane root = new BorderPane();
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().add(new Pane());
        splitPane.getItems().add(new Pane());
        splitPane.getItems().add(new Pane());
        splitPane.setDividerPositions(0.0f, 0.1f, 0.1f);
        root.setCenter(splitPane);


        /* Load and inject the "master "controller so we can load it with the model and root pane */
        FXMLLoader menubarloader = new FXMLLoader(getClass().getClassLoader().getResource("view/fxml/menubar.fxml"));
        MenubarController menubarController = new MenubarController(MODEL, root, splitPane);
        menubarloader.setController(menubarController);
        BorderPane bar = menubarloader.load();
        root.setTop(bar);

        /* Load and inject the sqlite executor controller */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/fxml/sqleditor.fxml"));
        loader.setController(new SqlEditorController(MODEL));
        AnchorPane loadedPane = loader.load();
        splitPane.getItems().set(2, loadedPane);

        /* Make sure we use a custom close to exit cleanly */
        primaryStage.setOnCloseRequest(event -> MODEL.exitProgram());

        /* Should move resolution to options / config. */
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("view/css/darktheme.css").toExternalForm());
        primaryStage.setTitle("Sqlite Database Visualiser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}