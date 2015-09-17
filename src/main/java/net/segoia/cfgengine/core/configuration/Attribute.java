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
package net.segoia.cfgengine.core.configuration;

/**
 * defines the attribute names that can be found in the xml configuration files
 * 
 * @author aionescu
 * @version $Revision: 1.6 $
 *
 */
public interface Attribute {
	
	public static String CLASS = "class";
	
	public static String ID = "id";
	
	public static String TYPE = "type";
	
	public static String NAME = "name";
	
	public static String VALUE = "value";
	
	public static String DEPTH ="depth";
	
	public static String VALUE_REF = "value-ref";
	
	public static String KEY = "key";
	
	public static String FILTER = "filter";
	
	public static String MIN = "min";
	
	public static String MAX = "max";
	
	public static String WORKER_REF = "worker-ref";
	
	public static String ID_REF = "id-ref";
	
	public static String TIMEOUT = "timeout";
	
	public static String EXIT ="exit";
	
	public static String INIT_METHOD="init-method";
	
	public static String DESTROY_METHOD="destroy-method";
	
	public static String TAG_NAME = "tag-name";
	
	public static String TAG_REF = "tag-ref";
	
	public static String RECURSIVE = "recursive";
	
}
