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

import com.bluewolfbr.ironbone.utils.IVisitable;
import java.io.File;
import java.util.Map;


public interface IResolver<T extends IResolver> extends IVisitable {
    public T build(IronBoneConfiguration.ResolverConfig resolverConfig);
    public File[] getTemplates();
    public File getOutputDirectory();
    public String getFileName(File template, String name);
    public Map getContext();
    
    public int generate();
}
