/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.cfgengine.util;

import java.util.List;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.configuration.loader.ConfigurationLoader;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;

public class GenericCfgLoader {
    private static GenericCfgLoader instance;
    
    protected GenericCfgLoader(){
	
    }
    
    public static GenericCfgLoader getInstance(){
	if(instance == null){
	    instance = new GenericCfgLoader();
	}
	return instance;
    }
    
    public ConfigurationManager load(ConfigurationLoader handlersLoader, ConfigurationLoader entryPointLoader) throws ConfigurationException{
	ConfigurationManager cfgManager = getCfgManager(handlersLoader);
	cfgManager.load(entryPointLoader);
	return cfgManager;
    }
    
    public ConfigurationManager load(ConfigurationLoader handlersLoader, ConfigurationLoader entryPointLoader,ClassLoader cl) throws ConfigurationException{
	ConfigurationManager cfgManager = new ConfigurationManager(handlersLoader,cl);
	cfgManager.load(entryPointLoader);
	return cfgManager;
    }
    
    public ConfigurationManager load(ConfigurationLoader entryPointLoader, ClassLoader cl) throws ConfigurationException {
	ConfigurationManager cfgManager = new ConfigurationManager(cl);
	cfgManager.load(entryPointLoader);
	return cfgManager;
	
    }
    
    public ConfigurationManager load(ConfigurationLoader handlersLoader, List<ConfigurationLoader> cfgLoaders) throws ConfigurationException{
	ConfigurationManager cfgManager = getCfgManager(handlersLoader);
	cfgManager.load(cfgLoaders);
	return cfgManager;
    }
    
    public ConfigurationManager load(ConfigurationLoader entryPoint) throws ConfigurationException{
	ConfigurationManager cfgManager = getCfgManager();
	cfgManager.load(entryPoint);
	return cfgManager;
    }
    
    public ConfigurationManager getCfgManager(){
	return new ConfigurationManager();
    }
    
    public ConfigurationManager getCfgManager(ConfigurationLoader handlersLoader) throws ConfigurationException{
	return new ConfigurationManager(handlersLoader);
    }
    
//    public ConfigurationManager getCfgManager(ConfigurationLoader handlersLoader,ClassLoader cl) throws ConfigurationException{
//	return new ConfigurationManager(handlersLoader,cl);
//    }
}
