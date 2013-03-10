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
import com.bluewolfbr.ironbone.model.ColumnImpl;
import com.bluewolfbr.ironbone.model.Table;
import com.bluewolfbr.ironbone.model.TableImpl;
import com.bluewolfbr.ironbone.template.java.JavaResolver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
        List<Column> columns = new ArrayList<Column>();

        ColumnImpl primaryKey = new ColumnImpl("ID", ColumnImpl.COLUMN_TYPE.LONG);
        columns.add(primaryKey);
        columns.add(new ColumnImpl("NOME", ColumnImpl.COLUMN_TYPE.STRING));
        columns.add(new ColumnImpl("DESCRICAO", ColumnImpl.COLUMN_TYPE.STRING));
        Table data = new TableImpl("PRODUTO", columns);
        IronBoneRender instance = new IronBoneRender(new JavaResolver());
        int expResult = 0;
        int result = instance.render(data);
        assertEquals(expResult, result);
    }

    public void testRenderResolverConfig() throws Exception {
        /*System.out.println("render");

         Table data = new TableImpl("PRODUCT_MARVEN");
         ColumnImpl primaryKey = new ColumnImpl("ID", ColumnImpl.COLUMN_TYPE.LONG);
         primaryKey.primaryKey = true;
         data.columns.add(primaryKey);
         data.columns.add(new ColumnImpl("NAME", ColumnImpl.COLUMN_TYPE.STRING));
         data.columns.add(new ColumnImpl("DESCRIPTION", ColumnImpl.COLUMN_TYPE.STRING));
         IronBoneConfiguration properties = readYamlFile(
         new File(this.getClass().getResource("config.yml").toURI()));

         IronBoneRender instance = new IronBoneRender(new JavaResolver().build(properties.config.resolver));


         int result = instance.render(data);
         int expResult = 0;
         assertEquals(expResult, result);
         */
    }

    /**
     * test the possibility to call render with a new Resolver.
     *
     * @throws Exception
     */
    @Test
    public void testeRenderResolver() throws Exception {
        System.out.println("render");
        List<Column> columns = new ArrayList<Column>();

        ColumnImpl primaryKey = new ColumnImpl("ID", ColumnImpl.COLUMN_TYPE.LONG);
        columns.add(primaryKey);
        columns.add(new ColumnImpl("NOME", ColumnImpl.COLUMN_TYPE.STRING));
        columns.add(new ColumnImpl("DESCRICAO", ColumnImpl.COLUMN_TYPE.STRING));
        Table data = new TableImpl("PRODUTO", columns);
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

    private IronBoneConfiguration readYamlFile(File yamlfile) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream read = new FileInputStream(yamlfile);
        IronBoneConfiguration data = yaml.loadAs(read, IronBoneConfiguration.class);
        return data;
    }
}