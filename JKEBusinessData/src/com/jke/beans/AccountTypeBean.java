/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010, 2011. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.beans;

public enum AccountTypeBean {
	Checking("Checking Account"), 
	Savings("Savings Account"), 
	IRA("IRA Retirement Account"), 
	Money_Market("Money Market Savings Account");

	private String fName;

	AccountTypeBean(String name) {
		fName= name;
	}

	public String getName() {
		return fName;
	};
}