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

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;


/**
 * ZoomableScrollPane for visualising the b-trees
 *
 * @author Paul Batty
 */
public class ZoomableScrollPane extends ScrollPane {

    private Group zoomGroup;
    private Scale scaleTransform;
    private Node content;
    private double scale = 1.0;
    private double delta = 0.1;

    /**
     * Creates a new zoomable pane
     *
     */
    public ZoomableScrollPane() {
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        setContent(contentGroup);
        scaleTransform = new Scale(scale, scale, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);

        /**
         * inner class to handle the zooming
         */
        zoomGroup.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() < 0) {
                    scale -= delta;
                } else {
                    scale += delta;
                }
                zoomTo(scale);
                event.consume();
            }
        });
    }

    /**
     * Sets the content
     */
    public void setNodeContent(Node content) {
        this.zoomGroup.getChildren().add(content);
    }

    /**
     * Zooms to the value passed
     *
     * @param scale Value to zoom to
     */
    public void zoomTo(double scale) {
        this.scale = scale;
        scaleTransform.setX(scale);
        scaleTransform.setY(scale);
    }

    /**
     * Zooms out
     */
    public void zoomOut() {
        scale -= delta;
        if (Double.compare(scale, 0.1) < 0) {
            scale = 0.1;
        }
        zoomTo(scale);
    }

    /**
     * Zooms in
     */
    public void zoomIn() {
        scale += delta;
        if (Double.compare(scale, 10) > 0) {
            scale = 10;
        }
        zoomTo(scale);
    }
}
