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

import com.bluewolfbr.ironbone.IResolver;
import com.bluewolfbr.ironbone.IronBoneConfiguration;
import com.bluewolfbr.ironbone.utils.IVisitor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaResolver implements IResolver {

    private Map context;
    private String outputBaseDir = new File("").getAbsolutePath();
    private String templateDir = "";
    private List<File> templateFileList = new ArrayList<File>();

    public JavaResolver() {
        this.context = new HashMap();
        context.put("package", "");
        try {
            URL defaultClassFolder = this.getClass().getResource("JavaResolver.class");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public JavaResolver build(IronBoneConfiguration.ResolverConfig resolverConfig) {
        this.context.put("sourcepackage", resolverConfig.sourcepackage);
        this.context.put("outputdir", resolverConfig.outputdir);
        this.context.put("templatedir", resolverConfig.templatedir);
        this.templateDir = resolverConfig.templatedir;
        this.outputBaseDir = resolverConfig.outputdir;

        populateTemplateList(resolverConfig.templates);
        return this;
    }

    private void populateTemplateList(String[] templates) {
        templateFileList.clear();
        File templateDir = new File(this.templateDir);
        System.out.println("template directory lookup " + templateDir.getPath());
        for (String template : templates) {
            
            File f = new File(templateDir, template);
            System.out.println("seeking for template " + f);
            System.out.println("did you find - " + f.exists());
            if (f.exists()) {
                templateFileList.add(f);
            }
        }
        System.out.println("loaded templates : " + Arrays.toString(this.templateFileList.toArray()));
    }

    @Override
    public File[] getTemplates() {
        return this.templateFileList.toArray(new File[]{});
    }

    @Override
    public File getOutputDirectory() {
        File outputDirectory = new File(this.outputBaseDir);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
        return outputDirectory;
    }

    @Override
    public String getFileName(File template, String name) {
        String templateName = template.getName();
        String fileName = templateName + name + ".java";
        return fileName;
    }

    @Override
    public Map getContext() {
        return this.context;
    }

    @Override
    public void accept(IVisitor visitor) {
        final List<String> properties = new ArrayList<String>();
        try {
            properties.add(
                    "resolverdir:"
                    + this.getClass().getResource("JavaResolver.class").toURI().getPath());
        } catch (URISyntaxException ex) {
            Logger.getLogger(JavaResolver.class.getName()).log(Level.SEVERE, null, ex);
        }

        IVisitor.IContextData contextData = new IVisitor.IContextData() {
            @Override
            public Iterator<String> iterator() {
                return properties.iterator();
            }
        };

        visitor.visit(contextData);
    }

    /**
     * @see IResolver#generate()
     * @return
     */
    @Override
    public int generate() {
        File baseDir;

        baseDir = new File("Null").getAbsoluteFile().getParentFile();
        String[] files = new String[]{
            "config.yml",
            "templates/README"
        };
        try {
            for (String file : files) {
                this.copyFile(file, baseDir);
            }
        } catch (IOException ex) {
            return 1;
        }
        return 0;
    }

    private void copyFile(String in, final File outBase) throws IOException {
        InputStream is = null;
        try {
            is = this.getClass().getResourceAsStream(in);
            File outFile = new File(outBase, in);
            outFile.getAbsoluteFile().getParentFile().mkdirs();
            if (!outFile.exists()) {
                outFile.createNewFile();
            }

            Path path = outFile.getAbsoluteFile().toPath();
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
