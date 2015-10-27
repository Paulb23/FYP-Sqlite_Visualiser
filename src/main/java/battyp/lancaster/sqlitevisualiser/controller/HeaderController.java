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
 * <h1> Header Controller </h1>
 *
 * <p>
 * FXML Controller for the Header information tab, located in header.fxml.
 *
 * <p>
 * All of the data that is needed for this tab is stored in the Metadata object, so is
 * fairly simple to show in the view.
 *
 * @author Paul Batty
 * @see Metadata
 * @since 0.5
 */
public class HeaderController extends Controller {

    @FXML
    private TextArea headerTextArea;

    /**
     * Constructor.
     *
     * @param model The model that this controller will use.
     */
    public HeaderController(Model model) {
        super(model);
    }

    /**
     * {@inheritDoc}
     */
    public void notifyObserver() {
        reload();
    }

    /**
     * Injected via FXML but also called on creation and when a database updated
     * is detected.
     *
     * This is designed to reload / display the information for this view.
     */
    @FXML
    private void reload() {
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
                            "Change Counter:"    + metadata.fileChangeCounter + "\n" +
                            "Size in pages:"     + metadata.sizeOfDatabaseInPages        + "\n" +
                            "Freelist Pages:"    + metadata.totalFreeListPages           + "\n" +
                            "Schema cookie:"     + metadata.schemaCookie                 + "\n" +
                            "Schema Format:"     + metadata.schemaFormat                 + "\n" +
                            "Cache Size:"        + metadata.defaultPageCacheSize + "\n" +
                            "Page Large Tree:"   + metadata.pageNumberToLargestBTreePage + "\n" +
                            "text encoding:"     + metadata.textEncoding                 + "\n" +
                            "User Version:"      + metadata.userVersion                  + "\n" +
                            "Vacuum Mode:"       + metadata.vacuumMode + "\n" +
                            "Application ID:"    + metadata.appID                        + "\n" +
                            "Valid Number"       + metadata.versionValidNumber           + "\n" +
                            "Sqlite version:"   + metadata.sqliteVersion                 + "\n"
            );
        }
    }
}
