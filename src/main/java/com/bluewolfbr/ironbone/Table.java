package com.bluewolfbr.ironbone;

import java.util.ArrayList;
import java.util.Collection;

public class Table {

    public String name;
    public Collection<Column> columns;
    
    public Table(String name) {
        this.name = name;
        columns = new ArrayList<Column>();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
                return true;
        if (other == null || (other.getClass() != this.getClass())){
            return false;
        }
        
        Table otherTable = (Table) other;
        
        if (!this.name.toLowerCase().equals(otherTable.name.toLowerCase())) {
            return false;
        }
        
        if( !this.columns.equals(otherTable.columns))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return this.name;
    }
    
}
