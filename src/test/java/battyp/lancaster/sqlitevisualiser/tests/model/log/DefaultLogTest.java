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

package battyp.lancaster.sqlitevisualiser.tests.model.log;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.log.DefaultLog;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for DefaultLog
 *
 * @see DefaultLog
 *
 * @author Paul Batty
 */
public class DefaultLogTest {

    @Test
    public void TestGetLogIsEmptyOnCreation() {
        DefaultLog log = new DefaultLog();
        Assert.assertEquals(0, log.getLog().size());
    }

    @Test
    public void TestDetectChangesNoChangesDoesNothing() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(database, database);
        Assert.assertEquals(0, log.getLog().size());
    }

    @Test
    public void TestDetectChangesUpdateSingleRow() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell newCell = new BTreeCell(5, 1, 1);
        newCell.data[0] = "newTestData";
        BTreeNode newRoot = new BTreeNode(newCell);
        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals("'testData' TO 'newTestData'\n", log.getLog().get(0).items.get(0));
    }


    @Test
    public void TestDetectChangesUpdateMultipleRows() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 3, 1);
        cell.data[0] = "testData1";
        cell.data[1] = "testData2";
        cell.data[2] = "testData3";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell newCell = new BTreeCell(5, 3, 1);
        newCell.data[0] = "newTestData1";
        newCell.data[1] = "testData2";
        newCell.data[2] = "newTestData3";
        BTreeNode newRoot = new BTreeNode(newCell);
        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(2, log.getLog().get(0).items.size());
        Assert.assertEquals("'testData1' TO 'newTestData1'", log.getLog().get(0).items.get(0));
        Assert.assertEquals("'testData3' TO 'newTestData3'\n", log.getLog().get(0).items.get(1));
    }

    @Test
    public void TestDetectChangesAddNewRow() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData1";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell newCell = new BTreeCell(5, 2, 1);
        newCell.data[0] = "testData1";
        newCell.data[1] = "testData2";
        BTreeNode newRoot = new BTreeNode(newCell);
        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals("ADDED 'testData2'\n", log.getLog().get(0).items.get(0));
    }

    @Test
    public void TestDetectChangesAddMultipleNewRows() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData1";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell newCell = new BTreeCell(5, 3, 1);
        newCell.data[0] = "testData1";
        newCell.data[1] = "testData2";
        newCell.data[2] = "testData3";
        BTreeNode newRoot = new BTreeNode(newCell);
        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(2, log.getLog().get(0).items.size());
        Assert.assertEquals("ADDED 'testData2'", log.getLog().get(0).items.get(0));
        Assert.assertEquals("ADDED 'testData3'\n", log.getLog().get(0).items.get(1));
    }

    @Test
    public void TestDetectChangesRemoveRow() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 2, 1);
        cell.data[0] = "testData1";
        cell.data[1] = "testData2";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell newCell = new BTreeCell(5, 1, 1);
        newCell.data[0] = "testData1";
        BTreeNode newRoot = new BTreeNode(newCell);
        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(1, log.getLog().get(0).items.size());
        Assert.assertEquals("REMOVED 'testData2'\n", log.getLog().get(0).items.get(0));
    }

    @Test
    public void TestDetectChangesRemoveMultipleRows() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 3, 1);
        cell.data[0] = "testData1";
        cell.data[1] = "testData2";
        cell.data[2] = "testData3";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell newCell = new BTreeCell(5, 1, 1);
        newCell.data[0] = "testData1";
        BTreeNode newRoot = new BTreeNode(newCell);
        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(2, log.getLog().get(0).items.size());
        Assert.assertEquals("REMOVED 'testData2'", log.getLog().get(0).items.get(0));
        Assert.assertEquals("REMOVED 'testData3'\n", log.getLog().get(0).items.get(1));
    }

    @Test
    public void TestDetectChangesAddPage() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData1";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell newCell = new BTreeCell(5, 1, 2);
        newCell.data[0] = "testData1";
        BTreeNode newRoot = new BTreeNode(cell);
        BTreeNode newNode = new BTreeNode(newCell);
        newRoot.addChild(newNode);

        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(1, log.getLog().get(0).items.size());
        Assert.assertEquals("ADDED PAGE '2'\n", log.getLog().get(0).items.get(0));
    }


    @Test
    public void TestDetectChangesAddMultiplePage() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData1";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell newCell = new BTreeCell(5, 1, 2);
        newCell.data[0] = "testData1";
        BTreeCell otherNewCell = new BTreeCell(5, 1, 3);
        otherNewCell.data[0] = "testData1";
        BTreeNode newRoot = new BTreeNode(cell);
        BTreeNode newNode = new BTreeNode(newCell);
        BTreeNode otherNewNode = new BTreeNode(otherNewCell);
        newRoot.addChild(newNode);
        newRoot.addChild(otherNewNode);

        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(2, log.getLog().get(0).items.size());
        Assert.assertEquals("ADDED PAGE '2'", log.getLog().get(0).items.get(0));
        Assert.assertEquals("ADDED PAGE '3'\n", log.getLog().get(0).items.get(1));
    }

    @Test
    public void TestDetectChangesRemovePage() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData1";
        BTreeCell newCell = new BTreeCell(5, 1, 2);
        newCell.data[0] = "testData1";
        BTreeNode root = new BTreeNode(cell);
        BTreeNode node = new BTreeNode(newCell);
        root.addChild(node);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeNode newRoot = new BTreeNode(cell);

        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(1, log.getLog().get(0).items.size());
        Assert.assertEquals("REMOVED PAGE '2'\n", log.getLog().get(0).items.get(0));
    }

    @Test
    public void TestDetectChangesRemoveMultiplePages() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData1";
        BTreeCell newCell = new BTreeCell(5, 1, 2);
        newCell.data[0] = "testData1";
        BTreeCell otherNewCell = new BTreeCell(5, 1, 3);
        otherNewCell.data[0] = "testData1";
        BTreeNode root = new BTreeNode(cell);
        BTreeNode node = new BTreeNode(newCell);
        BTreeNode otherNode = new BTreeNode(otherNewCell);
        root.addChild(node);
        root.addChild(otherNode);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeNode newRoot = new BTreeNode(cell);

        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(2, log.getLog().get(0).items.size());
        Assert.assertEquals("REMOVED PAGE '2'", log.getLog().get(0).items.get(0));
        Assert.assertEquals("REMOVED PAGE '3'\n", log.getLog().get(0).items.get(1));
    }

    @Test
    public void TestDetectChangesAddPageAndChangeData() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData1";
        BTreeNode root = new BTreeNode(cell);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell changedCell = new BTreeCell(5, 1, 1);
        changedCell.data[0] = "testData2";
        BTreeCell newCell = new BTreeCell(5, 1, 2);
        newCell.data[0] = "testData1";
        BTreeNode newRoot = new BTreeNode(changedCell);
        BTreeNode newNode = new BTreeNode(newCell);
        newRoot.addChild(newNode);

        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(2, log.getLog().get(0).items.size());
        Assert.assertEquals("ADDED PAGE '2'", log.getLog().get(0).items.get(0));
        Assert.assertEquals("'testData1' TO 'testData2'\n", log.getLog().get(0).items.get(1));
    }

    @Test
    public void TestDetectChangesRemovePageAndChangeData() {
        BTree tree = new BTree();

        BTreeCell cell = new BTreeCell(5, 1, 1);
        cell.data[0] = "testData1";
        BTreeCell cell2 = new BTreeCell(5, 1, 2);
        cell2.data[0] = "testData1";

        BTreeNode root = new BTreeNode(cell);
        BTreeNode node = new BTreeNode(cell2);
        root.addChild(node);
        tree.setRoot(root);
        Database database = new Database(tree, new Metadata());

        BTree newTree = new BTree();

        BTreeCell changedCell = new BTreeCell(5, 1, 1);
        changedCell.data[0] = "testData2";
        BTreeNode newRoot = new BTreeNode(changedCell);

        newTree.setRoot(newRoot);
        Database newDatabase = new Database(newTree, new Metadata());

        DefaultLog log = new DefaultLog();
        log.detectChanges(newDatabase, database);
        Assert.assertEquals(1, log.getLog().size());
        Assert.assertEquals(2, log.getLog().get(0).items.size());
        Assert.assertEquals("REMOVED PAGE '2'", log.getLog().get(0).items.get(0));
        Assert.assertEquals("'testData1' TO 'testData2'\n", log.getLog().get(0).items.get(1));
    }
}
