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
 * <h1> Sqlite Constants </h1>
 *
 * <p>
 * A data structure for holding the constants around a
 * Sqlite database.
 *
 * @author Paul Batty
 * @since 0.6
 */
public class SqliteConstants {

    /**
     *  Size of the header.
     */
    public static final int HEADER_SIZE = 100;

    /**
     * Length of the magic number.
     */
    public static final int MAGIC_NUMBER_LENGTH = 16;

    /**
     * The magic number in bytes, in text it reads:
     * SQLite format 3.
     */
    public static final int MAGIC_NUMBER[] = new int[] {0x53, 0x51, 0x4c, 0x69, 0x74, 0x65, 0x20, 0x66, 0x6f, 0x72, 0x6d, 0x61, 0x74, 0x20, 0x33, 0x00};

    /**
     * Table leaf cell identifier.
     */
    public static final int TABLE_BTREE_LEAF_CELL = 0x0d;

    /**
     * Table interior cell identifier.
     */
    public static final int TABLE_BTREE_INTERIOR_CELL = 0x05;

    /**
     * Index leaf cell identifier.
     */
    public static final int INDEX_BTREE_LEAF_CELL = 0x0a;

    /**
     * Index interior cell identifier.
     */
    public static final int INDEX_BTREE_INTERIOR_CELL = 0x02;

    /**
     * Private constructor so it cannot be created.
     */
    private SqliteConstants() {
    }
}
