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

import net.segoia.cfgengine.core.configuration.Attribute;
import net.segoia.cfgengine.core.configuration.ManageableObject;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.util.data.reflection.ReflectionUtility;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TypedValueConfigurationHandler extends BaseConfigurationHandler{
    private String valueAttributeName = Attribute.VALUE;
    private String typeAttributeName = Attribute.TYPE;
    private String defaultType = "String";
    
    public ManageableObject configure(Node node) throws ConfigurationException{
	String type = defaultType;
	NamedNodeMap attributes = node.getAttributes();
	Node valueAttr = attributes.getNamedItem(valueAttributeName);
	if(valueAttr == null){
	    throw new ConfigurationException("Value node with name "+valueAttributeName+" cannot be null.");
	}
	Node typeAttr = attributes.getNamedItem(typeAttributeName);
	if( typeAttr != null ){
	    type = typeAttr.getNodeValue();
	}
	Object value = valueAttr.getNodeValue();
	if( type != null){
	    try {
		value = ReflectionUtility.createObjectByTypeAndValue(type, value.toString());
	    } catch (Exception e) {
		throw new ConfigurationException("Error casting value '"+value+"' to type "+type,e);
	    }
	}
	
	ManageableObject mo = new ManageableObject(value);
	
	return mo;
    }

    public String getValueAttributeName() {
        return valueAttributeName;
    }

    public String getTypeAttributeName() {
        return typeAttributeName;
    }

    public void setValueAttributeName(String valueAttributeName) {
        this.valueAttributeName = valueAttributeName;
    }

    public void setTypeAttributeName(String typeAttributeName) {
        this.typeAttributeName = typeAttributeName;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }
    
}
