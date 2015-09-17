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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.segoia.cfgengine.core.configuration.listener.DependencyListener;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.util.data.reflection.ReflectionUtility;

public class ManageableObject {
    private String id;
    private String tagName;
    private Object target;
    private String initMethodName;
    private String destroyMethodName;
    private String resourceLoaderPropertyName;
    private Map<String, List<DependencyListener>> dependencyListeners = new Hashtable<String, List<DependencyListener>>();
    private List<ManageableObject> nestedObjects = new Vector<ManageableObject>();
    /**
     * The depth in of this {@link ManageableObject}
     * whenever a nested object is added the depth will be increased with 1 on that object  
     */
    private int depth = 0;
    private Map<String, List<Dependency>> dependencies = new Hashtable<String, List<Dependency>>();
    /**
     * Holds the implementation classes for complex properties like Lists and Maps
     * </br>
     * key - name of the property
     * </br>
     * value - implementation class name (e.g. java.util.Vector for List)
     */
    private Map<String,String> complexPropsImplementations = new Hashtable<String, String>();
    
    /**
     * Holds the values in the order they should be inserted in object of complex types like
     * Lists and sorted Maps
     * This property will hold either the actual values that should be added to the specified
     * property of a {@link Dependency} object that will hold the id of the object that
     * should be added to the property
     * The {@link Dependency} objects will be replaced with the real values after the 
     * {@link #resolveDependency(String, Object)} method for that dependency is called
     * When the {@link #callInitMethod()} is called the objects found in the list defined
     * by the "value" of this map, will be set on the property
     * defined by the "key" of this map. 
     */
    private Map<String,List<Object>> valuesForComplexProps = new Hashtable<String, List<Object>>();
    
    /**
     * Default implementation user for List
     */
    private String defaultListImplementation = "java.util.Vector";
    /**
     * Default implementation used for Map
     */
    private String defaultMapImplementation = "java.util.LinkedHashMap";

    
    public ManageableObject(Object target) {
	this.target = target;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getTagName() {
	return tagName;
    }

    public void setTagName(String tagName) {
	this.tagName = tagName;
    }
    
    public void setResourcesLoader(ClassLoader cl) throws ConfigurationException{
	if(resourceLoaderPropertyName != null){
	    setFieldValue(resourceLoaderPropertyName, cl);
	}
    }

    public void addDependency(String dependencyId, DependencyListener listener) {
	List<DependencyListener> depList = dependencyListeners.get(dependencyId);
	if (depList == null) {
	    depList = new Vector<DependencyListener>();
	    dependencyListeners.put(dependencyId, depList);
	}
	depList.add(listener);
    }
    
    public void addDependency(Dependency dep) throws ConfigurationException{
	List<Dependency> depList = dependencies.get(dep.getDependencyId());
	if(depList == null){
	    depList = new Vector<Dependency>();
	    dependencies.put(dep.getDependencyId(), depList);
	}
	depList.add(dep);
	/* add it to valuesForComplexProps if we're dealing witha complex prop */
	try {
	    addValueForComplexProp(dep.getPropertyName(),dep);
	} catch (Exception e) {
	    throw new ConfigurationException("Could not set dependency as value for complex prop "+dep.getPropertyName(),e);
	}
    }
    
    
    
    private boolean checkIsComplexType(String propName) throws Exception{
	boolean isComplexType = ReflectionUtility.checkFieldType(target, propName, List.class);
	if(!isComplexType){
	    isComplexType =  ReflectionUtility.checkFieldType(target, propName, Map.class);
	}
	return isComplexType;
    }
    
    /**
     * Ads a value for complex props. Returns true if added , false otherwise.
     * @param propName
     * @param value
     * @return
     * @throws Exception
     */
    private boolean addValueForComplexProp(String propName, Object value) throws ConfigurationException{
	boolean isComplexType = false;
	try {
	    isComplexType = checkIsComplexType(propName);
	} catch (Exception e) {
	    throw new ConfigurationException("Error checking if property '"+propName+"' on object "+target+" is complex type",e);
	}
	if(isComplexType){
	    addComplexPropValue(propName, value);
	    return true;
	}
	return false;
    }

    public void addNestedObject(ManageableObject nestedObject) {
	nestedObjects.add(nestedObject);
    }
    /*
     * Used when multiple objects are obtained from the same node.
     * This is used usually with wrapper objects.
     * For each wrapped object the depth is increased with one.
     * This will help with the property setting, in order to identify on which instance
     * a certain property should be set
     */
    public void addWrappedNestedObject(ManageableObject nestedObject){
	addNestedObject(nestedObject);
	nestedObject.depth++;
    }

    public Object getTarget() {
	return target;
    }

    public Set<String> getDependencies() {
	Set<String> dependenciesIds = new HashSet<String>(dependencyListeners.keySet());
	// get also the dependencies for the nested objects
	for (ManageableObject nestedObject : nestedObjects) {
	    dependenciesIds.addAll(nestedObject.getDependencies());
	}
	dependenciesIds.addAll(dependencies.keySet());
	
	return dependenciesIds;
    }

    public void callInitMethod() throws Exception {
	/* init complex props */
	initComplexProps();
	
	/* call init on nested objects */
	for (ManageableObject nestedObject : nestedObjects) {
	    nestedObject.callInitMethod();
	}

	if (initMethodName != null) {
	    ReflectionUtility.callMethod(target, initMethodName, new Object[0]);
	}
    }

    public void callDestroyMethod() throws Exception {
	/* call destroy on nested objects */
	for(ManageableObject nestedObject : nestedObjects){
		nestedObject.callDestroyMethod();
	}
	if (destroyMethodName != null) {
	    ReflectionUtility.callMethod(target, destroyMethodName, new Object[0]);
	}
    }

    private void initComplexProps() throws Exception{
	for(String propName : valuesForComplexProps.keySet()){
	    initComplexProp(propName);
	}
    }
    
    private void initComplexProp(String propName) throws Exception{
	if(ReflectionUtility.checkFieldType(target, propName, List.class)){
	    initListProp(propName);
	}
	else if(ReflectionUtility.checkFieldType(target, propName, Map.class)){
	    initMapProp(propName);
	}
    }
    
    private void initListProp(String propName) throws Exception{
	List<Object> object = (List<Object>)getObjectForComplexProp(propName,defaultListImplementation);
	for(Object o : valuesForComplexProps.get(propName)){
	    /* we should have only IdValueWrapper instances if everything went ok */
	    IdValueWrapper w = (IdValueWrapper)o;
	    object.add(w.getValue());
	}
    }
    
    private void initMapProp(String propName) throws Exception{
	Map<Object,Object> object = (Map<Object,Object>)getObjectForComplexProp(propName,defaultMapImplementation);
	for(Object o : valuesForComplexProps.get(propName)){
	    /* we should have only IdValueWrapper instances if everything went ok */
	    IdValueWrapper w = (IdValueWrapper)o;
	    object.put(w.getId(), w.getValue());
	}
    }
    
    private Object getObjectForComplexProp(String propName, String defaultImpl) throws Exception{
	Object obj = ReflectionUtility.getValueForField(target, propName);
	if( obj == null ){
	    String complexPropImplClass = complexPropsImplementations.get(propName);
	    if(complexPropImplClass == null){
		complexPropImplClass = defaultImpl;
	    }
	    obj = Class.forName(complexPropImplClass).newInstance();
	    ReflectionUtility.setValueToField(target, propName, obj);
	}
	return obj;
    }
    
    public void resolveDependency(String dependencyId, Object dependency) throws ConfigurationException {
	if (dependency == null) {
	    return;
	}
	List<DependencyListener> depList = dependencyListeners.get(dependencyId);
	if (depList != null) {
	    for (DependencyListener listener : depList) {
		if (listener != null) {
		    listener.onDependency(dependency);
		}
	    }
	}

	// resolve the dependency for the nested objects
	for (ManageableObject nestedObject : nestedObjects) {
	    nestedObject.resolveDependency(dependencyId, dependency);
	}
	dependencyListeners.remove(dependencyId);
	
	List<Dependency> simpleDepList = dependencies.get(dependencyId);
	if(simpleDepList != null){
	    for (Dependency dep : simpleDepList){
		setValueForDependency(dep, dependency);
	    }
	    dependencies.remove(dependencyId);
	}
	
	
    }
    
    public void setComplexPropImplementation(String propName, String className){
	complexPropsImplementations.put(propName, className);
    }
    
    public void addComplexPropValue(String propName, Object value){
	List<Object> values = valuesForComplexProps.get(propName);
	if (values == null) {
	    values = new Vector<Object>();
	    valuesForComplexProps.put(propName, values);
	}
	values.add(value);
    }
    
    public void setValueForProperty(String propName, Object value) throws ConfigurationException{
	IdValueWrapper w = new IdValueWrapper(null,value);
	boolean addedComplexValue = addValueForComplexProp(propName, w);
	
	if(!addedComplexValue){
	    setFieldValue(propName, value);
	}
    }
    
    public void setValueForProperty(String propName, Object value, String associatedId) throws ConfigurationException{
	IdValueWrapper w = new IdValueWrapper(associatedId,value);
	
	boolean addedComplexValue = addValueForComplexProp(propName, w);
	
	if(!addedComplexValue){
	    setFieldValue(propName, value);
	}
    }
    
    public void overrideProperty(String propName, Object value) throws ConfigurationException{
	    setFieldValue(propName, value);
    }
    
    private void setValueForDependency(Dependency dep, Object value) throws ConfigurationException{
	/* if we deal with a complex prop replace dependency with value */
	if(valuesForComplexProps.containsKey(dep.getPropertyName())){
	    replaceDependencyWithValueForComplexProps(dep, value);
	}
	/* we have a simple property, then go ahead and set the value */
	else{
	    setFieldValue(dep.getPropertyName(), value);
	}
    }
    
    private void setFieldValue(String propName, Object value) throws ConfigurationException{
	try {
	    ReflectionUtility.setValueToField(target, propName, value);
	} catch (Exception e) {
	   throw new ConfigurationException("Error setting value '"+value+"' for" +
	   		" field '"+propName+" on object "+target,e);
	}
    }
    
    private void replaceDependencyWithValueForComplexProps(Dependency dep, Object value){
	List<Object> valuesList = valuesForComplexProps.get(dep.getPropertyName());
	int indexOfDependency = valuesList.indexOf(dep);
	valuesList.remove(indexOfDependency);
	IdValueWrapper w = new IdValueWrapper(dep.getCurrentNodeId(),value);
	if(indexOfDependency >= valuesList.size()){
	    valuesList.add(w);
	}
	else{
	    valuesList.add(indexOfDependency, w);
	}
    }

    public String getInitMethodName() {
	return initMethodName;
    }

    public String getDestroyMethodName() {
	return destroyMethodName;
    }

    public void setInitMethodName(String initMethodName) {
	this.initMethodName = initMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
	this.destroyMethodName = destroyMethodName;
    }

    public String getDefaultListImplementation() {
        return defaultListImplementation;
    }

    public String getDefaultMapImplementation() {
        return defaultMapImplementation;
    }

    public void setDefaultListImplementation(String defaultListImplementation) {
        this.defaultListImplementation = defaultListImplementation;
    }

    public void setDefaultMapImplementation(String defaultMapImplementation) {
        this.defaultMapImplementation = defaultMapImplementation;
    }

    public int getDepth() {
        return depth;
    }

    public Map<String, List<DependencyListener>> getDependencyListeners() {
        return dependencyListeners;
    }

    /**
     * @return the resourceLoaderPropertyName
     */
    public String getResourceLoaderPropertyName() {
        return resourceLoaderPropertyName;
    }

    /**
     * @param resourceLoaderPropertyName the resourceLoaderPropertyName to set
     */
    public void setResourceLoaderPropertyName(String resourceLoaderPropertyName) {
        this.resourceLoaderPropertyName = resourceLoaderPropertyName;
    }

}
