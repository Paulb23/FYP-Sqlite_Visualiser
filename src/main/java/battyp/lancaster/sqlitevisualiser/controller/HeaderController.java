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

package battyp.lancaster.sqlitevisualiser.controller;

import battyp.lancaster.sqlitevisualiser.model.Model;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import javafx.fxml.FXML;

import javafx.scene.control.TextArea;

/**
 * Controller for header.fxml
 *
 * @author Paul Batty
 */
public class HeaderController extends Controller {

    @FXML
    private TextArea headerTextArea;

    /**
     * Creates a new Controller with the model set
     *
     * @param model The model to use
     */
    public HeaderController(Model model) {
        super(model);
    }

    @FXML
    private void initialise() {
        if (model.isFileOpen()) {
            Metadata metadata = this.model.getDatabase().getMetadata();
            this.headerTextArea.setText(
                            "Page Size: "        + metadata.pageSize                     + "\n" +
                            "write Version:"     + metadata.writeVersion                 + "\n" +
                            "read Version:"      + metadata.readVersion                  + "\n" +
                            "Unused Space page:" + metadata.unusedSpaceAtEndOfEachPage   + "\n" +
                            "Max Embed Payload:" + metadata.maxEmbeddedPayload           + "\n" +
                            "Min Embed Payload:" + metadata.minEmbeddedPayload           + "\n" +
                            "Leaf Payload:"      + metadata.leafPayloadFraction          + "\n" +
                            "Change Counter:"    + metadata.fileChageCounter             + "\n" +
                            "Size in pages:"     + metadata.sizeOfDatabaseInPages        + "\n" +
                            "Freelist Pages:"    + metadata.totalFreeListPages           + "\n" +
                            "Schema cookie:"     + metadata.schemaCookie                 + "\n" +
                            "Schema Format:"     + metadata.schemaFormat                 + "\n" +
                            "Cache Size:"        + metadata.defualtPageCacheSize         + "\n" +
                            "Page Large Tree:"   + metadata.pageNumberToLargestBTreePage + "\n" +
                            "text encoding:"     + metadata.textEncoding                 + "\n" +
                            "User Version:"      + metadata.userVersion                  + "\n" +
                            "Vacuum Mode:"       + metadata.vacuummMode                  + "\n" +
                            "Application ID:"    + metadata.appID                        + "\n" +
                            "Valid Number"       + metadata.versionValidNumber           + "\n" +
                            "Sqlite version:"   + metadata.sqliteVersion                 + "\n"
            );
        }
    }
}