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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * Read database information and give the connection
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
    
    public DatabaseConfig(String yamlfile) throws MalformedURLException, URISyntaxException, FileNotFoundException {
        this(new URI(yamlfile));
    }
    
    public DatabaseConfig(URL yamlfile) throws FileNotFoundException, URISyntaxException {
        this(yamlfile.toURI());
    }
    
    public DatabaseConfig(URI yamlfile) throws FileNotFoundException {
        this(new File(yamlfile));
    }
    
    public DatabaseConfig(File yamlfile) throws FileNotFoundException {
        readYamlFile(yamlfile);
    }
    
    private void readYamlFile(File yamlfile) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream read = new FileInputStream(yamlfile);
        Map data = (Map) yaml.load(read);
        System.out.println(data);
        try {
            mappingYaml((Map) data.get("database"));
        }catch(MalformedURLException e) {
            
        }
    }
    
    private void mappingYaml(Map data) throws MalformedURLException {
        this.driver = (String) data.get("driver");
        this.database =(String) data.get("database");
        this.user =(String) data.get("user");
        this.password =(String) data.get("password");
        this.catalog =(String) data.get("catalog");
        this.schema =(String) data.get("schema");
        this.url = (String) data.get("url");        
    }    
    /**
     * return a connection based in configfile
     * @return 
     */
    public Connection getConnection() {
        if(conn == null) {
            try {
                conn = createConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseConfig.class.getName()).log(Level.SEVERE, null, ex);
            }catch (SQLException ex) {
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
    
    String getUrl() {
        return this.url;
    }




}
