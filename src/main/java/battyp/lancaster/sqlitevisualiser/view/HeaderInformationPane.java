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

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * <h1>< Header Information Pane </h1>
 *
 * <p>
 * Implementation of the information panesdisplacedd in the header tab.
 *
 * @author Paul Batty
 * @see battyp.lancaster.sqlitevisualiser.controller.HeaderController
 * @since 0.9
 */
public class HeaderInformationPane extends GridPane {

    private int currentRow;

    /**
     * Constructor.
     *
     * @param title Title of the pane.
     */
    public HeaderInformationPane(String title) {
        this.getStyleClass().add("headerBackgroundInformationPane");
        this.currentRow = 1;

        Label header = new Label(title);
        header.getStyleClass().add("headerTitle");
        this.add(header, 0, 0);
    }

    /**
     * Adds a item to the pane.
     *
     * @param key Key / Name of the item
     * @param value Value of the item.
     */
    public void addItem(String key, String value) {
        Label item = new Label(key);
        item.getStyleClass().add("text");

        Label itemValue = new Label(value);
        itemValue.getStyleClass().add("text");

        this.add(item, 0, currentRow);
        this.add(itemValue, 1, currentRow);

        this.currentRow++;
    }
}
