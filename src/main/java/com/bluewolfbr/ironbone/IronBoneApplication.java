package com.bluewolfbr.ironbone;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bluewolfbr.ironbone.utils.TransformSQLType;

public class IronBoneApplication {
    private Connection conn;

    public IronBoneApplication(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {

    }

    public Table getTableRef(String tablename) throws SQLException {
        Table ref = new Table(tablename);
        ref.columns.addAll(this.getColumnsRef(tablename));
        return ref;
    }

    /**
     * Retrieve the columns into collection of Columns <br>
     * 
     * @see <a
     *      href="http://docs.oracle.com/javase/6/docs/technotes/guides/jdbc/getstart/mapping.html">JDBC
     *      Mapping</a>
     * @param tablename
     * @return Collection of mapped column type
     * @throws SQLException
     */
    private Collection<Column> getColumnsRef(String tablename)
            throws SQLException {
        Collection<Column> columns = new ArrayList<Column>();

        DatabaseMetaData metadata = conn.getMetaData();

        ResultSet rs = metadata.getColumns(null,null, tablename, null);

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

    /**
     * read the columns name of the table. <br>
     * 
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     * @param table
     * @return
     * @throws SQLException
     */
    @Deprecated
    public String[] getColumnsTable(String table) throws SQLException {
        List<String> columns = new ArrayList<String>();

        DatabaseMetaData metadata = conn.getMetaData();

        ResultSet rs = metadata.getColumns(null, null, table, null);

        while (rs.next()) {
            System.out.println(rs.getString("TABLE_CAT"));
            System.out.println(rs.getString("TABLE_SCHEM"));
            System.out.println(rs.getString("COLUMN_NAME"));
            System.out.println(rs.getString("TYPE_NAME"));
            System.out.println(rs.getInt("DATA_TYPE"));

            columns.add(rs.getString("COLUMN_NAME"));
        }

        return columns.toArray(new String[] {});
    }
}
