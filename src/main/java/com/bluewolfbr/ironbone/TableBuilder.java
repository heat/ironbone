/*
 * Copyright 2013 OnezinoGabriel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluewolfbr.ironbone;

import com.bluewolfbr.ironbone.model.Column;
import com.bluewolfbr.ironbone.model.Table;
import com.bluewolfbr.ironbone.model.TableImpl;
import com.bluewolfbr.ironbone.model.MutableTableImpl;
import java.util.List;

/**
 *
 * @author OnezinoGabriel
 */
public class TableBuilder {

    public class ColumnDefinitionBuilder {

        private TableBuilder tableBuilder;
        private final List<Column> columns;

        private ColumnDefinitionBuilder(TableBuilder parentBuilder, List<Column> columns) {
            this.tableBuilder = parentBuilder;
            this.columns = columns;
        }

        public Table getResult() {
            return this.tableBuilder.getResult();
        }

        private MutableTableImpl getResult(MutableTableImpl table) {
            table.setColumns(this.columns);
            return table;
        }
    }

    public class TableInitialBuilder {

        private String tableName;
        private String catalog;
        private String schema;
        TableBuilder tableBuilder;
        private String className;

        private TableInitialBuilder(TableBuilder parentBuilder, String tableName, String catalog, String schema) {
            this.tableBuilder = parentBuilder;
            this.tableName = tableName;
            this.catalog = catalog;
            this.schema = schema;
        }

        public TableBuilder setClassName(String className) {
            this.className = className;
            return this.tableBuilder;
        }

        /**
         * A proxy to columnDefinitionBuilder if user wants ignore the complete
         * path.
         *
         * @see TableBuilder#setColumns(java.util.List)
         * @param columns
         * @return
         */
        public ColumnDefinitionBuilder setColumns(List<Column> columns) {
            return this.tableBuilder.setColumns(columns);
        }

        public Table getResult() {
            return this.tableBuilder.getResult();
        }

        private MutableTableImpl getResult(MutableTableImpl table) {
            table.setTableName(this.tableName);
            if (this.className == null) {
                this.className = this.tableName;
            }
            table.setClassName(this.className);
            table.setCatalog(this.catalog);
            table.setSchema(this.schema);
            return table;
        }
    }
    private TableInitialBuilder initialBuilder;
    private ColumnDefinitionBuilder columnBuilder;

    public TableBuilder() {
    }

    public TableInitialBuilder buildTable(String tableName, String catalog, String schema) {
        this.initialBuilder = new TableInitialBuilder(this, tableName, catalog, schema);
        return this.initialBuilder;
    }

    public ColumnDefinitionBuilder setColumns(List<Column> columns) {
        this.columnBuilder = new ColumnDefinitionBuilder(this, columns);
        return this.columnBuilder;
    }

    private Table getResult() {
        MutableTableImpl mutableTable = new MutableTableImpl();
        if (this.initialBuilder != null) {
            this.initialBuilder.getResult(mutableTable);
        }
        if (this.columnBuilder != null) {
            this.columnBuilder.getResult(mutableTable);
        }

        return getImutableTable(mutableTable);
    }

    private Table getImutableTable(MutableTableImpl mutableTable) {
        return new TableImpl(mutableTable.getTableName(), mutableTable.getColumns());
    }
}
