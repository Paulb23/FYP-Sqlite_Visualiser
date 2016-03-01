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

package battyp.lancaster.sqlitevisualiser.model.datastructures;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * <h1> B-Tree Node </h1>
 *
 * <p>
 * This class is a generic implementation of a node for
 * use inside the Btree data structure.
 *
 * @author Paul Batty
 * @see BTree
 * @since 0.1
 */
public class BTreeNode<T> {

    private T data;
    private List<BTreeNode<T>> children;
    private BTreeNode<T> parent;

    /**
     * Creates a new BTreeNode with the specified type.
     *
     * @param data The data to store inside the node.
     */
    public BTreeNode(T data) {
        this.data = data;
        this.children = new ArrayList<>();
        this.parent = this;
    }

    /**
     * Creates a new BTreeNode with data as null.
     */
    public BTreeNode() {
        this.children = new ArrayList<>();
        this.parent = this;
    }

    /**
     * Gets this nodes parent.
     *
     * @return This nodes parent.
     */
    public BTreeNode<T> getParent() {
        return this.parent;
    }

    /**
     * Sets this nodes parent.
     *
     * @param parent The new parent of the node.
     */
    public void setParent(BTreeNode<T> parent) {
        this.parent = parent;
    }

    /**
     * Adds this node, and all children including, children's children to a stack.
     *
     * @return Stack containing all children.
     */
    public Stack<T> childrenToStack() {
        Stack<T> stack = new Stack<>();
        addNodesToStack(this, stack);
        return stack;
    }

    /**
     * Takes a tree and turns it into a stack.
     *
     * @param node Root node.
     * @param stack Stack to add the nodes to.
     */
    private void addNodesToStack(BTreeNode<T> node, Stack<T> stack) {
        List<BTreeNode<T>> children = node.getChildren();
        if (node.getNumberOfChildren() > 0) {
            for (BTreeNode<T> child : children) {
                addNodesToStack(child, stack);
            }
        }
        stack.add(node.getData());
    }

    /**
     * Gets this nodes children.
     *
     * @return This nodes children.
     */
    public List<BTreeNode<T>> getChildren() {
        return this.children;
    }

    /**
     * Gets the number of children attached to this node.
     *
     * @return Number of children attached to this node.
     */
    public int getNumberOfChildren() {
        return children.size();
    }

    /**
     * Get whether the node has any children true if so else false.
     *
     * @return True if the node has children else false.
     */
    public boolean hasChildren() {
        return (children.size() > 0);
    }

    /**
     * Sets the children to this node to the list passed in
     * Removes any existing children.
     *
     * @param children Children to set.
     */
    public void setChildren(List<BTreeNode<T>> children) {
        for (BTreeNode<T> child : children) {
            child.setParent(this);
        }
        this.children = children;
    }

    /**
     * Adds a child to the node.
     *
     * @param child The child node to add.
     */
    public void addChild(BTreeNode<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Adds a collection children to the node.
     *
     * @param children The children to add.
     */
    public void addChildren(List<BTreeNode<T>> children) {
        for (BTreeNode<T> child : children) {
            child.setParent(this);
            this.children.add(child);
        }
    }

    /**
     * Adds a child at the given index.
     *
     * @param index Index to add the child at.
     * @param child The child to add.
     */
    public void addChildAt(final int index, BTreeNode<T> child) {
        child.setParent(this);
        this.children.add(index, child);
    }

    /**
     * removes all children from the node.
     */
    public void removeChildren() {
        this.children = new ArrayList<>();
    }

    /**
     * remove the child form this node.
     *
     * @param child The child to remove.
     */
    public void removeChild(BTreeNode<T> child) {
        this.children.remove(child);
    }

    /**
     * Removed a child at the given index.
     *
     * @param index Index of the child to remove.
     */
    public void removeChildAt(final int index) {
        this.children.remove(index);
    }

    /**
     * Gets the data stored inside the node.
     *
     * @return Data stored in the node.
     */
    public T getData() {
        return this.data;
    }

    /**
     * Sets the data in  the node
     *
     * @param data The data to set to.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * calculate the hash of this node, sum of all children hashes.
     */
    @Override
    public int hashCode() {
        if (this.getNumberOfChildren() == 0) {
            return 1;
        }
        final int prime = 31;
        int hash = 1;
        for (BTreeNode<T> child : children) {
            hash = hash * prime + child.hashCode();
            hash = hash * prime + child.getData().hashCode();
        }
        return hash;
    }

    /**
     * Compares to object. They are equal if they contain the
     * same data and children.
     *
     * @param obj Other node to compare with.
     *
     * @return True if equal else false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)  {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        BTreeNode<?> node = (BTreeNode<?>) obj;
        if (this.data != null && node.getData() != null) {
            if (!this.data.equals(node.getData())) {
                return false;
            }
        } else if (this.data == null && node.getData() != null) {
            return false;
        } else if (this.data != null && node.getData() == null) {
            return false;
        }
        return (this.hashCode() == obj.hashCode());
    }
}
