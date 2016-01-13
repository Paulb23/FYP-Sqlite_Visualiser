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

package battyp.lancaster.sqlitevisualiser.model.log;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * <h1> Default Log </h1>
 *
 * <p>
 * Default implementation of Log.
 *
 * @author Paul Batty
 * @see Log
 * @since 0.9
 */
public class DefaultLog implements Log {


    private String pathToDatabase;
    private List<String> sqlLog;

    /**
     * Constructor.
     */
    public DefaultLog() {
        this.sqlLog = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detectChanges(Database newDatabase, Database previousDatabase) {
        if (previousDatabase != null && newDatabase != null) {
            BTreeNode<BTreeCell> oldRoot = previousDatabase.getBTree().getRoot();
            BTreeNode<BTreeCell> newRoot = newDatabase.getBTree().getRoot();

            if (oldRoot != null && newRoot != null) {
                Stack<BTreeCell> oldTree = oldRoot.childrenToStack();
                Stack<BTreeCell> newTree = newRoot.childrenToStack();

                detectTreeChanges(newTree, oldTree);
                detectDataChanges(newTree, oldTree);
            }
        }
    }

    private void detectTreeChanges(Stack<BTreeCell> newTree, Stack<BTreeCell> oldTree) {
        int newNumNodes = newTree.size();
        int oldNumNodes = oldTree.size();

        if (newNumNodes > oldNumNodes) {
            detectAddedPages(newTree, oldTree);
        } else if (newNumNodes < oldNumNodes) {
            detectRemovedPages(newTree, oldTree);
        }
    }

    private void detectAddedPages(Stack<BTreeCell> newTree, Stack<BTreeCell> oldTree) {
        detectPageChanges(newTree, oldTree, "ADDED PAGE ");
    }

    private void detectRemovedPages(Stack<BTreeCell> newTree, Stack<BTreeCell> oldTree) {
        detectPageChanges(oldTree, newTree, "REMOVED PAGE ");
    }

    private void detectPageChanges(Stack<BTreeCell> newTree, Stack<BTreeCell> oldTree, String message) {
        for (BTreeCell n : newTree) {
            for (BTreeCell nn : oldTree) {
                if (nn.pageNumber != n.pageNumber) {
                    sqlLog.add(message + "'" + n.pageNumber + "'");
                }
            }
        }
    }

    private void detectDataChanges(Stack<BTreeCell> newTree, Stack<BTreeCell> oldTree) {
        for (BTreeCell newTreeCell : newTree) {
            for (BTreeCell oldTreeCell : oldTree) {
                if (newTreeCell.pageNumber == oldTreeCell.pageNumber) {

                    int newCellCount = newTreeCell.cellCount;
                    int oldCellCount = oldTreeCell.cellCount;

                    if (newCellCount > oldCellCount) {
                        detectAddedRows(newTreeCell, oldTreeCell);
                    } else if (newCellCount < oldCellCount) {
                        detectRemovedRows(newTreeCell, oldTreeCell);
                    } else if (!oldTreeCell.equals(newTreeCell)) {
                        detectUpdatedRows(newTreeCell, oldTreeCell);
                    }
                }
            }
        }
    }

    private void detectAddedRows(BTreeCell newCell, BTreeCell oldCell) {
        detectRowChanges(newCell, oldCell, "ADDED ");
    }

    private void detectRemovedRows(BTreeCell newCell, BTreeCell oldCell) {
        detectRowChanges(oldCell, newCell, "REMOVED ");
    }

    private void detectRowChanges(BTreeCell mainCell, BTreeCell comparisonCell, String message) {
        mainCell.changed = true;
        List<String> newCellData = new ArrayList<>(Arrays.asList(mainCell.data));
        newCellData.removeAll(new ArrayList<>(Arrays.asList(comparisonCell.data)));
        sqlLog.addAll(newCellData.stream().map(s -> message + "'" + s + "'").collect(Collectors.toList()));
    }

    private void detectUpdatedRows(BTreeCell newCell, BTreeCell oldCell) {
        newCell.changed = true;
        String[] oldData = oldCell.data;
        String[] newData = newCell.data;
        int size = oldCell.cellCount;
        for (int j = 0; j < size; j++) {
            if (!oldData[j].equals(newData[j])) {
                sqlLog.add("'" + oldData[j] + "' TO '" + newData[j] + "'");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFile(String pathToDatabase) {
        this.pathToDatabase = pathToDatabase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLog() {
        return this.sqlLog;
    }
}
