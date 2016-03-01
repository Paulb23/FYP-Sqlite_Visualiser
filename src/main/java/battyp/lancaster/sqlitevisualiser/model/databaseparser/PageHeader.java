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

    private long   pageSize;
    private long   realPageNumber;
    private long   pageOffset;
    private  int   pageType;
    private  int   firstFreeBlockOffset;
    private  int   numberOfCells;
    private  int   startOfCell;
    private  int   fagmentedFreeBytes;
    private  int   rightMostPointer;
    private long[] cellPointers;

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
        this.pageSize = pageSize;
        this.realPageNumber = pageNumber - 1;               /* take one as Sqlite start count at one rather than zero */
        this.pageOffset = this.realPageNumber * pageSize;
        if (this.realPageNumber == 0) {                     /* if its the first page skip the header */
            in.seek(SqliteConstants.HEADER_SIZE);
        } else {
            in.seek(this.pageOffset);
        }

        this.pageType = in.readByte();
        this.firstFreeBlockOffset = in.readShort();
        this.numberOfCells = in.readShort();
        this.startOfCell = in.readShort();
        if (this.startOfCell == 1) {                        /* see sqlite header taber for more information but */
            this.startOfCell = 65536;                       /* max size is stored as one                        */
        }
        this.fagmentedFreeBytes = in.readByte();
        this.rightMostPointer = 0;

        if (this.pageType == SqliteConstants.INDEX_BTREE_INTERIOR_CELL || this.pageType == SqliteConstants.TABLE_BTREE_INTERIOR_CELL) {
            this.rightMostPointer = in.readInt();
        }

        this.cellPointers = new long[this.numberOfCells];
        for (int i = 0; i < this.numberOfCells; i++) {
            this.cellPointers[i] = in.readShort() + this.pageOffset;
        }
    }

    /**
     * Gets the page size.
     *
     * @return Page size.
     */
    public long getPageSize() {
        return  this.pageSize;
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
        return this.realPageNumber;
    }

    /**
     * Gets the page offset
     *
     * @return Page offset int bytes from the start of the file.
     */
    public long getPageOffset() {
        return this.pageOffset;
    }

    /**
     * Gets the page type.
     *
     * @see SqliteConstants
     *
     * @return Page type.
     */
    public int getPageType() {
        return this.pageType;
    }

    /**
     * Gets the first free block cell offset from the start of the page.
     *
     * @return Freeblock cell pointer.
     */
    public int getFirstFreeBlockOffset() {
        return this.firstFreeBlockOffset;
    }

    /**
     * Gets the number of cells in this page.
     *
     * @return Number of cells
     */
    public int getNumberOfCells() {
        return this.numberOfCells;
    }

    /**
     * Gets the start of the cell block offset from the start of the page.
     *
     * @return cell block offset.
     */
    public int getStartOfCell() {
        return this.startOfCell;
    }

    /**
     * Gets the number of fragmented bytes.
     *
     * @return number of fragmented bytes.
     */
    public int getFagmentedFreeBytes() {
        return this.fagmentedFreeBytes;
    }

    /**
     * Gets the right pointer.
     *
     * The pointer is a page number.
     *
     * @return Rightmost pointer page number.
     */
    public int getRightMostPointer() {
        return this.rightMostPointer;
    }

    /**
     * Gets the list of cell pointers.
     *
     * @return Array of cell pointers, byte offset from the start of the file.
     */
    public long[] getCellPointers() {
        return  this.cellPointers;
    }
}
