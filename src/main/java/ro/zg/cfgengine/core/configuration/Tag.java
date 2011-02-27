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
 * Defines all the tag names that can be found in an xml configuration file
 * 
 * @author aionescu
 * @version $Revision: 1.8 $
 *
 */
public interface Tag {
	
	public static String PROPERTY="property";
	
	public static String MAP="map";
	
	public static String ENTRY="entry";
	
	public static String VALUE="value";
	
	public static String KEY="key";
	
	public static String RESOURCE="resource";
	
	public static String POOL="pool";
	
	public static String ENTRY_POINT="entry-point";
		
	public static String VALUE_REF = "value-ref";
	
	public static String KEY_REF="key-ref";
	
	public static String CONTEXT = "context";
	
	public static String VALUE_TYPE ="value-type";
}
