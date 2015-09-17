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
 * $Id: MapDependencyListener.java,v 1.1 2007/09/14 11:20:43 aionescu Exp $
 */
package net.segoia.cfgengine.core.configuration.listener;

import java.util.Map;

public class MapDependencyListener implements DependencyListener {
	Map target;
	Object key;
	
	public MapDependencyListener(Map target,Object key) {
		this.target = target;
		this.key = key;
	}
	
	public void onDependency(Object objRef) {
		target.put(key, objRef);		
	}

}
