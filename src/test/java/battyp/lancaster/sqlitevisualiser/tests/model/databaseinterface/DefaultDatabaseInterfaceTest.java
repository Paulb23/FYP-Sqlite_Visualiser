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

package battyp.lancaster.sqlitevisualiser.tests.model.databaseinterface;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.databaseinterface.DefaultDatabaseInterface;
import battyp.lancaster.sqlitevisualiser.model.datastructures.Metadata;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for DefaultDatabaseInterface
 *
 * @see battyp.lancaster.sqlitevisualiser.model.databaseinterface.DefaultDatabaseInterface
 *
 * @author Paul Batty
 */
public class DefaultDatabaseInterfaceTest {

    @Test
    public void TestGetCurrentReturnsNullOnCreation() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Assert.assertEquals(null, databaseInterface.getCurrent());
    }

    @Test
    public void TestGetPreviousReturnsNullOnCreation() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Assert.assertEquals(null, databaseInterface.getPrevious());
    }

    @Test
    public void TestGetNextReturnsNullOnCreation() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Assert.assertEquals(null, databaseInterface.getNext());
    }

    @Test
    public void TestNextStepDoesNothingOnCreation() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        databaseInterface.nextStep();
        Assert.assertEquals(null, databaseInterface.getCurrent());
    }

    @Test
    public void TestPreviousStepDoesNothingOnCreation() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        databaseInterface.previousStep();
        Assert.assertEquals(null, databaseInterface.getCurrent());
    }

    @Test
    public void TestClearChangesNothingOnCreation() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        databaseInterface.clear();
        Assert.assertEquals(null, databaseInterface.getCurrent());
    }

    @Test
    public void TestAddAndGetCurrentReturnsCorrectDatabase() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(database);
        Assert.assertEquals(database, databaseInterface.getCurrent());
    }

    @Test
    public void TestAddAndGetNextReturnsNull() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(database);
        Assert.assertEquals(null, databaseInterface.getNext());
    }

    @Test
    public void TestAddAndGetPreviousReturnsNull() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(database);
        Assert.assertEquals(null, databaseInterface.getPrevious());
    }

    @Test
    public void TestAddAndNextStepDoesNothing() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(database);
        databaseInterface.nextStep();
        Assert.assertEquals(database, databaseInterface.getCurrent());
    }

    @Test
    public void TestAddAndPreviousStepDoesNothing() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(database);
        databaseInterface.previousStep();
        Assert.assertEquals(database, databaseInterface.getCurrent());
    }

    @Test
    public void TestNextStep() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(new Database(new Metadata()));
        databaseInterface.addDatabase(database);
        databaseInterface.nextStep();
        Assert.assertEquals(database, databaseInterface.getCurrent());
    }

    @Test
    public void TestPreviousStep() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(database);
        databaseInterface.addDatabase(new Database(new Metadata()));
        databaseInterface.nextStep();
        databaseInterface.previousStep();
        Assert.assertEquals(database, databaseInterface.getCurrent());
    }

    @Test
    public void TestGetNext() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(new Database(new Metadata()));
        databaseInterface.addDatabase(database);
        Assert.assertEquals(database, databaseInterface.getNext());
    }

    @Test
    public void TestGetPrevious() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        Database database = new Database(new Metadata());
        databaseInterface.addDatabase(database);
        databaseInterface.addDatabase(new Database(new Metadata()));
        databaseInterface.nextStep();
        Assert.assertEquals(database, databaseInterface.getPrevious());
    }

    @Test
    public void TestClearRemovesAllDatabases() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        databaseInterface.addDatabase(new Database(new Metadata()));
        databaseInterface.clear();
        Assert.assertEquals(null, databaseInterface.getCurrent());
    }

    @Test
    public void TestClearResetsCurrent() {
        DefaultDatabaseInterface databaseInterface = new DefaultDatabaseInterface();
        databaseInterface.addDatabase(new Database(new Metadata()));
        databaseInterface.addDatabase(new Database(new Metadata()));
        databaseInterface.nextStep();
        databaseInterface.clear();
        Assert.assertEquals(null, databaseInterface.getCurrent());
    }
}
