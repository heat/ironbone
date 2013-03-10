package com.bluewolfbr.ironbone.model;

import com.bluewolfbr.ironbone.utils.Formatter;

public class ColumnImpl implements Column {

    public enum COLUMN_TYPE {

        STRING("String"), INTEGER("Integer"), DOUBLE("Double"), BIGDECIMAL("BigDecimal"), BOOLEAN("Boolean"), LONG("Long"), FLOAT("Float");
        String type;

        COLUMN_TYPE(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
    private String name;
    private COLUMN_TYPE type;
    private String referencedTable = "";
    private boolean primaryKey = false;
    private boolean foreignKey = false;

    public ColumnImpl(String name, COLUMN_TYPE type) {
        this.name = name;
        this.type = type;
    }

    public ColumnImpl(String name, COLUMN_TYPE type, boolean isPk, boolean isFk, String referencedTable) {
        this(name, type);
        this.primaryKey = isPk;
        this.foreignKey = isFk;
        this.referencedTable = referencedTable;
    }

    @Override
    public String getDbName() {
        return this.name;
    }

    @Override
    public String getCamelName() {
        return Formatter.toCamelCase(this.name);
    }

    @Override
    public String getFieldName() {
        return Formatter.toLowerCamelCase(this.name);
    }

    @Override
    public String getType() {
        return this.type.toString();
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
        return "ColumnImpl{" + "name=" + name + ", type=" + type + ", referencedTable=" + referencedTable + ", primaryKey=" + primaryKey + ", foreignKey=" + foreignKey + '}';
    }

    @Override
    public ColumnImpl clone() throws CloneNotSupportedException {
        return new ColumnImpl(this.name, this.type);
    }
}
