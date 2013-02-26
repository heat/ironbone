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
import java.sql.Statement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author OnezinoGabriel
 */
public class IronBoneRunnerTest {

    private static Connection conn;

    public IronBoneRunnerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:mem:data/tutorial";

        conn = DriverManager.getConnection(url, "sa", "");
        String createTable = "DROP TABLE PRODUTO IF EXISTS; CREATE TABLE PRODUTO ( ID INTEGER NOT NULL PRIMARY KEY, NOME VARCHAR(50) NOT NULL, DESCRICAO VARCHAR(50) NOT NULL)";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(createTable);
        stmt.close();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = new String[]{this.getClass().getResource("configIronBoneRunner.yml").toString(), "PRODUTO"};
        IronBoneRunner.main(args);
    }
}