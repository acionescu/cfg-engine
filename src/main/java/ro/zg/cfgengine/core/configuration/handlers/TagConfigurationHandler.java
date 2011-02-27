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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import ro.zg.cfgengine.core.configuration.Attribute;
import ro.zg.cfgengine.core.configuration.ManageableObject;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;


public class TagConfigurationHandler extends CustomConfigurationHandler{
	
	public TagConfigurationHandler(){
		
	}
	
	public ManageableObject configure(Node node) throws Exception{
		NamedNodeMap attributes = node.getAttributes();
		if(getDefaultClassName() == null && attributes.getNamedItem(Attribute.CLASS) == null){
				Node parentTagAttr = attributes.getNamedItem(Attribute.TAG_REF);
				if(parentTagAttr == null){
					throw new ConfigurationException("The class or tag-ref attribute should be specified for node "+node.getNodeName());
				}
				String refferenceHandlerName = parentTagAttr.getNodeValue();
				ConfigurationHandler cfgHandler = getUpperConfigurationHandler(refferenceHandlerName);
				if(cfgHandler == null){
					throw new ConfigurationException("No handler registered for tag name "+parentTagAttr.getNodeValue());
				}
				ManageableObject mo = new ManageableObject(cfgHandler);
				
				return configure(mo,node);
		}
		else{
			return super.configure(node);
		}
	}
	
	public ManageableObject configure(ManageableObject manageableObject,Node node) throws Exception{
		Object object = super.configure(manageableObject, node).getTarget();
		NamedNodeMap attributes = node.getAttributes();
		if(attributes != null){
			Node tagAttr = attributes.getNamedItem(Attribute.TAG_NAME);
			if(tagAttr != null){
				String tagName = tagAttr.getNodeValue();
				if(object instanceof ConfigurationHandler){
					ConfigurationHandler handler = (ConfigurationHandler)object;
//					handler.setParentConfigurationHandler(getParentConfigurationHandler());
					ConfigurationHandler parentCfgHandler = getParentConfigurationHandler();
					if(parentCfgHandler == null){
						throw new ConfigurationException("No parent configuration handler found to register" +
								" handler for tag '"+tagName+"'.");
					}
					else{
						parentCfgHandler.registerLocalConfigurationHandler(tagName, handler);
					}
				}
				else{
					throw new ConfigurationException("The class '"+object.getClass().getName()+"' is not a subclass of ConfigurationHandler");
				}
			}
		}
		return new ManageableObject(object);
	}
	
}
