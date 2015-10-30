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

package battyp.lancaster.sqlitevisualiser.model.log;

import battyp.lancaster.sqlitevisualiser.util.FileUtil;

import javax.security.auth.callback.Callback;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1> Default Log </h1>
 *
 * <p>
 * Default implementation of Log.
 *
 * @author Paul Batty
 * @see Log
 * @since 0.9
 */
public class DefaultLog implements Log {


    private String pathToDatabase;
    private List<String> sqlLog;

    /**
     * Constructor.
     */
    public DefaultLog() {
        this.sqlLog = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFile(String pathToDatabase) {
        this.pathToDatabase = pathToDatabase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLog() {
        return this.sqlLog;
    }
}
