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

package battyp.lancaster.sqlitevisualiser.model.SqlExecutor;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SqlExecutor is a interface that all outgoing sql commands and executions will be ran through
 *
 * @author Paul Batty
 */
public interface SqlExecutor {

    /**
     * Sets the current database file
     *
     * @param path path to the database
     */
    public void setDatabaseFile(String path);

    /**
     * Connects to the database
     */
    public void connect() throws FileNotFoundException, SQLException, ClassNotFoundException;

    /**
     * Disconnects the database
     */
    public void disconnect();

    /**
     * Executes Sql on the database such as Selects
     *
     * @param sql Sql to execute
     *
     * @return Result Set containing the result
     */
    public ResultSet executeSql(String sql) throws SQLException;

    /**
     * Executes Sql on the database such as Update, Delete
     *
     * @param sql Sql to execute
     *
     * @return Result Set containing the result
     */
    public void performUpdate(String sql) throws SQLException;
}
