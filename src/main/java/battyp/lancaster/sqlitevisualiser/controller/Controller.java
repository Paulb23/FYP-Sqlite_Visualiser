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
import battyp.lancaster.sqlitevisualiser.observerinterface.Observer;

/**
 * <h1> Controller </h1>
 *
 * <p>
 * Abstract Controller that all FXML controllers extends, in order to allow
 * the Model to be injected on creation.
 *
 * <p>
 * This also implements Observer to allow the controller to receive
 * notifications as to when the database file was modified / changed.
 *
 * @author Paul Batty
 * @see Observer
 * @see Model
 * @since 0.5
 */
public abstract class Controller implements Observer {

    /* protected so we stop outside access while
    * removing the need for getters.          */
    protected Model model;

    /**
     * Constructor.
     *
     * @param model The model that this controller will use.
     */
    public Controller(Model model) {
        this.model = model;
    }
}
