package com.bluewolfbr.ironbone;

import com.bluewolfbr.ironbone.Column;
import com.bluewolfbr.ironbone.Table;
import com.bluewolfbr.ironbone.IronBoneApplication;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.bluewolfbr.ironbone.Column.COLUMN_TYPE;
import org.junit.Test;

public class IronBoneApplicationTest {

    private static final String connectionURL = "jdbc:derby:memory:ironbone";
    private static Connection conn;

    @org.junit.BeforeClass
    public static void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:mem:data/tutorial";

        conn = DriverManager.getConnection(url, "sa", "");
        String createTable = "CREATE TABLE PRODUTO ( ID INTEGER NOT NULL PRIMARY KEY, NOME VARCHAR(50) NOT NULL, DESCRICAO VARCHAR(50) NOT NULL)";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(createTable);
        stmt.close();
        /*
         String driver = "org.apache.derby.jdbc.EmbeddedDriver";
         String connectionURL = IronBoneApplicationTest.connectionURL + ";create=true";
        

         Class.forName(driver);
         if (conn != null) {
         return;
         }
         conn = DriverManager.getConnection(connectionURL);
         Statement stmt = conn.createStatement();
         stmt.executeUpdate(createTable);
         stmt.close();
         * */
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
        while(rsa.next()){
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
        IronBoneApplication.main(args);

        assertTrue(Boolean.TRUE);
    }

    @Test
    @Deprecated
    public void testGetColumns() throws SQLException {

        String[] actuals;
        String[] expecteds = new String[]{"ID", "NOME", "DESCRICAO"};
        IronBoneApplication app = new IronBoneApplication(conn);

        actuals = app.getColumnsTable("PRODUTO");

        assertArrayEquals(expecteds, actuals);

    }

    @Test
    /**
     * test if lower or uppercase are the same in table comparison
     */
    public void testTableNameComparison() {
        Table product = new Table("ProducT");
        product.columns.add(new Column("NoMe", COLUMN_TYPE.STRING));
        Table other = new Table("PRODUCT");
        other.columns.add(new Column("NOME", COLUMN_TYPE.STRING));
        
        
        assertEquals(other, product);
    }
        
    @Test
    /**
     * test the read metadata product table and her representation
     */
    public void testGetTableRef() throws SQLException, CloneNotSupportedException {
        Table product = new Table("PRODUTO");
        product.columns.add(new Column("ID", COLUMN_TYPE.INTEGER));
        product.columns.add(new Column("NOME", COLUMN_TYPE.STRING));
        product.columns.add(new Column("DESCRICAO", COLUMN_TYPE.STRING));

        IronBoneApplication app = new IronBoneApplication(conn);

        Table actual = app.getTableRef("PRODUTO");

        assertEquals(product, actual);

        Collection<Column> store = actual.columns;
        actual.columns = new ArrayList<Column>();
        for (Column c : product.columns) {
            Column columnClone = c.clone();
            actual.columns.add(columnClone);
        }
        assertEquals(product.columns, actual.columns);

        actual.columns = store;

        assertEquals(product.columns, actual.columns);
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
