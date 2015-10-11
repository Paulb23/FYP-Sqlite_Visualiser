package battyp.lancaster.sqlitevisualiser.tests.ui;

import battyp.lancaster.sqlitevisualiser.controller.MenubarController;
import battyp.lancaster.sqlitevisualiser.model.DefaultModel;
import battyp.lancaster.sqlitevisualiser.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

public class UiTest extends GuiTest {

    @Override
    protected Parent getRootNode() {

        try {
            Model MODEL = new DefaultModel();
            BorderPane root = new BorderPane();
            FXMLLoader menubarloader = new FXMLLoader(getClass().getClassLoader().getResource("view/fxml/menubar.fxml"));
            MenubarController menubarController = new MenubarController(MODEL, root);
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

    @Test
    public void TestSqlEditorTabShowsSqlEditorFile() {
        click("Sql editor");
        exists("#SqlEditorPage");
    }
}
