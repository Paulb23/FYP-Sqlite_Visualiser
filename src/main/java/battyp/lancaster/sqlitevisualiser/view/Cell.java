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


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Cell AKA node in the the b-tree
 *
 * @author Paul Batty
 */
public class Cell extends Pane {

    private CellType type;

    /**
     * Creates a new cell
     *
     * @param type Type of the cell
     */
    public Cell(CellType type) {
        if (type == null) {
          type = CellType.Default;
        }
        this.type= type;
        setView();
    }

    /**
     * Set the view based on the type
     */
    private void setView() {
        switch(this.type) {
            case Default: {
                Rectangle view = new Rectangle( 50,50);
                view.setStroke(Color.DODGERBLUE);
                view.setFill(Color.DODGERBLUE);

                getChildren().add(view);
            }
            break;
            case Table: {
                Rectangle view = new Rectangle( 50,50);
                view.setStroke(Color.INDIANRED);
                view.setFill(Color.INDIANRED);

                getChildren().add(view);
            }
            break;
            case Data: {
                Rectangle view = new Rectangle( 50,50);
                view.setStroke(Color.CHARTREUSE);
                view.setFill(Color.CHARTREUSE);

                getChildren().add(view);
            }
            break;
            case Table_Pointer_Internal: {
                Rectangle view = new Rectangle( 50,50);
                view.setStroke(Color.DARKORANGE);
                view.setFill(Color.DARKORANGE);

                getChildren().add(view);
            }
            break;
            case Index_Leaf: {
                Rectangle view = new Rectangle( 50,50);
                view.setStroke(Color.DARKGOLDENROD);
                view.setFill(Color.DARKGOLDENROD);

                getChildren().add(view);
            }
            break;
            case Index_Pointer_Internal: {
                Rectangle view = new Rectangle( 50,50);
                view.setStroke(Color.DARKSLATEBLUE);
                view.setFill(Color.DARKSLATEBLUE);

                getChildren().add(view);
            }
            break;
        }
    }
}
