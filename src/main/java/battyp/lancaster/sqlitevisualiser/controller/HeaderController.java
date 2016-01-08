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
import battyp.lancaster.sqlitevisualiser.view.HeaderInformationPane;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;

import javafx.scene.layout.FlowPane;

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
    private FlowPane headerFlowPane;

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
     * Called on creation and when a database updated
     * is detected.
     *
     * This is designed to reload / display the information for this view.
     */
    private void reload() {
        if (model.isFileOpen()) {
            Metadata metadata = this.model.getDatabase().getMetadata();
            headerFlowPane.getChildren().clear();

            HeaderInformationPane generic = new HeaderInformationPane("Generic");
            generic.addItem("Filename", "Name");
            generic.addItem("ApplicationID", String.valueOf(metadata.appID));
            generic.addItem("Database Size", String.valueOf(metadata.pageSize * metadata.sizeOfDatabaseInPages) + " Bytes");
            generic.addItem("Number of changes", String.valueOf(metadata.fileChangeCounter));
            headerFlowPane.getChildren().add(generic);


            HeaderInformationPane type = new HeaderInformationPane("Type");
            type.addItem("Schema Cookie", String.valueOf(metadata.schemaCookie));
            type.addItem("Schema Format", String.valueOf(metadata.schemaFormat));
            type.addItem("Text Encoding", String.valueOf(metadata.textEncoding));
            headerFlowPane.getChildren().add(type);


            HeaderInformationPane version = new HeaderInformationPane("Version");
            version.addItem("User Version", String.valueOf(metadata.userVersion));
            version.addItem("Write Version", String.valueOf(metadata.writeVersion));
            version.addItem("Read Version", String.valueOf(metadata.readVersion));
            version.addItem("Valid Version", String.valueOf(metadata.versionValidNumber));
            version.addItem("Sqlite Version", String.valueOf(metadata.sqliteVersion));
            headerFlowPane.getChildren().add(version);


            HeaderInformationPane pageInfo = new HeaderInformationPane("Page Information");
            pageInfo.addItem("Page Size", String.valueOf(metadata.pageSize));
            pageInfo.addItem("Page Count", String.valueOf(metadata.sizeOfDatabaseInPages));
            pageInfo.addItem("Freelist count", String.valueOf(metadata.totalFreeListPages));
            pageInfo.addItem("Unused space", String.valueOf(metadata.unusedSpaceAtEndOfEachPage));
            pageInfo.addItem("Max Payload", String.valueOf(metadata.maxEmbeddedPayload));
            pageInfo.addItem("Min Payload", String.valueOf(metadata.minEmbeddedPayload));
            pageInfo.addItem("Leaf Payload", String.valueOf(metadata.leafPayloadFraction));
            pageInfo.addItem("Page of Largest Tree", String.valueOf(metadata.pageNumberToLargestBTreePage));
            headerFlowPane.getChildren().add(pageInfo);


            HeaderInformationPane misc = new HeaderInformationPane("Misc");
            misc.addItem("Cache Size", String.valueOf(metadata.defaultPageCacheSize));
            misc.addItem("Vacuum Mode", String.valueOf(metadata.vacuumMode));
            headerFlowPane.getChildren().add(misc);
        }
    }
}
