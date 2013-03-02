package com.bluewolfbr.ironbone;

import com.bluewolfbr.ironbone.utils.Formatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Table {

    public String name;
    public Collection<Column> columns;

    public Table(String name) {
        this.name = name;
        columns = new HashSet<Column>();
    }

    public String getName() {
        return this.name;
    }

    public String getDbName() {
        return this.name;
    }

    public String getClassName() {
        return Formatter.toCamelCase(this.name);
    }

    public String getVariableName() {
        return Formatter.toLowerCamelCase(this.name);
    }

    public List<Column> getPrimaryKeys() {
        List<Column> columns = new ArrayList<Column>();
        for (Column c : this.columns) {
            if (c.primaryKey) {
                columns.add(c);
            }
        }
        return columns;
    }

    public List<Column> getColumns() {
        List<Column> columns = new ArrayList<Column>();

        for (Column c : this.columns) {
            if (!c.primaryKey) {
                columns.add(c);
            }
        }
        return columns;
    }

    public Collection<Column> getAllColumns() {
        return this.columns;
    }

    public List<Column> getForeignKeys() {
        List<Column> columns = new ArrayList<Column>();
        for (Column c : this.columns) {
            if (c.foreignTable != null && !c.foreignTable.isEmpty()) {
                columns.add(c);
            }
        }
        return columns;
    }

    /**
     * find a column that match the given name.
     *
     * @param columnName
     * @return
     */
    public Column getColumnByName(String columnName) {
        Column returnColumn = null;
        for (Column column : columns) {
            if (column.name.equals(columnName)) {
                returnColumn = column;
                break;
            }
        }
        return returnColumn;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || (other.getClass() != this.getClass())) {
            return false;
        }

        Table otherTable = (Table) other;

        if (!this.name.toLowerCase().equals(otherTable.name.toLowerCase())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
