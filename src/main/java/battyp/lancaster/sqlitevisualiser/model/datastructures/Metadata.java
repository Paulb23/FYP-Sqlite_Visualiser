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
 * <h1> Metadata </h1>
 *
 * <p>
 * This class is designed to hold all the relevant metadata information about
 * the database.
 *
 * <p>
 * Everything in this class is public as it is only data storage and servers
 * no other purpose. So is designed to be modified and read.  This is filled
 * via the Database Parser and is stored inside a database object.
 *
 * @author Paul Batty
 * @see battyp.lancaster.sqlitevisualiser.model.database.Database
 * @see battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser
 * @since 0.2
 */
public class Metadata {

    /**
     * The database file name.
     */
    public String fileName;

    /**
     * The database Page size in bytes.
     */
    public int pageSize;

    /**
     * Format write version, 1 for legacy; 2 for WAL.
     */
    public int writeVersion;

    /**
     * Format read version, 1 for legacy; 2 for WAL.
     */
    public int readVersion;

    /**
     * Free Space or unused space at the end of each page.
     */
    public int unusedSpaceAtEndOfEachPage;

    /**
     * Maximum Embedded payload Must be 64.
     */
    public int maxEmbeddedPayload;

    /**
     * Minimum Embedded payload Must be 32.
     */
    public int minEmbeddedPayload;

    /**
     * Leaf payload Fraction Must be 32.
     */
    public int leafPayloadFraction;

    /**
     * The File Change counter.
     */
    public int fileChangeCounter;

    /**
     * Size of the database file in pages.
     */
    public int sizeOfDatabaseInPages;


    /**
     * Page number of first freelist page.
     */
    public int pageNumberOfFirstFreelistPage;

    /**
     * The total number of freelist pages.
     */
    public int totalFreeListPages;

    /**
     * The schema cookie.
     */
    public int schemaCookie;

    /**
     * The schema format valid formats are, 1,2,3,4.
     */
    public int schemaFormat;

    /**
     * The default page cache size.
     */
    public int defaultPageCacheSize;

    /**
     * Page number to the largest Btree else zero.
     */
    public int pageNumberToLargestBTreePage;

    /**
     * The type of text encoding the database uses.
     * 1 = UTF-8.
     * 2 = UTF-16le.
     * 3 = UTF-16be.
     */
    public int textEncoding;

    /**
     * The User version set by user_version_pragma.
     */
    public int userVersion;


    /**
     * The vacuum mode.
     * 1 = incremental-vacuum.
     * 2 = auto-vacuum.
     */
    public int vacuumMode;

    /**
     * The application id.
     */
    public int appID;

    /**
     * The version valid number.
     */
    public int versionValidNumber;

    /**
     * The sqlite version.
     */
    public int sqliteVersion;

    /**
     * Number of tables in the database.
     */
    public int numberOfTables;

    /**
     * Number of entries / rows in the database.
     */
    public int numberOfEntries;

    /**
     * Number of primary keys in the database.
     */
    public int numberOfPrimaryKeys;

    /**
     * Number of foreign keys in the database.
     */
    public int numberOfForeignKeys;
}
