/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.db.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The base implementation creates an instance of the simple JKE_DB_C connection
 * manager. Subclasses may override to create a different manager. One, for
 * example, that uses J2EE connection pooling. An application that uses a
 * subclass must make sure the factory is initialized before a connection is
 * requested. This implementation gets the connection parameters from the
 * JKEDB.properties file found in the same package as this factory.
 */
public class JKE_DB_Factory {
	private static JKE_DB_Factory fgFactory;
	private JKE_DB_I fDB;

	/**
	 * A subclass static getFactory method can use this method to set the factory
	 * its custom factory instance in here in addition to holding its own
	 * reference for use in overridden methods.
	 * 
	 * @param factory
	 * @return
	 */
	public static synchronized JKE_DB_Factory initFactory(JKE_DB_Factory factory) {
		if (factory == null)
			fgFactory= new JKE_DB_Factory();
		else
			fgFactory= factory;
		return fgFactory;
	}

	/**
	 * Returns the factory singleton. Creates an instance of this class if needed.
	 * 
	 * @return
	 */
	public static synchronized JKE_DB_Factory getFactory() {
		if (fgFactory == null)
			initFactory(null);
		return fgFactory;
	}

	/**
	 * Only the factory initialization can create a factory instance, but
	 * subclasses can use this constructor.
	 */
	protected JKE_DB_Factory() {
		super();
	}

	/**
	 * Returns the application's JKE DB singleton.
	 * 
	 * @return the JKE database manager
	 */
	public JKE_DB_I getDB() {
		if (fDB == null) {
			fDB= createJKE_DB();
		}
		return fDB;
	}

	/**
	 * Subclasses should override.
	 * 
	 * @return a new instance of the database manager
	 * @throws IOException if the properties file read fails
	 */
	protected JKE_DB_I createJKE_DB() {
		Properties props= new Properties();
		InputStream is= this.getClass().getResourceAsStream("JKEDB.properties");
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new JKE_DB_C(props);
	}
}