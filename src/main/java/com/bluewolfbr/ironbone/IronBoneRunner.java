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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

public class IronBoneRunner {

    public static void main(String[] args) throws Exception {
        String yamlConfigFile = "";
        String tableName = "";
        if (args.length == 2) {
            yamlConfigFile = args[0];
            tableName = args[1];
        } else {
            throw new RuntimeException("numero de parametros incorreto");
        }
        File yamlFile = null;

        if (yamlConfigFile != null && !yamlConfigFile.isEmpty()) {
            yamlFile = new File(new URI(yamlConfigFile));
            if (!yamlFile.exists()) {
                throw new RuntimeException("arquivo de configuração faltando");
            }
        } else {
            //TODO load default config file ?
            throw new UnsupportedOperationException("não existe suporte para defaul");
        }

        DatabaseConfig dbconfig;
        try {
            dbconfig = new DatabaseConfig(yamlFile);
            Resolver templateResolver = getResolver(yamlFile);
            IronBoneRender render = new IronBoneRender(templateResolver);
            IronBoneApplication app = new IronBoneApplication(dbconfig.getConnection(), render);
            app.run(tableName);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * busca a classe responsaber por resolver template
     *
     * @param yamlFile
     * @return
     */
    private static Resolver getResolver(File yamlFile) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Yaml yaml = new Yaml();
        InputStream read = new FileInputStream(yamlFile);
        Map data = (Map) yaml.load(read);
        Map resolverProperties = (Map) ((Map) data.get("config")).get("resolver");
        String className = (String) resolverProperties.get("class");
        Resolver resolver = null;
        resolver = (Resolver) Class.forName(className).newInstance();
        resolver.build(resolverProperties);

        return resolver;
    }
}
