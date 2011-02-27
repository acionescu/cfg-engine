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
package ro.zg.cfgengine.core.configuration.listener;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import ro.zg.cfgengine.core.configuration.Attribute;


public class DefaultConfigurationListener implements ConfigurationListener{
	/**
	 * @uml.property  name="destroyMethods"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.String"
	 */
	private Map<String,String> destroyMethods = new HashMap<String,String>();
	/**
	 * @uml.property  name="initMethods"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.String"
	 */
	private Map<String,String> initMethods = new HashMap<String,String>();
	
	
	public void afterConfiguration(Node node) {
		Node idAttr = node.getAttributes().getNamedItem(Attribute.ID);
		if(idAttr == null) {
			return;
		}
		String id = idAttr.getNodeValue();
		
		Node initMethodAttribute = node.getAttributes().getNamedItem(Attribute.INIT_METHOD);
		if(initMethodAttribute != null) {
			String initMethodName = initMethodAttribute.getNodeValue();
			initMethods.put(id, initMethodName);
		}
		
		Node destroyMethodAttribute = node.getAttributes().getNamedItem(Attribute.DESTROY_METHOD);
		if(destroyMethodAttribute != null) {
			String destroyMethodName = destroyMethodAttribute.getNodeValue();
			destroyMethods.put(id, destroyMethodName);
		}
	}

	public void beforeConfiguration(Node node) {
		// TODO Auto-generated method stub
		
	}

}
