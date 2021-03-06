/**
 * cfg-engine - A Java dependency injection engine
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.cfgengine.core.configuration.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PackageConfigurationLoader implements ConfigurationLoader {
    private String targetToLoad;
    private ClassLoader classLoader; 

    public PackageConfigurationLoader(String targetToLoad) {
	this.targetToLoad = targetToLoad;
	this.classLoader = getClass().getClassLoader();
    }
    
    public PackageConfigurationLoader(String targetToLoad, ClassLoader cl) {
	this.targetToLoad = targetToLoad;
	this.classLoader = cl;
    }

    public InputStream load() {
	InputStream in = classLoader.getResourceAsStream(targetToLoad);
	if(in == null){
	    File f = new File(targetToLoad);
	    if(f.exists()){
		try {
		    return new FileInputStream(f);
		} catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	return in;
    }

    public String getTargetToLoad() {
	return targetToLoad;
    }

}
