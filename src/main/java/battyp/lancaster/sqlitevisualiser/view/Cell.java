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

package battyp.lancaster.sqlitevisualiser.view;

import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1> Cell </h1>
 *
 * <p>
 * This class is used to visualise the different nodes within the database.
 *
 * thanks to: https://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx
 *
 * @author Paul Batty
 * @since 0.7
 */
public class Cell extends Pane {

    /**
     * Cell this pane represents.
     */
    public final BTreeCell cell;

    private String cellId;

    private List<Cell> children = new ArrayList<>();
    private List<Cell> parents = new ArrayList<>();

    private Node view;

    /**
     * Constructor.
     *
     * NOTE: Internal use only.
     *
     * @param id The id of this cell
     */
    public Cell(String id) {
        this.cell = new BTreeCell(0, 0, 0);
        this.cellId = id;
    }

    /**
     * Constructor.
     *
     * @param cell The cell this Cell represents.
     */
    public Cell(BTreeCell cell) {
        this.cell = cell;
        this.cellId = String.valueOf(cell.pageNumber);
    }

    /**
     * Adds a outline border to the the cell.
     */
    public void highlight() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.YELLOW);
        borderGlow.setSpread(0.7);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setRadius(125);
        borderGlow.setHeight(125);
        borderGlow.setWidth(125);
        this.setEffect(borderGlow);
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {
        this.view = view;
        getChildren().add(view);
    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return this.cellId;
    }
}
