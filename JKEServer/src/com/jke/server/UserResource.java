/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2009, 2010. All Rights Reserved.
 * 
 * Note to U.S. Government Users Restricted Rights:  Use,
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.server;

import com.ibm.team.json.JSONArray;
import com.jke.beans.AccountBean;
import com.jke.beans.UserBean;
import com.jke.logic.AccountLogic;
import com.jke.logic.JKE_Util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserResource extends HttpServlet {
	private static final long serialVersionUID= 1L;

	public UserResource() {}

	/**
	 * GET ../user/<userid> returns if the user is known, 404 (NOT_FOUND)
	 * otherwise GET ../user/<userid>/accounts returns the accounts known for this
	 * user, or 404 (NOT_FOUND) if the user is not known
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);

		List<String> paths= new ArrayList<String>();
		StringTokenizer st= new StringTokenizer(request.getRequestURI(), "/", false);
		while (st.hasMoreTokens())
			paths.add(st.nextToken());

		String userId= getString(paths, 1);
		String action= getString(paths, 2);

		if (paths.isEmpty() || userId == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			PrintWriter out= new PrintWriter(response.getOutputStream());
			out.println("Missing the user id in the request url");
			out.close();
			return;
		}

		UserBean user= new UserBean(userId, "", "");

		if (action == null) {
			user= JKE_Util.login(userId, null);
			if (user == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				PrintWriter out= new PrintWriter(response.getOutputStream());
				out.println("Could not find the specified user");
				out.close();
				return;
			}
			user.toJson().serialize(response.getWriter());
		} else if (action.equals("accounts")) {
			AccountLogic logic= new AccountLogic();
			List<AccountBean> accounts= logic.getUserAccounts(user);
			JSONArray accountsObj= new JSONArray();
			for (AccountBean accountBean : accounts) {
				accountsObj.add(accountBean.toJson());
			}
			accountsObj.serialize(response.getWriter());
		}
	}

	private String getString(List<String> strings, int index) {
		try {
			return strings.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}