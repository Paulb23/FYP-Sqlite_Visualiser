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

package battyp.lancaster.sqlitevisualiser.model.databaseinterface;

import battyp.lancaster.sqlitevisualiser.model.database.Database;

/**
 * <h1> Database Interface </h1>
 *
 * <p>
 * This Class is designed to provide and interface into the storage of the
 * Database objects.  And as such provide interactions such as next, previous
 * current, and Adding of a new database object.
 *
 * <p>
 * Only one of theses should exist per database else the history will get
 * muddled up. The interaction should be via the LiveUpdater controlling the adding and
 * status, also the Model to retrieve the current database.
 *
 * @author Paul Batty
 * @see Database
 * @see battyp.lancaster.sqlitevisualiser.model.liveupdater.LiveUpdater
 * @see battyp.lancaster.sqlitevisualiser.model.Model
 * @since 0.4
 */
public interface DatabaseInterface {

    /**
     * Gets the current database being viewed.
     *
     * @return Current Database being viewed.
     */
    public Database getCurrent();

    /**
     * Gets the previous database to the one being shown.
     *
     * @return Previous database to the one shown.
     */
    public Database getPrevious();

    /**
     * Gets the next database to the one being shown.
     *
     * @return Next database to the one being shown.
     */
    public Database getNext();

    /**
     * Steps into the next database.
     */
    public void nextStep();

    /**
     * Steps into the last database.
     */
    public void previousStep();

    /**
     * Gets the number of databases.
     */
    public int getCount();

    /**
     * Gets the current database id.
     */
    public int getCurrentPos();

    /**
     * Sets the timeline position.
     */
    public void setCurrent(int current);

    /**
     * Adds a database to the front of the history queue.
     *
     * @param database Database to add.
     */
    public void addDatabase(Database database);

    /**
     * Clears everything in the database history.
     */
    public void clear();
}
