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

package battyp.lancaster.sqlitevisualiser.model.datastructures;

import battyp.lancaster.sqlitevisualiser.view.CellType;

/**
 * <h1> B-tree Cell </h1>
 *
 * <p>
 * This class is a data structure for holing information about
 * each cell in the database.  All fields are pubic as this
 * contains no implementation as is meant to be modified.
 *
 * @author Paul Batty
 * @since 0.6
 */
public class BTreeCell {

    /**
     * Cell type should be from the constants.
     * Internal use?
     *
     * @see SqliteConstants
     */
    public final int cellType;

    /**
     * The type of cell.
     */
    public CellType type;

    /**
     * Page number of the cells.
     */
    public final long pageNumber;

    /**
     * Number of cells.
     */
    public final int cellCount;

    /**
     * The page numbers.
     */
    public int[] leftChildPointers;

    /**
     * The right child pointer.
     */
    public int rightChildPointer;

    /**
     * Payload size.
     */
    public long[] payLoadSize;

    /**
     * The row ids.
     */
    public long[] rowId;

    /**
     * Preview data.
     */
    public String[] data;

    /**
     * Is this data a table.
     */
    public Boolean[] isTable;

    /**
     * Page number of Children.
     */
    public int[] childrenPageNumbers;

    /**
     * Overflow page numbers.
     */
    public int[] overflowPageNumbers;

    /**
     * Detects if the cell comparison is changed.
     */
    public boolean changed;

    /**
     * Creates a new Btree Cell.
     *
     * @param cellType Type of cell.
     * @param cellCount Number of cells on this page.
     * @param pageNumber Page number for these cells.
     */
    public BTreeCell(int cellType, int cellCount, long pageNumber) {
        this.cellType = cellType;
        this.cellCount = cellCount;
        this.pageNumber = pageNumber;
        this.leftChildPointers = new int[cellCount];
        this.rowId = new long[cellCount];
        this.payLoadSize = new long[cellCount];
        this.data = new String[cellCount];
        this.overflowPageNumbers = new int[cellCount];
        this.isTable = new Boolean[cellCount];
        this.childrenPageNumbers = new int[cellCount];
        this.changed = false;
    }

    /**
     * Calculates the hash of this cell
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;

        for( String s : data) {
            if (s != null) {
                hash = hash * prime + s.hashCode();
            }
        }
        return hash;
    }

    /**
     * Compares to object. They are equal if they contain the
     * same data.
     *
     * @param obj Other node to compare with.
     *
     * @return True if equal else false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)  {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        BTreeCell cell = (BTreeCell) obj;
        return this.hashCode() == cell.hashCode();
    }
}
