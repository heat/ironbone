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

import java.util.Iterator;

public class ContextVisitor implements IVisitor{

    public ContextVisitor() {
    }
    
    
    @Override
    public void visit(IContextData contextData) {
        Iterator<String> iterator = contextData.iterator();
        while(iterator.hasNext())
        {
            String data = iterator.next();
            System.out.println("attempting add propertie " + data);
            String[] map = data.split(":", 2);
            System.out.println("splited in " + map);
            PropertiesParser.addContextProperties(map[0], map[1]);
        }
    }
    
}
