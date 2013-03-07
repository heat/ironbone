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
package com.bluewolfbr.ironbone.interfaces;

import com.bluewolfbr.ironbone.IronBoneConfiguration;
import com.bluewolfbr.ironbone.utils.IVisitable;
import java.io.File;
import java.util.Map;


public interface IResolver<T extends IResolver> extends IVisitable {
    /**
     * build Resolver with new parameters
     * @param resolverConfig
     * @return 
     */
    public T build(IronBoneConfiguration.ResolverConfig resolverConfig);
    /**
     * get template files
     * @return 
     */
    public File[] getTemplates();
    /**
     * get outputDirectory
     * @return 
     */
    public File getOutputDirectory();
    /**
     * return the file render
     * @param template
     * @param name
     * @return 
     */
    public File getFileName(File template, String name);
    /**
     * get the default context of Resolver
     * @return 
     */
    public Map getContext();
    /**
     * add process based in current template
     * @param template
     * @return 
     */
    public Map getContext(File template);
    
    /**
     * generate a default layout of files.
     * @return 
     */
    public int generate();
}
