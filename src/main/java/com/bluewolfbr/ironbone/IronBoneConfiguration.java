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

import com.bluewolfbr.ironbone.utils.PropertiesParser;
import no.tornado.template.TemplateException;

public class IronBoneConfiguration {

    public static class Database {
        public String url;
        public String database;
        public String driver;
        public String user;
        public String password;
        public String schema;
        public String catalog;
    }
    public static class ResolverConfig {
        public String driver;
        public String outputdir;
        public String sourcepackage;
        public String suffix;
        public String templatedir;
        public String[][] templates = new String[][]{};
    }
    public static class Configuration {
        public ResolverConfig resolver = new ResolverConfig();
    }
    
    public Database database = new Database();
    public Configuration config = new Configuration();

    void parser() throws TemplateException {
        //TODO to attempt use reflect to parser;
        this.config.resolver.outputdir =
                PropertiesParser.parser(this.config.resolver.outputdir);
        this.config.resolver.sourcepackage = 
                PropertiesParser.parser(this.config.resolver.sourcepackage);
        this.config.resolver.templatedir = 
                PropertiesParser.parser(this.config.resolver.templatedir);
    }
}
