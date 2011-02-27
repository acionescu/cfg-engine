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
 * $Id: ListConfigurationHandler.java,v 1.3 2007/09/07 11:02:07 aionescu Exp $
 */
package ro.zg.cfgengine.core.configuration.handlers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.zg.cfgengine.core.configuration.ManageableObject;
import ro.zg.cfgengine.core.configuration.Tag;
import ro.zg.cfgengine.core.configuration.listener.DependencyListener;
import ro.zg.cfgengine.core.configuration.listener.ListDependencyListener;


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
