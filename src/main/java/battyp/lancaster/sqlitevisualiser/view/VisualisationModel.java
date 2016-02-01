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

package battyp.lancaster.sqlitevisualiser.view;

import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <h1> Visualisation Model </h1>
 *
 * <p>
 * This class is used to store all the nodes for adjustment in the visualisation.
 *
 * thanks to: https://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx
 *
 * @author Paul Batty
 * @since 0.9
 */
public class VisualisationModel {
    Cell graphParent;

    List<Cell> allCells;
    List<Cell> addedCells;
    List<Cell> removedCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    Map<String,Cell> cellMap; // <id,cell>

    /**
     * Constructor.
     */
    public VisualisationModel() {
        graphParent = new Cell(new BTreeCell(0, 0, 0));
        clear();
    }

    /**
     * Clears everything.
     */
    public void clear() {
        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>
    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }

    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    public List<Cell> getAllCells() {
        return allCells;
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public void addCell(Cell cell) {
        addedCells.add(cell);
        cellMap.put( cell.getCellId(), cell);

    }

    public void addEdge( String sourceId, String targetId) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);

        Edge edge = new Edge( sourceCell, targetCell);

        addedEdges.add( edge);

    }

    /**
     * Attach all cells which don't have a parent to graphParent
     * @param cellList
     */
    public void attachOrphansToGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            if( cell.getCellParents().size() == 0) {
                graphParent.addCellChild( cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     * @param cellList
     */
    public void disconnectFromGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            graphParent.removeCellChild( cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll( addedCells);
        allCells.removeAll( removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}
