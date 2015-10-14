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
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTree;
import battyp.lancaster.sqlitevisualiser.model.datastructures.BTreeNode;
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

    private static final int TABLE_BTREE_LEAF_CELL = 0x0d;
    private static final int TABLE_BTREE_INTERIOR_CELL = 0x05;
    private static final int INDEX_BTREE_LEAF_CELL = 0x0a;
    private static final int INDEX_BTREE_INTERIOR_CELL = 0x02;

    /**
     * {@inheritDoc}
     */
    @Override
    public Database parseDatabase(String pathToDatabase, Database database) throws IOException, FileNotFoundException, InvalidFileException {

        File file = openDatabase(pathToDatabase);
        RandomAccessFile in = new RandomAccessFile(file, "r");

        checkMagicNumber(in);
        readSqliteHeader(in, database.getMetadata());
        readBTrees(in, database);

        in.close();
        return database;
    }

    /**
     * Opens the database file
     *
     * @param pathToDatabase Path to the database including file name and extension
     *
     * @return The file
     * @throws FileNotFoundException
     */
    private File openDatabase(String pathToDatabase) throws FileNotFoundException {
        /**
         * Try to load from resources if fails then convert to
         * URI --> URL
         */
        URL fileURL = getClass().getClassLoader().getResource(pathToDatabase);
        if (fileURL == null) {
            try {
                fileURL = new File(pathToDatabase).toURI().toURL();

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
        return file;
    }

    /**
     * Checks the input stream against the magic number
     *
     * @param in The InputStream
     * @throws InvalidFileException Invalid magic number
     */
    private void checkMagicNumber(RandomAccessFile in) throws InvalidFileException {
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
     * Reads the first 100 byte header in the database
     *
     * @param in The InputStream
     * @param metadata the metadata object to store to
     */
    private void readSqliteHeader(RandomAccessFile in, Metadata metadata) throws IOException, InvalidFileException {
        metadata.pageSize = in.readShort();
        metadata.writeVersion = in.readByte();
        metadata.readVersion = in.readByte();
        metadata.unusedSpaceAtEndOfEachPage = in.readByte();
        metadata.maxEmbeddedPayload = in.readByte();
        metadata.minEmbeddedPayload = in.readByte();
        metadata.leafPayloadFraction = in.readByte();
        metadata.fileChageCounter = in.readInt();
        metadata.sizeOfDatabaseInPages = in.readInt();
        metadata.pageNumberOfFirstFreelistPage = in.readInt();
        metadata.totalFreeListPages = in.readInt();
        metadata.schemaCookie = in.readInt();
        metadata.schemaFormat = in.readInt();
        metadata.defualtPageCacheSize = in.readInt();
        metadata.pageNumberToLargestBTreePage = in.readInt();
        metadata.textEncoding = in.readInt();
        metadata.userVersion = in.readInt();
        metadata.vacuummMode = in.readInt();
        metadata.appID = in.readInt();
        in.skipBytes(20);
        metadata.versionValidNumber = in.readInt();
        metadata.sqliteVersion = in.readInt();
    }

    /**
     * Reads the Btree sections of the file
     *
     * @param in The inputStream
     * @param database The tree to store in
     * @throws InvalidFileException
     */
    private void readBTrees(RandomAccessFile in, Database database) throws  IOException, InvalidFileException {

        BTreeNode root = parseBtree(in);

        

        database.getBTree().setRoot(root);
    }

    public BTreeNode parseBtree(RandomAccessFile in) throws IOException, InvalidFileException {
        BTreeNode<String> node = new BTreeNode();

        // read b-tree header
        int type = in.readByte();
        int firstFreeBlockOffset = in.readShort();
        int numberOfCells = in.readShort();
        int startOfCell = in.readShort();
        if (startOfCell == 0) {
            startOfCell = 65536;
        }
        int fagmentedFreeBytes = in.readByte();

        if (type == INDEX_BTREE_INTERIOR_CELL || type == INDEX_BTREE_LEAF_CELL) {
            int rightMostPointer = in.readInt();
        }

        // get pointer array
        int[] cellPointers = new int[numberOfCells];
        for (int i = 0; i < numberOfCells; i++) {
            cellPointers[i] = in.readShort();
        }

        // follow pointer and get b-tree
        for (int i = 0; i < numberOfCells; i++) {
            if (cellPointers[i] == 0) {
                return node;
            }

            in.seek(cellPointers[i]);

            // parse that type of b-tree
            switch (type) {
                case TABLE_BTREE_LEAF_CELL: {
                    // varint
                    // varint
                    // payload
                    // overflow pages
                }
                break;
                case TABLE_BTREE_INTERIOR_CELL: {
                    int leftChildPointer = in.readInt();
                    byte[] varint = new byte[1];
                    in.read(varint);
                    long rowID = decodeVarint(in);
                }
                break;
                case INDEX_BTREE_LEAF_CELL: {

                }
                break;
                case INDEX_BTREE_INTERIOR_CELL: {

                }
                break;
            }
        }

        return node;
    }

    /**
     * Decodes the varint variable type
     *
     * @param in The input stream to read the varint from
     * @return
     */
    private long decodeVarint(RandomAccessFile in) throws IOException {
        long value = 0;
        byte[] varint = new byte[9];
        int i;
        for (i = 0; i < 9; i++) {
            varint[i] = in.readByte();
            if (((varint[i] >> 7) & 1) == 0) {
                break;
            }
        }

        if (i == 0) {
            value = varint[0];
        } else {
            for (int j = 0; j < i; j++) {
                varint[j] = (byte)(varint[j] << 1);
            }
            value = ByteBuffer.wrap(varint).getLong();
        }
        return value;
    }
}