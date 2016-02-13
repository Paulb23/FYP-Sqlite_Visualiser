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

import javafx.beans.property.*;

/**
 * <h1> Visualisation Table Cell </h1>
 *
 * <p>
 * Intermediate class for the table in the Visualisation tab.
 *
 * @author Paul Batty
 * @see battyp.lancaster.sqlitevisualiser.controller.VisualisationController
 * @since 0.9
 */
public class VisualisationTableCell {
    public final StringProperty cell;
    public final StringProperty leftChild;
    public final StringProperty rowId;
    public final StringProperty payloadSize;
    public final StringProperty payload;

    /**
     * Contructor.
     *  @param cell The cell id
     * @param leftChild The left child pointer
     * @param rowId The row id.
     * @param payloadSize The payload size.
     * @param payload the payload.
     */
    public VisualisationTableCell(int cell, int leftChild, long rowId, long payloadSize, String payload) {
        this.cell = new SimpleStringProperty(String.valueOf(cell));
        this.leftChild = new SimpleStringProperty(String.valueOf(leftChild));
        this.rowId = new SimpleStringProperty(String.valueOf(rowId));
        this.payloadSize = new SimpleStringProperty(String.valueOf(payloadSize));
        this.payload = new SimpleStringProperty(payload);
    }

}

