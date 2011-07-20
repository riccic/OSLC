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
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionBean {
	private int transactionID;
	private String transactionType;
	private int accountNumber;
	private String source;
	private double amount;
	private double postBalance;
	private String date;

	public TransactionBean() {
		//Default Constructor
	}

	public TransactionBean(int transactionID, String transactionType, int accountNumber, String source, double amount, double postBalance, String date) {
		super();
		this.transactionID= transactionID;
		this.transactionType= transactionType;
		this.accountNumber= accountNumber;
		this.source= source;
		this.amount= amount;
		this.postBalance= postBalance;
		this.date= date;
	}

	public TransactionBean(String transactionType, int accountNumber, String source, double amount, double postBalance, String date) {
		super();
		this.transactionType= transactionType;
		this.accountNumber= accountNumber;
		this.source= source;
		this.amount= amount;
		this.postBalance= postBalance;
		this.date= date;
	}

	public TransactionBean(AccountBean account, String transType, String source, double amt, double post) {
		this.accountNumber= account.getAccountNumber();
		this.transactionType= transType;
		this.source= source;
		this.amount= amt;
		this.postBalance= post;
		this.date= new SimpleDateFormat("MM/dd/yyyy").format(new Date());
	}

	public int getTransactionID() {
		return transactionID;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public String getSource() {
		return source;
	}

	public double getAmount() {
		return amount;
	}

	public String getDate() {
		return date;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID= transactionID;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType= transactionType;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber= accountNumber;
	}

	public void setSource(String source) {
		this.source= source;
	}

	public void setAmount(double amount) {
		this.amount= amount;
	}

	public void setDate(String date) {
		this.date= date;
	}

	public void setPostBalance(double postBalance) {
		this.postBalance= postBalance;
	}

	public double getPostBalance() {
		return postBalance;
	}

	public JSONObject toJson() {
		JSONObject to= new JSONObject();
		to.put("accountNumber", new Long(getAccountNumber()));
		to.put("amount", getAmount());
		to.put("date", getDate());
		to.put("balance", getPostBalance());
		to.put("source", getSource());
		to.put("id", new Long(getTransactionID()));
		to.put("type", getTransactionType());
		return to;
	}

	public void deserializeFromJson(Reader r) throws IOException {
		JSONObject from= JSONObject.parse(r);
		fromJson(from);
	}

	public void fromJson(JSONObject from) {
		Long l= (Long) from.get("accountNumber");
		setAccountNumber(l.intValue());
		setAmount((Double) from.get("amount"));
		setDate((String) from.get("date"));
		setPostBalance((Double) from.get("balance"));
		l= (Long) from.get("id");
		setTransactionID(l.intValue());
		setTransactionType((String) from.get("type"));
		setSource((String) from.get("source"));
	}
}