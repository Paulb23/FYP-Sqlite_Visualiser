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

package battyp.lancaster.sqlitevisualiser.tests.filewatcher;

import battyp.lancaster.sqlitevisualiser.model.filewatcher.DefaultFileWatcher;
import battyp.lancaster.sqlitevisualiser.observerinterface.Observer;
import battyp.lancaster.sqlitevisualiser.tests.filewatcher.mocks.MockObserver;
import battyp.lancaster.sqlitevisualiser.util.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * JUnit tests for the FileWatcher
 *
 * @see battyp.lancaster.sqlitevisualiser.model.filewatcher.DefaultFileWatcher
 *
 * @author Paul Batty
 */
public class DefaultFileWatcherTest {

    @Test
    public void TestPathIsNullOnCreation() {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        Assert.assertEquals(null, fileWatcher.getFilePath());
    }

    @Test(expected = IOException.class)
    public void TestThrowsExceptionWithInvalidPath() throws IOException {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        fileWatcher.setFile("fake/path/to/fake_file.txt");
    }

    @Test
    public void TestFileNameIsNullOnCreation() {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        Assert.assertEquals(null, fileWatcher.getFileName());
    }

    @Test
    public void TestGetPathReturnsCorrectPath() throws IOException {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        String path = "filewatcher.txt";

        fileWatcher.setFile(path);

        File file = FileUtil.openFile(path);
        path = file.getCanonicalPath();
        path = path.substring(0, path.lastIndexOf(File.separator));

        Assert.assertEquals(path, fileWatcher.getFilePath());
    }

    @Test
    public void TestGetFileNameReturnsCorrectFileName() throws IOException {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        fileWatcher.setFile("filewatcher.txt");
        Assert.assertEquals("filewatcher.txt", fileWatcher.getFileName());
    }

    @Test
    public void TestAddObserver() {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        fileWatcher.addObserver(new MockObserver());
    }

    @Test
    public void TestRemoveObserver() {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        Observer o = new MockObserver();
        fileWatcher.addObserver(o);
        fileWatcher.removeObserver(o);
    }

    @Test
    public void TestRemoveAllObservers() {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        Observer o = new MockObserver();
        fileWatcher.addObserver(o);
        fileWatcher.removeAllObservers();
    }

    @Test
    public void TestNotifyObservers() {
        DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        MockObserver o = new MockObserver();
        fileWatcher.addObserver(o);
        fileWatcher.notifyObservers();
        Assert.assertTrue(o.notified);
    }

    @Test
    public void TestDetectChanges() throws IOException, InterruptedException {
      /*  DefaultFileWatcher fileWatcher = new DefaultFileWatcher();
        MockObserver o = new MockObserver();
        fileWatcher.addObserver(o);
        fileWatcher.setFile("filewatcher.txt");
        Thread thread = new Thread(fileWatcher, "File Watcher");
        thread.start();

        File file = FileUtil.openFile("filewatcher.txt");
        FileWriter writer = new FileWriter(file);
        writer.write("Test" + new Random(hashCode()).nextInt());
        writer.close();

        fileWatcher.terminate();
        thread.join();
        Assert.assertTrue(o.notified);*/
    }
}
