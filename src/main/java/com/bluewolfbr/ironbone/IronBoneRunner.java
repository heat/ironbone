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

import com.bluewolfbr.ironbone.utils.ContextVisitor;
import com.bluewolfbr.ironbone.utils.IVisitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class IronBoneRunner {

    IVisitor contextVisitor = new ContextVisitor();

    public void run(IronBoneConfiguration configuration, String tableName) throws Exception {
        //start visiting de IBApplication
        IronBoneApplication.getEmptyInstance().accept(contextVisitor);
        //Recover the resolver class and visit it
        IResolver resolverDriver = loadResolverDriver(configuration.config.resolver.driver);
        resolverDriver.accept(contextVisitor);

        //after all visits parse the configuration
        configuration.parser();
        //after that initialize it
        resolverDriver.build(configuration.config.resolver);


        DatabaseConfig dbconfig = new DatabaseConfig(configuration);


        IronBoneRender render = new IronBoneRender(resolverDriver);

        IronBoneApplication app = new IronBoneApplication(dbconfig, render);

        app.run(tableName);
    }

    public static void main(String[] args) throws Exception {
        String yamlConfigFile = "";
        String tableName = "";
        if (args.length == 2) {
            yamlConfigFile = args[0];
            tableName = args[1];
        } else {
            throw new RuntimeException("numero de parametros incorreto");
        }
        File yamlFile = new File(yamlConfigFile);
        if (!yamlFile.exists()) {
            throw new RuntimeException("arquivo de configuração faltando");
        }
        InputStream is = new FileInputStream(yamlFile);
        Yaml yaml = new Yaml();
        IronBoneConfiguration configuration = yaml.loadAs(is, IronBoneConfiguration.class);
        IronBoneRunner runner = new IronBoneRunner();
        runner.run(configuration, tableName);
    }

    private IResolver loadResolverDriver(String driver) {
        IResolver obj = null;
        try {
            obj = (IResolver) Class.forName(driver).newInstance();
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        }
        return obj;
    }
}
