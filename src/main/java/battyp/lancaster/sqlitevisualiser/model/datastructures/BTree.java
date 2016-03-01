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

import java.util.ArrayList;
import java.util.List;

/**
 * <h1> B-Tree </h1>
 *
 * <p>
 * This class implements a very basic B-tree data structure with generic
 * typing.  Using BtreeNode, as the nodes inside the tree.
 *
 * @author Paul Batty
 * @see BTreeNode
 * @since 0.1
 */
public class BTree<T> {

    private BTreeNode<T> root;

    /**
     * Gets the number of nodes in the tree.
     *
     * @return Number of nodes in the tree.
     */
    public int getNumberOfNodes() {
        if (this.root == null) {
            return 0;
        }

        int numberOfNodes = 0;
        List<BTreeNode<T>> currentNodes = new ArrayList<>();
        List<BTreeNode<T>> nextNodes = new ArrayList<>();

        currentNodes.addAll(this.root.getChildren());
        while (currentNodes.size() > 0) {
            for (BTreeNode<T> node : currentNodes) {
                numberOfNodes += 1;
                nextNodes.addAll(node.getChildren());
            }
            currentNodes = nextNodes;
            nextNodes = new ArrayList<>();
        }
        return numberOfNodes + 1;
    }

    /**
     * Finds the node with the corresponding data, using depth first
     * search.
     *
     * @param data Data to search for
     *
     * @return The Node with the corresponding data.
     */
    public BTreeNode<T> find(final T data) {
        if (this.root == null) {
            return null;
        }
        if (this.root.getData().equals(data)) {
            return this.root;
        }

        List<BTreeNode<T>> currentNodes = new ArrayList<>();
        List<BTreeNode<T>> nextNodes = new ArrayList<>();

        currentNodes.addAll(this.root.getChildren());
        while (currentNodes.size() > 0) {
            for (BTreeNode<T> node : currentNodes) {
                if (node.getData().equals(data)) {
                    return node;
                }
                nextNodes.addAll(node.getChildren());
            }
            currentNodes = nextNodes;
            nextNodes = new ArrayList<>();
        }
        return null;
    }

    /**
     * Finds the node with the corresponding data, using depth first
     * search, returns true if found else false.
     *
     * @param data Data to search for.
     *
     * @return true if a node is found else false.
     */
    public boolean exists(final T data) {
        return (find(data) != null);
    }

    /**
     * Sets the root to the node passed in.
     *
     * @param node The node to set as root.
     */
    public void setRoot(BTreeNode<T> node) {
        this.root = node;
    }

    /**
     * Gets the root node of the tree.
     *
     * @return Root node of the tree.
     */
    public BTreeNode<T> getRoot() {
        return this.root;
    }

    /**
     * Gets whether the tree is empty.
     *
     * @return True if empty else false.
     */
    public boolean isEmpty() {
        return (this.root == null);
    }
}
