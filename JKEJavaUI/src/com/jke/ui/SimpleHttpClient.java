/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.ui;

import com.jke.beans.ContributionBean;
import com.jke.beans.TransactionBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class SimpleHttpClient {
	private String baseUrl;

	public SimpleHttpClient() {
		this("http://localhost:8080/");
	}

	public SimpleHttpClient(String baseUrl) {
		if (!baseUrl.endsWith("/")) {
			baseUrl+= "/";
		}
		this.baseUrl= baseUrl;
	}

	public String request(String service) throws IOException {
		return request(service, null);
	}

	public String request(String service, Map<String, String> requestParams) throws IOException {
		service= buildParams(service, requestParams);
		URL url= new URL(baseUrl + service);
		URLConnection conn= url.openConnection();
		conn.setReadTimeout(10000);
		conn.connect();
		BufferedReader rd= new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb= new StringBuffer();
		String line;
		while ((line= rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		return sb.toString();
	}

	public String post(String service) throws IOException {
		return post(service, null);
	}

	public String post(String service, Map<String, String> requestParams) throws IOException {
		service= buildParams(service, requestParams);
		URL url= new URL(baseUrl + service);
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		BufferedReader rd= new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb= new StringBuffer();
		String line;
		while ((line= rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		return sb.toString();
	}

	private String encode(String key, String val) throws UnsupportedEncodingException {
		return URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
	}

	public String getUserResource(String userId) {
		return "user/" + userId;
	}

	public String getUserAccounts(String userId) {
		return "user/" + userId + "/accounts";
	}

	public String getOrganizations() {
		return "organizations";
	}

	public String getTransactionsForAccount(String userId, String accountName) {
		return "transactions/" + userId + "/" + accountName;
	}

	public TransactionBean getTransactionPreview(ContributionBean contribution) {
		String transaction;
		TransactionBean bean= new TransactionBean();
		try {
			transaction= request(getDonation(contribution, "preview"));
			bean.deserializeFromJson(new StringReader(transaction));
			return bean;
		} catch (IOException e) {
			e.printStackTrace();
			return bean;
		}
	}

	public TransactionBean postTransaction(ContributionBean contribution) {
		String transaction;
		TransactionBean bean= new TransactionBean();
		try {
			transaction= post(getDonation(contribution, "create"));
			bean.deserializeFromJson(new StringReader(transaction));
			return bean;
		} catch (IOException e) {
			e.printStackTrace();
			return bean;
		}
	}

	public String getDonation(ContributionBean contribution, String action) {
		return "transactions/" + action + "?" + "account=" + contribution.getAccountNumber() + "&" + "org=" + URLEncoder.encode(contribution.getOrganization()) + "&" + "date=" + URLEncoder.encode(contribution.getDate()) + "&" + "percent=" + contribution.getPercentage();
	}

	public String getSubmitDonation() {
		return "";
	}

	private String buildParams(String service, Map<String, String> requestParams) throws UnsupportedEncodingException {
		if (requestParams != null) {
			StringBuffer param= new StringBuffer();
			for (String key : requestParams.keySet()) {
				if (param.length() != 0)
					param.append("&");
				param.append(encode(key, requestParams.get(key)));
			}
			if (param.length() > 0) {
				service+= "?" + param.toString();
			}
		}
		return service;
	}
}