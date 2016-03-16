/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Paul Batty
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

package battyp.lancaster.sqlitevisualiser.view.cells;

import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.view.Cell;
import battyp.lancaster.sqlitevisualiser.view.CellType;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * <h1> TABLE_LEAF Cell </h1>
 *
 * <p>
 * Cell type to represent the data cell, within the Sqlite
 * database.
 *
 * @author Paul Batty
 * @see Cell
 * @since 0.8
 */
public class DataCell extends Cell{

    /**
     * Constructor.
     *
     * @param cell The cell this Cell represents.
     */
    public DataCell(BTreeCell cell) {
        super(cell);

        StackPane pane = new StackPane();
        Rectangle view = new Rectangle( 50,50);
        view.setId("data-cell");
        pane.getChildren().add(view);

        Text label = new Text(String.valueOf(this.cell.pageNumber + 1));
        label.setId("cell-text");

        Bounds rb = view.getBoundsInLocal();
        Bounds t2b = label.getBoundsInLocal();
        double scalex = (rb.getWidth()/t2b.getWidth()) / 2;
        double scaley = rb.getHeight()/t2b.getHeight() / 2;
        label.setScaleX( scalex );
        label.setScaleY( scaley );

        pane.getChildren().add(label);
        setView(pane);
    }
}
