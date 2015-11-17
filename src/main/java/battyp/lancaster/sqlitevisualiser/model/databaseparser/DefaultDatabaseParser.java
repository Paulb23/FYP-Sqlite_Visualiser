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

import battyp.lancaster.sqlitevisualiser.util.FileUtil;
import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.datastructures.*;
import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;
import battyp.lancaster.sqlitevisualiser.view.CellType;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>< Default Database Parser </h1>
 *
 * <p>
 * Default implementation of the Database Parser designed for
 * Sqlite databases.
 *
 * @author Paul Batty
 * @see DatabaseParser
 * @see <a href="https://www.sqlite.org/fileformat2.html">Sqlite file format</a>
 * @since 0.3
 */
public class DefaultDatabaseParser implements DatabaseParser {

    /**
     * {@inheritDoc}
     */
    @Override
    public Database parseDatabase(String pathToDatabase, Database database) throws IOException, InvalidFileException {

        File file = FileUtil.openFile(pathToDatabase);
        RandomAccessFile in = new RandomAccessFile(file, "r");

        checkMagicNumber(in);
        readSqliteHeader(in, database.getMetadata());
        readBTrees(in, database);

        in.close();
        return database;
    }

    /**
     * Checks the input stream against the magic number.
     *
     * <p>
     * <b>NOTE:<b/> Assumes input stream is already in position.
     *
     * @param in The InputStream.
     *
     * @throws InvalidFileException Invalid File if the magic number is incorrect.
     */
    private void checkMagicNumber(RandomAccessFile in) throws InvalidFileException {
        try {
            for (int i = 0; i < SqliteConstants.MAGIC_NUMBER_LENGTH; i++) {
                if (in.read() != SqliteConstants.MAGIC_NUMBER[i]) {
                    throw new InvalidFileException();
                }
            }
        } catch (IOException e) {
            throw new InvalidFileException();
        }
    }

    /**
     * Reads the first 100 byte header in the database.
     *
     * <p>
     * <b>NOTE:<b/> Assumes input stream is already in position
     *
     * @param in The InputStream.
     * @param metadata The metadata object to store to.
     */
    private void readSqliteHeader(RandomAccessFile in, Metadata metadata) throws IOException, InvalidFileException {
        metadata.pageSize = in.readShort();
        metadata.writeVersion = in.readByte();
        metadata.readVersion = in.readByte();
        metadata.unusedSpaceAtEndOfEachPage = in.readByte();
        metadata.maxEmbeddedPayload = in.readByte();
        metadata.minEmbeddedPayload = in.readByte();
        metadata.leafPayloadFraction = in.readByte();
        metadata.fileChangeCounter = in.readInt();
        metadata.sizeOfDatabaseInPages = in.readInt();
        metadata.pageNumberOfFirstFreelistPage = in.readInt();
        metadata.totalFreeListPages = in.readInt();
        metadata.schemaCookie = in.readInt();
        metadata.schemaFormat = in.readInt();
        metadata.defaultPageCacheSize = in.readInt();
        metadata.pageNumberToLargestBTreePage = in.readInt();
        metadata.textEncoding = in.readInt();
        metadata.userVersion = in.readInt();
        metadata.vacuumMode = in.readInt();
        metadata.appID = in.readInt();
        in.skipBytes(20);
        metadata.versionValidNumber = in.readInt();
        metadata.sqliteVersion = in.readInt();
    }

    /**
     * Reads the Btree sections of the file.
     *
     * @param in The inputStream.
     * @param database The tree to store in.
     *
     * @throws IOException If there is a problem reading the file.
     * @throws InvalidFileException If there is an unusual format.
     */
    private void readBTrees(RandomAccessFile in, Database database) throws  IOException, InvalidFileException {

        int pageSize = database.getMetadata().pageSize;
        BTreeNode<BTreeCell> root = parseBtree(in, 1, pageSize);

        database.getBTree().setRoot(root);
    }

    /**
     * Parses the btree page.
     *
     * <p>
     * Uses recursion to load up each node, with it's children.
     *
     * TODO: Refactor
     * TODO: Fix setting to page 0
     * TODO: Read Overflow Pages
     * TODO: Read INDEX_BTREE_LEAF_CELL and INDEX_BTREE_INTERIOR_CELL Payloads
     *
     * @param in The input stream.
     * @param pageNumber The page number we are on.
     * @param pageSize The page size.
     *
     * @return  BtreeNode with the cell data.
     *
     * @throws IOException If there is a problem reading the file.
     * @throws InvalidFileException If there is an unusual format.
     */
    public BTreeNode<BTreeCell> parseBtree(RandomAccessFile in, long pageNumber, long pageSize) throws IOException, InvalidFileException {
        BTreeNode<BTreeCell> node = new BTreeNode();
        long realPageNumber = pageNumber - 1;
        long pageOffset = realPageNumber * pageSize;
        if (realPageNumber == 0) {
         //   in.seek(SqliteConstants.HEADER_SIZE);
        } else {
            in.seek(pageOffset);
        }

        // read b-tree header
        int type = in.readByte();
        int firstFreeBlockOffset = in.readShort();
        int numberOfCells = in.readShort();
        int startOfCell = in.readShort();
        if (startOfCell == 0) {
            startOfCell = 65536;
        }
        int fagmentedFreeBytes = in.readByte();
        int rightMostPointer = 0;

        if (type == SqliteConstants.INDEX_BTREE_INTERIOR_CELL || type == SqliteConstants.TABLE_BTREE_INTERIOR_CELL) {
            rightMostPointer = in.readInt();
        }

        BTreeCell cell = new BTreeCell(type, numberOfCells, realPageNumber);
        cell.rightChildPointer = rightMostPointer;

        long[] cellPointers = new long[numberOfCells];
        for (int i = 0; i < numberOfCells; i++) {
            cellPointers[i] = in.readShort() + pageOffset;
        }

        // follow pointer and get b-tree
        for (int i = 0; i < numberOfCells; i++) {
            in.seek(cellPointers[i]);

            // parse that type of b-tree
            switch (type) {
                case SqliteConstants.TABLE_BTREE_LEAF_CELL: {
                    cell.payLoadSize[i] = decodeVarint(in)[0];     //read cell header
                    cell.rowId[i] = decodeVarint(in)[0];
                    long bytesInHeader = decodeVarint(in)[0] - 1;
                    int[] types = new int[(int)bytesInHeader];
                    int headerSize = 0;
                    int bytesCounted = 0;
                    int k = 0;
                    while (bytesCounted < bytesInHeader) {
                        long[] varint = decodeVarint(in);
                        types[k] = (int)varint[0];
                        bytesCounted += varint[1];
                        k++;
                    }

                    int table = 0;
                    int index = 0;
                    int tablePage = 0;
                    for (int j = 0; j < k; j++) {      // read payload
                        if (types[j] == 0) {
                           cell.data[i] += " null ";
                        } else if (types[j] == 1) {
                            short bytes = in.readByte();
                            if (table == 1 && tablePage == 0) {
                                tablePage = bytes;
                            }
                           cell.data[i] += " " + bytes + " ";
                        } else if (types[j] == 2) {
                            cell.data[i] += " " + new Short(in.readShort()) + " ";
                        } else if (types[j] == 3) {
                            cell.data[i] += " " + in.readShort() + in.readByte() + " "; // fix
                        } else if (types[j] == 4) {
                            cell.data[i] += " " + new Integer(in.readInt()) + " ";
                        } else if (types[j] == 5) {
                            cell.data[i] += " " + in.readInt() + in.readShort() + " "; // fix
                        } else if (types[j] == 6) {
                            cell.data[i] += " " + new Long(in.readLong()) + " ";
                        } else if (types[j] == 7) {
                            cell.data[i] += " " + new Double(in.readDouble()) + " ";
                        } else if (types[j] == 8) {
                            cell.data[i] += " 0 ";
                        } else if (types[j] == 9) {
                            cell.data[i] += " 0 ";
                        } else if (types[j] >= 12 && types[j] % 2 == 0) {
                            byte[] bytes = new byte[(types[j]-12)/2];
                            in.read(bytes);
                            cell.data[i] += " " + new String(bytes) + " ";
                        } else if (types[j] >= 13 && types[j] % 2 != 0) {
                            byte[] bytes = new byte[(types[j]-13)/2];
                            in.read(bytes);
                            String str = new String(bytes);
                            cell.data[i] += " " + str + " ";
                            if (str.equals("table") || str.equals("index")) {
                                table = 1;
                            }
                        }
                    }
                    if (table == 1) {
                        cell.type = CellType.Table;
                        node.addChild(parseBtree(in, tablePage, pageSize));
                    } else if (cell.type == null) {
                        cell.type = CellType.Data;
                    }
                    // read overflow
                   // cell.overflowPageNumbers[i] = in.readInt();
                }
                break;
                case SqliteConstants.TABLE_BTREE_INTERIOR_CELL: {
                    cell.type = CellType.Table_Pointer_Internal;
                    cell.leftChildPointers[i] = in.readInt();
                    cell.rowId[i] = decodeVarint(in)[0];
                    node.addChild(parseBtree(in, cell.leftChildPointers[i], pageSize));
                }
                break;
                case SqliteConstants.INDEX_BTREE_LEAF_CELL: {
                    cell.type = CellType.Index_Leaf;
                    cell.payLoadSize[i] = decodeVarint(in)[0];
                    byte[] bytes = new byte[(int)cell.payLoadSize[i]];
                    in.read(bytes);
                    cell.data[i] = new String(bytes);
                  //  cell.overflowPageNumbers[i] = in.readInt();
                }
                break;
                case SqliteConstants.INDEX_BTREE_INTERIOR_CELL: {
                    cell.type = CellType.Index_Pointer_Internal;
                    cell.leftChildPointers[i] = in.readInt();
                    cell.payLoadSize[i] = decodeVarint(in)[0];
                    //cell.data[i] = in.readUTF();
                    //cell.overflowPageNumbers[i] = in.readInt();
                }
                break;
            }
        }

        if (type == SqliteConstants.INDEX_BTREE_INTERIOR_CELL || type == SqliteConstants.TABLE_BTREE_INTERIOR_CELL) {
            if (rightMostPointer != 0) {
                node.addChild(parseBtree(in, rightMostPointer, pageSize));
            }
        }

        node.setData(cell);
        return node;
    }

    /**
     * Decodes the varint variable type.
     *
     * <p>
     * <b>NOTE:</b> Assumes input stream is already at the correct position.
     *
     * @param in The input stream to read the varint from.
     *
     * @return long array with the first element the value, the second the number of bytes else null if invalid.
     */
    private long[] decodeVarint(RandomAccessFile in) throws IOException {
        long[] value = new long[2];
        byte[] varint = new byte[9];
        int i;
        for (i = 0; i < 9; i++) {
            varint[i] = in.readByte();
            if (((varint[i] >> 7) & 1) == 0) {
                break;
            }
        }

        if (i == 0) {
            value[0] = varint[0];
            value[1]= 1;
        } else {
            for (int j = 0; j < i; j++) {
                varint[j] = (byte)(varint[j] << 1);
            }
            value[0] = ByteBuffer.wrap(varint).getLong();
            value[1] = i + 1;
        }
        return value;
    }
}