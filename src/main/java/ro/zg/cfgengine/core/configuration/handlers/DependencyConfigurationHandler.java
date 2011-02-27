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

import ro.zg.cfgengine.core.configuration.DependencyRef;
import ro.zg.cfgengine.core.configuration.ManageableObject;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;

public class DependencyConfigurationHandler extends BaseConfigurationHandler{
    private String referenceAttributeName;
    
    public ManageableObject configure(Node node) throws Exception{
	NamedNodeMap attributes = node.getAttributes();
	if(attributes == null){
	    throw new ConfigurationException("Node "+node.getNodeName()+" must have an attribute with name "+referenceAttributeName);
	}
	Node refAttr = attributes.getNamedItem(referenceAttributeName);
	if(refAttr == null){
	    throw new ConfigurationException("Node "+node.getNodeName()+" must have an attribute with name "+referenceAttributeName);
	}
	String refId = refAttr.getNodeValue();
	return new ManageableObject(new DependencyRef(refId));
    }

    public String getReferenceAttributeName() {
        return referenceAttributeName;
    }

    public void setReferenceAttributeName(String referenceAttributeName) {
        this.referenceAttributeName = referenceAttributeName;
    }
    
    
}
