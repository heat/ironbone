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
package com.bluewolfbr.ironbone.template.java;

import com.bluewolfbr.ironbone.utils.PropertiesParser;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.tornado.template.Template;
import no.tornado.template.TemplateException;

public class Resolver {

    private Map context;
    private String outputBaseDir = new File("").getAbsolutePath();
    private String templateDir;
    private List<File> templateFileList = new ArrayList<File>();

    public Resolver(Map properties) {
        this();
        build(properties);
    }

    public void build(Map properties) {
        if (properties.containsKey("package") && properties.get("package") != null) {
            this.context.put("package", properties.get("package"));
        }
        if (properties.containsKey("outputdir") && properties.get("outputdir") != null) {
            this.outputBaseDir = (String) properties.get("outputdir");
        }
        if (properties.containsKey("templatedir") && properties.get("templatedir") != null) {
            try {
                this.templateDir =  PropertiesParser.parser((String) properties.get("templatedir"), context);

            } catch (TemplateException ex) {
            }
        }
        if (properties.containsKey("templates") && properties.get("templates") != null) {
            List templateList = (List) properties.get("templates");
            String[] templates = new String[templateList.size()];
            for (int i = 0; i < templateList.size(); i++) {
                templates[i] = (String) templateList.get(i);
            }
            populateTemplateList(templates);
        }
    }

    public Resolver() {
        this.context = new HashMap();
        context.put("package", "");
        try {
            URL defaultClassFolder = Resolver.class.getResource("Resolver.class");
            this.templateDir = defaultClassFolder.getPath();
        } catch (Exception ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateTemplateList(new String[]{"dao.template", "entity.template"});
    }

    private void populateTemplateList(String[] templates) {
        templateFileList.clear();

        for (String template : templates) {
            File f = new File(this.templateDir + template);
            if (f.exists()) {
                templateFileList.add(f);
            }
        }
    }

    public File[] getTemplates() {
        List<File> f = new ArrayList<File>();
        try {
            f.add(new File(Resolver.class.getResource("entity.template").toURI()));
            f.add(new File(Resolver.class.getResource("dao.template").toURI()));
        } catch (URISyntaxException e) {
        }
        return f.toArray(new File[]{});
    }

    public File getOutputDirectory() {
        File outputDirectory = new File(this.outputBaseDir);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
        return outputDirectory;
    }

    public String getFileName(File template, String name) {
        String templateName = template.getName();
        String fileName = templateName + name + ".java";
        return fileName;
    }

    public Map getContext() {
        return this.context;
    }

    private String complieTemplateDir(String string) throws TemplateException {
        Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put("resolverdir",
                this.getClass().getResource("Resolver.class").getPath());
        Template template = new Template(string, context);
        return template.render();
    }
}
