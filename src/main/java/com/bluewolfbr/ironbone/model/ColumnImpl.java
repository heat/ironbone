package com.bluewolfbr.ironbone.model;

import com.bluewolfbr.ironbone.utils.Formatter;

public class ColumnImpl {

    public enum COLUMN_TYPE {

        STRING("String"), INTEGER("Integer"), DOUBLE("Double"), BIGDECIMAL("BigDecimal"), BOOLEAN("Boolean"), LONG("Long"), FLOAT("Float");
        String type;

        COLUMN_TYPE(String type) {
            this.type = type;
        }

        public String toString() {
            return this.type;
        }
    }
    public String name;
    public COLUMN_TYPE type;
    public String referencedTable = "";
    public boolean primaryKey = false;
    public boolean foreignKey = false;

    public ColumnImpl(String name, COLUMN_TYPE type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getClassName() {
        return Formatter.toCamelCase(this.name);
    }

    public String getDbName() {
        return this.name;
    }

    public String getVariableName() {
        return Formatter.toLowerCamelCase(this.name);
    }

    /**
     * retorna a tabela referenciada se for uma columna do tipo FK
     * ou o  tipo da coluna.
     * @return 
     */
    public String getReferencedType() {
        if (this.foreignKey) {
            return this.referencedTable;
        }
        return this.type.type;
    }

    public String getReferencedName() {
        if (this.foreignKey) {
            return this.referencedTable;
        }
        return this.name;
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ColumnImpl other = (ColumnImpl) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.toLowerCase().equals(other.name.toLowerCase())) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + this.name + "";
    }

    @Override
    public ColumnImpl clone() throws CloneNotSupportedException {
        return new ColumnImpl(this.name, this.type);
    }
}
