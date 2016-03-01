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

package battyp.lancaster.sqlitevisualiser.tests.model.database;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for the Database class
 *
 * @see battyp.lancaster.sqlitevisualiser.model.database.Database
 *
 * @author Paul Batty
 */
public class DefaultDatabaseTest {

    @Test
    public void TestCreationWithNull() {
        Database database = new Database(null, null);
    }

    @Test
    public void TestGetBTreeWithNull() {
        Database database = new Database(null, null);
        Assert.assertEquals(null, database.getBTree());
    }


    @Test
    public void TestGetMetadataWithNull() {
        Database database = new Database(null, null);
        Assert.assertEquals(null, database.getMetadata());
    }

    @Test
    public void TestGetBtree() {
        BTree<String> tree = new BTree<>();
        Database database = new Database(tree, new Metadata());
        Assert.assertEquals(tree, database.getBTree());
    }

    @Test
    public void TestGetMetadata() {
        Metadata metadata = new Metadata();
        Database database = new Database(new BTree(), metadata);
        Assert.assertEquals(metadata, database.getMetadata());
    }
}
