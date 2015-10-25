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

package battyp.lancaster.sqlitevisualiser.model.liveupdater;

import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;
import battyp.lancaster.sqlitevisualiser.observerinterface.Observer;

import java.io.IOException;

/**
 * Live updater, performs the update and controls to the update, play and pausing of the
 * System
 *
 * @author Paul Batty
 */
public interface LiveUpdater extends Observer {

    /**
     * Performs an update with the passed objects
     *
     * @param path Path to the database
     * @param databaseParser The database parser to parse with
     * @param databaseInterface The database interface to store the parsed database
     */
    public void update(String path, DatabaseParser databaseParser, DatabaseInterface databaseInterface) throws IOException, InvalidFileException;

    /**
     * Sets up the updater for the incoming requests
     *
     * @param path Path to the database
     * @param databaseParser Parser to parse the database with
     * @param databaseInterface Interface to store the database
     */
    public void setDatabase(String path, DatabaseParser databaseParser, DatabaseInterface databaseInterface);

    /**
     * Starts the updating
     * On by default
     */
    public void startUpdating();

    /**
     * Stops the live updater
     */
    public void stopUpdating();

    /**
     * Gets whether we are currently live updating
     *
     * @return True if updating else false
     */
    public boolean isUpdating();
}
