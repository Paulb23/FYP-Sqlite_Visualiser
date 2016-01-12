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

package battyp.lancaster.sqlitevisualiser.tests.model.LiveUpdater;

import battyp.lancaster.sqlitevisualiser.model.exceptions.InvalidFileException;
import battyp.lancaster.sqlitevisualiser.model.liveupdater.DefaultLiveUpdater;
import battyp.lancaster.sqlitevisualiser.tests.model.mocks.MockDatabaseInterface;
import battyp.lancaster.sqlitevisualiser.tests.model.mocks.MockDatabaseParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit tests for LiveUpdater
 *
 * @see battyp.lancaster.sqlitevisualiser.model.liveupdater.DefaultLiveUpdater
 *
 * @author Paul Batty
 */
public class DefaultLiveUpdaterTest {

    @Test
    public void TestLiveUpdatingIsTrueOnCreation() {
        DefaultLiveUpdater liveUpdater = new DefaultLiveUpdater(null);
        Assert.assertEquals(true, liveUpdater.isUpdating());
    }

    @Test
    public void TestStopUpdating() {
        DefaultLiveUpdater liveUpdater = new DefaultLiveUpdater(null);
        liveUpdater.stopUpdating();
        Assert.assertEquals(false, liveUpdater.isUpdating());
    }

    @Test
    public void TestStartUpdating() {
        DefaultLiveUpdater liveUpdater = new DefaultLiveUpdater(null);
        liveUpdater.stopUpdating();
        liveUpdater.startUpdating();
        Assert.assertEquals(true, liveUpdater.isUpdating());
    }

    @Test
    public void TestNotifyObserverWithoutSettingUp() {
        DefaultLiveUpdater liveUpdater = new DefaultLiveUpdater(null);
        liveUpdater.notifyObserver();
    }

    @Test
    public void TestUpdateDatabase() throws IOException, InvalidFileException {
        DefaultLiveUpdater liveUpdater = new DefaultLiveUpdater(null);
        liveUpdater.update("LiveTestDatabase", new MockDatabaseParser(), new MockDatabaseInterface());
    }
}
