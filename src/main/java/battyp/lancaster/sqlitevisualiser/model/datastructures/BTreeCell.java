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

package battyp.lancaster.sqlitevisualiser.model.datastructures;

/**
 * A data structure for holding the data located in the cells
 *
 * @author Paul Batty
 */
public class BTreeCell {

    /**
     * Cell type should be from the constants
     *
     * @see SqliteConstants
     */
    public final int CellType;

    /**
     *
     */
    public final int cellCount;

    /**
     * The page numbers
     */
    public int[] leftChildPointers;

    /**
     * Payload size
     */
    public long[] payLoadSize;

    /**
     * The row ids
     */
    public long[] rowId;

    /**
     * Preview data
     */
    public String[] previewData;

    /**
     * Overflow page numbers
     */
    public int[] overflowPageNumbers;

    /**
     * Creates a new Btree Cell
     *
     * @param cellType type of cell
     */
    public BTreeCell(int cellType, int cellCount) {
        this.CellType = cellType;
        this.cellCount = cellCount;
        this.leftChildPointers = new int[cellCount];
        this.rowId = new long[cellCount];
        this.payLoadSize = new long[cellCount];
        this.previewData = new String[cellCount];
        this.overflowPageNumbers = new int[cellCount];
    }
}