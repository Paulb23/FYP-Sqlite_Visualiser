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

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DatabaseParser;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.IOException;
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

    private boolean live;
    private String path;
    private DatabaseParser databaseParser;
    private DatabaseInterface databaseInterface;

    /**
     * Constructor.
     */
    public DefaultLiveUpdater() {
        live = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatabase(String path, DatabaseParser databaseParser, DatabaseInterface databaseInterface) {
        this.path = path;
        this.databaseParser = databaseParser;
        this.databaseInterface = databaseInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String path, DatabaseParser databaseParser, DatabaseInterface databaseInterface) throws IOException, InvalidFileException {
        Database newDatabase = databaseParser.parseDatabase(path, new Database(new BTree(), new Metadata()));
        Database previousDatabase = databaseInterface.getCurrent();

        if (previousDatabase != null && newDatabase != null) {
            BTreeNode<BTreeCell> oldRoot = previousDatabase.getBTree().getRoot();
            BTreeNode<BTreeCell> newRoot = newDatabase.getBTree().getRoot();

            if (oldRoot != null && newRoot != null) {
                Stack<BTreeCell> oldTree = new Stack<>();
                Stack<BTreeCell> newTree = new Stack<>();

                // create stacks
                addNodesToStack(oldRoot, oldTree);
                addNodesToStack(newRoot, newTree);

                // check current node for changes
                int numNodes = oldTree.size();
                for (int i = 0; i < numNodes; i++) {
                    BTreeCell oldCell = oldTree.pop();
                    BTreeCell newCell = newTree.pop();

                    if (oldCell.hashCode() != newCell.hashCode()) {
                        newCell.changed = true;
                    }
                }
            }
        }

        databaseInterface.addDatabase(newDatabase);
        if (live) {
            databaseInterface.nextStep();
        }
    }

    private void addNodesToStack(BTreeNode<BTreeCell> node, Stack<BTreeCell> stack) {

        List<BTreeNode<BTreeCell>> children = node.getChildren();
        if (node.getNumberOfChildren() > 0) {
            for (BTreeNode<BTreeCell> child : children) {
                addNodesToStack(child, stack);
            }
        }

        stack.add(node.getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObserver() {
        try {
            if (path != null && databaseParser != null && databaseInterface != null) {
                update(path, databaseParser, databaseInterface);
            }
        } catch (IOException e) {
        } catch (InvalidFileException e) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextStep() {
        this.databaseInterface.nextStep();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void previousStep() {
        this.databaseInterface.previousStep();
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
