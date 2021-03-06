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
 * $Id: ConfigurationException.java,v 1.2 2007/06/18 07:55:11 victor Exp $
 */
package net.segoia.cfgengine.core.exceptions;

/**
 * @author Victor Manea
 * @version $Revision: 1.2 $
 *
 */
public class ConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3764298018607496189L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(Throwable e) {
		super(e);
	}

	public ConfigurationException(String ex) {
		super(ex);
	}

	public ConfigurationException(String message, Throwable e) {
		super(message, e);
	}

}
