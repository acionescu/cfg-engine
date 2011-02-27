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
/**
 * $Id: FileConfigurationHandler.java,v 1.2 2007/06/18 07:47:06 victor Exp $
 */
package ro.zg.cfgengine.core.configuration.handlers;

import org.w3c.dom.Node;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.configuration.ManageableObject;
import ro.zg.cfgengine.core.configuration.loader.ConfigurationLoader;
import ro.zg.cfgengine.core.configuration.loader.PackageConfigurationLoader;

/**
 * Loads configuration from the specified file
 * 
 * @author aionescu
 * @version $Revision: 1.2 $
 */
public class FileConfigurationHandler extends BaseConfigurationHandler{
	private ConfigurationManager cfgManager;
	public FileConfigurationHandler(ConfigurationManager manager){
		cfgManager = manager;
	}

	
	/**
	 * The tag should look like : <file>url to the file</file>
	 */
	public ManageableObject configure(Node node) throws Exception{
		String nodeValue = node.getFirstChild().getNodeValue();
		if (nodeValue != null && !"".equals(nodeValue.trim())) {
//			ConfigurationLoader loader = new PackageConfigurationLoader(nodeValue);
//			cfgManager.addLoader(loader);
		    cfgManager.addResourceToLoad(nodeValue);
		}
		return null;
	}

}
