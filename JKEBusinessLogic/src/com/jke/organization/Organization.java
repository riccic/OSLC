/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.organization;

public abstract class Organization {
	private String fName;
	private String fWebPage;

	protected Organization(String name, String webPage) {
		fName= name;
		fWebPage= webPage;
	}

	public String getName() {
		return fName;
	}

	public String getWebPage() {
		return fWebPage;
	}
}