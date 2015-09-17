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
import java.util.List;

import net.segoia.cfgengine.core.exceptions.ConfigurationException;

/**
 * Creates a base of objects created from xml definitions
 * 
 * @author aionescu
 * @version $Revision: 1.11 $
 *
 */
public interface ConfigurationBuilder {
//	
//	/**
//	 * Registers a configuration handler {@link ConfigurationHandler} for a certain tag name
//	 * You can only register a configuration handler only for the tags that have and id attribute 
//	 * in order for theese objects to have an unique identifier by which they will be retrieved later
//	 * @param tagName - name of the tag
//	 * @param handler - handler instance
//	 * @throws Exception
//	 */
//	void registerConfigurationHandler(String tagName, ConfigurationHandler handler) throws Exception;
//	/**
//	 * Returns the configuration handler {@link ConfigurationHandler} 
//	 * registered for the tag with the specified name
//	 * @param tagName - name of the tag
//	 * @return handler instance if any registered or null if no handler is registered for the tag
//	 */
//	ConfigurationHandler getConfigurationHandler(String tagName);
//	
//	/**
//	 * Gets an object from the objects base identified by the specified id
//	 * @param id - lookup id
//	 * @return The object added with that id or null if no object exists 
//	 */
//	Object getObjectById(String id);
//	/**
//	 * Returns a map representing the objects of the specified type
//	 * The map will hold as a key the object id and as a value the object itself
//	 * For example if an object is created from the tag <example-tag> quering this method
//	 * with the parameter "example-tag" will result in a map that contains the object created 
//	 * from this tag
//	 * @param type
//	 * @return
//	 */
//	Map getObjectsByType(String type);
//	/**
//	 * Loads a configuration file from the specified path
//	 * @param path - the path were the xml configuration file is located
//	 * @throws Exception
//	 */
//	void load(String path) throws ConfigurationException;
//	/**
//	 * Loads a configuration file identified by a File java object
//	 * @param file - the file object
//	 * @throws Exception
//	 */
//	void load(File file) throws ConfigurationException;
//	/**
//	 * Loads multiple configuration files passed as an array of urls
//	 * @param paths - array of urls
//	 * @throws Exception
//	 */
//	void load(String[] paths) throws ConfigurationException;
//	/**
//	 * Loads multiple configuration files passed as an array of Files
//	 * @param file
//	 * @throws ConfigurationException
//	 */
//	void load(File[] file) throws ConfigurationException;
//	/**
//	 * Loads a configuration from an InputStream
//	 * @param input
//	 * @throws ConfigurationException
//	 */
//	public void load(InputStream input) throws ConfigurationException;
//	/**
//	 * Tries to resolve a dependency.If the object with dependencyId exists it will be 
//	 * resolved instantly otherwise it will be resolved at a later stage or not at all 
//	 * if the required dependecy is not created by the end of configuration loading. 
//	 * @param dependencyId
//	 * @param propertyName
//	 * @param targeObject
//	 */
//	public void resolveDependency(String dependencyId, String propertyName, Object targeObject) throws ConfigurationException;
//	/**
//	 * Adds a listener {@link DependencyListener} for a certain dependency identified by its id
//	 * When the dependency will be resolved the listener will be automatically removed
//	 * @param dependencyId
//	 * @param listener
//	 */
//	public void addDependencyListener(String dependencyId, DependencyListener listener);
//	
//	/**
//	 * Adds a lifecycle listener for this configuration builder
//	 * @param listener
//	 */
//	public void addLifeCycleListener(LifeCycleListener listener);
//	
//	/**
//	 * Removes a lifecycle listener
//	 * @param listener
//	 */
//	public void removeLifeCycleListener(LifeCycleListener listener);
//	
//	/**
//	 * Destroys this configuration builder
//	 *
//	 */
//	public void destroy();
	
	public List<ManageableObject> configure(InputStream inputStream) throws ConfigurationException;
}
