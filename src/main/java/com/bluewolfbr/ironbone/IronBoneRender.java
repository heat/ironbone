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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.tornado.template.Template;
import no.tornado.template.TemplateException;

public class IronBoneRender {

    private Resolver templateResolver;

    public IronBoneRender(Resolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    /**
     * A shortcut when you want call it with another resolver.
     * @param data
     * @param resovlerTemplate
     * @return
     * @throws Exception 
     */
    public int render(Table data, Resolver resovlerTemplate) throws Exception {
        IronBoneRender render = new IronBoneRender(resovlerTemplate);
        return render.render(data);
    }
    
    /**
     * This method start the render process
     * @param data
     * @return
     * @throws Exception 
     */
    public int render(Table data) throws Exception {

        File[] templates = this.templateResolver.getTemplates();
        List<File> outputFileList = new ArrayList<File>();
        try {
            for (File template : templates) {
                outputFileList.add(
                        render(template, data));
            }
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }

    /**
     * Render individual templates with data
     * @param template a @code{File} template
     * @param data the table
     * @return return a @code{File} that contain render result 
     * @throws IOException
     * @throws TemplateException 
     */
    private File render(File template, Table data) throws IOException, TemplateException {
        Map ctx = new HashMap();
        ctx.put("table", data);
        //merge additional context information
        ctx.putAll(this.templateResolver.getContext());
        File outputdir = this.templateResolver.getOutputDirectory();
        String filename = this.templateResolver.getFileName(template, data.name);
        Template simpleTemplate = new Template(template, ctx);
        
        File outputFile = new File(outputdir, filename);
        System.out.println("write in" + outputFile.toString());
        Writer writer = new FileWriter(outputFile);
        writer.write(simpleTemplate.render());
        writer.flush();
        writer.close();
        return outputFile;
    }
}
