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

import com.bluewolfbr.ironbone.interfaces.IResolver;
import com.bluewolfbr.ironbone.model.TableImpl;

/**
 *
 * @author OnezinoGabriel
 */
public interface IRender {

    /**
     * A shortcut when you want call it with another resolver.
     * @param data
     * @param resovlerTemplate
     * @return
     * @throws Exception
     */
    int render(TableImpl data, IResolver resovlerTemplate) throws Exception;

    /**
     * This method start the render process
     * @param data
     * @return
     * @throws Exception
     */
    int render(TableImpl data) throws Exception;
    
}
