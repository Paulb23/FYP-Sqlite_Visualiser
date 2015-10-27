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

package battyp.lancaster.sqlitevisualiser.observerinterface;

/**
 * <h1> Subject </h1>
 *
 * <p>
 * This class is used in the observer pattern, where
 * the subject will notify all observers on a event.
 *
 * <p>
 * In the case of this program the subject is the file watcher
 * and will notify subject when the database is modified.
 *
 * @author Paul Batty
 * @see Observer
 * @see battyp.lancaster.sqlitevisualiser.model.filewatcher.FileWatcher
 * @since 0.8
 */
public interface Subject {

    /**
     * Adds a observer to be notified
     *
     * @param observer Observer to add
     */
    public void addObserver(Observer observer);

    /**
     * Removes a observer
     *
     * @param observer Observer to remove
     */
    public void removeObserver(Observer observer);

    /**
     * Removes all observers
     */
    public void removeAllObservers();

    /**
     * Notifies all observers added
     */
    public void notifyObservers();
}
