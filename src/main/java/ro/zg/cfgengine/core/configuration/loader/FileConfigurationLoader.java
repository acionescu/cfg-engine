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
package ro.zg.cfgengine.core.configuration.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class FileConfigurationLoader implements ConfigurationLoader {
    private String targetToLoad;

    public FileConfigurationLoader(String targetToLoad) {
	this.targetToLoad = targetToLoad;
    }

    public InputStream load() throws Exception {
	InputStream in = new FileInputStream(new File(targetToLoad));
	return in;
    }

    public String getTargetToLoad() {
	return targetToLoad;
    }

    public static void main(String[] args) throws Exception{
	URL u = new URL("file:///home");
    }

}
