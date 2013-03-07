package com.bluewolfbr.ironbone.models;

import com.bluewolfbr.ironbone.utils.Formatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Table {

    public String name;
    public Collection<Column> columns;

    public Table(String name) {
        this.name = name;
        columns = new LinkedList<Column>();
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

    public List<Column> getPrimaryKey() {
        List<Column> columns = new ArrayList<Column>();
        for (Column c : this.columns) {
            if (c.primaryKey) {
                columns.add(c);
            }
        }
        return columns;
    }

    public List<Column> getOwnColumns() {
        List<Column> columns = new ArrayList<Column>();
        
        for (Column c: this.columns) {
            if(!c.primaryKey && !c.foreignKey) {
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

    public List<Column> getForeignKey() {
        List<Column> columns = new ArrayList<Column>();
        for (Column c : this.columns) {
            if (c.referencedTable != null && !c.referencedTable.isEmpty()) {
                columns.add(c);
            }
        }
        return columns;
    }

    /**
     * retorna se a tabela é composição de outra tabela
     * @return 
     */
    public boolean getComposition() {
        for(Column c: this.columns){
            if(c.primaryKey && c.foreignKey) {
                return true;
            }
        } 
        return false;
    }
    
    /**
     * @see Table#getComposition() 
     * @return 
     */
    public boolean isComposition() {
        return getComposition();
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
