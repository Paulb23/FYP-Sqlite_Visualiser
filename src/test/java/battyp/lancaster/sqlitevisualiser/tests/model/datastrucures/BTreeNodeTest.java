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

package battyp.lancaster.sqlitevisualiser.tests.model.datastrucures;

import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * JUnit tests for BTreeNode data structure
 *
 * @see battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode
 *
 * @author Paul Batty
 */
public class BTreeNodeTest {

    @Test
    public void TestDataIsCorrectOnCreation() {
        BTreeNode node = new BTreeNode<>("Test data");
        Assert.assertEquals(node.getData(), "Test data");
    }

    @Test
    public void TestGetNumberOfChildrenReturnZerOnOnCreation() {
        BTreeNode node = new BTreeNode<String>(null);
        Assert.assertEquals(node.getNumberOfChildren(), 0);
    }

    @Test
    public void TestHasChildrenReturnsFalseOnCreation() {
        BTreeNode node = new BTreeNode<String>(null);
        Assert.assertEquals(node.hasChildren(), false);
    }

    @Test
    public void TestParentIsSelfOnCreation() {
        BTreeNode node = new BTreeNode<String>(null);
        Assert.assertEquals(node, node.getParent());
    }

    @Test
    public void TestSetAndGetData() {
        BTreeNode<String> node = new BTreeNode<>(null);
        Assert.assertEquals(node.getData(), null);
        node.setData("Test Data");
        Assert.assertEquals(node.getData(), "Test Data");
    }

    @Test
    public void TestAddChildAndGetChildCount() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChild(new BTreeNode(null));
        Assert.assertEquals(node.getNumberOfChildren(), 1);
    }

    @Test
    public void TestAddChildAndHasChildrenIsTrue() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChild(new BTreeNode(null));
        Assert.assertEquals(node.hasChildren(), true);
    }

    @Test
    public void TestSetChildrenAddsChildren() {
        BTreeNode<String> node = new BTreeNode<>(null);

        List<BTreeNode<String>> children = new ArrayList<>();
        children.add(new BTreeNode<>("Test"));

        node.setChildren(children);
        Assert.assertEquals(node.getChildren(), children);
    }

    @Test
    public void TestSetChildrenOverridesCurrentChildren() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChild(new BTreeNode<>("Test"));

        List<BTreeNode<String>> children = new ArrayList<>();
        children.add(new BTreeNode<>("Test"));

        node.setChildren(children);
        Assert.assertEquals(node.getChildren(), children);
    }

    @Test
    public void TestAddAndGetChildren() {
        BTreeNode<String> node = new BTreeNode<>(null);
        BTreeNode child = new BTreeNode<String>(null);
        node.addChild(child);
        Assert.assertEquals(node.getChildren().size(), 1);
        Assert.assertEquals(node.getChildren().get(0), child);
    }

    @Test
    public void TestAddAndRemoveChildAt() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChildAt(0, new BTreeNode(null));
        node.removeChildAt(0);
        Assert.assertEquals(node.hasChildren(), false);
    }

    @Test
    public void TestAddAndRemoveChildByObject() {
        BTreeNode<String> node = new BTreeNode<>(null);
        BTreeNode child = new BTreeNode<String>(null);
        node.addChild(child);
        node.removeChild(child);
        Assert.assertEquals(node.hasChildren(), false);
    }

    @Test
    public void TestAddChildrenAddsChildren() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChild(new BTreeNode<>("Test"));

        List<BTreeNode<String>> children = new ArrayList<>();
        children.add(new BTreeNode<>("Test"));

        node.addChildren(children);
        Assert.assertEquals(node.getNumberOfChildren(), 2);
    }

    @Test
    public void TestRemoveChildrenRemovesAllChildren() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChild(new BTreeNode(null));
        node.removeChildren();
        Assert.assertEquals(node.hasChildren(), false);
    }

    @Test(expected = java.lang.IndexOutOfBoundsException.class)
    public void TestAddChildAtThrowsOutOfBoundsException() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChildAt(10, new BTreeNode(null));
    }

    @Test(expected = java.lang.IndexOutOfBoundsException.class)
    public void TestRemoveChildAtThrowsOutOfBoundsException() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.removeChildAt(10);
    }

    @Test
    public void TestAddChildHasCorrectParent() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChild(new BTreeNode(null));
        BTreeNode<String> child = node.getChildren().get(0);
        Assert.assertEquals(child.getParent(), node);
    }

    @Test
    public void TestAddChildAtHasCorrectParent() {
        BTreeNode<String> node = new BTreeNode<>(null);
        node.addChildAt(0, new BTreeNode(null));
        BTreeNode<String> child = node.getChildren().get(0);
        Assert.assertEquals(child.getParent(), node);
    }

    @Test
    public void TestAddChildrenHasCorrectParents() {
        BTreeNode<String> node = new BTreeNode<>(null);

        List<BTreeNode<String>> children = new ArrayList<>();
        children.add(new BTreeNode<>("Test"));

        node.addChildren(children);
        Assert.assertEquals(node.getChildren().get(0).getParent(), node);
    }

    @Test
    public void TestSetChildrenHasCorrectParents() {
        BTreeNode<String> node = new BTreeNode<>(null);

        List<BTreeNode<String>> children = new ArrayList<>();
        children.add(new BTreeNode<>("Test"));

        node.setChildren(children);
        Assert.assertEquals(node.getChildren().get(0).getParent(), node);
    }
}