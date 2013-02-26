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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Read database information and give the connection
 *
 * @author OnezinoGabriel
 */
public class DatabaseConfig {

    private String url;
    private String driver;
    private String database;
    private String user;
    private String password;
    private String catalog;
    private String schema;
    private Connection conn;

    public DatabaseConfig(IronBoneConfiguration.Database database) {
        this.driver = database.driver;
        this.database = database.database;
        this.user = database.user;
        this.password = database.password;
        this.schema = database.schema;
        this.catalog = database.catalog;
        this.url = database.url;
    }

    public DatabaseConfig(IronBoneConfiguration appconfig) {
        this(appconfig.database);
    }

    /**
     * return a connection based in configfile
     *
     * @return
     */
    public Connection getConnection() {
        if (conn == null) {
            try {
                conn = createConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseConfig.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conn;
    }

    private Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(this.driver);
        //TODO necessario fazer validacao e concatenacao para database
        return DriverManager.getConnection(this.url, this.user, this.password);

    }

}
