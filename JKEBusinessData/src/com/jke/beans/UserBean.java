/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.beans;

import com.ibm.team.json.JSONObject;

import java.io.IOException;
import java.io.Reader;

public class UserBean {
	private String userName;
	private String firstName;
	private String lastName;

	public UserBean() {
		//Default Constructor
	}

	public UserBean(String uname, String fname, String lname) {
		this.userName= uname;
		this.firstName= fname;
		this.lastName= lname;
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setUserName(String userName) {
		this.userName= userName;
	}

	public void setFirstName(String firstName) {
		this.firstName= firstName;
	}

	public void setLastName(String lastName) {
		this.lastName= lastName;
	}

	public JSONObject toJson() {
		JSONObject userObj= new JSONObject();
		userObj.put("first", getFirstName());
		userObj.put("last", getLastName());
		userObj.put("userId", getUserName());
		return userObj;
	}

	public void deserializeFromJson(Reader r) throws IOException {
		JSONObject userObj= JSONObject.parse(r);
		setFirstName((String) userObj.get("first"));
		setLastName((String) userObj.get("last"));
		setUserName((String) userObj.get("userId"));
	}
}