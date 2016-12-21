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
package net.segoia.cfgengine.util;

import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.core.configuration.loader.UrlConfigurationLoader;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;

public class UrlCfgLoader extends GenericCfgLoader{
    private static UrlCfgLoader instance;
    
    private UrlCfgLoader(){
	
    }
    
    public static UrlCfgLoader getInstance(){
	if(instance == null){
	    instance = new UrlCfgLoader();
	}
	return instance;
    }
    
    
    public ConfigurationManager load(String repositoryDir, String entryPointFile) throws ConfigurationException{
	UrlConfigurationLoader ul = new UrlConfigurationLoader(new String[]{repositoryDir},entryPointFile);
	ConfigurationManager cm = new ConfigurationManager(ul.getClassLoader());
	cm.load(ul);	
	return cm;
    }
    
    public ConfigurationManager load(String repositoryDir, String entryPointFile, ClassLoader parentClassLoader) throws ConfigurationException{
	UrlConfigurationLoader ul = new UrlConfigurationLoader(new String[]{repositoryDir},entryPointFile,parentClassLoader);
	ConfigurationManager cm = new ConfigurationManager(ul.getClassLoader());
	cm.load(ul);	
	return cm;
    }

}
