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

package battyp.lancaster.sqlitevisualiser.model.databaseparser;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.IOException;

/**
 * <h1> Database Parser </h1>
 *
 * <p>
 * This class is designed to parse the database file. And retrieve the
 * information then store it inside the database object passed in.
 *
 * <p>
 * This Class only contains one, method as it should only parse the file
 * All other action are coordinated inside the model, for opening the
 * database, And the updating is done via the live updater.
 *
 * @author Paul Batty
 * @see Database
 * @see battyp.lancaster.sqlitevisualiser.model.Model
 * @see battyp.lancaster.sqlitevisualiser.model.liveupdater.LiveUpdater
 * @since 0.3
 */
public interface DatabaseParser {

    /**
     * Takes the database file in the path as parses it into the database
     * object, this is automatically called when a database change
     * is detected, or when a new database in opened.
     *
     * @param pathToDatabase The string path to the database including file name and extension.
     * @param database The database class to fill with data.
     *
     * @return Database class filled with the format data.
     */
    public Database parseDatabase(final String pathToDatabase, Database database) throws IOException, InvalidFileException;
}