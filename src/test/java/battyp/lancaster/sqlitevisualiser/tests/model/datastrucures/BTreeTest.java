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

package battyp.lancaster.sqlitevisualiser.tests.model.datastrucures;

import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for the BTree data structure
 *
 * @see battyp.lancaster.sqlitevisualiser.model.datastructures.BTree
 *
 * @author Paul Batty
 */
public class BTreeTest {

    @Test
    public void TestRootIsNullOnCreation() {
        BTree<String> tree = new BTree<>();
        Assert.assertEquals(tree.getRoot(), null);
    }

    @Test
    public void TestGetNumberOfNodesIsZeroOnCreation() {
        BTree<String> tree = new BTree<>();
        Assert.assertEquals(tree.getNumberOfNodes(), 0);
    }

    @Test
    public void TestIsEmptyReturnsTrueOnCreation() {
        BTree<String> tree = new BTree<>();
        Assert.assertEquals(tree.isEmpty(), true);
    }

    @Test
    public void TestSetAndGetRoot() {
        BTree<String> tree = new BTree<>();
        BTreeNode<String> node = new BTreeNode<>(null);

        tree.setRoot(node);
        Assert.assertEquals(tree.getRoot(), node);
    }

    @Test
    public void TestNumberOfNodesWithOnlyRoot() {
        BTree<String> tree = new BTree<>();
        BTreeNode<String> node = new BTreeNode<>(null);

        tree.setRoot(node);
        Assert.assertEquals(tree.getNumberOfNodes(), 1);
    }

    @Test
    public void TestIsEmptyReturnsFalseWithOnlyRoot() {
        BTree<String> tree = new BTree<>();
        BTreeNode<String> node = new BTreeNode<>(null);

        tree.setRoot(node);
        Assert.assertEquals(false, tree.isEmpty());
    }

    @Test
    public void TestGetNumberOfNodes() {
        BTree<String> tree = new BTree<>();
        BTreeNode<String> root = new BTreeNode<>("root");
        BTreeNode<String> nodeA = new BTreeNode<>("A");
        BTreeNode<String> nodeB = new BTreeNode<>("B");
        BTreeNode<String> nodeC = new BTreeNode<>("C");
        BTreeNode<String> nodeD = new BTreeNode<>("D");

        nodeC.addChild(nodeD);
        nodeA.addChild(nodeC);

        root.addChild(nodeA);
        root.addChild(nodeB);

        tree.setRoot(root);

        Assert.assertEquals(5, tree.getNumberOfNodes());
    }

    @Test
    public void TestFindReturnsNullWithNoRoot() {
        BTree<String> tree = new BTree<>();
        Assert.assertEquals(null, tree.find("data"));
    }

    @Test
    public void TestFindReturnsRootWithTheData() {
        BTree<String> tree = new BTree<>();
        BTreeNode<String> node = new BTreeNode<>("data");
        tree.setRoot(node);

        Assert.assertEquals(node, tree.find("data"));
    }

    @Test
    public void TestFind() {
        BTree<String> tree = new BTree<>();
        BTreeNode<String> root = new BTreeNode<>("root");
        BTreeNode<String> nodeA = new BTreeNode<>("A");
        BTreeNode<String> nodeB = new BTreeNode<>("B");
        BTreeNode<String> nodeC = new BTreeNode<>("C");
        BTreeNode<String> nodeD = new BTreeNode<>("D");

        nodeC.addChild(nodeD);
        nodeA.addChild(nodeC);

        root.addChild(nodeA);
        root.addChild(nodeB);

        tree.setRoot(root);

        Assert.assertEquals(nodeC, tree.find("C"));
    }

    @Test
    public void TestExistReturnsFalseWithNoRoot() {
        BTree<String> tree = new BTree<>();
        Assert.assertEquals(false, tree.exists("data"));
    }

    @Test
    public void TestExistsReturnsTrueWithTheData() {
        BTree<String> tree = new BTree<>();
        BTreeNode<String> node = new BTreeNode<>("data");
        tree.setRoot(node);

        Assert.assertEquals(true, tree.exists("data"));
    }

    @Test
    public void TestExists() {
        BTree<String> tree = new BTree<>();
        BTreeNode<String> root = new BTreeNode<>("root");
        BTreeNode<String> nodeA = new BTreeNode<>("A");
        BTreeNode<String> nodeB = new BTreeNode<>("B");
        BTreeNode<String> nodeC = new BTreeNode<>("C");
        BTreeNode<String> nodeD = new BTreeNode<>("D");

        nodeC.addChild(nodeD);
        nodeA.addChild(nodeC);

        root.addChild(nodeA);
        root.addChild(nodeB);

        tree.setRoot(root);

        Assert.assertEquals(true, tree.exists("C"));
    }
}
