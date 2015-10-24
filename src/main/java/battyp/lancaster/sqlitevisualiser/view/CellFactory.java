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

import battyp.lancaster.sqlitevisualiser.view.Cells.*;

/**
 * Factory for creating view cells
 *
 * @author Paul Batty
 */
public class CellFactory {

    /**
     * Creates a new cell factory
     */
    public CellFactory(){
    }

    /**
     * Creates a new Cell
     *
     * @param type Type of the cell
     * @param label Text to pout on the cell
     *
     * @return Cell of the corresponding type.
     */
    public Cell createCell(CellType type, String label) {
        switch(type) {
            case Default: {
                return new DefaultCell(type, label);
            }
            case Table: {
                return new TableCell(type, label);
            }
            case Data: {
                return new DataCell(type, label);
            }
            case Table_Pointer_Internal: {
                return new TablePointerInternal(type, label);
            }
            case Index_Leaf: {
                return new IndexLeaf(type, label);
            }
            case Index_Pointer_Internal: {
                return new IndexPointerInternal(type, label);
            }
        }
        return new DefaultCell(type, label);
    }
}
