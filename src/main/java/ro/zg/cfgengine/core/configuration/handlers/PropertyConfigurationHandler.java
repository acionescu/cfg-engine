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
 * $Id: PropertyConfigurationHandler.java,v 1.5 2007/10/18 10:51:53 aionescu Exp $
 */
package ro.zg.cfgengine.core.configuration.handlers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.zg.cfgengine.core.configuration.Attribute;
import ro.zg.cfgengine.core.configuration.ManageableObject;
import ro.zg.cfgengine.core.configuration.Property;
import ro.zg.cfgengine.core.configuration.Tag;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.util.logging.MasterLogManager;

/**
 * Creates a Property object form a <property> tag
 * 
 * @author aionescu
 * @version $Revision: 1.5 $
 * 
 */
public class PropertyConfigurationHandler extends BaseConfigurationHandler {

    public PropertyConfigurationHandler() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.core.util.BaseConfigurationHandler#configure(org.w3c.dom.Node)
     */
    public ManageableObject configure(Node node) throws Exception {
	// gets the property name
	String propName = node.getAttributes().getNamedItem(Attribute.NAME).getNodeValue();
	if (propName == null || "".equals(propName.trim())) { // if no property name found throws exception
	    throw new ConfigurationException("No property name set");
	}

	Property property = new Property();
	// Node depthAttr = node.getAttributes().getNamedItem(Attribute.DEPTH);
	// if(depthAttr != null){
	// property.setDepth(Integer.parseInt(depthAttr.getNodeValue()));
	// }

	ManageableObject manageableObject = new ManageableObject(property);

	property.setName(propName);

	// looks for a value attribute
	Node valueAttribute = node.getAttributes().getNamedItem(Attribute.VALUE);
	String propValue = null;

	if (valueAttribute != null) {// if not null gets the value
	    propValue = valueAttribute.getNodeValue();
	}

	if (propValue == null || "".equals(propValue.trim())) {
	    String propValueRef = null;
	    // looks for a reference to another entity
	    Node propValueAttribute = node.getAttributes().getNamedItem(Attribute.VALUE_REF);

	    if (propValueAttribute != null) {
		propValueRef = propValueAttribute.getNodeValue();
	    }

	    if (propValueRef == null || "".equals(propValueRef.trim())) {

		NodeList childNodes = node.getChildNodes();
		if (1 == childNodes.getLength()) { // can contain only one node
		    Node currentNode = childNodes.item(0);
		    if (currentNode.getNodeName().equals(Tag.VALUE)) { // the node must be a value node
			ManageableObject nestedObject = configureNestedNode(currentNode);
			property.setValue(nestedObject.getTarget());
			manageableObject.addNestedObject(nestedObject);
		    }
		} else {
		    /* no value or value-ref found */
		    throw new ConfigurationException("No value specified for property " + propName);
		}
	    } else {
		property.setValueRef(propValueRef);
	    }

	} else {
	    property.setValue(propValue);
	}
	return manageableObject;
    }

}
