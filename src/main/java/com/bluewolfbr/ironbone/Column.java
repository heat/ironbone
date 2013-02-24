package com.bluewolfbr.ironbone;

public class Column {
    public enum COLUMN_TYPE{
        STRING, INTEGER, DOUBLE, BIGDECIMAL, BOOLEAN, LONG, FLOAT
    }

        public String name;
        public COLUMN_TYPE type;
        
    public Column(String name, COLUMN_TYPE type) {
        this.name = name;
        this.type = type;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Column other = (Column) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.toLowerCase().equals(other.name.toLowerCase()))
            return false;
        if (type != other.type)
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "" + this.name + "";
    }


    @Override
    protected Column clone() throws CloneNotSupportedException {
        return new Column(this.name, this.type);
    }
    
    
}
