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
 * $Id: DirConfigurationHandler.java,v 1.3 2007/10/19 12:52:43 aionescu Exp $
 */
package net.segoia.cfgengine.core.configuration.handlers;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.List;

import net.segoia.cfgengine.core.configuration.Attribute;
import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.core.configuration.ManageableObject;
import net.segoia.util.resources.ResourcesUtil;

import org.w3c.dom.Node;

/**
 * Loads configuration files from a specified directory
 * 
 * @author aionescu
 * @version $Revision: 1.3 $
 */
public class DirConfigurationHandler extends BaseConfigurationHandler {
    private ConfigurationManager cfgManager;

    public DirConfigurationHandler(ConfigurationManager manager) {
	cfgManager = manager;
    }

    /**
     * Tag should look like <dir filter="[the regex]">the url to the directory</dir> It's not necesary to specify the
     * filter attribute, in which case a default one would be used loading only the xml files
     */
    public ManageableObject configure(Node node) throws Exception {
	String nodeValue = node.getFirstChild().getNodeValue();
	if (nodeValue != null && !"".equals(nodeValue.trim())) {

	    // String dirPath = cfgManager.getResourcesLoader().getResource(nodeValue).getPath();
	    // File dir = new File(dirPath);
	    URL urlPath = cfgManager.getResourcesLoader().getResource(nodeValue);

	    // if (dir.exists() && dir.isDirectory()) {
	    if (urlPath != null) {
		Node filterAttr = node.getAttributes().getNamedItem(Attribute.FILTER);
		String filterRegex = ".*\\.xml"; // select only xml files
		boolean recursive = false;
		if (filterAttr != null) {
		    filterRegex = filterAttr.getNodeValue();

		}

		Node recursiveAttr = node.getAttributes().getNamedItem(Attribute.RECURSIVE);
		if (recursiveAttr != null) {
		    recursive = new Boolean(recursiveAttr.getNodeValue());
		}
		final String filter = filterRegex;
		FilenameFilter fileFilter = new FilenameFilter() {

		    public boolean accept(File directory, String fileName) {
			if (fileName.matches(filter)) {
			    return true;
			}
			return false;
		    }

		};
		// loadFiles(dir,fileFilter,recursive);
		loadFiles(nodeValue, recursive);
	    }
	}
	return null;
    }

    private void loadFiles(File dir, FilenameFilter filter, boolean recursive) {
	File[] fileCandidates = dir.listFiles(filter);
	for (File f : fileCandidates) {
	    // ConfigurationLoader loader = new PackageConfigurationLoader(f.getPath());
	    // cfgManager.addLoader(loader);
	    cfgManager.addResourceToLoad(f.getPath());
	}
	if (recursive) {
	    File[] children = dir.listFiles();
	    for (File f : children) {
		if (f.isDirectory()) {
		    loadFiles(f, filter, recursive);
		}
	    }
	}
    }

    private void loadFiles(String path, boolean recursive) throws Exception {
	if (path.endsWith(File.separator)) {
	    /* this is a directory */
	    URL url = cfgManager.getResourcesLoader().getResource(path);
	    if (url != null) {
		List<String> files = ResourcesUtil.listFiles(url);
		if (files != null) {
		    for (String f : files) {
			String newf = path + File.separator + f;
			String sep = File.separator;
			newf = newf.replace(sep + sep, sep);
			URL newfUrl = cfgManager.getResourcesLoader().getResource(newf);
			boolean isFile = false;
			if (newfUrl != null) {
			    if (newfUrl.getProtocol().equals("file")) {
				File ff = new File(newfUrl.toURI());
				isFile = ff.isFile();
			    } else {
				isFile = true;
			    }
			}
			if (isFile) {
			    cfgManager.addResourceToLoad(newf);
			} else {
			    /* it's a directory */
			    newf += File.separator;
			    loadFiles(newf, recursive);
			}
		    }
		}
	    }
	}
    }

}
