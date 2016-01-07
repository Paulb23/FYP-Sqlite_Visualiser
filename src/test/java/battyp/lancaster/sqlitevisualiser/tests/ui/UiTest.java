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

package battyp.lancaster.sqlitevisualiser.tests.ui;

import battyp.lancaster.sqlitevisualiser.controller.MenubarController;
import battyp.lancaster.sqlitevisualiser.model.DefaultModel;
import battyp.lancaster.sqlitevisualiser.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

/**
 * JUnit tests for the User interface
 *
 * @see battyp.lancaster.sqlitevisualiser.model.database.Database
 *
 * @author Paul Batty
 */
public class UiTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        try {
            Model MODEL = new DefaultModel();
            BorderPane root = new BorderPane();
            SplitPane splitPane = new SplitPane();
            splitPane.getItems().add(new Pane());
            splitPane.getItems().add(new Pane());
            splitPane.getItems().add(new Pane());
            splitPane.setDividerPositions(0.0f, 0.6f, 0.9f);
            root.setCenter(splitPane);
            FXMLLoader menubarloader = new FXMLLoader(getClass().getClassLoader().getResource("view/fxml/menubar.fxml"));
            MenubarController menubarController = new MenubarController(MODEL, root, splitPane);
            menubarloader.setController(menubarController);
            BorderPane bar = menubarloader.load();
            root.setTop(bar);
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void TestHeaderTabShowsHeaderFile() {
       click("Table View");
       click("Header");
       exists("#HeaderPage");
    }

    @Test
    public void TestTableViewTabShowTableViewFile() {
        click("Table View");
        exists("#TablePage");
    }

    @Test
    public void TestVisualisationTabShowsVisualisationFile() {
        click("Visualisation");
        exists("#VisualisationPage");
    }

    @Test
    public void TestLogTabShowsLogFile() {
        click("Log");
        exists("#LogPage");
    }
}
