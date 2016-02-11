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
import battyp.lancaster.sqlitevisualiser.view.*;
import battyp.lancaster.sqlitevisualiser.view.Cell;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private BorderPane visPane;

    @FXML
    private Label cellDataPage;

    @FXML
    private Label cellDataCount;

    @FXML
    private Label cellDataType;

    @FXML
    private Label cellRightPointer;

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
            Database database = model.getDatabase();

            VisualisationGraph graph = new VisualisationGraph();
            VisualisationModel model = graph.getModel();

            ZoomableScrollPane zoomPane = (ZoomableScrollPane) visPane.getCenter();
            double hvalue = 0;
            double vvalue = 0;
            double zoom = 0.2;
            if (zoomPane != null) {
                hvalue = zoomPane.getHvalue();
                vvalue = zoomPane.getVvalue();
                zoom = zoomPane.getScaleValue();
            }

            visPane.setCenter(graph.getScrollPane());
            graph.getScrollPane().setHvalue(hvalue);
            graph.getScrollPane().setVvalue(vvalue);

            graph.beginUpdate();

            BTreeNode<BTreeCell> tree = database.getBTree().getRoot();
            addCell(tree, null, 1000, model);


            graph.endUpdate();

            List<Cell> cells = graph.getModel().getAllCells();
            Collections.reverse(cells);
            double x;
            double lowestY = 0;
            double leafX = 1000;
            for (Cell cell : cells) {
                List<Cell> children = cell.getCellChildren();
                double y = cell.getLayoutY();
                if (y > lowestY) {
                    lowestY = y;
                }

                if (children.size() > 0) {
                    if (children.size() == 1) {
                        x = children.get(0).getLayoutX();
                    } else {
                        x =  ((children.get(0).getLayoutX() + children.get(children.size() - 1).getLayoutX()) / 2);
                    }
                    cell.relocate(x, y);
                } else {
                    cell.relocate(leafX, y);
                    leafX += 150;
                }
            }
            graph.getScrollPane().zoomTo(zoom);

            Pane topLeftPane = new Pane();
            topLeftPane.relocate(0, 0);

            Pane bottomRightPane = new Pane();
            bottomRightPane.relocate(leafX + 1000, lowestY + 1000);
            graph.getCellLayer().getChildren().add(topLeftPane);
            graph.getCellLayer().getChildren().add(bottomRightPane);
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
     * @param node Node to represent
     * @param parent Parent to the node or null for self.
     * @param y Y pos to draw the node.
     * @param model Pane to attach the node to.
     */
    private void addCell(BTreeNode<BTreeCell> node, Cell parent, int y, VisualisationModel model) {
        Cell cell = cellFactory.createCell(node.getData().type, node.getData());

        cell.setLayoutY(y);
        cell.setOnMouseClicked(event -> {
            Cell cell1 = (Cell)event.getSource();
            showData(cell1);
        });

        model.addCell(cell);
        if (parent != null) {
            model.addEdge(parent.getCellId(), cell.getCellId());
        }

        List<BTreeNode<BTreeCell>> children = node.getChildren();

        y+=150;
        if (node.getNumberOfChildren() > 0) {
            for (BTreeNode<BTreeCell> child : children) {
                addCell(child, cell, y, model);
            }
        }
    }

    /**
     * Shows the Clicked cell data to the user.
     *
     * @param cell cell to show.
     */
    private void showData(Cell cell) {
        cellDataPage.setText("Page Number: " + (cell.cell.pageNumber + 1));
        cellDataCount.setText("Cell Count: " + cell.cell.cellCount);
        cellDataType.setText("Cell Type: " + cell.cell.cellType);
        cellRightPointer.setText("Right Pointer: " + cell.cell.rightChildPointer);
        int cellCount =  cell.cell.cellCount;
        cellDataCombo.getItems().removeAll(cellDataCombo.getItems());
        cellDataCombo.getItems().add("All");
        for (int i = 0; i < cellCount; i++) {
            cellDataCombo.getItems().add(i);
        }
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
            BTreeCell cell = selectedCell.cell;
            String selected = cellDataCombo.getValue().toString();
            if (selected.equals("All")) {
                int cellCount =  selectedCell.cell.cellCount;
                for (int i = 0; i < cellCount; i++) {
                    cellDataData.appendText("\n-----------" +
                                    "\nCell: " + i +
                                    "\nLeft Child pointer: " + cell.leftChildPointers[i] +
                                    "\nRow Id: " + cell.rowId[i] +
                                    "\nPayload Size: " + cell.payLoadSize[i] +
                                    "\nData: " + cell.data[i]
                    );
                }
            } else {
                int numSelected = Integer.parseInt(selected);
                cellDataData.appendText("\n-----------" +
                        "\nCell: " + numSelected +
                        "\nLeft Child pointer: " + cell.leftChildPointers[numSelected] +
                        "\nRow Id: " + cell.rowId[numSelected] +
                        "\nPayload Size: " + cell.payLoadSize[numSelected] +
                        "\nData: " + cell.data[numSelected]
                );
            }
        }
    }
}