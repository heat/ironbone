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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaResolver implements IResolver {

    private Map context;
    private String outputBaseDir = new File("").getAbsolutePath();
    private String templateDir = "";
    private String packageBase = "";
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
        this.packageBase = resolverConfig.sourcepackage;
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
    public File getFileName(File template, String name) {

        String relativeTemplateDir = template.getParentFile().getAbsolutePath().replace(
                new File(this.templateDir).getAbsolutePath(), "");
        //remove o separador do final
        if (relativeTemplateDir.startsWith(File.separator)) {
            relativeTemplateDir = relativeTemplateDir.substring(1);
        }

        File outputPath = new File(this.outputBaseDir);
        outputPath = new File(outputPath,
                this.packageBase.replace(".", File.separator));
        outputPath = new File(outputPath, relativeTemplateDir);
        
        String filename = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase() + ".java";

        outputPath.mkdirs();       

        return new File(outputPath, filename);
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

    @Override
    public Map getContext(File template) {
        File templateDir = new File(this.templateDir).getAbsoluteFile();
        File templateParent = template.getAbsoluteFile().getParentFile();
        
        String realPakcage = templateParent.getAbsolutePath().replace(templateDir.getAbsolutePath(), "").replace(File.separator, ".");
        Map ctx = new HashMap(this.context);
        ctx.put("sourcepackage", this.packageBase + realPakcage);
        
        return ctx;
    }
}
