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
package com.bluewolfbr.ironbone.utils;

import java.util.Map;
import java.util.HashMap;
import no.tornado.template.Template;
import no.tornado.template.TemplateException;

public class PropertiesParser {
    private static final Map applicationContext = new HashMap();
    {
        
    }
    
    public static String parser(String propertie) throws TemplateException {
        return parser(propertie, applicationContext);
    }
    @Deprecated
    public static String parser(String propertie, Map context) throws TemplateException {
        Template template = new Template(propertie, PropertiesParser.applicationContext);
        return template.render();
    }

    public static void  addContextProperties(String key, String value) {
        System.out.println(String.format("included propertie %s : %s", key, value));
        PropertiesParser.applicationContext.put(key,value);
    }
}
