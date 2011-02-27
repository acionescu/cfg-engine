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
package ro.zg.cfgengine.core.configuration.handlers;

import org.w3c.dom.Node;

import ro.zg.cfgengine.core.configuration.ManageableObject;


public class ConfigurationHandlerWrapper extends CustomConfigurationHandler{
	private String tagName;
	private ConfigurationHandler innerHandler;
	
	public ManageableObject configure(Node node) throws Exception{
		return innerHandler.configure(node);
	}

	public ConfigurationHandler getInnerHandler() {
		return innerHandler;
	}

	public void setInnerHandler(ConfigurationHandler innerHandler) {
		this.innerHandler = innerHandler;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	
	
	
}
