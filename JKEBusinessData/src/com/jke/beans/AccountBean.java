/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010, 2011. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.beans;

import com.ibm.team.json.JSONObject;

import java.io.IOException;
import java.io.Reader;

public class AccountBean {
	private int accountNumber;
	private String userName;
	private String type;
	private double balance;
	private double dividends;
	private double dividendsETD;
	private double contributions;
	private double contributionsETD;

	public AccountBean() {
		//Default Constructor
	}

	public AccountBean(int num, double b, double d, double e, double f, double g, String userName, String type) {
		this.accountNumber= num;
		this.balance= b;
		this.dividends= d;
		this.dividendsETD= e;
		this.contributions= f;
		this.contributionsETD= g;
		this.userName= userName;
		this.type= type;
	}

	public void setBalance(double balance) {
		this.balance= balance;
	}

	public void setDividends(double dividends) {
		this.dividends= dividends;
	}

	public void setDividendsETD(double dividendsETD) {
		this.dividendsETD= dividendsETD;
	}

	public void setContributions(double contributions) {
		this.contributions= contributions;
	}

	public void setContributionsETD(double contributionsETD) {
		this.contributionsETD= contributionsETD;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public String getUserName() {
		return userName;
	}

	public String getType() {
		return type;
	}

	public AccountTypeBean getTypeBean() {
		return AccountTypeBean.valueOf(type);
	}

	public double getBalance() {
		return balance;
	}

	public double getContributions() {
		return contributions;
	}

	public double getContributionsETD() {
		return contributionsETD;
	}

	public double getDividends() {
		return dividends;
	}

	public double getDividendsETD() {
		return dividendsETD;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber= accountNumber;
	}

	public void setUserName(String userName) {
		this.userName= userName;
	}

	public void setType(String type) {
		this.type= type;
	}

	public JSONObject toJson() {
		JSONObject accountObj= new JSONObject();
		accountObj.put("accountNumber", new Long(getAccountNumber()));
		accountObj.put("userName", getUserName());
		accountObj.put("type", getType());
		accountObj.put("typeName", getTypeBean().getName());
		accountObj.put("balance", getBalance());
		accountObj.put("dividends", getDividends());
		accountObj.put("dividendsETD", getDividendsETD());
		accountObj.put("contributions", getContributions());
		accountObj.put("contributionsETD", getContributionsETD());
		return accountObj;
	}

	public void deserializeFromJson(Reader r) throws IOException {
		JSONObject from= JSONObject.parse(r);
		fromJson(from);
	}

	public void fromJson(JSONObject from) {
		Long number= (Long) from.get("accountNumber");
		setAccountNumber(number.intValue());
		setUserName((String) from.get("userName"));
		setType((String) from.get("type"));
		setBalance((Double) from.get("balance"));
		setDividends((Double) from.get("dividends"));
		setDividendsETD((Double) from.get("dividendsETD"));
		setContributions((Double) from.get("contributions"));
		setContributionsETD((Double) from.get("contributionsETD"));
	}
}