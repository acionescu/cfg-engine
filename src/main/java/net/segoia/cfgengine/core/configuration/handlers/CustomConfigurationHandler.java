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
package net.segoia.cfgengine.core.configuration.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.segoia.cfgengine.core.configuration.Attribute;
import net.segoia.cfgengine.core.configuration.Dependency;
import net.segoia.cfgengine.core.configuration.ManageableObject;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author adi
 */
public class CustomConfigurationHandler extends BaseConfigurationHandler {
    /**
     * Keeps the mapping between attributes and properties
     * 
     * @uml.property name="attributesToProperties"
     * @uml.associationEnd qualifier="key:java.lang.Object java.lang.String"
     */
    private Map<String, String> attributesToProperties = new HashMap<String, String>();
    /**
     * Keeps the mapping between the tags used inside the parent tag and the corresponding properties of the object to
     * be created
     * 
     * @uml.property name="tagsToProperties"
     * @uml.associationEnd qualifier="key:java.lang.Object java.lang.String"
     */
    private Map<String, String> tagsToProperties = new HashMap<String, String>();

    /**
     * For a tag mapped to a property of type {@link Map} and id attribute must be used as key element of the map. If an
     * id is not specified then another field can be used as key In this map for a given tag, the name of another
     * attribute can be specified to be used as key in the Map. </br> key - tag name </br> value - name of the attribute
     * to be used as key
     */
    private Map<String, String> attributesAsIdsForTags = new HashMap<String, String>();

    /**
     * The list with the attributes that are actually references to another resource
     */
    private List<String> referenceAttributes = new ArrayList<String>();

    /**
     * Defines the tags that are actually references to other resources key - the name of the tag value - the attribute
     * that defines the reference id to the other resource
     */
    private Map<String, String> referenceTags = new HashMap<String, String>();

   
    /**
	 * 
	 */
    private String idRefAttributeName = Attribute.ID_REF;
    /**
     * Default implementation user for List
     */
    private String defaultListImplementation = "java.util.Vector";
    /**
     * Default implementation used for Map
     */
    private String defaultMapImplementation = "java.util.LinkedHashMap";

    /**
     * In case you are crazy enough and you have a wrapped property in your main class an you want to configure it from
     * the same node the main class is configured, then this is for you!! </br> key - the name of the property </br>
     * value - the configuration handler to be used to configure this property from the main node
     */
    private Map<String, ConfigurationHandler> wrappedPropertiesHandlers = new HashMap<String, ConfigurationHandler>();
/**
 * Overridden props. Useful for maps and lists.
 */
    private List<String> overRiddenProps = new ArrayList<String>();
    
    public ManageableObject configure(Node node) throws Exception {
	ManageableObject manageableObject = getManageableObjectSkeleton(node);
	manageableObject = configure(manageableObject, node);
	if (wrappedPropertiesHandlers.size() > 0) {
	    /* set wrapped properties */
	    for (String propName : wrappedPropertiesHandlers.keySet()) {
		ConfigurationHandler ch = wrappedPropertiesHandlers.get(propName);
		ManageableObject nestedObject = ch.configure(node);
		Object currentObj = nestedObject.getTarget();
		manageableObject.addWrappedNestedObject(nestedObject);
		manageableObject.setValueForProperty(propName, currentObj);
	    }
	    return manageableObject;
	}

	addProperies(manageableObject, node);
	return manageableObject;
    }

    protected ManageableObject configure(ManageableObject manageableObject, Node node) throws Exception {
	manageableObject.setDefaultListImplementation(defaultListImplementation);
	manageableObject.setDefaultMapImplementation(defaultMapImplementation);
	// Object object = manageableObject.getTarget();

	/* set default values for props */
//	for (String propName : defaultValuesForProperties.keySet()) {
//	    Object value = defaultValuesForProperties.get(propName);
//	    manageableObject.setValueForProperty(propName, value);
//	}
//
//	for (String propName : defaultReferencesForProperties.keySet()) {
//	    String refid = defaultReferencesForProperties.get(propName);
//	    manageableObject.addDependency(new Dependency(refid, propName));
//	}

	configureDefaultValuesForProps(manageableObject);
	
	NamedNodeMap attributes = node.getAttributes();
	if (attributes != null) {
	    // if the attribute is mapped to a property
	    // the set the specified value to that property
	    for (int i = 0; i < attributes.getLength(); i++) {
		Node currentAttribute = attributes.item(i);
		String attributeName = currentAttribute.getNodeName();
		String mappedProperty = attributesToProperties.get(attributeName);
		if (mappedProperty != null && !"".equals(mappedProperty)) {
		    String value = currentAttribute.getNodeValue();
		    /* we have a reference attribute */
		    if (referenceAttributes.contains(attributeName)) {
			/* tell the builder to resolve this dependency */
			manageableObject.addDependency(new Dependency(value, mappedProperty));
		    }
		    /* just a plain value set it through reflection */
		    else {
			// ReflectionUtility.setValueToField(object, mappedProperty, value);
			manageableObject.setValueForProperty(mappedProperty, value);
		    }
		}
	    }
	}
	Map<String, Object> complexProps = new HashMap<String, Object>();

	getPropertiesFromTags(manageableObject, node);

	return manageableObject;
    }

    private void getPropertiesFromTags(ManageableObject manageableObject, Node node) throws Exception {
	NodeList nodes = node.getChildNodes();
	for (int i = 0; i < nodes.getLength(); i++) {
	    /* iterates through the child nodes */
	    Node currentNode = nodes.item(i);
	    getPropertyFromTag(manageableObject, currentNode);

	}// end for
    }

    private void getPropertyFromTag(ManageableObject manageableObject, Node currentNode) throws Exception {
	String currentNodeName = currentNode.getNodeName();
	if (tagsToProperties.containsKey(currentNodeName)) {
	    String propName = tagsToProperties.get(currentNodeName);

	    /* check if this tag is a reference to another resource */
	    if (referenceTags.keySet().contains(currentNodeName)) {
		getPropertyFromReferenceTag(manageableObject, currentNode, propName);

		return;
	    }
	    /* try to get an object from this tag */
	    getPropertyFromRegisteredTag(manageableObject, currentNode, propName);
	}
	/*
	 * if this tag is unknown check its children maybe we'll find some known tags
	 */
	else {
	    getPropertiesFromTags(manageableObject, currentNode);
	}
    }

    private void getPropertyFromRegisteredTag(ManageableObject manageableObject, Node currentNode, String propName)
	    throws Exception {
	ManageableObject nestedObject = configureNestedNode(currentNode);
	Object currentObj = nestedObject.getTarget();
	manageableObject.addNestedObject(nestedObject);

	if (currentObj != null) {
	    String idAttrValue = null;
	    String idAttributeName = attributesAsIdsForTags.get(currentNode.getNodeName());
	    /* if another attribute is specified to be used as key do it */
	    if (idAttributeName != null) {
		Node idAttr = currentNode.getAttributes().getNamedItem(idAttributeName);
		idAttrValue = idAttr.getNodeValue();
	    }
	    if(overRiddenProps.contains(propName)){
		manageableObject.overrideProperty(propName,currentObj);
	    }
	    else{
		manageableObject.setValueForProperty(propName, currentObj, idAttrValue);
	    }
	} // end if
    }

    private void getPropertyFromReferenceTag(ManageableObject manageableObject, Node currentNode, String propName)
	    throws ConfigurationException {
	String currentNodeName = currentNode.getNodeName();
	String refAttrName = referenceTags.get(currentNodeName);
	Node refAttr = currentNode.getAttributes().getNamedItem(refAttrName);
	if (refAttr == null) {
	    throw new ConfigurationException("The reference attribute with name " + refAttrName
		    + " does not exist on node " + currentNodeName);
	}
	String refName = refAttr.getNodeValue();
	/*
	 * get the value for the id attribute if specified this should be specified for Map objects
	 */
	String idAttrValue = null;
	String idAttributeName = attributesAsIdsForTags.get(currentNode.getNodeName());
	/* if another attribute is specified to be used as key do it */
	if (idAttributeName != null) {
	    Node idAttr = currentNode.getAttributes().getNamedItem(idAttributeName);
	    idAttrValue = idAttr.getNodeValue();
	}
	manageableObject.addDependency(new Dependency(refName, propName, idAttrValue));
    }

    /**
     * @return the tagsToProperties
     * @uml.property name="tagsToProperties"
     */
    public Map<String, String> getTagsToProperties() {
	return tagsToProperties;
    }

    /**
     * @param tagsToProperties
     *            the tagsToProperties to set
     * @uml.property name="tagsToProperties"
     */
    public void setTagsToProperties(Map<String, String> tagsToProperties) {
	this.tagsToProperties = tagsToProperties;
    }

    /**
     * @return the attributesToProperties
     * @uml.property name="attributesToProperties"
     */
    public Map<String, String> getAttributesToProperties() {
	return attributesToProperties;
    }

    /**
     * @param attributesToProperties
     *            the attributesToProperties to set
     * @uml.property name="attributesToProperties"
     */
    public void setAttributesToProperties(Map<String, String> attributesToProperties) {
	this.attributesToProperties = attributesToProperties;
    }

    /**
     * @return the defaultListImplementation
     * @uml.property name="defaultListImplementation"
     */
    public String getDefaultListImplementation() {
	return defaultListImplementation;
    }

    /**
     * @param defaultListImplementation
     *            the defaultListImplementation to set
     * @uml.property name="defaultListImplementation"
     */
    public void setDefaultListImplementation(String defaultListImplementation) {
	this.defaultListImplementation = defaultListImplementation;
    }

    /**
     * @return the defaultMapImplementation
     * @uml.property name="defaultMapImplementation"
     */
    public String getDefaultMapImplementation() {
	return defaultMapImplementation;
    }

    /**
     * @param defaultMapImplementation
     *            the defaultMapImplementation to set
     * @uml.property name="defaultMapImplementation"
     */
    public void setDefaultMapImplementation(String defaultMapImplementation) {
	this.defaultMapImplementation = defaultMapImplementation;
    }

    public Map<String, String> getAttributesAsIdsForTags() {
	return attributesAsIdsForTags;
    }

    public void setAttributesAsIdsForTags(Map<String, String> attributesAsIdsForTags) {
	this.attributesAsIdsForTags = attributesAsIdsForTags;
    }

    public String getIdRefAttributeName() {
	return idRefAttributeName;
    }

    public void setIdRefAttributeName(String idRefAttributeName) {
	this.idRefAttributeName = idRefAttributeName;
    }

    public List<String> getReferenceAttributes() {
	return referenceAttributes;
    }

    public void setReferenceAttributes(List<String> referenceAttributes) {
	this.referenceAttributes = referenceAttributes;
    }

    public Map<String, String> getReferenceTags() {
	return referenceTags;
    }

    public void setReferenceTags(Map<String, String> referenceTags) {
	this.referenceTags = referenceTags;
    }


    public Map<String, ConfigurationHandler> getWrappedPropertiesHandlers() {
	return wrappedPropertiesHandlers;
    }

    public void setWrappedPropertiesHandlers(Map<String, ConfigurationHandler> wrappedPropertiesHandlers) {
	this.wrappedPropertiesHandlers = wrappedPropertiesHandlers;
    }

    public List<String> getOverRiddenProps() {
        return overRiddenProps;
    }

    public void setOverRiddenProps(List<String> overRiddenProps) {
        this.overRiddenProps = overRiddenProps;
    }

}
