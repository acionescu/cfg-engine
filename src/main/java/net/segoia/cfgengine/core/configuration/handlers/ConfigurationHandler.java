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
/**
 * $Id: ConfigurationHandler.java,v 1.2 2007/09/06 11:03:23 victor Exp $
 */
package net.segoia.cfgengine.core.configuration.handlers;

import java.util.Map;

import net.segoia.cfgengine.core.configuration.ConfigurationBuilder;
import net.segoia.cfgengine.core.configuration.ManageableObject;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;

import org.w3c.dom.Node;


/**
 * Defines a ConfigurationHandler contract This interface should be implemented by all the objects that want to register  as a tag configuration handler in   {@link ConfigurationBuilder}
 * @author   aionescu
 * @version   $Revision: 1.2 $
 */
public interface ConfigurationHandler {
	/**
	 * Generates a java object from an xml node.
	 * 
	 * @param node - xml node
	 * @return an object instance
	 * @throws Exception
	 */
	 public ManageableObject configure(Node node) throws Exception;
	 /**
	 * Sets the   {@link ConfigurationBuilder}   instance
	 * @param  configurationBuilder
	 * @uml.property  name="parentConfigurationHandler"
	 */
	 public void setParentConfigurationHandler(ConfigurationHandler parentCfgHandler);
	 
	 public ConfigurationHandler getParentConfigurationHandler();
	 /**
		 * @return   the localConfigurationHandlers
		 * @uml.property  name="localConfigurationHandlers"
		 */
	 public Map<String, ConfigurationHandler> getLocalConfigurationHandlers();
	 /**
		 * @param localConfigurationHandlers   the localConfigurationHandlers to set
		 * @uml.property  name="localConfigurationHandlers"
		 */
	 public void setLocalConfigurationHandlers(Map<String, ConfigurationHandler> localConfigurationHandlers);
	 
	 public void registerLocalConfigurationHandler(String name, ConfigurationHandler cfgHandler) throws ConfigurationException;
	 
	 public void removeLocalConfigurationHandler(String name);
	 
	 public ConfigurationHandler getLocalConfigurationHandlerForName(String name);
	 
	 /**
	  * Indicates if the handler has the Attribute ID.
	  * 
	 * @return <code>true</code> if it has, otherwise <code>false</code>.
	 */
	public boolean hasIdAttribute();
	
}
