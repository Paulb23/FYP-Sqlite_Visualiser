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

import battyp.lancaster.sqlitevisualiser.model.Model;
import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Stack;

/**
 * <h1> Default Live Updater </h1>
 *
 * <p>
 * Default implementation of the live updater.
 *
 * @author Paul Batty
 * @see LiveUpdater
 * @since 0.8
 */
public class DefaultLiveUpdater implements LiveUpdater {

    private Model model;
    private boolean live;
    private String path;

    /**
     * Constructor.
     */
    public DefaultLiveUpdater(Model model) {
        live = true;
        this.model = model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatabase(String path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String path, DatabaseParser databaseParser, DatabaseInterface databaseInterface) throws IOException, InvalidFileException {
        if (model == null) {
            return;
        }

        Database newDatabase = databaseParser.parseDatabase(path, new Database(new BTree(), new Metadata()));
        updateMetaData(newDatabase);
        this.model.getLog().detectChanges(newDatabase, databaseInterface.getCurrent());
        databaseInterface.addDatabase(newDatabase);
        if (live) {
            databaseInterface.nextStep();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMetaData(Database database) {
        if (database == null || model == null) {
            return;
        }

        Metadata metadata = database.getMetadata();
        metadata.fileName = this.model.getFileWatcher().getFileName();
        try {
            this.model.getSqlExecutor().connect();

            DatabaseMetaData databaseMetaData = this.model.getSqlExecutor().getDatabaseMetaData();
            String catalog = databaseMetaData.getConnection().getCatalog();

            metadata.numberOfTables = 0;
            metadata.numberOfPrimaryKeys = 0;
            metadata.numberOfForeignKeys = 0;
            metadata.numberOfEntries = 0;

            ResultSet count = databaseMetaData.getTables(catalog, null, null, null);

            while(count.next()) {
                String table = count.getString("TABLE_NAME");
                metadata.numberOfTables += 1;

                ResultSet pkeys = databaseMetaData.getPrimaryKeys(catalog, null, table);
                metadata.numberOfPrimaryKeys += getRowCount(pkeys);
                pkeys.close();

                ResultSet fkeys = databaseMetaData.getImportedKeys(catalog, null, table);
                metadata.numberOfForeignKeys += getRowCount(fkeys);
                fkeys.close();

                ResultSet entries = this.model.getSqlExecutor().executeSql("Select * from " + table);
                metadata.numberOfEntries += getRowCount(entries);
                entries.close();
            }
            count.close();

            this.model.getSqlExecutor().disconnect();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getRowCount(ResultSet set) throws SQLException {
        int rowCount = 0;
        while(set.next()) {
            rowCount++;
        }
        return rowCount;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObserver() {
        try {
            if (path != null) {
                update(path, this.model.getDatabaseParser(), this.model.getDatabaseInterface());
            }
        } catch (IOException | InvalidFileException ignored) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextStep() {
        this.model.getDatabaseInterface().nextStep();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void previousStep() {
        this.model.getDatabaseInterface().previousStep();
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
