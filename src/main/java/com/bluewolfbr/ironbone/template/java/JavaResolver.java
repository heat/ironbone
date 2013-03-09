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
import com.bluewolfbr.ironbone.utils.PropertiesParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
import no.tornado.template.TemplateException;

public class JavaResolver implements IResolver {

    private Map context;
    private String outputBaseDir = new File("").getAbsolutePath();
    private String suffix = "";
    private String templateDir = "";
    private String packageBase = "";
    private String[][] templateFileList = new String[][]{};

    public JavaResolver() {
        this.context = new HashMap();
    }

    @Override
    public JavaResolver build(IronBoneConfiguration.ResolverConfig resolverConfig) {
        this.context.put("sourcepackage", resolverConfig.sourcepackage);
        this.context.put("outputdir", resolverConfig.outputdir);
        this.context.put("templatedir", resolverConfig.templatedir);
        this.context.put("suffix", resolverConfig.suffix);
        this.suffix = resolverConfig.suffix;
        this.templateDir = resolverConfig.templatedir;
        this.outputBaseDir = resolverConfig.outputdir;
        this.packageBase = resolverConfig.sourcepackage;
        populateTemplateList(resolverConfig.templates);
        return this;
    }

    private void populateTemplateList(String[][] templates) {
        this.templateFileList = Arrays.copyOf(templates, templates.length);
        
        File templateDirectory = new File(this.templateDir);
        
        for(int i = 0; i < this.templateFileList.length; i++) {
            String templateName = templateFileList[i][0];
             File f = new File(templateDirectory, templateName);
             
             if(f.exists()) {
                 templateFileList[i][0] = f.getAbsolutePath();
             } else {
                 templateFileList[i][0] = null;
                 templateFileList[i][1] = null;
             }
        }
    }

    @Override
    public File[] getTemplates() {
        List<File> templateFiles = new ArrayList<File>();
        for(String[] templateData : this.templateFileList) {
            if(templateData[0] != null) {
                templateFiles.add(new File(templateData[0]));
            }
        }
        return templateFiles.toArray(new File[]{});
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
        String filename = null;
        try{
            for(int i = 0; i < this.templateFileList.length; i++){
                String[] templateData = this.templateFileList[i];
                if(templateData == null) {
                    continue;
                }
                File originalTemplate = new File(templateData[0]);
                if(Files.isSameFile(template.toPath(), originalTemplate.toPath())){
                    filename = templateData[1] + this.context.get("suffix");
                }
            }

            PropertiesParser.addContextProperties("name", name);
            filename = PropertiesParser.parser(filename);
        }catch(TemplateException ex) {
            
        }catch(IOException ex){
            
        }
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
        File templateDirectory = new File(this.templateDir).getAbsoluteFile();
        File templateParent = template.getAbsoluteFile().getParentFile();
        
        String realPakcage = templateParent.getAbsolutePath().replace(templateDirectory.getAbsolutePath(), "").replace(File.separator, ".");
        Map ctx = new HashMap(this.context);
        ctx.put("sourcepackage", this.packageBase + realPakcage);
        
        return ctx;
    }
}
