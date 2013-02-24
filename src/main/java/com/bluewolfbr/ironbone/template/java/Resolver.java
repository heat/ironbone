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

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Resolver {

    private Map context;
    public Resolver() {
        this.context = new HashMap();
        
        context.put("package", "com.bluewolfbr.out");
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
        File outputDirectory = new File("c:/tmp/output");
        outputDirectory.mkdirs();
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
}
