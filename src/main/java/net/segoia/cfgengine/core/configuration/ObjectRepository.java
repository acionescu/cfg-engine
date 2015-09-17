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

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ObjectRepository {
	private Map<String,ManageableObject> repository = new Hashtable<String,ManageableObject>();
	private Map<String,List<ManageableObject>> repositoryByTagName = new Hashtable<String,List<ManageableObject>>();
	
	public void addObject(String id, ManageableObject object){
		if(repository.containsKey(id)){
			throw new IllegalArgumentException("An object with id '"+id+"' already exists in the repository.");
		}
		else{
			repository.put(id, object);
		}
		
		String tagName = object.getTagName();
		if(tagName != null){
			List<ManageableObject> listOfObjects = repositoryByTagName.get(tagName);
			if(listOfObjects == null){
				listOfObjects = new Vector<ManageableObject>();
				repositoryByTagName.put(tagName, listOfObjects);
			}
			listOfObjects.add(object);
		}
	}
	
	public void addListOfObjects(List<ManageableObject> moList){
		for(ManageableObject mo : moList){
			if(mo.getId() != null){
				addObject(mo.getId(), mo);
			}
		}
	}
	
	public void purge(){
	    repository.clear();
	    repositoryByTagName.clear();
	}
	
	public ManageableObject getObject(String id){
		return repository.get(id);
	}
	
	public List<ManageableObject> getObjectsByTagName(String tagName){
	    return repositoryByTagName.get(tagName);
	}
	
	public String getObjectTagById(String id){
	    ManageableObject mo = getObject(id);
	    if(mo != null){
		return mo.getTagName();
	    }
	    return null;
	}
	
	public List<ManageableObject> getAllObjects(){
		return new Vector<ManageableObject>(repository.values());
	}
	
	public List<ManageableObject> getOjbectsByTagName(String tagName){
		return repositoryByTagName.get(tagName);
	}
	
	public boolean containsObjectWithId(String id){
	    return repository.containsKey(id);
	}
}
 
