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

package battyp.lancaster.sqlitevisualiser.model.sqlexecutor;

import battyp.lancaster.sqlitevisualiser.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;

/**
 * <h1> Default SQl Executor </h1>
 *
 * <p>
 * Default implementation of Sql Executor.
 *
 * @author Paul Batty
 * @see SqlExecutor
 * @since 0.7
 */
public class DefaultSqlExecutor implements SqlExecutor {

    private static final String SQLITE_DATABASE_LOCATION = "jdbc:sqlite:";
    private static final String SQLite_DRIVER = "org.sqlite.JDBC";
    private Connection connection;
    private String databasePath;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatabaseFile(String path) {
        this.databasePath = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() throws FileNotFoundException, SQLException, ClassNotFoundException {
        if (databasePath != null) {
            File file = FileUtil.openFile(databasePath);
            Class.forName(SQLite_DRIVER);
            this.connection = DriverManager.getConnection(SQLITE_DATABASE_LOCATION + file.getAbsolutePath());
            this.connection.setAutoCommit(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        if (this.connection != null) {
            try {
                if (this.connection.isClosed()) {
                    return;
                }
                this.connection.commit();
                this.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet executeSql(String sql) throws SQLException {
        if (this.connection.isClosed()) {
            return null;
        }
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performUpdate(String sql) throws SQLException {
        if (this.connection.isClosed()) {
            return;
        }
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseMetaData getDatabaseMetaData() throws SQLException {
        return this.connection.getMetaData();
    }
}
