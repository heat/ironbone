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

import com.bluewolfbr.ironbone.models.Column;
import com.bluewolfbr.ironbone.models.Table;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import com.bluewolfbr.ironbone.models.Column.COLUMN_TYPE;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.HashSet;
import org.junit.Test;
import org.mockito.Mockito;

public class IronBoneApplicationTest {

    private static final String connectionURL = "jdbc:derby:memory:ironbone";
    private static Connection conn;

    @org.junit.BeforeClass
    public static void setUp() throws ClassNotFoundException, SQLException, MalformedURLException, URISyntaxException, FileNotFoundException {

        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:mem:data/tutorial";

        conn = DriverManager.getConnection(url, "sa", "");
        String createTable = "DROP TABLE VENDA IF EXISTS;"
                + "DROP TABLE PRODUTO IF EXISTS;"
                + " CREATE TABLE PRODUTO ( "
                + " ID INTEGER NOT NULL PRIMARY KEY, "
                + " NOME VARCHAR(50) NOT NULL, "
                + " DESCRICAO VARCHAR(50) NOT NULL) ;"
                + " CREATE TABLE VENDA ( "
                + " ID INTEGER NOT NULL PRIMARY KEY,"
                + " ID_PRODUTO INTEGER NOT NULL , "
                + "    FOREIGN KEY (ID_PRODUTO) "
                + "    REFERENCES PRODUTO(ID) "
                + " ) ;";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(createTable);
        stmt.close();
    }

    @org.junit.AfterClass
    public static void setDown() throws SQLException {
        conn.close();

    }

    @Test
    public void testEmbeddedDerby() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
        Connection conn = getConnection();
        DatabaseMetaData mtdt = conn.getMetaData();

        ResultSet rs = mtdt.getTables(conn.getCatalog(), "%", "%", null);

        ResultSetMetaData rsmd = rs.getMetaData();
        int numCols = rsmd.getColumnCount();
        for (int i = 1; i <= numCols; i++) {
            if (i > 1) {
                System.out.print(", ");
            }
            System.out.print(rsmd.getColumnLabel(i));
        }
        System.out.println("");
        while (rs.next()) {
            for (int i = 1; i <= numCols; i++) {
                if (i > 1) {
                    System.out.print(", ");
                }
                System.out.print(rs.getString(i));
            }
            System.out.println("");
        }

        DatabaseMetaData metadata = conn.getMetaData();

        ResultSet rsa = metadata.getColumns(null, null, "PRODUTO", null);

        rsmd = rsa.getMetaData();
        numCols = rsmd.getColumnCount();
        while (rsa.next()) {
            System.out.println(rsa.getString("COLUMN_NAME"));
        }
        for (int i = 1; i <= numCols; i++) {
            if (i > 1) {
                System.out.print(", ");
            }
            System.out.print(rsmd.getColumnLabel(i));
            System.out.println("teste>.>>>");
        }
        conn.close();
    }

    private static Connection getConnection() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:mem:data/tutorial";

        return DriverManager.getConnection(url, "sa", "");
    }

    public void testMain() throws SQLException {
        String[] args = new String[0];
        assertTrue(Boolean.TRUE);
    }

    @Test
    public void testePrimaryKey() throws SQLException {
        IronBoneRender render = Mockito.mock(IronBoneRender.class);
        IronBoneApplication app = new IronBoneApplication(conn, render);

        Table product = app.getTableRef("PRODUTO");

        Column primaryKey = new Column("ID", COLUMN_TYPE.INTEGER);
        assertEquals(primaryKey, product.getPrimaryKey().get(0));

    }

    /**
     * test if lower or uppercase are the same in table comparison
     */
    @Test
    public void testTableNameComparison() {
        Table product = new Table("ProducT");
        product.columns.add(new Column("NoMe", COLUMN_TYPE.STRING));
        Table other = new Table("PRODUCT");
        other.columns.add(new Column("NoMe", COLUMN_TYPE.STRING));


        assertEquals(other, product);
    }

    @Test
    /**
     * test the read metadata product table and her representation
     */
    public void testGetTableRef() throws SQLException, CloneNotSupportedException {
        Table product = new Table("PRODUTO");
        Column primaryKey = new Column("ID", COLUMN_TYPE.INTEGER);
        primaryKey.primaryKey = true;
        product.columns.add(primaryKey);

        product.columns.add(new Column("NOME", COLUMN_TYPE.STRING));
        product.columns.add(new Column("DESCRICAO", COLUMN_TYPE.STRING));
        IronBoneRender renderEngine = Mockito.mock(IronBoneRender.class);
        IronBoneApplication app = new IronBoneApplication(conn, renderEngine);

        Table actual = app.getTableRef("PRODUTO");

        assertEquals(product, actual);

        Collection<Column> store = actual.columns;
        actual.columns = new HashSet<Column>();
        for (Column c : product.columns) {
            Column columnClone = c.clone();
            actual.columns.add(columnClone);
        }
        assertEquals(product.columns, actual.columns);

        actual.columns = store;

        assertEquals(product.columns, actual.columns);


        assertFalse(actual.getPrimaryKey().isEmpty());
    }
    
    @Test
    public void getForeignKeyRef() throws SQLException {
        IronBoneRender renderEngine = Mockito.mock(IronBoneRender.class);
        IronBoneApplication app = new IronBoneApplication(conn, renderEngine);

        Table actual = app.getTableRef("VENDA");
        assertFalse(actual.getPrimaryKey().isEmpty());
        assertFalse(actual.getForeignKey().isEmpty());
    }
    
    @Test
    public void getResourceTemplate() throws URISyntaxException {
        URL file = IronBoneApplication.class
                .getResource("readme.txt");

        System.out.println(file);
        File f = new File(file.toURI());

        assertTrue(f.exists());
    }
}
