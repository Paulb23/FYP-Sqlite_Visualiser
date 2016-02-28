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

package battyp.lancaster.sqlitevisualiser.model.databaseinterface;

import battyp.lancaster.sqlitevisualiser.model.database.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1> Default Database Interface </h1>
 *
 * <p>
 * Default implementation of the Database Interface Interface.
 *
 * @author Paul Batty
 * @see DatabaseInterface
 * @since 0.4
 */
public class DefaultDatabaseInterface implements DatabaseInterface {

    private int current;
    private List<Database> history;

    /**
     * Constructor.
     */
    public DefaultDatabaseInterface() {
        this.current = 0;
        this.history = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Database getCurrent() {
        if (this.current < this.history.size() && this.current >= 0) {
            return this.history.get(this.current);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Database getPrevious() {
        if (this.current - 1 < this.history.size() && this.current - 1 >= 0) {
            return this.history.get(this.current - 1);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Database getNext() {
        if (this.current + 1 < this.history.size() && this.current + 1 >= 0) {
            return this.history.get(this.current + 1);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return  this.history.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentPos() {
        return  this.current;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrent(int current) {
        if (current < this.history.size() && current >= 0) {
            this.current = current;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextStep() {
        if (this.current < this.history.size() - 1) {
            this.current++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void previousStep() {
        if (this.current < this.history.size() + 1 && this.current != 0) {
            this.current--;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDatabase(Database database) {
        this.history.add(database);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.current = 0;
        this.history = new ArrayList<>();
    }
}
