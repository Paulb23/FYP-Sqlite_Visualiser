/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Paul Batty
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

    private static final int VALUE_NULL = 0;
    private static final int VALUE_ONE_BYTE_INT = 1;
    private static final int VALUE_TWO_BYTE_INT = 2;
    private static final int VALUE_THREE_BYTE_INT = 3;
    private static final int VALUE_FOUR_BYTE_INT = 4;
    private static final int VALUE_SIX_BYTE_INT = 5;
    private static final int VALUE_EIGHT_BYTE_INT = 6;
    private static final int VALUE_EIGHT_BYTE_FLOAT = 7;
    private static final int VALUE_ZERO = 8;
    private static final int VALUE_ONE = 9;
    private static final int VALUE_BLOB = 12;
    private static final int VALUE_STRING = 13;

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

        in.skipBytes(SqliteConstants.HEADER_RESERVED_SPACE_SIZE);

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

        final int starting_page_number = 1;

        int pageSize = database.getMetadata().pageSize;
        database.getBTree().setRoot(parseBtree(in, starting_page_number, pageSize, database.getMetadata()));
    }

    /**
     * Parses the btree page.
     *
     * <p>
     * Uses recursion to load up each node, with it's children.
     *
     * @param in The input stream.
     * @param pageNumber The page number we are on.
     * @param pageSize The page size.
     *
     * @param metadata
     * @return  BtreeNode with the cell data.
     *
     * @throws IOException If there is a problem reading the file.
     * @throws InvalidFileException If there is an unusual format.
     */
    public BTreeNode<BTreeCell> parseBtree(RandomAccessFile in, long pageNumber, long pageSize, Metadata metadata) throws IOException, InvalidFileException {
        BTreeNode<BTreeCell> node = new BTreeNode<>();
        PageHeader pageHeader = new PageHeader(in, pageNumber, pageSize);

        final int cellType = pageHeader.getPageType();
        BTreeCell cell;
        switch (cellType) {
            case SqliteConstants.TABLE_BTREE_LEAF_CELL: {
                cell = parseTableBtreeLeafCell(in, pageHeader, node, metadata);
            }
            break;
            case SqliteConstants.TABLE_BTREE_INTERIOR_CELL: {
                cell = parseTableBtreeInteriorCell(in, pageHeader, node, metadata);
            }
            break;
            case SqliteConstants.INDEX_BTREE_LEAF_CELL: {
                cell = parseIndexBtreeLeafCell(in, pageHeader, node, metadata);
            }
            break;
            case SqliteConstants.INDEX_BTREE_INTERIOR_CELL: {
                cell = parseIndexBtreeInteriorCell(in, pageHeader, node, metadata);
            }
            break;
            default: {
                cell = new BTreeCell(cellType, pageHeader.getNumberOfCells(), pageHeader.getPageNumber());
            }
            break;
        }

        if (cellType == SqliteConstants.INDEX_BTREE_INTERIOR_CELL || cellType == SqliteConstants.TABLE_BTREE_INTERIOR_CELL) {
            if (pageHeader.getRightMostPointer() != 0) {
                node.addChild(parseBtree(in, pageHeader.getRightMostPointer(), pageSize, metadata));
            }
        }
        cell.rightChildPointer = pageHeader.getRightMostPointer();
        node.setData(cell);
        return node;
    }

    /**
     * Parses a Table Btree Leaf Cell: Hex value of: 0x0D
     *
     * @param in RandomAccessFile input stream
     * @param pageHeader PageHeader of the page with the cells.
     * @param node Btree node to attach children cell to if found.
     *
     * @param metadata
     * @return BTreeCell containing the cell data.
     *
     * @throws IOException If there is a problem reading the file.
     * @throws InvalidFileException If there is an unusual format.
     */
    private BTreeCell parseTableBtreeLeafCell(RandomAccessFile in, PageHeader pageHeader, BTreeNode<BTreeCell> node, Metadata metadata) throws IOException, InvalidFileException {
        final int numberOfCells = pageHeader.getNumberOfCells();
        final int cellType = pageHeader.getPageType();
        final long[] cellPointers = pageHeader.getCellPointers();

        BTreeCell cell = new BTreeCell(cellType, numberOfCells, pageHeader.getPageNumber());
        cell.type = CellType.TABLE_LEAF;

        for (int i = 0; i < numberOfCells; i++) {
            in.seek(cellPointers[i]);

            cell.payLoadSize[i] = decodeVarint(in)[0];
            cell.rowId[i] = decodeVarint(in)[0];
            parseRecordPayload(in, cell, i, metadata);
            if (cell.isTable[i]) {
                node.addChild(parseBtree(in, cell.childrenPageNumbers[i], pageHeader.getPageSize(), metadata));
            }
            // read overflow
            // cell.overflowPageNumbers[i] = in.readInt();
        }
        return cell;
    }

    /**
     * Parses a Table Btree Interior Cell: Hex value of: 0x05
     *
     * @param in RandomAccessFile input stream
     * @param pageHeader PageHeader of the page with the cells.
     * @param node Btree node to attach children cell to if found.
     *
     * @param metadata
     * @return BTreeCell containing the cell data.
     *
     * @throws IOException If there is a problem reading the file.
     * @throws InvalidFileException If there is an unusual format.
     */
    private BTreeCell parseTableBtreeInteriorCell(RandomAccessFile in, PageHeader pageHeader, BTreeNode<BTreeCell> node, Metadata metadata) throws IOException, InvalidFileException {
        final int numberOfCells = pageHeader.getNumberOfCells();
        final int cellType = pageHeader.getPageType();
        final long[] cellPointers = pageHeader.getCellPointers();

        BTreeCell cell = new BTreeCell(cellType, numberOfCells, pageHeader.getPageNumber());
        cell.type = CellType.Table_Pointer_Internal;

        for (int i = 0; i < numberOfCells; i++) {
            in.seek(cellPointers[i]);

            cell.leftChildPointers[i] = in.readInt();
            cell.rowId[i] = decodeVarint(in)[0];
            node.addChild(parseBtree(in, cell.leftChildPointers[i], pageHeader.getPageSize(), metadata));
        }
        return cell;
    }

    /**
     * Parses a Index Btree Leaf Cell: Hex value of: 0x0A
     *
     * @param in RandomAccessFile input stream
     * @param pageHeader PageHeader of the page with the cells.
     *
     * @param metadata
     * @return BTreeCell containing the cell data.
     *
     * @throws IOException If there is a problem reading the file.
     * @throws InvalidFileException If there is an unusual format.
     */
    private BTreeCell parseIndexBtreeLeafCell(RandomAccessFile in, PageHeader pageHeader, BTreeNode<BTreeCell> node, Metadata metadata) throws IOException, InvalidFileException {
        final int numberOfCells = pageHeader.getNumberOfCells();
        final int cellType = pageHeader.getPageType();
        final long[] cellPointers = pageHeader.getCellPointers();

        BTreeCell cell = new BTreeCell(cellType, numberOfCells, pageHeader.getPageNumber());
        cell.type = CellType.Index_Leaf;

        for (int i = 0; i < numberOfCells; i++) {
            in.seek(cellPointers[i]);

            cell.payLoadSize[i] = decodeVarint(in)[0];
            parseRecordPayload(in, cell, i, metadata);
            if (cell.isTable[i]) {
                node.addChild(parseBtree(in, cell.childrenPageNumbers[i], pageHeader.getPageSize(), metadata));
            }
            //  cell.overflowPageNumbers[i] = in.readInt();
        }
        return cell;
    }

    /**
     * Parses a Index Btree Interior Cell: Hex value of: 0x02
     *
     * @param in RandomAccessFile input stream
     * @param pageHeader PageHeader of the page with the cells.
     *
     * @param metadata
     * @return BTreeCell containing the cell data.
     *
     * @throws IOException If there is a problem reading the file.
     * @throws InvalidFileException If there is an unusual format.
     */
    private BTreeCell parseIndexBtreeInteriorCell(RandomAccessFile in, PageHeader pageHeader, BTreeNode<BTreeCell> node, Metadata metadata) throws IOException, InvalidFileException {
        final int numberOfCells = pageHeader.getNumberOfCells();
        final int cellType = pageHeader.getPageType();
        final long[] cellPointers = pageHeader.getCellPointers();

        BTreeCell cell = new BTreeCell(cellType, numberOfCells, pageHeader.getPageNumber());
        cell.type = CellType.Index_Pointer_Internal;

        for (int i = 0; i < numberOfCells; i++) {
            in.seek(cellPointers[i]);

            cell.leftChildPointers[i] = in.readInt();
            cell.payLoadSize[i] = decodeVarint(in)[0];
            parseRecordPayload(in, cell, i, metadata);
            if (cell.isTable[i]) {
                node.addChild(parseBtree(in, cell.childrenPageNumbers[i], pageHeader.getPageSize(), metadata));
            }
            //cell.overflowPageNumbers[i] = in.readInt();
        }
        return cell;
    }

    /**
     * Parsers a record
     *
     * @param in input stream.
     * @param cell cell to read from.
     * @param cellNumber cell number to read.
     *
     * @throws IOException
     */
    private void parseRecordPayload(RandomAccessFile in, BTreeCell cell, int cellNumber,  Metadata metadata) throws IOException {

        // check for overflow page
        int usableSize = metadata.pageSize + metadata.unusedSpaceAtEndOfEachPage;
        int maxLocal;
        int minLocal;
        long localSize;
        boolean hasOverflowPage = false;
        if (cell.type == CellType.TABLE_LEAF) {
            maxLocal = usableSize - 35;
        } else {
            maxLocal = (usableSize - 12) * metadata.maxEmbeddedPayload / 255 - 23;
        }
        if (cell.payLoadSize[cellNumber] > maxLocal) {
            hasOverflowPage = true;

            minLocal = (usableSize - 12) * metadata.minEmbeddedPayload / 255 - 23;
            localSize = minLocal + (cell.payLoadSize[cellNumber] - minLocal) % (usableSize - 4);

            if (localSize > maxLocal) {
                localSize = maxLocal;
            }
        }

        long bytesInHeader = decodeVarint(in)[0] - 1;
        int[] types = new int[(int) bytesInHeader];
        int bytesCounted = 0;
        int numberOfItems = 0;
        while (bytesCounted < bytesInHeader) {
            long[] varint = decodeVarint(in);
            types[numberOfItems] = (int) varint[0];
            bytesCounted += varint[1];
            numberOfItems++;
        }

        boolean isTable = false;
        int tablePageNumber = 0;
        int bytesRead = 0;
        int nextOverflowPage = 0;
        boolean inOverflow = false;
        for (int j = 0; j < numberOfItems; j++) {

            // overflow page
            if (hasOverflowPage) {
                if (bytesRead > maxLocal - 4) {
                    int overflowPageNumber = in.readInt();
                    in.seek(((overflowPageNumber -1) * metadata.pageSize));
                    inOverflow = true;
                    bytesRead = 0;
                    nextOverflowPage = 0;
                } else if (bytesRead > metadata.pageSize) {
                    in.seek(((nextOverflowPage -1) * metadata.pageSize));
                    inOverflow = true;
                    bytesRead = 0;
                    nextOverflowPage = 0;
                }

                if (inOverflow && nextOverflowPage == 0) {
                    nextOverflowPage = in.readInt();
                }
            }

            switch (types[j]) {
                case VALUE_NULL: {
                    cell.data[cellNumber] += "";
                    break;
                }
                case VALUE_ONE_BYTE_INT: {
                    short bytes = in.readByte();
                    if (isTable && tablePageNumber == 0) {
                        tablePageNumber = bytes;
                    }
                    cell.data[cellNumber] += " " + bytes + " ";
                    bytesRead += 1;
                    break;
                }
                case VALUE_TWO_BYTE_INT: {
                    cell.data[cellNumber] += " " + in.readShort() + " ";
                    bytesRead += 2;
                    break;
                }
                case VALUE_THREE_BYTE_INT: {
                    cell.data[cellNumber] += " " + readBytesAsLong(in, 3) + " ";
                    bytesRead += 3;
                    break;
                }
                case VALUE_FOUR_BYTE_INT: {
                    cell.data[cellNumber] += " " + in.readInt() + " ";
                    bytesRead += 4;
                    break;
                }
                case VALUE_SIX_BYTE_INT: {
                    cell.data[cellNumber] += " " + readBytesAsLong(in, 6) + " ";
                    bytesRead += 6;
                    break;
                }
                case VALUE_EIGHT_BYTE_INT: {
                    cell.data[cellNumber] += " " + in.readLong() + " ";
                    bytesRead += 8;
                    break;
                }
                case VALUE_EIGHT_BYTE_FLOAT: {
                    cell.data[cellNumber] += " " + in.readDouble() + " ";
                    bytesRead += 8;
                    break;
                }
                case VALUE_ZERO: {
                    cell.data[cellNumber] += " 0 ";
                    break;
                }
                case VALUE_ONE: {
                    cell.data[cellNumber] += " 1 ";
                    break;
                }
                default: {
                    if (types[j] >= VALUE_BLOB && types[j] % 2 == 0) {
                        cell.data[cellNumber] += " " + readBytesAsString(in, (types[j] - 12) / 2) + " ";
                        bytesRead += (types[j] - 12) / 2;
                    } else if (types[j] >= VALUE_STRING && types[j] % 2 != 0) {
                        String str = readBytesAsString(in, (types[j] - 13) / 2);
                        cell.data[cellNumber] += " " + str + " ";
                        bytesRead += (types[j] - 13) / 2;
                        if (str.equals("table") || str.equals("index")) {
                            isTable = true;
                        }
                    }
                    break;
                }
            }
        }
        if (isTable) {
            cell.type = CellType.Table;
            cell.isTable[cellNumber] = true;
            cell.childrenPageNumbers[cellNumber] = tablePageNumber;
        } else {
            cell.isTable[cellNumber] = false;
            cell.childrenPageNumbers[cellNumber] = 0;
        }
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

    /**
     * Reads a long from the input stream.
     *
     * @param in The input stream to read the from.
     * @param len Size of the long
     *
     * @return long read from the input stream
     *
     * @throws IOException If there is a problem reading the file.
     */
    private long readBytesAsLong (RandomAccessFile in, int len) throws IOException {
        byte[] byteArray = new byte[len];
        in.read(byteArray);

        long val = 0;
        len = Math.min(len, 8);
        for (int i = (len - 1); i >= 0; i--) {
            val <<= 8;
            val |= (byteArray [i] & 0x00FF);
        }
        return val;
    }

    /**
     * Reads a string from the input stream.
     *
     * @param in The input stream to read the from.
     * @param numberOfBytes Size of the string
     *
     * @return String read from the input stream
     *
     * @throws IOException If there is a problem reading the file.
     */
    private String readBytesAsString(RandomAccessFile in, int numberOfBytes) throws IOException {
        byte[] bytes = new byte[numberOfBytes];
        in.read(bytes);
        return new String(bytes);
    }
}