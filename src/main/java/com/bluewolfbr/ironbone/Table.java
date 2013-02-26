package com.bluewolfbr.ironbone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Table {

    public String name;
    public Collection<Column> columns;
    //TODO chave composta
    public Column primaryKey;

    public Table(String name) {
        this.name = name;
        columns = new HashSet<Column>();

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
        if (!this.primaryKey.equals(otherTable.primaryKey)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
