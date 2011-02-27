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
 * $Id: ValueConfigurationHandler.java,v 1.3 2007/06/18 07:47:06 victor Exp $
 */
package ro.zg.cfgengine.core.configuration.handlers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.zg.cfgengine.core.configuration.ManageableObject;


/**
 * Configuration Handler implementation for a <value> tag
 * 
 * @author aionescu
 * @version $Revision: 1.3 $
 *
 */
public class ValueConfigurationHandler extends BaseConfigurationHandler {
	
	public ValueConfigurationHandler(){
		
	}

	/**
	 * A value tag can contain any other tag the has specified a configuration
	 * handler
	 */
	public ManageableObject configure(Node node) throws Exception {

		String nodeValue = node.getFirstChild().getNodeValue();
		if (nodeValue != null && !"".equals(nodeValue.trim())) {// if exists
																// will return
																// the node's
																// text content
			ManageableObject manageableObject = new ManageableObject(nodeValue.trim());
			return manageableObject;
		}

		NodeList childNodes = node.getChildNodes();

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node currentNode = childNodes.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {// returns
																	// an object
																	// created
																	// from the
																	// first
																	// registered
																	// node
				return configureNestedNode(currentNode);
			}
		}
		return null;

	}

}
