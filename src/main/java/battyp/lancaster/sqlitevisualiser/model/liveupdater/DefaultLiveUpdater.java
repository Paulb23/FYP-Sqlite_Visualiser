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

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.IOException;

/**
 * Default implementation of the live updater
 *
 * @author Paul Batty
 */
public class DefaultLiveUpdater implements LiveUpdater {

    private boolean live;
    private String path;
    private DatabaseParser databaseParser;
    private DatabaseInterface databaseInterface;

    public DefaultLiveUpdater() {
        live = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatabase(String path, DatabaseParser databaseParser, DatabaseInterface databaseInterface) {
        this.path = path;
        this.databaseParser = databaseParser;
        this.databaseInterface = databaseInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String path, DatabaseParser databaseParser, DatabaseInterface databaseInterface) throws IOException, InvalidFileException {
        databaseInterface.addDatabase(databaseParser.parseDatabase(path, new Database(new BTree(), new Metadata())));
        if (live) {
            databaseInterface.nextStep();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObserver() {
        try {
            if (path != null && databaseParser != null && databaseInterface != null) {
                update(path, databaseParser, databaseInterface);
            }
        } catch (IOException e) {
        } catch (InvalidFileException e) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startUpdating() {
        live = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopUpdating() {
        live = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUpdating() {
        return live;
    }
}
