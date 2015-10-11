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
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * DefaultDatabaseParser is a database parser for Sqlite databases
 *
 * @see <a href="https://www.sqlite.org/fileformat2.html">Sqllite file format</a>
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

        /**
         * Try to load from resources if fails then convert to
         * URI --> URL
         */
        URL file = getClass().getClassLoader().getResource(pathToDatabase);
        if (file == null) {
            try {
                file = new File(pathToDatabase).toURI().toURL();

                if (file == null) {
                    throw new FileNotFoundException();
                }
            } catch (MalformedURLException e) {
                throw new FileNotFoundException();
            }
        }

        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(file.toURI())));
        } catch (URISyntaxException e) {
            throw new FileNotFoundException();
        }

        checkMagicNumber(in);
        readSqliteHeader(in, database.getMetadata());

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return database;
    }

    /**
     * Reads the first 100 byte header in the database
     * @param in The InputStream
     * @param metadata the metadata object to store to
     */
    private void readSqliteHeader(InputStream in, Metadata metadata) throws InvalidFileException {
        metadata.pageSize = readShort(in);
        metadata.writeVersion = readByte(in);
        metadata.readVersion = readByte(in);
        metadata.unusedSpaceAtEndOfEachPage = readByte(in);
        metadata.maxEmbeddedPayload = readByte(in);
        metadata.minEmbeddedPayload = readByte(in);
        metadata.leafPayloadFraction = readByte(in);
        metadata.fileChageCounter = readInt(in);
        metadata.sizeOfDatabaseInPages = readInt(in);
        metadata.pageNumberOfFirstFreelistPage = readInt(in);
        metadata.totalFreeListPages = readInt(in);
        metadata.schemaCookie = readInt(in);
        metadata.schemaFormat = readInt(in);
        metadata.defualtPageCacheSize = readInt(in);
        metadata.pageNumberToLargestBTreePage = readInt(in);
        metadata.textEncoding = readInt(in);
        metadata.userVersion = readInt(in);
        metadata.vacuummMode = readInt(in);
        metadata.appID = readInt(in);
        skipBytes(in, 20);
        metadata.versionValidNumber = readInt(in);
        metadata.sqliteVersion = readInt(in);
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

    /**
     * Read a byte, 1 bytes from the input stream
     *
     * @param in the input stream to read from
     */
    private int readByte(InputStream in) throws InvalidFileException {
        try {
            byte[] bytes = new byte[1];
            in.read(bytes, 0, 1);
            return (bytes[0] & 0xFF);
        } catch (IOException e) {
            throw new InvalidFileException();
        }
    }

    /**
     * Read a short, 2 bytes from the input stream
     *
     * @param in the input stream to read from
     */
    private int readShort(InputStream in) throws InvalidFileException {
        try {
            byte[] bytes = new byte[2];
            in.read(bytes, 0, 2);
            return ByteBuffer.wrap(bytes).getShort();
        } catch (IOException e) {
            throw new InvalidFileException();
        }
    }

    /**
     * Read a int, 4 bytes from the input stream
     *
     * @param in the input stream to read from
     */
    private int readInt(InputStream in) throws InvalidFileException {
        try {
            byte[] bytes = new byte[4];
            in.read(bytes, 0, 4);
            return ByteBuffer.wrap(bytes).getInt();
        } catch (IOException e) {
            throw new InvalidFileException();
        }
    }

    /**
     * Skips the number of bytes passed int
     *
     * @param in the input stream to read from
     * @param bytesTiSkip The number of bytes to skip
     */
    private void skipBytes(InputStream in, long bytesTiSkip) throws InvalidFileException {
        try {
            in.skip(bytesTiSkip);
        } catch (IOException e) {
            throw new InvalidFileException();
        }
    }
}