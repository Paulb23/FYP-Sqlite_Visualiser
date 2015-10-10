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

package battyp.lancaster.sqlitevisualiser.model.databaseparser;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * DefaultDatabaseParser is a database parser for Sqlite databases
 *
 * @author Paul Batty
 */
public class DefaultDatabaseParser implements DatabaseParser {

    private static final int MAGIC_NUMBER_LENGTH = 16;
    private static final int MAGIC_NUMBER[] = new int[] {0x53, 0x51, 0x4c, 0x69, 0x74, 0x65, 0x20, 0x66, 0x6f, 0x72, 0x6d, 0x61, 0x74, 0x20, 0x33, 0x00};

    /**
     * {@inheritDoc}
     */
    @Override
    public Database parseDatabase(String pathToDatabase, Database database) throws FileNotFoundException, InvalidFileException {
        URL file = getClass().getClassLoader().getResource(pathToDatabase);
        if (file == null) {
            throw new FileNotFoundException();
        }

        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(file.toURI())));
        } catch (URISyntaxException e) {
            throw new FileNotFoundException();
        }

        checkMagicNumber(in);

        return database;
    }

    /**
     * Checks the input stream against the magic number
     *
     * @param in The InputStream
     * @throws InvalidFileException Invalid magic number
     */
    private void checkMagicNumber(InputStream in) throws InvalidFileException {
        try {
            for (int i = 0; i < MAGIC_NUMBER_LENGTH; i++) {
                if (in.read() != MAGIC_NUMBER[i]) {
                    throw new InvalidFileException();
                }
            }
        } catch (IOException e) {
            throw new InvalidFileException();
        }
    }
}