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

import battyp.lancaster.sqlitevisualiser.model.datastructures.SqliteConstants;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * <h1> Page Header </h1>
 *
 * <p>
 * A data structure for holding information about the page header
 *
 * @author Paul Batty
 * @since 0.9
 */
public class PageHeader {

    private long realPageNumber;
    private long pageOffset;
    private  int pageType;
    private  int firstFreeBlockOffset;
    private  int numberOfCells;
    private  int startOfCell;
    private  int fagmentedFreeBytes;
    private  int rightMostPointer;

    /**
     * Constructor.
     */
    public PageHeader() {
    }

    /**
     * Constructor.
     *
     * @param in File input stream to the file
     * @param pageNumber Page number to parse.
     * @param pageSize Size of the pages.
     */
    public PageHeader(RandomAccessFile in, long pageNumber, long pageSize) throws IOException {
        parsePageHeader(in, pageNumber, pageSize);
    }

    /**
     * Parses the page header.
     *
     * @param in File input stream to the file
     * @param pageNumber Page number to parse.
     * @param pageSize Size of the pages.
     */
    public void parsePageHeader(RandomAccessFile in, long pageNumber, long pageSize) throws IOException {
        realPageNumber = pageNumber - 1;
        pageOffset = realPageNumber * pageSize;
        if (realPageNumber == 0) {
            //   in.seek(SqliteConstants.HEADER_SIZE);
        } else {
            in.seek(pageOffset);
        }

        pageType = in.readByte();
        firstFreeBlockOffset = in.readShort();
        numberOfCells = in.readShort();
        startOfCell = in.readShort();
        if (startOfCell == 0) {
            startOfCell = 65536;
        }
        fagmentedFreeBytes = in.readByte();
        rightMostPointer = 0;

        if (pageType == SqliteConstants.INDEX_BTREE_INTERIOR_CELL || pageType == SqliteConstants.TABLE_BTREE_INTERIOR_CELL) {
            rightMostPointer = in.readInt();
        }
    }

    /**
     * Gets the real page number.
     *
     * Sqlite starts counting from 1 rather then 0, this will get the page
     * number starting at 0.
     *
     * @return the Page number
     */
    public long getPageNumber() {
        return realPageNumber;
    }

    /**
     * Gets the page offset
     *
     * @return Page offset int bytes from the start of the file.
     */
    public long getPageOffset() {
        return pageOffset;
    }

    /**
     * Gets the page type.
     *
     * @see SqliteConstants
     *
     * @return Page type.
     */
    public int getPageType() {
        return pageType;
    }

    /**
     * Gets the first free block cell offset from the start of the page.
     *
     * @return Freeblock cell pointer.
     */
    public int getFirstFreeBlockOffset() {
        return firstFreeBlockOffset;
    }

    /**
     * Gets the number of cells in this page.
     *
     * @return Number of cells
     */
    public int getNumberOfCells() {
        return numberOfCells;
    }

    /**
     * Gets the start of the cell block offset from the start of the page.
     *
     * @return cell block offset.
     */
    public int getStartOfCell() {
        return startOfCell;
    }

    /**
     * Gets the number of fragmented bytes.
     *
     * @return number of fragmented bytes.
     */
    public int getFagmentedFreeBytes() {
        return fagmentedFreeBytes;
    }

    /**
     * Gets the right pointer.
     *
     * The pointer is a page number.
     *
     * @return Rightmost pointer page number.
     */
    public int getRightMostPointer() {
        return rightMostPointer;
    }
}
