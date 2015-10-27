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

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

/**
 * <h1> Edge </h1>
 *
 * <p>
 * This class is used to represent edges between cells during the visualisation
 * of the database.
 *
 * @author Paul Batty
 * @see Cell
 * @since 0.7
 */
public class Edge extends Group {

    private Cell source;
    private Cell target;

    private Line line;

    /**
     * Constructor.
     *
     * @param source Starting cell.
     * @param target Ending cell.
     */
    public Edge(Cell source, Cell target) {
        this.source = source;
        this.target = target;

        this.line = new Line();

        line.setStartX(source.getLayoutX() + (source.getBoundsInParent().getWidth() / 2));
        line.setStartY(source.getLayoutY() + (source.getBoundsInParent().getHeight() / 2));

        line.setEndX(target.getLayoutX() + (target.getBoundsInParent().getWidth() / 2));
        line.setEndY(target.getLayoutY() + (target.getBoundsInParent().getHeight() / 2));

        line.setStrokeWidth(2);
        line.setStroke(new Color(Math.random(), Math.random(), Math.random(), 1));
        getChildren().add(line);
    }
}
