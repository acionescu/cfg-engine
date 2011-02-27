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
import org.w3c.dom.NodeList;

import ro.zg.cfgengine.core.configuration.ManageableObject;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;

public class TypeConfigurationHandler extends AbstractConfigurationHandler {
    private String typeAttributeName;
    private boolean parseNested;
    private ConfigurationHandler fallBackHandler;

    public ManageableObject configure(Node node) throws Exception {
	if (typeAttributeName == null) {
	    throw new ConfigurationException("typeAttributeName must be specified");
	}
	ManageableObject manageableObject = obtainObjectFromNode(node);
	if (manageableObject == null && fallBackHandler != null) {
	    manageableObject = fallBackHandler.configure(node);
	}
	if(manageableObject == null){
	    manageableObject = scanChildNodesWithParentHandler(node.getChildNodes(),(BaseConfigurationHandler)this.getParentConfigurationHandler());
	}
	return manageableObject;
    }

    private ManageableObject obtainObjectFromNode(Node node) throws Exception {
	NamedNodeMap attributes = node.getAttributes();
	if (attributes != null) {
	    Node typeAttribute = attributes.getNamedItem(typeAttributeName);
	    if (typeAttribute != null) {
		String type = typeAttribute.getNodeValue();
		ConfigurationHandler registeredHandler = getLocalConfigurationHandlerForName(type);
		if (registeredHandler != null) {
		    return registeredHandler.configure(node);
		}
	    }
	}
	if(parseNested){
	    return scanChildNodes(node); 
	}
	return null;
    }

    private ManageableObject scanChildNodes(Node node) throws Exception {
	NodeList childNodes = node.getChildNodes();
	for (int i = 0; i < childNodes.getLength(); i++) {
	    ManageableObject childObj = obtainObjectFromNode(childNodes.item(i));
	    if (childObj != null) {
		return childObj;
	    }
	}
	return null;
    }
    
    private ManageableObject scanChildNodesWithParentHandler(NodeList nodes, BaseConfigurationHandler parentHandler) throws Exception{
	for (int i = 0; i < nodes.getLength(); i++) {
	    ManageableObject childObj = configureWithParentHandler(nodes.item(i),parentHandler);
	    if (childObj != null) {
		return childObj;
	    }
	}
	return null;
    }
    
    private ManageableObject configureWithParentHandler(Node node, BaseConfigurationHandler parentHandler) throws Exception{
	ManageableObject obj = parentHandler.configureNestedNode(node);
	if(obj == null){
	    obj = scanChildNodesWithParentHandler(node.getChildNodes(), parentHandler);
	}
	return obj;
    }

    public String getTypeAttributeName() {
	return typeAttributeName;
    }

    public void setTypeAttributeName(String typeAttributeName) {
	this.typeAttributeName = typeAttributeName;
    }

    public ConfigurationHandler getFallBackHandler() {
	return fallBackHandler;
    }

    public void setFallBackHandler(ConfigurationHandler fallBackHandler) {
	this.fallBackHandler = fallBackHandler;
    }

    public boolean isParseNested() {
        return parseNested;
    }

    public void setParseNested(boolean parseNested) {
        this.parseNested = parseNested;
    }

}
