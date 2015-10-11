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

package battyp.lancaster.sqlitevisualiser.model;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DefaultDatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DefaultDatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.FileNotFoundException;

/**
 * DefaultModel is the default interface into the model
 *
 * @author Paul Batty
 */
public class DefaultModel implements Model {

    private boolean isFileOpen;
    private DatabaseInterface databaseInterface;
    private DatabaseParser databaseParser;

    /**
     * {@inheritDoc}
     */
    @Override
    public void openDatabase(final String path, Database database) throws FileNotFoundException, InvalidFileException {
        database = this.databaseParser.parseDatabase(path, database);
        this.databaseInterface.clear();
        this.databaseInterface.addDatabase(database);
        isFileOpen = true;
    }

    /**
     * Creates a new instance of the default model with default mode
     */
    public DefaultModel() {
        databaseInterface = new DefaultDatabaseInterface();
        databaseParser = new DefaultDatabaseParser();
        isFileOpen = false;
    }

    /**
     * Creates a new instance of the model with custom modes
     *
     * @param databaseInterface DatabaseInterface to use
     * @param databaseParser DatabaseParser to use
     */
    public DefaultModel(DatabaseInterface databaseInterface, DatabaseParser databaseParser) {
        this.databaseInterface = databaseInterface;
        this.databaseParser = databaseParser;
        this.isFileOpen = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Database getDatabase() {
        return this.databaseInterface.getCurrent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseParser getDatabaseParser() {
        return this.databaseParser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFileOpen() {
        return this.isFileOpen;
    }
}
