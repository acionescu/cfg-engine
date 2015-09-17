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
 * $Id: BaseConfigurationHandler.java,v 1.7 2007/09/07 11:02:07 aionescu Exp $
 */
package net.segoia.cfgengine.core.configuration.handlers;

import java.lang.reflect.Field;
import java.util.Map;

import net.segoia.cfgengine.core.configuration.Attribute;
import net.segoia.cfgengine.core.configuration.ManageableObject;
import net.segoia.cfgengine.core.configuration.Property;
import net.segoia.cfgengine.core.configuration.Tag;
import net.segoia.cfgengine.core.configuration.listener.DependencyListener;
import net.segoia.cfgengine.core.configuration.listener.PropertyDependencyListener;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.util.data.reflection.ReflectionUtility;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Base implementation for a{@link ConfigurationHandler} This can be registered as a configuration handler for any tag
 * that defines a basic configuration Constraints : The tag must have at least an <b>id</b> attribute and a <b>class</b>
 * attribute specifing the full name of the java class which will be used to create an object It can also define a
 * <b>property</b> tag Example: <property name="name" value="value"/> or <property name="name"> <value>any other
 * registered tag</value> </property>
 * 
 * @author aionescu
 * @version $Revision: 1.7 $
 */
public class BaseConfigurationHandler extends AbstractConfigurationHandler {
    /**
     * the default class name for the objects that will be created
     */
    private String defaultClassName;

    /**
     * instructs the handler to always use default class name if specified, ignoring the class attribute
     */
    private boolean alwaysUseDefaultClass;

    /**
     * The default init method to be called for this type of object
     */
    private String defaultInitMethod;

    /**
     * The default destroy method to be called from this type of object
     */
    private String defaultDestroyMethod;

    private String resourcesLoaderPropertyName;
    
    public BaseConfigurationHandler() {

    }

    protected ManageableObject getManageableObjectSkeleton(Node node) throws Exception {
	NamedNodeMap attributes = node.getAttributes();
	String className = defaultClassName;
	String id = null;

	if (attributes != null) {
	    Node idAttr = attributes.getNamedItem(Attribute.ID);
	    if (idAttr != null) {
		id = idAttr.getNodeValue();
	    }
	    /*
	     * tries to use the class attribute only if no default class defined or the overriding of the default class
	     * is allowed
	     */
	    if (className == null || !alwaysUseDefaultClass) {
		Node classAttr = attributes.getNamedItem(Attribute.CLASS);
		if (classAttr != null) {
		    className = classAttr.getNodeValue();
		}
	    }
	}

	if (hasIdAttribute() && id == null) {
	    throw new ConfigurationException("An 'id' attribute must be specified for a node of type "
		    + node.getNodeName());
	}

	Object object = Class.forName(className).newInstance(); // creates an
	// object from
	// that class

	ManageableObject manageableObject = new ManageableObject(object);
	manageableObject.setId(id);
	manageableObject.setTagName(node.getNodeName());

	/* set the init method */
	Node initMethodAttr = attributes.getNamedItem(Attribute.INIT_METHOD);
	if (initMethodAttr != null) {
	    manageableObject.setInitMethodName(initMethodAttr.getNodeValue());
	}

	Node destroyMethodAttr = attributes.getNamedItem(Attribute.DESTROY_METHOD);
	if (destroyMethodAttr != null) {
	    manageableObject.setDestroyMethodName(destroyMethodAttr.getNodeValue());
	}

	/* set default init and destroy methods if specified */
	if (defaultInitMethod != null) {
	    manageableObject.setInitMethodName(defaultInitMethod);
	}
	if (defaultDestroyMethod != null) {
	    manageableObject.setDestroyMethodName(defaultDestroyMethod);
	}

	if (id != null) {
	    try { // try to set the id on this object
		// ReflectionUtility.setValueToField(object, Attribute.ID, id);
		manageableObject.setValueForProperty(Attribute.ID, manageableObject.getId());
	    } catch (Exception e) {
		// empty
	    }
	}
	
	/* set resource loader property name */
	if(resourcesLoaderPropertyName != null){
	    manageableObject.setResourceLoaderPropertyName(resourcesLoaderPropertyName);
	}

	return manageableObject;
    }

    /**
     * Configures a basic object injecting all the declared properties with values defined in the configuration file
     * 
     */
    public ManageableObject configure(Node node) throws Exception {
	// NamedNodeMap attributes = node.getAttributes();
	// String className = defaultClassName;
	// String id = null;
	//
	// if (attributes != null) {
	// Node idAttr = attributes.getNamedItem(Attribute.ID);
	// if (idAttr != null) {
	// id = idAttr.getNodeValue();
	// }
	// /*
	// * tries to use the class attribute only if no default class defined or
	// * the overriding of the default class is allowed
	// */
	// if (className == null || !alwaysUseDefaultClass) {
	// Node classAttr = node.getAttributes().getNamedItem(Attribute.CLASS);
	// if (classAttr != null) {
	// className = classAttr.getNodeValue();
	// }
	// }
	// }
	//
	// if (hasIdAttribute && id == null) {
	// throw new ConfigurationException("An 'id' attribute must be specified for a node of type "
	// + node.getNodeName());
	// }
	//
	// Object object = Class.forName(className).newInstance(); // creates an
	// // object from
	// // that class
	//
	// ManageableObject manageableObject = new ManageableObject(object);
	// manageableObject.setId(id);
	// manageableObject.setTagName(node.getNodeName());
	//
	// /* set the init method */
	// Node initMethodAttr = attributes.getNamedItem(Attribute.INIT_METHOD);
	// if (initMethodAttr != null) {
	// manageableObject.setInitMethodName(initMethodAttr.getNodeValue());
	// }
	//
	// Node destroyMethodAttr = attributes.getNamedItem(Attribute.DESTROY_METHOD);
	// if (destroyMethodAttr != null) {
	// manageableObject.setDestroyMethodName(destroyMethodAttr.getNodeValue());
	// }

	ManageableObject manageableObject = getManageableObjectSkeleton(node);
	addProperies(manageableObject, node);

	return configure(manageableObject, node);

    }
    
    protected void addProperies(ManageableObject manageableObject, Node node) throws Exception {
	NodeList nodes = node.getChildNodes();
	for (int i = 0; i < nodes.getLength(); i++) { // iterates through the
	    // child nodes
	    Node currentNode = nodes.item(i);
	    if (currentNode.getNodeName().equals(Tag.PROPERTY)) { // current
		// node is a
		// property
		// node

		ManageableObject nestedProperty = configureNestedNode(currentNode);
		Property property = (Property) nestedProperty.getTarget(); // creates a property object

		manageableObject.addNestedObject(nestedProperty); // from the current property;
		// node
		if (property.getValue() != null) {
		    ReflectionUtility.setValueToField(manageableObject.getTarget(), property.getName(), property
			    .getValue());
		    // handleValueSetting(manageableObject.getTarget(), property);
		} else if (property.getValueRef() != null) {// resolving dependency
		    DependencyListener depListener = new PropertyDependencyListener(manageableObject.getTarget(),
			    property.getName());
		    manageableObject.addDependency(property.getValueRef(), depListener);
		    // manageableObject.addDependency(new Dependency(property.getValueRef(), property.getName()));
		}
	    }
	}
    }

    /*
     * tried to use the map implementation specified in the class but it's a problem because of the dependency
     * mechanisms. The original map is being populated and the new one remains empty.
     */
    // private void handleValueSetting(ManageableObject m, Property prop) throws Exception {
    // Object target = m.getTarget();
    // Field f = ReflectionUtility.getFieldForFieldName(target.getClass(), prop.getName());
    // if (ReflectionUtility.checkInstanceOf(f.getType(), Map.class)) {
    // Map fieldValue = (Map) ReflectionUtility.getValueForField(target, prop.getName());
    // if (fieldValue == null && !f.getType().isInterface()) {
    // fieldValue = (Map) f.getType().newInstance();
    // }
    // if (fieldValue != null) {
    // fieldValue.putAll((Map) prop.getValue());
    //		
    // return;
    // }
    // }
    // ReflectionUtility.setValueToField(target, prop.getName(), prop.getValue());
    // }
    protected ManageableObject configure(ManageableObject manageableObject, Node node) throws Exception {
	return manageableObject; // returns the created object
    }

    /**
     * @return the defaultClassName
     * @uml.property name="defaultClassName"
     */
    public String getDefaultClassName() {
	return defaultClassName;
    }

    /**
     * @param defaultClassName
     *            the defaultClassName to set
     * @uml.property name="defaultClassName"
     */
    public void setDefaultClassName(String defaultClassName) {
	this.defaultClassName = defaultClassName;
    }

    public void setAlwaysUseDefaultClass(boolean alwaysUseDefaultClass) {
	this.alwaysUseDefaultClass = alwaysUseDefaultClass;
    }

    public String getDefaultInitMethod() {
	return defaultInitMethod;
    }

    public String getDefaultDestroyMethod() {
	return defaultDestroyMethod;
    }

    public void setDefaultInitMethod(String defaultInitMethod) {
	this.defaultInitMethod = defaultInitMethod;
    }

    public void setDefaultDestroyMethod(String defaultDestroyMethod) {
	this.defaultDestroyMethod = defaultDestroyMethod;
    }

    /**
     * @return the resourcesLoaderPropertyName
     */
    public String getResourcesLoaderPropertyName() {
        return resourcesLoaderPropertyName;
    }

    /**
     * @param resourcesLoaderPropertyName the resourcesLoaderPropertyName to set
     */
    public void setResourcesLoaderPropertyName(String resourcesLoaderPropertyName) {
        this.resourcesLoaderPropertyName = resourcesLoaderPropertyName;
    }
    
}
