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
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.tornado.template.Template;
import no.tornado.template.TemplateException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlConfigTest {

    public YamlConfigTest() {
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

    /**
     * Tenta carregar um arquivo de configuracao mapeado para a classe de
     * configuracao
     */
    @Test
    public void testLoadAs() throws Exception {

        File yamlFile = new File(this.getClass().getResource("config.yml").toURI());

        Yaml yamlParser = new Yaml();
        InputStream is = new FileInputStream(yamlFile);

        IronBoneConfiguration config = yamlParser.loadAs(is, IronBoneConfiguration.class);

        is.close();

        assertNotNull(config);
    }

    @Test
    public void testDumpConfig() throws Exception {
        File tmpFile = File.createTempFile("config", ".yml");

        Writer outputFile = new FileWriter(tmpFile);

        IronBoneConfiguration config = new IronBoneConfiguration();
        config.config.resolver.templates = new String[][]{
            new String[]{"entity.template", "aa"},new String[]{"entity.template", "aa"},new String[]{"entity.template", "aa"}};
        Yaml yaml = new Yaml();

        String outputContent = yaml.dumpAs(config, Tag.YAML, DumperOptions.FlowStyle.BLOCK);
        outputFile.write(outputContent);

        outputFile.close();

        System.out.println(tmpFile.getAbsoluteFile());
        yaml = new Yaml();
        IronBoneConfiguration parsed = yaml.loadAs(outputContent, IronBoneConfiguration.class);
        assertNotNull(parsed);
        assertTrue(true);
    }
    @Test
    public void testParserIterator() throws TemplateException {
        Template template;
        String content = "[#list names as name]my name is: ${name} [/#list]";
        List<String> namesList = new ArrayList<String>();
        namesList.add("Onezino");
        namesList.add("Luisa");
        Map ctx = new HashMap();
        ctx.put("names", namesList);
        template = new Template(content, ctx);
        String expected = "my name is: Onezino my name is: Luisa ";
        String result = template.render();
        assertEquals(expected, result);
    }
    @Test
    public void testParserEnhancedIterator() throws TemplateException {
        Template template;
        String content = "[#list names as name]my name is: ${name}[/#list separator=',']";
        List<String> namesList = new ArrayList<String>();
        namesList.add("Onezino");
        namesList.add("Luisa");
        Map ctx = new HashMap();
        ctx.put("names", namesList);
        template = new Template(content, ctx);
        String expected = "my name is: Onezino,my name is: Luisa";
        String result = template.render();
        assertEquals(expected, result);
    }
    
    @Test
    public void testParserDirectoryPropertie() throws TemplateException {
        Template template;
        Map ctx = new HashMap();
        ctx.put("basedir", new File("null").getAbsolutePath());
        template = new Template("${basedir}", ctx);

        assertEquals(new File("null").getAbsolutePath(), template.render());
    }
    
    @Test
    public void testGetFullQualifyClass(){
        String separetor = "/";
        String basePackage = "com.bluewolf";
        String packageOutputDirectory = "c:/test/output/com/bluewolf";
        
        String templateFile = "c:/template/dao/arroba/dao.template";
        String templateDir  = "c:/template";
        
        String basePackageDirectory = basePackage.replace(".", separetor);
               
        File template = new File(templateFile);
        
        File parent = template.getParentFile();
        File dir = new File(templateDir);
        String expected = "dao.arroba";
        String result = parent.getAbsolutePath().replace(dir.getAbsolutePath(), "").replace(File.separator, ".");
        if(result.startsWith("."))
            result = result.replaceFirst(".", "");
        assertEquals(expected, result);
    }
}