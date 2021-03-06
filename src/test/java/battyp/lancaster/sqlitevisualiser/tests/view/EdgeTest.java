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

package battyp.lancaster.sqlitevisualiser.tests.view;

import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeCell;
import battyp.lancaster.sqlitevisualiser.tests.view.mocks.MockCell;
import battyp.lancaster.sqlitevisualiser.view.Cell;
import battyp.lancaster.sqlitevisualiser.view.CellType;
import battyp.lancaster.sqlitevisualiser.view.Edge;
import org.junit.Test;

/**
 * JUnit tests for the Edge class
 *
 * @see battyp.lancaster.sqlitevisualiser.view.Edge
 *
 * @author Paul Batty
 */
public class EdgeTest {

    @Test
    public void TestCreationWithValidCells() {
        new Edge(new MockCell(new BTreeCell(0, 0, 1)), new MockCell(new BTreeCell(0, 0, 0)));
    }

    @Test
    public void TestCreationWithSameCell() {
        Cell cell = new MockCell(new BTreeCell(0, 0, 0));
        new Edge(cell, cell);
    }

    @Test(expected = NullPointerException.class)
    public void TestCreationWithSingleNull() {
        new Edge(new MockCell(null), null);
    }

    @Test(expected = NullPointerException.class)
    public void TestCreationWithNull() {
        new Edge(null, null);
    }
}
