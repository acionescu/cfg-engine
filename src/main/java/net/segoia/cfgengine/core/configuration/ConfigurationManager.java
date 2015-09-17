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
package net.segoia.cfgengine.core.configuration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.segoia.cfgengine.core.configuration.handlers.BaseConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.ConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.CustomConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.DirConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.FileConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.ListConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.MapConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.PropertyConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.TypedValueConfigurationHandler;
import net.segoia.cfgengine.core.configuration.handlers.ValueConfigurationHandler;
import net.segoia.cfgengine.core.configuration.loader.ConfigurationLoader;
import net.segoia.cfgengine.core.configuration.loader.PackageConfigurationLoader;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.log.Log;

public class ConfigurationManager {
    private ObjectRepository repository = new ObjectRepository();
    private Queue<ConfigurationLoader> loadQueue = new ConcurrentLinkedQueue<ConfigurationLoader>();
    private XmlConfigurationBuilder handlerBuilder;
    private XmlConfigurationBuilder objectsBuilder;
    private String customHandlersFilePath = "cfg-handlers.xml";
    private ClassLoader resourcesLoader = ClassLoader.getSystemClassLoader();

    public ConfigurationManager() {
	initDefaultBuilders();
    }
    
    private void initDefaultBuilders() {
	ConfigurationHandler objectsRootHandler;
	try {
	    objectsRootHandler = createBasicConfigurationHandler();
	    objectsBuilder = new XmlConfigurationBuilder(objectsRootHandler,resourcesLoader);
	} catch (ConfigurationException e) {
	    Log.error(this, "Error creating ConfigurationManager", e);
	}
    }
    
    public ConfigurationManager(ClassLoader cl){
	resourcesLoader = cl;
	initDefaultBuilders();
    }

    public ConfigurationManager(ConfigurationLoader customHandlersLoader) throws ConfigurationException {
	initBuilders(customHandlersLoader);
    }
    
    public ConfigurationManager(ConfigurationLoader customHandlersLoader, ClassLoader cl) throws ConfigurationException {
	this.resourcesLoader = cl;
	initBuilders(customHandlersLoader);
    }
    
    private void initBuilders(ConfigurationLoader customHandlersLoader){
	// now create the builder which will load the custom configuration handlers
	try {
	    handlerBuilder = new XmlConfigurationBuilder(createCustomHandlerLoader(),resourcesLoader);
	    // load the custom handlers
	    List<ManageableObject> customHandlers = handlerBuilder.configure(customHandlersLoader.load());
	    ObjectRepository handlersRepository = new ObjectRepository();
	    handlersRepository.addListOfObjects(customHandlers);
	    resolveDependencies(handlersRepository);

	    ConfigurationHandler objectsRootHandler = createBasicConfigurationHandler();

	    ConfigurationHandler defHandler = (ConfigurationHandler) handlersRepository.getObject("rootHandler")
		    .getTarget();
	    //			
	    for (ManageableObject cc : customHandlers) {
		ConfigurationHandler cfgh = (ConfigurationHandler) cc.getTarget();
		cfgh.setParentConfigurationHandler(objectsRootHandler);
	    }

	    objectsRootHandler.getLocalConfigurationHandlers().putAll(defHandler.getLocalConfigurationHandlers());

	    objectsBuilder = new XmlConfigurationBuilder(objectsRootHandler,resourcesLoader);
	    // List<ManageableObject> loadedHandlers = handlersRepository.getOjbectsByTagName("cfg-handler");
	    // if(loadedHandlers != null){
	    // for(ManageableObject handler : loadedHandlers){
	    //					
	    // // objectsRootHandler.registerLocalConfigurationHandler(name, cfgHandler)
	    // }
	    // }

	} catch (ConfigurationException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void load() throws ConfigurationException {
	ConfigurationLoader loader = null;
	// ConfigurationBuilder xmlBuilder = new XmlConfigurationBuilder(this);
	while ((loader = loadQueue.poll()) != null) {
	    try {
		InputStream in = loader.load();
		List<ManageableObject> loadedObjects = objectsBuilder.configure(in);
		repository.addListOfObjects(loadedObjects);
	    } catch (Exception e) {
		throw new ConfigurationException("Error loading " + loader.getTargetToLoad(), e);
	    }
	}
	// now try to resolve the dependencies
	resolveDependencies(repository);

	try {
	    initializeObjects(repository.getAllObjects());
	} catch (Exception e) {
	    throw new ConfigurationException("Error initializing objects.", e);
	}
    }

    private void initializeObjects(List<ManageableObject> loadedObjects) throws Exception {
	for (ManageableObject o : loadedObjects) {
	    o.setResourcesLoader(resourcesLoader);
	    o.callInitMethod();
	}
    }
    
    

    /**
     * Destroys the objects by calling the method specified as destroy method This should be used for cleanup when the
     * configuration is not needed anymore
     * 
     * @throws Exception
     */
    public void destroy() throws Exception {
	List<ManageableObject> allObjects = repository.getAllObjects();
	for (ManageableObject mo : allObjects) {
	    mo.callDestroyMethod();
	}
	repository.purge();
    }

    public void load(List<ConfigurationLoader> loaders) throws ConfigurationException {
	loadQueue.addAll(loaders);
	load();
    }

    public void load(ConfigurationLoader loader) throws ConfigurationException {
	loadQueue.add(loader);
	load();
    }

    private ConfigurationHandler createBasicConfigurationHandler() throws ConfigurationException {
	// register xml configuration handlers
	ConfigurationHandler newCfgHandler = new BaseConfigurationHandler();

	newCfgHandler.registerLocalConfigurationHandler("property", new PropertyConfigurationHandler());
	newCfgHandler.registerLocalConfigurationHandler("map", new MapConfigurationHandler());
	newCfgHandler.registerLocalConfigurationHandler("value", new ValueConfigurationHandler());
	newCfgHandler.registerLocalConfigurationHandler("list", new ListConfigurationHandler());
	newCfgHandler.registerLocalConfigurationHandler("file", new FileConfigurationHandler(this));
	newCfgHandler.registerLocalConfigurationHandler("dir", new DirConfigurationHandler(this));
	newCfgHandler.registerLocalConfigurationHandler("resource", new BaseConfigurationHandler());
	newCfgHandler.registerLocalConfigurationHandler("key", new ValueConfigurationHandler());
	newCfgHandler.registerLocalConfigurationHandler("value-type", new TypedValueConfigurationHandler());
	return newCfgHandler;
    }

    public void addResourceToLoad(String resourceName){
	addLoader(new PackageConfigurationLoader(resourceName,resourcesLoader));
    }
    
    private ConfigurationHandler createCustomHandlerLoader() throws ConfigurationException {
	ConfigurationHandler newCfgHandler = createBasicConfigurationHandler();
	// create the handler which will create configuration handlers from xml definitions
	CustomConfigurationHandler customCfgHandler = new CustomConfigurationHandler();
	customCfgHandler.setParentConfigurationHandler(newCfgHandler);
	customCfgHandler.setHasIdAttribute(false);

	Map<String, String> attributesToProperties = new HashMap<String, String>();
	attributesToProperties.put("default-class", "defaultClassName");
	customCfgHandler.setAttributesToProperties(attributesToProperties);

	Map<String, String> tagsToProperties = new HashMap<String, String>();
	tagsToProperties.put("local-cfg-handler", "localConfigurationHandlers");
	// tagsToProperties.put("attribute-to-property", "attributesToProperties");
	// tagsToProperties.put("tag-to-property", "tagsToProperties");
	customCfgHandler.setTagsToProperties(tagsToProperties);

	Map<String, ConfigurationHandler> localHandlers = new HashMap<String, ConfigurationHandler>();
	localHandlers.put("local-cfg-handler", customCfgHandler);
	customCfgHandler.setLocalConfigurationHandlers(localHandlers);

	newCfgHandler.registerLocalConfigurationHandler("cfg-handler", customCfgHandler);

	return newCfgHandler;
    }

    public void resolveDependencies(ObjectRepository repository) throws ConfigurationException {
	List<ManageableObject> allObjects = repository.getAllObjects();
	for (ManageableObject mo : allObjects) {
	    String[] dependecies = mo.getDependencies().toArray(new String[0]);
	    for (String dependencyId : dependecies) {
		ManageableObject dependency = repository.getObject(dependencyId);
		if (dependency == null) {
		    throw new ConfigurationException("Error setting dependency with id '" + dependencyId
			    + " on object with id '" + mo.getId() + "'.");
		}
		mo.resolveDependency(dependencyId, dependency.getTarget());
	    }
	}
    }

    public void addLoader(ConfigurationLoader loader) {
	loadQueue.add(loader);
    }

    public Object getObjectById(String id) {
	ManageableObject mo = repository.getObject(id);
	if (mo != null) {
	    return mo.getTarget();
	}
	return null;
    }

    public Map getObjectsByTagName(String tagName) {
	List<ManageableObject> objList = repository.getObjectsByTagName(tagName);
	Map result = new HashMap<String, Object>();
	for (ManageableObject obj : objList) {
	    result.put(obj.getId(), obj.getTarget());
	}
	return result;
    }
    
    public String getObjectTagById(String id){
	return repository.getObjectTagById(id);
    }
    
    public Map<String,Object> getAllObjects(){
	List<ManageableObject> allObjects = repository.getAllObjects();
	Map<String,Object> objMap = new HashMap<String, Object>();
	for (ManageableObject mo : allObjects) {
	    objMap.put(mo.getId(), mo.getTarget());
	}
	return objMap;
    }
    
    public boolean containsObjectWithId(String id){
	return repository.containsObjectWithId(id);
    }

    /**
     * @return the resourcesLoader
     */
    public ClassLoader getResourcesLoader() {
        return resourcesLoader;
    }
    
}
