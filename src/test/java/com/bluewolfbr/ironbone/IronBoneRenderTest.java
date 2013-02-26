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

import com.bluewolfbr.ironbone.template.java.JavaResolver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.yaml.snakeyaml.Yaml;

public class IronBoneRenderTest {

    public IronBoneRenderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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
    public void testRender() throws Exception {
        System.out.println("render");
        Table data = new Table("PRODUTO");
        data.primaryKey = new Column("ID", Column.COLUMN_TYPE.INTEGER);
        data.columns.add(data.primaryKey);
        data.columns.add(new Column("NOME", Column.COLUMN_TYPE.STRING));
        data.columns.add(new Column("DESCRICAO", Column.COLUMN_TYPE.STRING));
        IronBoneRender instance = new IronBoneRender(new JavaResolver());
        int expResult = 0;
        int result = instance.render(data);
        assertEquals(expResult, result);
    }
    @Test
    public void testRenderResolverConfig() throws Exception {
        System.out.println("render");
        Table data = new Table("PRODUCT_MARVEN");
        data.primaryKey = new Column("ID", Column.COLUMN_TYPE.LONG);
        data.columns.add(data.primaryKey);
        data.columns.add(new Column("NAME", Column.COLUMN_TYPE.STRING));
        data.columns.add(new Column("DESCRIPTION", Column.COLUMN_TYPE.STRING));
        Map properties = (Map) readYamlFile(
                new File(this.getClass().getResource("config.yml").toURI())).get("config");
        properties = (Map) properties.get("resolver");
        IronBoneRender instance = new IronBoneRender(new JavaResolver().build(properties));
        
        
        int result = instance.render(data);
        int expResult = 0;
        assertEquals(expResult, result);
    }

    /**
     * test the possibility to call render with a new Resolver.
     *
     * @throws Exception
     */
    @Test
    public void testeRenderResolver() throws Exception {
        System.out.println("render");
        Table data = new Table("PRODUTO");
        data.primaryKey = new Column("ID", Column.COLUMN_TYPE.LONG);
        data.columns.add(new Column("NOME", Column.COLUMN_TYPE.STRING));
        data.columns.add(new Column("DESCRICAO", Column.COLUMN_TYPE.STRING));
        IronBoneRender instance = new IronBoneRender(new JavaResolver());


        int expResult = 0;
        
        int result = instance.render(data, new JavaResolver() {

            @Override
            public File getOutputDirectory() {
                File outputDirectory = new File("c:/tmp/test/new/output");
                outputDirectory.mkdirs();
                return outputDirectory;
            }
        });
        assertEquals(expResult, result);
    }

    private Map readYamlFile(File yamlfile) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream read = new FileInputStream(yamlfile);
        Map data = (Map) yaml.load(read);
        return data;
    }
}