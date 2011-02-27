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
 * $Id: MapConfigurationHandler.java,v 1.5 2007/09/28 16:17:57 aionescu Exp $
 */
package ro.zg.cfgengine.core.configuration.handlers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.zg.cfgengine.core.configuration.Attribute;
import ro.zg.cfgengine.core.configuration.DependencyRef;
import ro.zg.cfgengine.core.configuration.ManageableObject;
import ro.zg.cfgengine.core.configuration.Tag;
import ro.zg.cfgengine.core.configuration.listener.DependencyListener;
import ro.zg.cfgengine.core.configuration.listener.MapDependencyListener;
import ro.zg.util.data.reflection.ReflectionUtility;

/**
 * Creates a Map type object from an xml node Map entries can be set by using tags like this: <entry key="" value=""/>
 * 
 * @author aionescu
 * @version $Revision: 1.5 $
 * 
 */
public class MapConfigurationHandler extends BaseConfigurationHandler {

    public MapConfigurationHandler() {

    }

    public ManageableObject configure(Node node) throws Exception {
	Map map = new LinkedHashMap();
	ManageableObject manageableObject = new ManageableObject(map);
	NodeList nodes = node.getChildNodes();
	for (int i = 0; i < nodes.getLength(); i++) {
	    Node currentNode = nodes.item(i);
	    if (currentNode.getNodeName().equals(Tag.ENTRY)) {
		NamedNodeMap attributes = currentNode.getAttributes();
		Node keyAttr = attributes.getNamedItem(Attribute.KEY);
		Object key = null;
		if (keyAttr != null) {
		    key = keyAttr.getNodeValue();
		}

		Object value = null;
		if (attributes.getNamedItem(Attribute.VALUE) != null) {
		    value = attributes.getNamedItem(Attribute.VALUE).getNodeValue();
		    Node typeAttr = attributes.getNamedItem(Attribute.TYPE);
		    if (typeAttr != null) {
			value = ReflectionUtility.createObjectByTypeAndValue(typeAttr.getNodeValue(), value.toString());
		    }
		}

		if (key == null || value == null) {
		    NodeList entryChildNodes = currentNode.getChildNodes();
		    for (int k = 0; k < entryChildNodes.getLength(); k++) {
			Node entryChildNode = entryChildNodes.item(k);
			String childNodeName = entryChildNode.getNodeName();
			if (key == null) {
			    if (childNodeName.equals(Tag.KEY)) {
				ManageableObject nestedObject = configureNestedNode(entryChildNode);
				key = nestedObject.getTarget();
				manageableObject.addNestedObject(nestedObject);
			    }
			}
			if (value == null) {
			    if (childNodeName.equals(Tag.VALUE) || childNodeName.equals(Tag.VALUE_TYPE)) { // the node
													   // must be a
													   // value node
				ManageableObject nestedObject = configureNestedNode(entryChildNode);
				value = nestedObject.getTarget();
				if (value instanceof DependencyRef) {
				    DependencyRef depRef = (DependencyRef) value;
				    DependencyListener listener = new MapDependencyListener(map, key);
				    manageableObject.addDependency(depRef.getDependencyRef(), listener);
				    break;
				} else {
				    manageableObject.addNestedObject(nestedObject);
				}
			    } else if (childNodeName.equals(Tag.VALUE_REF)) { // the node points to a
				// resource
				DependencyListener listener = new MapDependencyListener(map, key);
				String dependencyId = entryChildNode.getFirstChild().getNodeValue().trim();
				manageableObject.addDependency(dependencyId, listener);
				break;
			    }
			}
			if (key != null && value != null) {
			    break;
			}

		    }

		}

		map.put(key, value);
	    }
	}

	return manageableObject;
    }

}
