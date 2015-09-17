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
package net.segoia.cfgengine.core.configuration.handlers;

import java.util.HashMap;
import java.util.Map;

import net.segoia.cfgengine.core.configuration.Dependency;
import net.segoia.cfgengine.core.configuration.ManageableObject;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;

import org.w3c.dom.Node;

/**
 * @author adi
 */
public abstract class AbstractConfigurationHandler implements ConfigurationHandler {
    /**
     * The parent configuration hadler If null then this is the root configuration handler
     */
    private ConfigurationHandler parentConfigurationHandler;

    /**
     * If a configuration handler is registered locally for a specific tag the this handler will be used to process the
     * tag, otherwise the processing will be delegated to the parent
     * 
     * @uml.property name="localConfigurationHandlers"
     * @uml.associationEnd 
     *                     qualifier="key:java.lang.Object com.cosmote.rtbus.core.configuration.handlers.ConfigurationHandler"
     */
    private Map<String, ConfigurationHandler> localConfigurationHandlers = new HashMap<String, ConfigurationHandler>();

    private boolean hasIdAttribute = true;
    
    /**
     * Holds the default values for certain properties
     */
    private Map<String, Object> defaultValuesForProperties = new HashMap<String, Object>();
    /**
     * Holds default references for certain properties 
     */
    private Map<String, String> defaultReferencesForProperties = new HashMap<String, String>();


    public ConfigurationHandler getLocalConfigurationHandlerForName(String name) {
	return localConfigurationHandlers.get(name);
    }

    /**
     * Configures a nested node using the first available handler for the node starting from the local handler and going
     * up the hierarchy
     * 
     * @param node
     * @return
     * @throws Exception
     *             - in case handler for the node could not be foud or an error happend during the node configuration
     */
    protected ManageableObject configureNestedNode(Node node) throws Exception {
	ConfigurationHandler currentCfgHandler = this;
	ManageableObject currentObj = null;

	do {
	    Map<String, ConfigurationHandler> cfgHandlers = currentCfgHandler.getLocalConfigurationHandlers();
	    ConfigurationHandler newConfigurationHandler = cfgHandlers.get(node.getNodeName());
	    if (newConfigurationHandler != null) {
		currentObj = newConfigurationHandler.configure(node);
	    } else {
		currentCfgHandler = currentCfgHandler.getParentConfigurationHandler();
	    }
	} while (currentObj == null && currentCfgHandler != null);

	if (currentObj == null) {
	    throw new ConfigurationException("Could not find a handler for node with name '" + node.getNodeName() + "'");
	}

	return currentObj;
    }

    public ConfigurationHandler getUpperConfigurationHandler(String name) {
	ConfigurationHandler parentCfgHandler = getParentConfigurationHandler();
	ConfigurationHandler nextUpperCfgHandler = null;
	while (parentCfgHandler != null && nextUpperCfgHandler == null) {
	    nextUpperCfgHandler = parentCfgHandler.getLocalConfigurationHandlerForName(name);
	    parentCfgHandler = parentCfgHandler.getParentConfigurationHandler();
	}

	return nextUpperCfgHandler;
    }

    /**
     * @param parentConfigurationHandler
     *            the parentConfigurationHandler to set
     * @uml.property name="parentConfigurationHandler"
     */
    public void setParentConfigurationHandler(ConfigurationHandler parentCfgHandler) {
	parentConfigurationHandler = parentCfgHandler;

    }

    /**
     * @return the parentConfigurationHandler
     * @uml.property name="parentConfigurationHandler"
     */
    public ConfigurationHandler getParentConfigurationHandler() {
	return parentConfigurationHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.core.configuration.handlers.ConfigurationHandler#getLocalConfigurationHandlers()
     */
    public Map<String, ConfigurationHandler> getLocalConfigurationHandlers() {
	return localConfigurationHandlers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cosmote.rtbus.core.configuration.handlers.ConfigurationHandler#setLocalConfigurationHandlers(java.util.Map)
     */
    public void setLocalConfigurationHandlers(Map<String, ConfigurationHandler> localConfigurationHandlers) {
	this.localConfigurationHandlers = localConfigurationHandlers;
    }

    public void registerLocalConfigurationHandler(String name, ConfigurationHandler cfgHandler)
	    throws ConfigurationException {
	// if(localConfigurationHandlers.containsKey(name)){
	// throw new ConfigurationException("A local configuration handler with name '"+name+"' already exists");
	// }

	localConfigurationHandlers.put(name, cfgHandler);

	cfgHandler.setParentConfigurationHandler(this);

    }

    public void removeLocalConfigurationHandler(String name) {
	localConfigurationHandlers.remove(name);
    }
    
    public void configureDefaultValuesForProps(ManageableObject manageableObject) throws ConfigurationException{
	/* set default values for props */
	for (String propName : defaultValuesForProperties.keySet()) {
	    Object value = defaultValuesForProperties.get(propName);
	    manageableObject.setValueForProperty(propName, value);
	}

	for (String propName : defaultReferencesForProperties.keySet()) {
	    String refid = defaultReferencesForProperties.get(propName);
	    manageableObject.addDependency(new Dependency(refid, propName));
	}
    }
    
    /**
     * @param hasIdAttribute
     *            the hasIdAttribute to set
     * @uml.property name="hasIdAttribute"
     */
    public void setHasIdAttribute(boolean hasIdAttribute) {
	this.hasIdAttribute = hasIdAttribute;
    }

    public boolean hasIdAttribute() {
	return hasIdAttribute;
    }

    public Map<String, Object> getDefaultValuesForProperties() {
        return defaultValuesForProperties;
    }

    public Map<String, String> getDefaultReferencesForProperties() {
        return defaultReferencesForProperties;
    }

    public void setDefaultValuesForProperties(Map<String, Object> defaultValuesForProperties) {
        this.defaultValuesForProperties = defaultValuesForProperties;
    }

    public void setDefaultReferencesForProperties(Map<String, String> defaultReferencesForProperties) {
        this.defaultReferencesForProperties = defaultReferencesForProperties;
    }
    
    
}
