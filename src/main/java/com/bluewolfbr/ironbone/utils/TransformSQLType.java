package com.bluewolfbr.ironbone.utils;

import java.sql.Types;

import com.bluewolfbr.ironbone.Column.COLUMN_TYPE;

public class TransformSQLType {

    public static COLUMN_TYPE toColumnType(int type) {
        switch(type) {
         case Types.VARCHAR:
         case Types.CHAR:
         case Types.LONGNVARCHAR:
             return COLUMN_TYPE.STRING;
         case Types.NUMERIC:
         case Types.DECIMAL:
             return COLUMN_TYPE.BIGDECIMAL;
         case Types.BIT:
             return COLUMN_TYPE.BOOLEAN;
         case Types.TINYINT:
         case Types.SMALLINT:
         case Types.INTEGER:
             return COLUMN_TYPE.INTEGER;
         case Types.BIGINT:
             return COLUMN_TYPE.LONG;
         case Types.REAL:
             return COLUMN_TYPE.FLOAT;
         case Types.FLOAT:
         case Types.DOUBLE:
             return COLUMN_TYPE.DOUBLE;
         //TODO need complete the mapping
        }
        throw new IllegalArgumentException("data type is invalid.");
    }

}
