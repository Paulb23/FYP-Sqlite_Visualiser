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

package battyp.lancaster.sqlitevisualiser.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * <h1> File Util </h1>
 *
 * <p>
 * This class is used to provide global utility methods
 * to handle files.
 *
 * @author Paul Batty
 * @since 0.6
 */
public class FileUtil {

    /**
     * Private constructor as it's a util class.
     */
    private FileUtil() {
    }

    /**
     * Opens a file.
     *
     * @param pathToFile Path to the file including file name and extension.
     *
     * @return The file.
     *
     * @throws FileNotFoundException If the file does not exist.
     */
    public static File openFile(String pathToFile) throws FileNotFoundException {
        /**
         * Try to load from resources if fails then convert to
         * URI --> URL
         */
        URL fileURL = FileUtil.class.getClassLoader().getResource(pathToFile);
        if (fileURL == null) {
            try {
                fileURL = new File(pathToFile).toURI().toURL();

            } catch (MalformedURLException e) {
                throw new FileNotFoundException();
            }
        }

        File file;
        try {
            file = new File(fileURL.toURI());
        } catch (URISyntaxException e) {
            file = new File(fileURL.getPath());
        }

        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }
}
