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

public class Formatter {

    public static String toLowerCamelCase(String text) {
        StringBuilder sb = new StringBuilder();
        String[] trunks = text.split("_");
        for (String trunk : trunks) {
            sb.append(String.valueOf(trunk.charAt(0)).toUpperCase());
            sb.append(trunk.substring(1).toLowerCase());
        }
        sb.replace(0, 1, text.substring(0, 1).toLowerCase());
        return sb.toString();
    }

    public static String toCamelCase(String text) {
        StringBuilder sb = new StringBuilder();
        String[] trunks = text.split("_");
        for (String trunk : trunks) {
            sb.append(String.valueOf(trunk.charAt(0)).toUpperCase());
            sb.append(trunk.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    public static String toUpperCase(String text) {
        return text.toUpperCase();
    }
}
