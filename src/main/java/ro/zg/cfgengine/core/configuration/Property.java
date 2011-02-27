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
package ro.zg.cfgengine.core.configuration;

/**
 * Java corespondend of a <property> xml tag  Afeter processing a <property> node a Property java object will be created
 * @author   aionescu
 * @version   $Revision: 1.3 $
 */
public class Property {
	String name;
	Object value;
	String valueRef;
	private int depth = -1;
	/**
	 * @return   the name
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name   the name to set
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return   the value
	 * @uml.property  name="value"
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value   the value to set
	 * @uml.property  name="value"
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return   the valueRef
	 * @uml.property  name="valueRef"
	 */
	public String getValueRef() {
		return valueRef;
	}
	/**
	 * @param valueRef   the valueRef to set
	 * @uml.property  name="valueRef"
	 */
	public void setValueRef(String valueRef) {
		this.valueRef = valueRef;
	}
	public int getDepth() {
	    return depth;
	}
	public void setDepth(int depth) {
	    this.depth = depth;
	}
	
	
}
