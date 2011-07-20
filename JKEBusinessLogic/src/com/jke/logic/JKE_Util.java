/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.logic;

import com.jke.beans.UserBean;
import com.jke.db.connection.JKE_DB_Factory;
import com.jke.db.data.BeanLoader;

/**
 * The Class JKE_Util provides simple utilities.
 */
public class JKE_Util {

	/**
	 * Shutdown DB Factory.
	 */
	public static void shutdown() {
		JKE_DB_Factory.getFactory().getDB().shutdown();
	}

	/**
	 * Login.
	 * 
	 * @param u the username
	 * @param p the password
	 * @return the user bean
	 */
	public static UserBean login(String u, String p) {
		BeanLoader loader= new BeanLoader();
		return loader.pullUserBean(u, p);
	}
}