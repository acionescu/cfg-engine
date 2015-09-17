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

import java.util.ArrayList;
import java.util.List;

import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.core.configuration.loader.ConfigurationLoader;
import net.segoia.cfgengine.core.configuration.loader.PackageConfigurationLoader;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;

public class PackageCfgLoader extends GenericCfgLoader{
    private static PackageCfgLoader instance;
    
    private PackageCfgLoader(){
	
    }

    public static PackageCfgLoader getInstance(){
	if(instance == null){
	    instance = new PackageCfgLoader();
	}
	return instance;
    }
    
    public ConfigurationManager load(String entryPoint) throws ConfigurationException{
	return super.load(new PackageConfigurationLoader(entryPoint));
    }
    
    public ConfigurationManager load(String entryPoint, ClassLoader classLoader) throws ConfigurationException {
	return super.load(new PackageConfigurationLoader(entryPoint,classLoader),classLoader);
    }
    
    public ConfigurationManager load(String handlers, String entryPoint) throws ConfigurationException{
	ConfigurationLoader configLoader = new PackageConfigurationLoader(entryPoint);
	ConfigurationLoader handlersLoader = new PackageConfigurationLoader(handlers);
	
	return super.getInstance().load(handlersLoader, configLoader);
    }
    
    public ConfigurationManager load(String handlers, String entryPoint, ClassLoader cl) throws ConfigurationException{
	ConfigurationLoader configLoader = new PackageConfigurationLoader(entryPoint,cl);
	ConfigurationLoader handlersLoader = new PackageConfigurationLoader(handlers,cl);
	
	return super.getInstance().load(handlersLoader, configLoader,cl);
    }
    
    public ConfigurationManager load(String handlers, List<String> entryPoints) throws ConfigurationException{
	ConfigurationLoader handlersLoader = new PackageConfigurationLoader(handlers);
	return super.getInstance().load(handlersLoader, getLoaders(entryPoints.toArray(new String[0])));
    }
    
    public ConfigurationManager load(String handlers, String[] entryPoints) throws ConfigurationException{
	ConfigurationLoader handlersLoader = new PackageConfigurationLoader(handlers);
	return super.getInstance().load(handlersLoader, getLoaders(entryPoints));
    }
    
    protected List<ConfigurationLoader> getLoaders(String[] files){
	List<ConfigurationLoader> list = new ArrayList<ConfigurationLoader>(files.length);
	for(String file : files){
	    list.add(new PackageConfigurationLoader(file));
	}
	return list;
    }
    
}
