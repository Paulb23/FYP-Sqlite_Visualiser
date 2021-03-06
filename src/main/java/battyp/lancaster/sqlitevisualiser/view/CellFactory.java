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

package battyp.lancaster.sqlitevisualiser.view;

import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.view.cells.*;

/**
 * <h1> Cell factory </h1>
 *
 * <p>
 * This class produces the corresponding cell types, based on
 * the CellType enum.
 *
 * @author Paul Batty
 * @see Cell
 * @see CellType
 * @since 0.8
 */
public class CellFactory {

    /**
     * Creates a new Cell.
     *
     * @param type Type of the cell.
     * @param cell The cell this Cell represents.
     *
     * @return Cell of the corresponding type.
     */
    public Cell createCell(CellType type, BTreeCell cell) {
        Cell cellReturn = new DefaultCell(cell);
        switch(type) {
            case Default: {
                cellReturn = new DefaultCell(cell);
                break;
            }
            case Table: {
                cellReturn = new TableCell(cell);
                break;
            }
            case TABLE_LEAF: {
                cellReturn = new DataCell(cell);
                break;
            }
            case Table_Pointer_Internal: {
                cellReturn = new TablePointerInternalCell(cell);
                break;
            }
            case Index_Leaf: {
                cellReturn = new IndexLeafCell(cell);
                break;
            }
            case Index_Pointer_Internal: {
                cellReturn = new IndexPointerInternalCell(cell);
                break;
            }
        }
        if (cell.changed) {
           cellReturn.highlight();
        }
        return cellReturn;
    }
}
