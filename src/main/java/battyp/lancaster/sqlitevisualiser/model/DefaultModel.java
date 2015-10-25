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

import battyp.lancaster.sqlitevisualiser.model.filewatcher.DefaultFileWatcher;
import battyp.lancaster.sqlitevisualiser.model.filewatcher.FileWatcher;
import battyp.lancaster.sqlitevisualiser.model.liveupdater.DefaultLiveUpdater;
import battyp.lancaster.sqlitevisualiser.model.liveupdater.LiveUpdater;
import battyp.lancaster.sqlitevisualiser.model.sqlexecutor.DefaultSqlExecutor;
import battyp.lancaster.sqlitevisualiser.model.sqlexecutor.SqlExecutor;
import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DefaultDatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DefaultDatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * DefaultModel is the default interface into the model
 *
 * @author Paul Batty
 */
public class DefaultModel implements Model {

    private boolean isFileOpen;
    private DatabaseInterface databaseInterface;
    private DatabaseParser databaseParser;
    private SqlExecutor sqlExecutor;
    private FileWatcher fileWatcher;
    private LiveUpdater liveUpdater;

    /**
     * {@inheritDoc}
     */
    @Override
    public void openDatabase(final String path, Database database) throws IOException, FileNotFoundException, InvalidFileException, SQLException, ClassNotFoundException {
        database = this.databaseParser.parseDatabase(path, database);
        this.databaseInterface.clear();
        this.databaseInterface.addDatabase(database);
        this.sqlExecutor.setDatabaseFile(path);
        this.sqlExecutor.connect();
        this.liveUpdater.setDatabase(path, this.databaseParser, this.databaseInterface);
        this.fileWatcher.setFile(path);
        isFileOpen = true;
    }

    /**
     * Creates a new instance of the default model with default mode
     */
    public DefaultModel() {
        databaseInterface = new DefaultDatabaseInterface();
        databaseParser = new DefaultDatabaseParser();
        sqlExecutor = new DefaultSqlExecutor();
        liveUpdater = new DefaultLiveUpdater();
        fileWatcher = new DefaultFileWatcher();
        Thread thread = new Thread(fileWatcher, "FileWatcher");
        thread.start();
        isFileOpen = false;
        fileWatcher.addObserver(liveUpdater);
    }

    /**
     * Creates a new instance of the model with custom modes
     *
     * @param databaseInterface DatabaseInterface to use
     * @param databaseParser DatabaseParser to use
     */
    public DefaultModel(DatabaseInterface databaseInterface, DatabaseParser databaseParser, SqlExecutor sqlExecutor, FileWatcher fileWatcher, LiveUpdater liveUpdater) {
        this.databaseInterface = databaseInterface;
        this.databaseParser = databaseParser;
        this.sqlExecutor = sqlExecutor;
        this.fileWatcher = fileWatcher;
        this.liveUpdater = liveUpdater;
        Thread thread = new Thread(this.fileWatcher, "FileWatcher");
        thread.start();
        this.isFileOpen = false;
        this.fileWatcher.addObserver(this.liveUpdater);
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
    public SqlExecutor getSqlExecutor() {
        return this.sqlExecutor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileWatcher getFileWatcher() {
        return this.fileWatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LiveUpdater getLiveUpdater() {
        return this.liveUpdater;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFileOpen() {
        return this.isFileOpen;
    }
}
