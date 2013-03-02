/*
 * Copyright 2013 OnezinoGabriel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluewolfbr.ironbone;

import com.bluewolfbr.ironbone.utils.IVisitable;
import com.bluewolfbr.ironbone.utils.IVisitor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bluewolfbr.ironbone.utils.TransformSQLType;
import java.io.File;
import java.util.Iterator;

public class IronBoneApplication implements IVisitable {

    private Connection conn;
    private IronBoneRender renderEngine;

    private IronBoneApplication() {
    }

    public IronBoneApplication(DatabaseConfig dbconfig, IronBoneRender renderEngine) {
        this(dbconfig.getConnection(), renderEngine);
    }

    public IronBoneApplication(Connection conn, IronBoneRender renderEngine) {
        this.conn = conn;
        this.renderEngine = renderEngine;
    }

    public void run(String tableName) throws Exception {
        Table table = getTableRef(tableName);
        renderEngine.render(table);
    }

    public Table getTableRef(String tablename) throws SQLException {
        Table ref = new Table(tablename);
        ref.columns.addAll(this.getColumnsRef(tablename));
        for (Column c : getColumnsPrimaryKey(tablename)) {
            Column column = ref.getColumnByName(c.name);
            column.primaryKey = true;
        }

        for (Column c : getColumnsForeignKey(tablename)) {
            Column column = ref.getColumnByName(c.name);
            column.foreignTable = c.foreignTable;
        }
        return ref;
    }

    public List<Column> getColumnsForeignKey(String tablename) throws SQLException {
        List<Column> columns = new ArrayList<Column>(0);
        DatabaseMetaData metadata = conn.getMetaData();
        ResultSet rs = metadata.getImportedKeys(null, null, tablename);
        while (rs.next()) {
            Column column = new Column(rs.getString("PKCOLUMN_NAME"), null);
            column.foreignTable = rs.getString("FKTABLE_NAME");
            columns.add(column);
            
        }
        return columns;
    }

    public List<Column> getColumnsPrimaryKey(String tablename) throws SQLException {
        List<Column> columns = new ArrayList<Column>(0);
        DatabaseMetaData metadata = conn.getMetaData();
        ResultSet rs = metadata.getPrimaryKeys(null, null, tablename);
        while (rs.next()) {
            columns.add(
                    new Column(rs.getString("COLUMN_NAME"), null));
        }
        return columns;
    }

    /**
     * Retrieve the columns into collection of Columns <br>
     *
     * @see <a
     * href="http://docs.oracle.com/javase/6/docs/technotes/guides/jdbc/getstart/mapping.html">JDBC
     * Mapping</a>
     * @param tablename
     * @return Collection of mapped column type
     * @throws SQLException
     */
    private Collection<Column> getColumnsRef(String tablename)
            throws SQLException {
        Collection<Column> columns = new ArrayList<Column>();

        DatabaseMetaData metadata = conn.getMetaData();

        ResultSet rs = metadata.getColumns(null, null, tablename, null);

        while (rs.next()) {
            columns.add(resultsetToColumn(rs));
        }
        if (rs != null) {
            rs.close();
        }
        return columns;
    }

    private Column resultsetToColumn(ResultSet rs) throws SQLException {
        Column column = new Column(rs.getString("COLUMN_NAME"),
                TransformSQLType.toColumnType(rs.getInt("DATA_TYPE")));
        return column;
    }

    public static IronBoneApplication getEmptyInstance() {
        return new IronBoneApplication();
    }

    @Override
    public void accept(IVisitor visitor) {
        final List<String> properties = new ArrayList<String>();
        properties.add(
                "basedir:"
                + new File("null").getAbsoluteFile().getParentFile().getPath());

        IVisitor.IContextData contextData = new IVisitor.IContextData() {
            @Override
            public Iterator<String> iterator() {
                return properties.iterator();
            }
        };
        visitor.visit(contextData);
    }
}
