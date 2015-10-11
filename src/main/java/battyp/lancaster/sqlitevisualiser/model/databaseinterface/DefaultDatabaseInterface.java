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
 * DefaultDatabaseInterface is a interface for the databases objects
 *
 * @see battyp.lancaster.sqlitevisualiser.model.database.Database
 *
 * @author Paul Batty
 */
public class DefaultDatabaseInterface implements DatabaseInferface {

    private int current;
    private List<Database> history;

    /**
     * Creates a new DatabaseInterface
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
}
