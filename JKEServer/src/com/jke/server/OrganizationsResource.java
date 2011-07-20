/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.server;

import com.ibm.team.json.JSONArray;
import com.ibm.team.json.JSONObject;
import com.jke.logic.OrganizationLogic;
import com.jke.organization.Organization;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrganizationsResource extends HttpServlet {
	private static final long serialVersionUID= 1L;

	public OrganizationsResource() {}

	/**
	 * GET ../organizations returns the list of known organizations
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);

		OrganizationLogic logic= new OrganizationLogic();
		Map<String, Organization> names= logic.asMap();
		JSONArray orgs= new JSONArray();
		for (Organization name : names.values()) {
			JSONObject org= new JSONObject();
			org.put("name", name.getName());
			org.put("website", name.getWebPage());
			orgs.add(org);
		}
		orgs.serialize(response.getWriter());
	}
}