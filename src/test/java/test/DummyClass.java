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
package test;

import java.util.List;

/**
 * @author  adi
 */
public class DummyClass {
	private String name;
	private DummyClass parent;
	private List<DummyClass> children;

	/**
	 * @return  the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name  the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return  the parent
	 */
	public DummyClass getParent() {
		return parent;
	}

	/**
	 * @param parent  the parent to set
	 */
	public void setParent(DummyClass parent) {
		this.parent = parent;
	}

	/**
	 * @return  the children
	 */
	public List<DummyClass> getChildren() {
		return children;
	}

	/**
	 * @param children  the children to set
	 */
	public void setChildren(List<DummyClass> children) {
		this.children = children;
	}
	
	public String toString(){
		return "[DummyClass : name = "+name+"; parent = "+parent+" ; children = "+children+"]";
	}
	
}
