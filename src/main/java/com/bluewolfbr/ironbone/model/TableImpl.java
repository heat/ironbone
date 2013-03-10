package com.bluewolfbr.ironbone.model;

import com.bluewolfbr.ironbone.utils.Formatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TableImpl implements Table {

    public String name;
    public List<Column> columns;

    public TableImpl(String name) {
        this.name = name;
        columns = new LinkedList<Column>();
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
            if (column.getDbName().equals(columnName)) {
                returnColumn = column;
                break;
            }
        }
        return returnColumn;
    }

    @Override
    public String getFieldName() {
        return Formatter.toLowerCamelCase(this.name);
    }

    @Override
    public List<Column> getColumns() {
        return this.columns;
    }

    @Override
    public List<Column> getPks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean hasFk() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Column> getFks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Column> getOwnColumns() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean hasComposition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Table> getCompositionChilds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isComposition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Table getCompositionParent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean hasAggregation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Table> getAggregationChilds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isAggregation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Table getAggregationParent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean hasAssociation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Table> getAssociationChilds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isAssociation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Table getAssociationParent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDbName() {
        return this.name;
    }

    @Override
    public String getClassName() {
        return Formatter.toCamelCase(this.name);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || (other.getClass() != this.getClass())) {
            return false;
        }

        TableImpl otherTable = (TableImpl) other;

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
