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

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.zg.cfgengine.core.configuration.ManageableObject;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;

public class CustomMapConfigurationHandler extends BaseConfigurationHandler{
    /**
     * Specifies which attribute to be used as key for each tag
     */
    private Map<String,String> keyAttributeNames = new HashMap<String,String>();

    protected ManageableObject configure(ManageableObject manageableObject, Node node) throws Exception{
	Map targetMap = (Map)manageableObject.getTarget();
	NodeList childNodes = node.getChildNodes();
	
	for(int i=0;i<childNodes.getLength();i++){
	    
	    Node currentNode = childNodes.item(i);
	    String nodeName = currentNode.getNodeName();
	    /* get name of the attribute to be used as key for this tag */
	    String keyAttributeName = keyAttributeNames.get(nodeName);
	    if(keyAttributeName == null){
		throw new ConfigurationException("No attribute specified to be used as key for tag "+nodeName);
	    }
	    /* now get the value of the attribute */
	    Node keyAttr = currentNode.getAttributes().getNamedItem(keyAttributeName);
	    if(keyAttr == null){
		throw new ConfigurationException("No attribute with name "+keyAttributeName+" found for tag "+nodeName);
	    }
	    String key = keyAttr.getNodeValue();
	    ManageableObject entry = configureNestedNode(currentNode);
	    Object entryObject = entry.getTarget();
	    targetMap.put(key, entryObject);
	}
	return manageableObject;
    }

    public Map<String, String> getKeyAttributeNames() {
        return keyAttributeNames;
    }

    public void setKeyAttributeNames(Map<String, String> keyAttributeNames) {
        this.keyAttributeNames = keyAttributeNames;
    }

}
