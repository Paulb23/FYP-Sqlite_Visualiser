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

package battyp.lancaster.sqlitevisualiser.tests.model;

import battyp.lancaster.sqlitevisualiser.model.DefaultModel;
import battyp.lancaster.sqlitevisualiser.model.databaseparser.DefaultDatabaseParser;
import battyp.lancaster.sqlitevisualiser.tests.model.Mocks.MockDatabase;
import battyp.lancaster.sqlitevisualiser.tests.model.Mocks.MockDatabaseInterface;
import battyp.lancaster.sqlitevisualiser.tests.model.Mocks.MockDatabaseParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for DefaultModel
 *
 * @see battyp.lancaster.sqlitevisualiser.model.DefaultModel
 *
 * @author Paul Batty
 */
public class DefaultModelTest {

    @Test
    public void TestDefaultModesAreCreatedOnCreation() {
        DefaultModel model = new DefaultModel();
        Assert.assertEquals(null, model.getDatabase());
        Assert.assertTrue(model.getDatabaseParser() instanceof DefaultDatabaseParser);
    }

    @Test
    public void TestCustomModesAreLoadedOnCreation() {
        DefaultModel model = new DefaultModel(new MockDatabaseInterface(), new MockDatabaseParser());
        Assert.assertTrue(model.getDatabase() instanceof MockDatabase);
        Assert.assertTrue(model.getDatabaseParser() instanceof MockDatabaseParser);
    }
}
