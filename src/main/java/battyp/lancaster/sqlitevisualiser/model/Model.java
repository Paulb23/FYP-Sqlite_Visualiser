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

import battyp.lancaster.sqlitevisualiser.model.sqlexecutor.SqlExecutor;
import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Model is a interface that all models will inherit from as a interface into the different model sections
 *
 * @author Paul Batty
 */
public interface Model {

    /**
     * Opens a database
     *
     * @param path path to the database
     * @param database The database to use
     */
    public void openDatabase(final String path, Database database) throws IOException, FileNotFoundException, InvalidFileException, SQLException, ClassNotFoundException;

    /**
     * Gets the current Database
     *
     * @return Current Database
     */
    public Database getDatabase();

    /**
     * Gets the current database parser
     *
     * @return current database parser
     */
    public DatabaseParser getDatabaseParser();

    /**
     * Gets the current Sql Executor
     *
     * @return current Sql Executor
     */
    public SqlExecutor getSqlExecutor();

    /**
     * Gets whether we currently have a file open
     *
     * @return true if file is open else false
     */
    public boolean isFileOpen();
}
