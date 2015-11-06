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
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;
import battyp.lancaster.sqlitevisualiser.view.Cell;
import battyp.lancaster.sqlitevisualiser.view.CellFactory;
import battyp.lancaster.sqlitevisualiser.view.Edge;
import battyp.lancaster.sqlitevisualiser.view.ZoomableScrollPane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * <h1> Visualisation Controller </h1>
 *
 * <p>
 * FXML Controller for the log tab, located in visualisation.fxml.
 *
 * <p>
 * The visualisation of the internal structure of a SqliteDatabase, using nodes
 * on a scrollable pane connected via lines, to referent the different types
 * of cells in the database.
 *
 * <p>
 * Uses a cell factory to create the correct node types, that are set when parsed
 * via the DatabaseParser. It also uses tree structure also produced when the
 * file was parsed.
 *
 * @author Paul Batty
 * @see ZoomableScrollPane
 * @see CellFactory
 * @see Cell
 * @see Edge
 * @see battyp.lancaster.sqlitevisualiser.model.datastructures.BTree
 * @see BTreeNode
 * @see BTreeCell
 * @since 0.5
 */
public class VisualisationController extends Controller {

    @FXML
    private ZoomableScrollPane zoomablepane;

    @FXML
    private Label cellDataPage;

    @FXML
    private Label cellDataCount;

    @FXML
    private ComboBox cellDataCombo;

    @FXML
    private TextArea cellDataData;

    private CellFactory cellFactory;

    private Cell selectedCell;

    /**
     * Constructor.
     *
     * @param model The model that this controller will use.
     */
    public VisualisationController(Model model) {
        super(model);
        cellFactory = new CellFactory();
    }

    /**
     * {@inheritDoc}
     */
    public void notifyObserver() {
        if (model.isFileOpen()) {
            zoomablepane.clear();

            Database database = model.getDatabase();

            Pane pane = new Pane();

            BTreeNode<BTreeCell> tree = database.getBTree().getRoot();
            addCell(tree, null, 50, 50, pane);

            //pane.setRotate(90);
            //pane.setScaleY(-1);
            zoomablepane.setNodeContent(pane);
        }
    }

    /**
     * Creates the visual structure of the btree and attached them to a pane.
     *
     * <p>
     * Uses recursion to illiterate over the node located in the tree, Therefor
     * the first call should always be with the root node. All children nodes
     * will then be added.
     *
     * TODO: Fix Drawing of tree all over the place.
     * TODO: Try drawing from bottom up
     * TODO: add action listeners for more details
     * TODO: add glow effect on change to previous b-tree
     *
     * @param node Node to represent
     * @param parent Parent to the node or null for self.
     * @param x X pos to draw the node.
     * @param y Y pos to draw the node.
     * @param pane Pane to attach the node to.
     */
    private void addCell(BTreeNode<BTreeCell> node, Cell parent, int x, int y, Pane pane) {
        Cell cell = cellFactory.createCell(node.getData().type, node.getData());

        cell.setLayoutX(x + 150);
        cell.setLayoutY(y);
        cell.setOnMouseClicked(event -> {
            Cell cell1 = (Cell)event.getSource();
            showData(cell1);
        });

        if (parent != null) {
            Edge edge = new Edge(parent, cell);
            pane.getChildren().add(edge);
        }
        pane.getChildren().add(cell);

        List<BTreeNode<BTreeCell>> children = node.getChildren();

        y+=150;
        if (node.getNumberOfChildren() > 0) {
            for (BTreeNode<BTreeCell> child : children) {
                if (pane.getChildren().size() > 0) {
                    Node previousAddedNode = pane.getChildren().get(pane.getChildren().size() - 1);
                    x = (int) previousAddedNode.getLayoutX();
                }
                addCell(child, cell, x, y, pane);
                x++;
            }
        }
    }

    /**
     * Shows the Clicked cell data to the user.
     *
     * @param cell cell to show.
     */
    private void showData(Cell cell) {
        cellDataPage.setText("Page Number: " + cell.cell.pageNumber);
        cellDataCount.setText("Cell Count: " + cell.cell.cellCount);
        int cellCount =  cell.cell.cellCount;
        cellDataCombo.getItems().removeAll(cellDataCombo.getItems());
        for (int i = 0; i < cellCount; i++) {
            cellDataCombo.getItems().add(i);
        }
        cellDataCombo.getItems().add("All");
        cellDataData.clear();
        this.selectedCell = cell;
    }

    /**
     * Called when the combo is changed
     */
    @FXML
    private void updateCellData() {
        if (cellDataCombo.getValue() != null) {
            cellDataData.clear();
            String selected = cellDataCombo.getValue().toString();
            if (selected.equals("All")) {
                int cellCount =  selectedCell.cell.cellCount - 1;
                for (int i = 0; i < cellCount; i++) {
                    cellDataData.appendText(selectedCell.cell.data[cellCount] + "\n");
                }
            } else {
                cellDataData.setText(selectedCell.cell.data[Integer.parseInt(selected)]);
            }
        }
    }
}