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
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.yaml.snakeyaml.Yaml;

public class DatabaseConfigTest {
    
    private static IronBoneConfiguration configuration = new IronBoneConfiguration();
    public DatabaseConfigTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws URISyntaxException, FileNotFoundException {
        URI file = DatabaseConfigTest.class.getResource("config.yml").toURI();
        InputStream is = new FileInputStream(new File(file));
        Yaml yaml = new Yaml();
        
        DatabaseConfigTest.configuration = yaml.loadAs(is, IronBoneConfiguration.class);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testYamlmelize() {
        Yaml yaml = new Yaml();
        System.out.println(yaml.dump(new Object(){
            private String url="teste";
            private String driver="jdbc";
            private String schema="asdasds";
            private String database="dsadsa";
            private String catalog;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDriver() {
                return driver;
            }

            public void setDriver(String driver) {
                this.driver = driver;
            }

            public String getSchema() {
                return schema;
            }

            public void setSchema(String schema) {
                this.schema = schema;
            }

            public String getDatabase() {
                return database;
            }

            public void setDatabase(String database) {
                this.database = database;
            }

            public String getCatalog() {
                return catalog;
            }

            public void setCatalog(String catalog) {
                this.catalog = catalog;
            }

        }));
        
    }
    
    @Test
    public void testGetConnection() throws Exception {
        System.out.println("getConnection");
        DatabaseConfig instance = new DatabaseConfig(DatabaseConfigTest.configuration);
        Connection result = instance.getConnection();
        assertNotNull(result);
    }
}