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
import battyp.lancaster.sqlitevisualiser.model.DefaultModel;
import battyp.lancaster.sqlitevisualiser.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * SqliteVisualiser start of the javaFx Application. Make sure to set the model else it will auto load the
 * default set.
 *
 * @author Paul Batty
 */
public class SqliteVisualiser extends Application {

    private static Model MODEL;

    /**
     * Sets the model to use throughout the program
     *
     * @param model The model to use
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

        /*
         inject the controller so we can load it with the model and root pane
          */
        FXMLLoader menubarloader = new FXMLLoader(getClass().getClassLoader().getResource("view/fxml/menubar.fxml"));
        MenubarController menubarController = new MenubarController(MODEL, root);
        menubarloader.setController(menubarController);
        BorderPane bar = menubarloader.load();

        root.setTop(bar);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}