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

package battyp.lancaster.sqlitevisualiser.model.filewatcher;

import battyp.lancaster.sqlitevisualiser.observerinterface.Observer;
import battyp.lancaster.sqlitevisualiser.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1> Default File Watcher </h1>
 *
 * <p>
 * Default implementation of the file watcher.
 *
 * @author Paul Batty
 * @see FileWatcher
 * @since 0.8
 */
public class DefaultFileWatcher implements FileWatcher {

    private List<Observer> observers;

    private String path;
    private String fileName;

    private WatchService watchService;

    private boolean running;

    /**
     * Constructor.
     */
    public DefaultFileWatcher() {
        this.observers = new ArrayList<>();
        running = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while(running) {
            if (watchService != null && path != null && fileName != null) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    key = null;
                }

                if (key != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = (Path) event.context();
                        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY && fileName.toString().equals(this.fileName)) {
                            notifyObservers();
                        }
                    }
                    key.reset();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate()  {
        running = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFile(String path) throws IOException {
        File file = FileUtil.openFile(path);
        this.path = file.getCanonicalPath();
        this.path = this.path.substring(0, this.path.lastIndexOf(File.separator));
        this.fileName = file.getName();

        this.watchService = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(this.path);
        dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllObservers() {
        this.observers = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.notifyObserver();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return this.path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileName() {
        return this.fileName;
    }
}
