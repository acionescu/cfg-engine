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
 * Holds the dependency information of a configured object to on another
 * @author adi
 *
 */
public class Dependency {
    /**
     * The id of object the target object depends on
     */
    private String dependencyId;
    /**
     * The property of the target object that will hold a reference to the object that
     * dependencyId property points to
     */
    private String propertyName;
    
    /**
     * The value of the attribute defined as id for the tag from which the dependency was
     * extracted
     */
    private String currentNodeId;
    
    public Dependency(String dependencyId, String propName){
	this.dependencyId = dependencyId;
	this.propertyName = propName;
    }
    
    public Dependency(String dependnecyId, String propName, String currentNodeId){
	this(dependnecyId,propName);
	this.currentNodeId = currentNodeId;
    }
    
    public String getDependencyId() {
        return dependencyId;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public String getCurrentNodeId() {
        return currentNodeId;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((currentNodeId == null) ? 0 : currentNodeId.hashCode());
	result = prime * result + ((dependencyId == null) ? 0 : dependencyId.hashCode());
	result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Dependency other = (Dependency) obj;
	if (currentNodeId == null) {
	    if (other.currentNodeId != null)
		return false;
	} else if (!currentNodeId.equals(other.currentNodeId))
	    return false;
	if (dependencyId == null) {
	    if (other.dependencyId != null)
		return false;
	} else if (!dependencyId.equals(other.dependencyId))
	    return false;
	if (propertyName == null) {
	    if (other.propertyName != null)
		return false;
	} else if (!propertyName.equals(other.propertyName))
	    return false;
	return true;
    }

    
}
