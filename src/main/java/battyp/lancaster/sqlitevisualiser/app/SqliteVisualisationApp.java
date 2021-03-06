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

package battyp.lancaster.sqlitevisualiser.app;

import battyp.lancaster.sqlitevisualiser.model.DefaultModel;

/**
 * <h1> Sqlite Visualisation App </h1>
 *
 * <p>
 * This Class only contains the main method.
 * It is designed to conduct the start up of the application and deal with any
 * special command line arguments that are passed in.
 *
 * @author Paul Batty
 * @version 1.0
 * @since 0.5
 */
public class SqliteVisualisationApp {

    /**
     * <h1> Main. </h1>
     *
     * <p>
     * Starting point for the application.
     *
     * @param args Command line arguments, Currently non are supported so does
     *             nothing.
     */
    public static void main(String args[]) {

        // help fix blurry font..
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");

        /* Use the static calls to start javaFx else
         * it wont launch properly.               */
        SqliteVisualiser.setModel(new DefaultModel());
        SqliteVisualiser.launch(SqliteVisualiser.class);
    }
}
