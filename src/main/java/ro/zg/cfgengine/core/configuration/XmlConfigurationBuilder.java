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

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ro.zg.cfgengine.core.configuration.handlers.ConfigurationHandler;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;
import ro.zg.util.xml.resources.ClasspathEntityResolver;

public class XmlConfigurationBuilder implements ConfigurationBuilder {
    private static Logger logger = MasterLogManager.getLogger(XmlConfigurationBuilder.class.getName());
    private DocumentBuilder parser;
    private ConfigurationHandler rootConfigurationHandler;
    private ClassLoader classLoader;

    public XmlConfigurationBuilder(ConfigurationHandler cfgHandler, ClassLoader cl) {
	rootConfigurationHandler = cfgHandler;
	this.classLoader = cl;
	init();
    }

    private void init() {
	// initialize the dom parser
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	factory.setValidating(true);
	factory.setIgnoringComments(true);
	factory.setIgnoringElementContentWhitespace(true);
	try {
	    parser = factory.newDocumentBuilder();
	    parser.setEntityResolver(new ClasspathEntityResolver(classLoader));
	    parser.setErrorHandler(new ErrorHandler() {

		public void error(SAXParseException exception) throws SAXException {
		    System.err.println(exception);
		    
		}

		public void fatalError(SAXParseException exception) throws SAXException {
		    System.err.println(exception);
		    
		}

		public void warning(SAXParseException exception) throws SAXException {
		    System.err.println(exception);
		}
		
	    });
	} catch (ParserConfigurationException e) {
	    logger.error("Could not instantiate a DOM parser", e);
	}

    }

    public List<ManageableObject> configure(InputStream inputStream) throws ConfigurationException {
	Document document;
	if(inputStream == null) {
	    throw new ConfigurationException("Input stream cannot be null!");
	}
	try {
	    document = parser.parse(inputStream);
	    return configure(document.getChildNodes());
	} catch (Exception e) {
	    throw new ConfigurationException("Error loading configuration from input stream ", e);
	}
    }

    private List<ManageableObject> configure(NodeList nodesList) throws ConfigurationException {
	List<ManageableObject> createdObjects = new Vector<ManageableObject>();
	for (int i = 0; i < nodesList.getLength(); i++) {
	    Node node = nodesList.item(i);
	    String nodeName = node.getNodeName();

	    ConfigurationHandler cfgHandler = rootConfigurationHandler.getLocalConfigurationHandlerForName(nodeName);
	    if (cfgHandler != null) {
		ManageableObject newManageableObject;
		try {
		    newManageableObject = cfgHandler.configure(node);
		} catch (Exception e) {
		    throw new ConfigurationException("Could not create object from node " + node.getNodeName(), e);
		}

		if (newManageableObject != null) {
		    createdObjects.add(newManageableObject);
		}
	    } else if (node.getChildNodes() != null && node.getChildNodes().getLength() > 0) {
		/*
		 * if no configuration handler found, processes the child nodes
		 */
		createdObjects.addAll(configure(node.getChildNodes()));
	    }
	} // end for

	return createdObjects;
    }

}
