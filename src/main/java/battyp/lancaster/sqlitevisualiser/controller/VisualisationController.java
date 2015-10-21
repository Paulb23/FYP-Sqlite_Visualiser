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
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;
import battyp.lancaster.sqlitevisualiser.view.Cell;
import battyp.lancaster.sqlitevisualiser.view.CellType;
import battyp.lancaster.sqlitevisualiser.view.Edge;
import battyp.lancaster.sqlitevisualiser.view.ZoomableScrollPane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for visualisation.fxml
 *
 * @author Paul Batty
 */
public class VisualisationController extends Controller {

    @FXML
    private ZoomableScrollPane zoomablepane;

    /**
     * Creates a new Controller with the model set
     *
     * @param model The model to use
     */
    public VisualisationController(Model model) {
        super(model);
    }

    /**
     * {@inheritDoc}
     */
    public void notifyObserver() {
        if (model.isFileOpen()) {
            Database database = model.getDatabase();

            Pane pane = new Pane();

            BTreeNode<BTreeCell> tree = database.getBTree().getRoot();
            addCell(tree, null, 500, 50, pane);

            zoomablepane.setNodeContent(pane);
        }
    }

    /**
     * Creates cells from a node, and add them to the panel with lines
     *
     * @param node Node to represent
     * @param parent Parent to the node or null for self
     * @param x X pos to draw the node
     * @param y Y pos to draw the node
     * @param pane Pane to attach the node to
     */
    private void addCell(BTreeNode<BTreeCell> node, Cell parent, int x, int y, Pane pane) {
        Cell cell = new Cell(CellType.Default);
        cell.setLayoutX(x);
        cell.setLayoutY(y);
        pane.getChildren().add(cell);

        if (parent != null) {
            Edge edge = new Edge(parent, cell);
            pane.getChildren().add(edge);
        }

        List<BTreeNode<BTreeCell>> children = node.getChildren();

        if (node.getNumberOfChildren() > 0) {
            y += 100;
            int xInc = x;
            x = x / node.getNumberOfChildren();
            System.out.println(x);
            for (BTreeNode<BTreeCell> child : children) {
                addCell(child, cell, x, y, pane);
                x += xInc;
            }
        }
    }
}