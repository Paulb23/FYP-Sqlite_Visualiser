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
 * A data structure for holding the database Metadata
 *
 * @author Paul Batty
 */
public class Metadata {

    /**
     * The database Page size in bytes
     */
    public int pageSize;

    /**
     * Format write version, 1 for legacy; 2 for WAL
     */
    public int writeVersion;

    /**
     * Format read version, 1 for legacy; 2 for WAL
     */
    public int readVersion;

    /**
     * Free Space or unused space at the end of each page
     */
    public int unusedSpaceAtEndOfEachPage;

    /**
     * Maximum Embedded payload Must be 64
     */
    public int maxEmbeddedPayload;

    /**
     * Minimum Embedded payload Must be 32
     */
    public int minEmbeddedPayload;

    /**
     * Leaf payload Fraction Must be 32
     */
    public int leafPayloadFraction;

    /**
     * The File Change counter
     */
    public int fileChageCounter;

    /**
     * Size of the database file in pages
     */
    public int sizeOfDatabaseInPages;


    /**
     * Page number of first freelist page
     */
    public int pageNumberOfFirstFreelistPage;

    /**
     * The total number of freelist pages
     */
    public int totalFreeListPages;

    /**
     * The schema cookie
     */
    public int schemaCookie;

    /**
     * The schema format valid formats are, 1,2,3,4
     */
    public int schemaFormat;

    /**
     * The default page cache size
     */
    public int defualtPageCacheSize;

    /**
     * Page number to the largest Btree else zero
     */
    public int pageNumberToLargestBTreePage;

    /**
     * The type of text encoding the database uses,
     * 1 = UTF-8
     * 2 = UTF-16le
     * 3 = UTF-16be
     */
    public int textEncoding;

    /**
     * The User version set by user_version_pragma
     */
    public int userVersion;


    /**
     * The vacuum mode
     * 1 = incremental-vacuum
     * 2 = auto-vacuum
     */
    public int vacuummMode;

    /**
     * The application id
     */
    public int appID;

    /**
     * The version valid number
     */
    public int versionValidNumber;

    /**
     * The sqlite version
     */
    public int sqliteVersion;
}
