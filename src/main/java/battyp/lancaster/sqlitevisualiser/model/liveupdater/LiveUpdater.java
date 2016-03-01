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

package battyp.lancaster.sqlitevisualiser.model.liveupdater;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;
import battyp.lancaster.sqlitevisualiser.observerinterface.Observer;

import java.io.IOException;

/**
 * <h1> Live updater </h1>
 *
 * <p>
 * This class is designed to handle the state of the database interface, and updating.
 * It should control the pause, play, next and previous buttons.  It should also handle
 * the parsing and storage of a Database object when a change is detected.
 *
 * @author Paul Batty
 * @see battyp.lancaster.sqlitevisualiser.model.filewatcher.FileWatcher
 * @see battyp.lancaster.sqlitevisualiser.model.database.Database
 * @see DatabaseInterface
 * @see DatabaseParser
 * @since 0.8
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
     * Updates the Metadata for the database.
     *
     * @param database Database with the metadata object to update.
     */
    public void updateMetaData(Database database);

    /**
     * Sets up the updater for the incoming requests
     *
     * @param path Path to the database
     */
    public void setDatabase(String path);

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
     * Gets the next step.
     */
    public void nextStep();

    /**
     * Gets the previous step.
     */
    public void previousStep();

    /**
     * Gets whether we are currently live updating
     *
     * @return True if updating else false
     */
    public boolean isUpdating();
}
