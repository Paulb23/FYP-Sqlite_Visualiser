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

package battyp.lancaster.sqlitevisualiser.tests.model;

import battyp.lancaster.sqlitevisualiser.model.DefaultModel;
import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DefaultDatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;
import battyp.lancaster.sqlitevisualiser.tests.model.mocks.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * JUnit tests for DefaultModel
 *
 * @see battyp.lancaster.sqlitevisualiser.model.DefaultModel
 *
 * @author Paul Batty
 */
public class DefaultModelTest {

    @Test
    public void TestDefaultModesAreCreatedOnCreation() {
        DefaultModel model = new DefaultModel();
        Assert.assertEquals(null, model.getDatabase());
        Assert.assertTrue(model.getDatabaseParser() instanceof DefaultDatabaseParser);
    }

    @Test
    public void TestCustomModesAreLoadedOnCreation() {
        DefaultModel model = new DefaultModel(new MockDatabaseInterface(), new MockDatabaseParser(), new MockSqlExecutor(), new MockFileWatcher(), new MockLiveUpdater(), new MockLog());
        Assert.assertTrue(model.getDatabase() instanceof MockDatabase);
        Assert.assertTrue(model.getDatabaseParser() instanceof MockDatabaseParser);
        Assert.assertTrue(model.getSqlExecutor() instanceof MockSqlExecutor);
        Assert.assertTrue(model.getFileWatcher() instanceof MockFileWatcher);
        Assert.assertTrue(model.getLiveUpdater() instanceof MockLiveUpdater);
        Assert.assertTrue(model.getLog() instanceof MockLog);
        Assert.assertTrue(model.getDatabaseInterface() instanceof  MockDatabaseInterface);
    }

    @Test
    public void TestIsFileOpenIsFalseOnCreation() {
        DefaultModel model = new DefaultModel();
        Assert.assertEquals(false, model.isFileOpen());
    }

    @Test
    public void TestIsFileOpenReturnsTrueWithOpenFile() throws IOException, InvalidFileException, SQLException, ClassNotFoundException {
        DefaultModel model = new DefaultModel();
        model.openDatabase("validDatabase", new Database(new BTree(), new Metadata()));
        Assert.assertEquals(true, model.isFileOpen());
    }

    @Test(expected = InvalidFileException.class)
    public void TestOpenFileReturnInValidFileWhenOpeningInValidFile() throws IOException, InvalidFileException, SQLException, ClassNotFoundException {
        DefaultModel model = new DefaultModel();
        model.openDatabase("invalidDatabase.db", new Database(new BTree(), new Metadata()));
    }

    @Test(expected = FileNotFoundException.class)
    public void TestOpenFileReturnFileNotFoundWhenOpeningInValidPath() throws IOException, InvalidFileException, SQLException, ClassNotFoundException {
        DefaultModel model = new DefaultModel();
        model.openDatabase("sadasda.db", new Database(new BTree(), new Metadata()));
    }
}
