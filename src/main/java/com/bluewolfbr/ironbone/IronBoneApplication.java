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

import com.bluewolfbr.ironbone.model.Column;
import com.bluewolfbr.ironbone.model.ColumnImpl;
import com.bluewolfbr.ironbone.model.Table;
import com.bluewolfbr.ironbone.model.TableImpl;
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
        TableBuilder tb= DomainModelBuilder.getTableBuilder();
        Table table = tb.buildTable(tablename, "","").setClassName("Nome da Classe").setColumns(
                 this.getColumnsRef(tablename)).getResult();
        return table;
    }

    public List<ColumnImpl> getForeignKeyColumns(String tablename) throws SQLException {
        return new ArrayList<ColumnImpl>(0);
        /*List<ColumnImpl> columns = new ArrayList<ColumnImpl>(0);
         DatabaseMetaData metadata = conn.getMetaData();
         ResultSet rs = metadata.getImportedKeys(null, null, tablename);
         while (rs.next()) {
         ColumnImpl column = new ColumnImpl(rs.getString("PKCOLUMN_NAME"), null);
         column.foreignKey = true;
         column.referencedTable = rs.getString("FKTABLE_NAME");
         columns.add(column);
            
         }
         return columns;
         */
    }

    public List<ColumnImpl> getPrimaryKeyColumns(String tablename) throws SQLException {
        List<ColumnImpl> columns = new ArrayList<ColumnImpl>(0);
        DatabaseMetaData metadata = conn.getMetaData();
        ResultSet rs = metadata.getPrimaryKeys(null, null, tablename);
        while (rs.next()) {
            columns.add(
                    new ColumnImpl(rs.getString("COLUMN_NAME"), null));
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
    private List<Column> getColumnsRef(String tablename)
            throws SQLException {
        List<Column> columns = new ArrayList<Column>();

        DatabaseMetaData metadata = conn.getMetaData();

        ResultSet rs = metadata.getColumns(null, null, tablename, null);

        if (rs != null) {
            while (rs.next()) {
                columns.add(resultsetToColumn(rs));
            }
            rs.close();
        }
        return columns;
    }

    private ColumnImpl resultsetToColumn(ResultSet rs) throws SQLException {
        ColumnImpl column = new ColumnImpl(rs.getString("COLUMN_NAME"),
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
