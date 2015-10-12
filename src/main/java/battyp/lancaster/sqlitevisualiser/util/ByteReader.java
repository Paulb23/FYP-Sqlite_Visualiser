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

import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Helper class for reading bytes from a input stream
 *
 * @author Paul Batty
 */
public class ByteReader {

    /**
     * Read a byte, 1 bytes from the input stream
     *
     * @param in the input stream to read from
     */
    public static int readByte(InputStream in) throws InvalidFileException {
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
    public static int readShort(InputStream in) throws InvalidFileException {
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
    public static int readInt(InputStream in) throws InvalidFileException {
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
    public static void skipBytes(InputStream in, long bytesTiSkip) throws InvalidFileException {
        try {
            in.skip(bytesTiSkip);
        } catch (IOException e) {
            throw new InvalidFileException();
        }
    }
}
