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

package battyp.lancaster.sqlitevisualiser.model.database;

import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;

/**
 * <h1> Database </h1>
 *
 * <p>
 * This class is intended to be a storage structure for everything relevant to the database.
 *
 * <p>
 * The class is filled with data via the Database parser, then stored inside the Database
 * Interface.  Currently the class stores the metadata and the btree structure of the
 * database. It is also passed to the view in order for the relevant data to be
 * drawn to the screen.
 *
 * @author Paul Batty
 * @see DatabaseParser
 * @see battyp.lancaster.sqlitevisualiser.model.databaseinterface.DatabaseInterface
 * @see Metadata
 * @see BTree
 * @since 0.2
 */
public class Database {

    private final BTree btree;
    private final Metadata metadata;

    /**
     * Constructor.
     *
     * @param btree The Btree object to store.
     * @param metadata The metadata object to store.
     */
    public Database(BTree btree, Metadata metadata) {
        this.btree = btree;
        this.metadata = metadata;
    }

    /**
     * Gets the btree structure representing the database.
     *
     * @return The BTree representing the database.
     */
    public BTree getBTree() {
        return this.btree;
    }

    /**
     * Gets the Metadata object representing all metadata about the database.
     *
     * @return The Metadata object.
     */
    public Metadata getMetadata() {
        return this.metadata;
    }
}