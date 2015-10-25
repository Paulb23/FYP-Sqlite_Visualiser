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

package battyp.lancaster.sqlitevisualiser.tests.model.sqlexecutor;

import battyp.lancaster.sqlitevisualiser.model.sqlexecutor.DefaultSqlExecutor;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * JUnit tests for DefaultSqlExecutor
 *
 * @see battyp.lancaster.sqlitevisualiser.model.sqlexecutor.SqlExecutor
 *
 * @author Paul Batty
 */
public class DefaultSqlExecutorTest {

    @Test
    public void TestSetDatabaseFile() {
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.setDatabaseFile("fake/path/to/file.txt");
    }

    @Test
    public void TestConnectWithNoPath() throws FileNotFoundException, SQLException, ClassNotFoundException {
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.connect();
    }

    @Test(expected = FileNotFoundException.class)
    public void TestConnectWithInvalidPath() throws FileNotFoundException, SQLException, ClassNotFoundException {
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.setDatabaseFile("fake/path/to/file.txt");
        defaultSqlExecutor.connect();
    }

    @Test
    public void TestConnectWithVaildPath() throws FileNotFoundException, SQLException, ClassNotFoundException {
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.setDatabaseFile("validDatabase");
        defaultSqlExecutor.connect();
    }

    @Test
    public void TestDisconnectWhileNotConnected() {
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.disconnect();
    }

    @Test
    public void TestDisconnectWhileConnected() throws FileNotFoundException, SQLException, ClassNotFoundException {
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.setDatabaseFile("validDatabase");
        defaultSqlExecutor.connect();
        defaultSqlExecutor.disconnect();
    }

    @Test(expected = NullPointerException.class)
    public void TestPerformUpdateWhileNotConnected() throws SQLException{
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.performUpdate("Update");
    }

    @Test
    public void TestPerformUpdateWhileConnected() throws FileNotFoundException, SQLException, ClassNotFoundException {
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.setDatabaseFile("LiveTestDatabase");
        defaultSqlExecutor.connect();
        defaultSqlExecutor.performUpdate("Update 'Customer' set name = 'John Smith' where customer_id = '1'");
    }

    @Test(expected = NullPointerException.class)
    public void TestPerformExecuteSqlNotConnected() throws SQLException{
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.executeSql("Select");
    }

    @Test
    public void TestPerformExecuteSqlConnected() throws FileNotFoundException, SQLException, ClassNotFoundException{
        DefaultSqlExecutor defaultSqlExecutor = new DefaultSqlExecutor();
        defaultSqlExecutor.setDatabaseFile("LiveTestDatabase");
        defaultSqlExecutor.connect();
        defaultSqlExecutor.executeSql("Select * From sqlite_master");
    }
}
