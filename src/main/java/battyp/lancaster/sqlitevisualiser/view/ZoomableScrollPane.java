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

package battyp.lancaster.sqlitevisualiser.view;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;


/**
 * <h1>< Zoomable Scroll Pane </h1>
 *
 * <p>
 * This class is used to add zoom functionality to the scroll pane,
 * and is used to draw the tree visualisation. Made up of Cells and
 * edges.
 *
 * thanks to: https://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx
 *
 * @author Paul Batty
 * @see Cell
 * @see Edge
 * @since 0.7
 */
public class ZoomableScrollPane extends ScrollPane {

    private Group zoomGroup;
    private Scale scaleTransform;
    private Node content;
    private double scaleValue = 1.0;
    private double delta = 0.1;

    /**
     * Creates a new zoomable pane
     *
     */
    public ZoomableScrollPane(Node content) {
        this.content = content;
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(content);
        setContent(contentGroup);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);

        /**
         * Disable scrolling with mouse wheel
         */
        this.addEventHandler(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();
            }
        });

        /**
         * inner class to handle the zooming
         */
        zoomGroup.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {
                    if (event.getDeltaY() < 0) {
                        zoomOut();
                    } else {
                        zoomIn();
                    }
                event.consume();
            }
        });
    }

    public void centerNode(Node node) {
        double width = this.getContent().getBoundsInLocal().getWidth();
        double height = this.getContent().getBoundsInLocal().getHeight();

        double x = node.getBoundsInParent().getMaxX();
        double y = node.getBoundsInParent().getMaxY();

        // scrolling values range from 0 to 1
        this.setVvalue(y/height);
        this.setHvalue(x/width);
    }


    /**
     * Removes all nodes attached to the pane.
     */
    public void clear() {
        this.zoomGroup.getChildren().clear();
    }

    /**
     * Zooms to the value passed
     *
     * @param scale Value to zoom to
     */
    public void zoomTo(double scale) {
        this.scaleValue = scale;
        scaleTransform.setX(scale);
        scaleTransform.setY(scale);
    }

    /**
     * Zooms out
     */
    public void zoomOut() {
        scaleValue -= delta;
        if (Double.compare(scaleValue, 0.1) < 0) {
            scaleValue = 0.1;
        }
        zoomTo(scaleValue);
    }

    /**
     * Zooms in
     */
    public void zoomIn() {
        scaleValue += delta;
        if (Double.compare(scaleValue, 10) > 0) {
            scaleValue = 10;
        }
        zoomTo(scaleValue);
    }

    public double getScaleValue() {
        return scaleValue;
    }
}
