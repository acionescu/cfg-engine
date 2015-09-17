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
package net.segoia.cfgengine.core.configuration.listener;

import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.util.data.reflection.ReflectionUtility;


public class PropertyDependencyListener implements DependencyListener{
	private Object target;
	private String propertyName;
	
	public PropertyDependencyListener(Object target, String propertyName){
		this.target = target;
		this.propertyName = propertyName;
	}

	public void onDependency(Object objRef) throws ConfigurationException{
		try {
			ReflectionUtility.setValueToField(target, propertyName, objRef);
		} catch (Exception e) {
			throw new ConfigurationException("Error setting property '"+propertyName+"' on " +
					"object of type "+target.getClass().getName(),e);
		}
		
	}

}
