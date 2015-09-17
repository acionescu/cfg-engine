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
 * $Id: ListConfigurationHandler.java,v 1.3 2007/09/07 11:02:07 aionescu Exp $
 */
package net.segoia.cfgengine.core.configuration.handlers;

import java.util.ArrayList;
import java.util.List;

import net.segoia.cfgengine.core.configuration.ManageableObject;
import net.segoia.cfgengine.core.configuration.Tag;
import net.segoia.cfgengine.core.configuration.listener.DependencyListener;
import net.segoia.cfgengine.core.configuration.listener.ListDependencyListener;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author Adrian Ionescu
 * @version $Revision: 1.3 $
 *
 */
public class ListConfigurationHandler extends BaseConfigurationHandler{
	
	public ListConfigurationHandler(){
		
	}


	public ManageableObject configure(Node node) throws Exception{
		List list = new ArrayList();
		ManageableObject manageableObject = new ManageableObject(list);
		
		NodeList childNodes = node.getChildNodes();
		
		for(int i=0;i<childNodes.getLength();i++){
			Node currentNode = childNodes.item(i);
			if(currentNode.getNodeName().equals(Tag.VALUE)){
				ManageableObject nestedObject = configureNestedNode(currentNode);
				list.add(nestedObject.getTarget());
				manageableObject.addNestedObject(nestedObject);
			}
			else if(currentNode.getNodeName().equals(Tag.VALUE_REF)) {
				DependencyListener listener = new ListDependencyListener(list);
				String dependencyId = currentNode.getFirstChild().getNodeValue();
				manageableObject.addDependency(dependencyId, listener);
			}
		}
		
		return manageableObject;
	}

}
