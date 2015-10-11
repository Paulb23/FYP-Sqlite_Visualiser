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

package battyp.lancaster.sqlitevisualiser.model.databaseinterface;

import battyp.lancaster.sqlitevisualiser.model.database.Database;

/**
 * Interface and all interaction with the inner storage will have to go through,
 * must implement, history, next, previous, adding of the Database object.
 * There should only be one of these and is controlled via the Model.
 *
 * @see battyp.lancaster.sqlitevisualiser.model.database.Database
 *
 * @author Paul Batty
 */
public interface DatabaseInterface {

    /**
     * Gets the current database the this time in history that is being viewed
     *
     * @return Current Database being viewed
     */
    public Database getCurrent();

    /**
     * Gets the previous database to the one being shown
     *
     * @return Previous database to the one shown
     */
    public Database getPrevious();

    /**
     * Gets the next database to the one being shown
     *
     * @return Next database to the one beign shown
     */
    public Database getNext();

    /**
     * Steps into the next database
     */
    public void nextStep();

    /**
     * Steps into the last database
     */
    public void previousStep();

    /**
     * Adds a database to the front of the history queue
     * @param database
     */
    public void addDatabase(Database database);
}
