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

package battyp.lancaster.sqlitevisualiser.tests.model.databaseparser;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DefaultDatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * JUnit tests for DefaultDatabaseParser
 *
 * @see battyp.lancaster.sqlitevisualiser.model.databaseparser.DefaultDatabaseParser
 *
 * @author Paul Batty
 */
public class DefaultDatabaseParserTest {

    @Test(expected = java.io.FileNotFoundException.class)
    public void TestFileNotFound() throws FileNotFoundException, InvalidFileException {
        DefaultDatabaseParser parser = new DefaultDatabaseParser();
        parser.parseDatabase("fake/file.txt", new Database(new BTree<String>(), new Metadata()));
    }

    @Test(expected = InvalidFileException.class)
    public void TestOpenInvalidFile() throws FileNotFoundException, InvalidFileException {
        DefaultDatabaseParser parser = new DefaultDatabaseParser();
        parser.parseDatabase("invalidDatabase.db", new Database(new BTree<String>(), new Metadata()));
    }

    @Test
    public void TestOpenValidDatabase() throws FileNotFoundException, InvalidFileException {
        DefaultDatabaseParser parser = new DefaultDatabaseParser();
        parser.parseDatabase("validDatabase", new Database(new BTree<String>(), new Metadata()));
    }

    @Test
    public void TestParsingMetaData() throws FileNotFoundException, InvalidFileException {
        DefaultDatabaseParser parser = new DefaultDatabaseParser();
        Database database = new Database(new BTree<String>(), new Metadata());
        parser.parseDatabase("validDatabase", database);

        Metadata metadata = database.getMetadata();

        Assert.assertEquals(1024, metadata.pageSize);
        Assert.assertEquals(1, metadata.writeVersion);
        Assert.assertEquals(1, metadata.readVersion);
        Assert.assertEquals(0, metadata.unusedSpaceAtEndOfEachPage);
        Assert.assertEquals(64, metadata.maxEmbeddedPayload);
        Assert.assertEquals(32, metadata.minEmbeddedPayload);
        Assert.assertEquals(32, metadata.leafPayloadFraction);
        Assert.assertEquals(1, metadata.fileChageCounter);
        Assert.assertEquals(3, metadata.sizeOfDatabaseInPages);
        Assert.assertEquals(0, metadata.pageNumberOfFirstFreelistPage);
        Assert.assertEquals(0, metadata.totalFreeListPages);
        Assert.assertEquals(1, metadata.schemaCookie);
        Assert.assertEquals(4, metadata.schemaFormat);
        Assert.assertEquals(0, metadata.defualtPageCacheSize);
        Assert.assertEquals(0, metadata.pageNumberToLargestBTreePage);
        Assert.assertEquals(1, metadata.textEncoding);
        Assert.assertEquals(0, metadata.userVersion);
        Assert.assertEquals(0, metadata.vacuummMode);
        Assert.assertEquals(0, metadata.appID);
        Assert.assertEquals(1, metadata.versionValidNumber);
        Assert.assertEquals(3008002, metadata.sqliteVersion);
    }
}
