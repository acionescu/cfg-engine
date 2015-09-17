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
package net.segoia.cfgengine.core.configuration.loader;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class UrlConfigurationLoader implements ConfigurationLoader{
    private URL[] urls;
    private String target;
    private ClassLoader classLoader;
    
    public UrlConfigurationLoader(List<String> urls,String target){
	setUrls(urls.toArray(new String[0]));
	this.target = target;
	 
    }
    
    public UrlConfigurationLoader(String[] urls, String target){
	setUrls(urls);
	this.target = target;
    }
    
    public String getTargetToLoad() {
	// TODO Auto-generated method stub
	return null;
    }

    public InputStream load() throws Exception {
	return classLoader.getResourceAsStream(target);
    }

    private void setUrls(String[] urlsString){
	urls = new URL[urlsString.length];
	int i = 0;
	for(String u : urlsString){
	    try {
		urls[i] = new URL(u);
	    } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	classLoader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());
    }

    /**
     * @return the classLoader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
}
