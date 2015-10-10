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
 * Database is the interface that all database storage will extend.
 * This class is filled with data via the {@link DatabaseParser databaseparser}
 * class, It is also passed to the view in order for the relevant data to be drawn to the screen.
 *
 * @author Paul Batty
 */
public class Database {

    private final BTree btree;
    private final Metadata metadata;

    /**
     * Creates a new instance of Database
     *
     * @param btree The btree object
     * @param metadata The metadata object
     */
    public Database(BTree btree, Metadata metadata) {
        this.btree = btree;
        this.metadata = metadata;
    }

    /**
     * Gets the btree structure representing the database
     *
     * @return The btree structure representing the database
     */
    public BTree getBTree() {
        return this.btree;
    }

    /**
     * Gets the Metadata object repenting all metadata about the database
     *
     * @return The Metadata object, containing metadata info
     */
    public Metadata getMetadata() {
        return this.metadata;
    }
}