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

package battyp.lancaster.sqlitevisualiser.view.cells;


import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.view.Cell;
import battyp.lancaster.sqlitevisualiser.view.CellType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <h1> Index Pointer Internal Cell </h1>
 *
 * <p>
 * Cell type to represent the internal index pointer cell, within the Sqlite
 * database.
 *
 * @author Paul Batty
 * @see Cell
 * @since 0.8
 */
public class IndexPointerInternalCell extends Cell {

    /**
     * Constructor.
     *
     * @param cell The cell this Cell represents.
     */
    public IndexPointerInternalCell(BTreeCell cell) {
        super(cell);

        Rectangle view = new Rectangle( 50,50);
        view.setStroke(Color.DARKSLATEBLUE);
        view.setFill(Color.DARKSLATEBLUE);

        getChildren().add(view);
        getChildren().add(new Label("Page: " + ((cell.pageNumber + 1))));
    }
}
