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

import java.util.List;
import java.util.Map;

import net.segoia.cfgengine.core.configuration.Dependency;
import net.segoia.cfgengine.core.configuration.DependencyRef;
import net.segoia.cfgengine.core.configuration.ManageableObject;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TypeDecoratorConfigurationHandler extends CustomConfigurationHandler {
    /**
     * Name of the attribute to be used as a type
     */
    private String typeAttributeName;

    /**
     * Mappings of type to properties Will be used to decorate the main object
     */
    private Map<String, String> typesToProperties;

    private ConfigurationHandler mainNodeHandler;

    private List<String> overRiddenProps;

    public ManageableObject configure(Node node) throws Exception {
	if (mainNodeHandler != null) {
	    return configure(mainNodeHandler.configure(node),node);
	}
	return super.configure(node);
    }

    protected ManageableObject configure(ManageableObject manageableObject, Node node) throws Exception {
	manageableObject = super.configure(manageableObject, node);
	if (typeAttributeName == null || typesToProperties == null) {
	    return manageableObject;
	}
	setProps(manageableObject, node);
	configureDefaultValuesForProps(manageableObject);
	return manageableObject;
    }

    private void setProps(ManageableObject mainObject, Node node) throws Exception {
	NodeList childNodes = node.getChildNodes();
	for (int i = 0; i < childNodes.getLength(); i++) {
	    Node currNode = childNodes.item(i);
	    String propName = getPropertyNameForType(currNode);
	    if (propName != null) {
		ManageableObject nestedObject = configureNestedNode(currNode);
		mainObject.addNestedObject(nestedObject);
		Object propValue = nestedObject.getTarget();
		if (propValue instanceof DependencyRef) {
		    DependencyRef depRef = (DependencyRef) propValue;
		    mainObject.addDependency(new Dependency(depRef.getDependencyRef(), propName));
		} else {
		    if (overRiddenProps != null && overRiddenProps.contains(propName)) {
			mainObject.overrideProperty(propName, propValue);
		    } else {
			mainObject.setValueForProperty(propName, propValue);
		    }
		}
	    }
	    else{
		setProps(mainObject, currNode);
	    }
	}
    }

    private String getPropertyNameForType(Node currentNode) {
	NamedNodeMap attributes = currentNode.getAttributes();
	if (attributes == null) {
	    return null;
	}
	Node typeAttr = attributes.getNamedItem(typeAttributeName);
	if (typeAttr == null) {
	    return null;
	}
	String typeName = typeAttr.getNodeValue();
	return typesToProperties.get(typeName);
    }

    public String getTypeAttributeName() {
	return typeAttributeName;
    }

    public Map<String, String> getTypesToProperties() {
	return typesToProperties;
    }

    public void setTypeAttributeName(String typeAttributeName) {
	this.typeAttributeName = typeAttributeName;
    }

    public void setTypesToProperties(Map<String, String> typesToProperties) {
	this.typesToProperties = typesToProperties;
    }

    public ConfigurationHandler getMainNodeHandler() {
	return mainNodeHandler;
    }

    public void setMainNodeHandler(ConfigurationHandler mainNodeHandler) {
	this.mainNodeHandler = mainNodeHandler;
    }

    public List<String> getOverRiddenProps() {
	return overRiddenProps;
    }

    public void setOverRiddenProps(List<String> overRiddenProps) {
	this.overRiddenProps = overRiddenProps;
    }

}
