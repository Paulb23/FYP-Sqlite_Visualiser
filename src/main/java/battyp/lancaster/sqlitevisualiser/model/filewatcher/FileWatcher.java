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

package battyp.lancaster.sqlitevisualiser.model.filewatcher;

import battyp.lancaster.sqlitevisualiser.observerinterface.Subject;

import java.io.IOException;

/**
 * <h1> File Watcher </h1>
 *
 * <p>
 * This class is designed to watch a file for modifications, once detected
 * notify all of it's observers.  It runs in a separate thread as not to
 * disrupt the normal flow of the program.
 *
 * @author Paul Batty
 * @since 0.8
 */
public interface FileWatcher extends Subject, Runnable {

    /**
     * Stops the thread.
     */
    public void terminate();

    /**
     * Gets the current file path.
     *
     * @return File path.
     */
    public String getFilePath();

    /**
     * Gets the current file name.
     *
     * @return File name.
     */
    public String getFileName();

    /**
     * Sets the file to watch.
     *
     * @param path Path to file including name and extension.
     */
    public void setFile(String path) throws IOException;
}
