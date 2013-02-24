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

import com.bluewolfbr.ironbone.template.java.Resolver;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        data.columns.add(new Column("NOME", Column.COLUMN_TYPE.STRING));
        data.columns.add(new Column("DESCRICAO", Column.COLUMN_TYPE.STRING));
        IronBoneRender instance = new IronBoneRender(new Resolver());
        int expResult = 0;
        int result = instance.render(data);
        assertEquals(expResult, result);
    }

    /**
     * test the possibility to call render with a new Resolver.
     * @throws Exception 
     */
    @Test
    public void testeRenderResolver() throws Exception {
        System.out.println("render");
        Table data = new Table("PRODUTO");
        data.columns.add(new Column("NOME", Column.COLUMN_TYPE.STRING));
        data.columns.add(new Column("DESCRICAO", Column.COLUMN_TYPE.STRING));
        IronBoneRender instance = new IronBoneRender(new Resolver());


        int expResult = 0;
        int result = instance.render(data, new Resolver() {
            @Override
            public File getOutputDirectory() {
                File outputDirectory = new File("c:/tmp/test/new/output");
                outputDirectory.mkdirs();
                return outputDirectory;
            }
        });
        assertEquals(expResult, result);
    }
}