/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.server;

import org.mortbay.util.UrlEncoded;

import com.jke.db.data.GenerateData;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The admin resource which provides access to administrative function and
 * helpers available on this server.
 */
public class AdminResource extends HttpServlet {
	private static final long serialVersionUID= 1L;

	public AdminResource() {}

	/**
	 * GET ../admin shows an html page to help administer the server GET
	 * ../admin/reset resets the database to it's initial state, nothing is
	 * returned to the client
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		List<String> paths= new ArrayList<String>();
		StringTokenizer st= new StringTokenizer(request.getRequestURI(), "/", false);
		while (st.hasMoreTokens())
			paths.add(st.nextToken());

		String action= getString(paths, 1);

		if (action == null) {
			String prefix= request.getServerName() + ":" + request.getLocalPort();
			PrintWriter out= new PrintWriter(response.getOutputStream());
			out.print("<html><head><STYLE type='text/css'>" + "h1,h2,h3,p {" + "	font-family: Verdana, Arial, Helvetica, sans-serif; }" + "table" + "	{ width: 100%;" + "	background-color: #fafafa;" + "	border: 1px #000000 solid;" + "	border-collapse: collapse;" + "	border-spacing: 0px; }" +

			"td.header" + "	{ background-color: #99CCCC;" + "	border: 1px #000000 solid;" + "	font-family: Verdana;" + "	font-weight: bold;" + "	font-size: 12px;" + "	color: #404040; }" +

			"td" + "	{ border-bottom: 1px #6699CC dotted;" + "	text-align: left;" + "	font-family: Verdana, sans-serif, Arial;" + "	font-weight: normal;" + "	font-size: .7em;" + "	color: #404040;" + "	background-color: #fafafa;" + "	padding-top: 4px;" + "	padding-bottom: 4px;" + "	padding-left: 8px;" + "	padding-right: 0px; }" + "</STYLE></head><body>");
			out.println("<h1>Money that Matters Administration</h1><p>This is the administrative page for the sample.</p><p>If you want to re-run the demo with the initial data you can " + getReferenceURL(prefix, "/admin/reset", "reset the sample data") + "</p>");
			out.println("<p>The sample server provides a set of RESTful services that can be accessed by different clients and others are welcome to write their own clients based on these services. You can click on each below to see the returned data</p>");
			out.println("<table width='100%' cellspacing='0'><tr><td class='header' colspan='2'>Services</td></tr>");
			out.println("<tr><td width='40%'>" + getReferenceURL(prefix, "/user/jbrown") + "</td><td width='60%'>Call this to verify that the provided user exists. An error response of 404 (NOT FOUND) is returned if the user is not known.</td></tr>");
			out.println("<tr><td width='40%'>" + getReferenceURL(prefix, "/user/jbrown/accounts") + "</td><td width='60%'>Call this to fetch the accounts for the provided user.</td></tr>");
			out.println("<tr><td width='40%'>" + getReferenceURL(prefix, "/organizations") + "</td><td width='60%'>Call this to fetch the known organizations that can accept dividend deposits</td></tr>");
			out.println("<tr><td width='40%'>" + getReferenceURL(prefix, "/transactions/jbrown/Checking") + "</td><td width='60%'>Call this to fetch the transaction history of the provided account.</td></tr>");
			out.println("<tr><td width='40%'>" + getReferenceURL(prefix, "/transactions/preview?account=200&org=Red%20Cross&date=" + UrlEncoded.encodeString(new Date(System.currentTimeMillis()).toGMTString()) + "&percent=0.10") + "</td><td width='60%'>Call this to preview a dividend deposit.</td></tr>");
			out.println("<tr><td width='40%'>" + "<form action='http://" + prefix + "/transactions/create?account=200&org=Red%20Cross&date=" + UrlEncoded.encodeString(new Date(System.currentTimeMillis()).toGMTString()) + "&percent=0.10" + "' method='POST'>" + "<input type='submit' value='Submit'/></form></td><td width='60%'>Post to this URL to submit dividend deposit.</td></tr>");
			out.println("</table>");
			out.println("</body></html>");
			out.close();
		} else if (action.equals("reset")) {
			GenerateData.resetDatabaseToInitialState();
			PrintWriter out= new PrintWriter(response.getOutputStream());
			out.println("<html><body><p>Database reset!</p></body></html>");
			out.close();
		}
	}

	private String getReferenceURL(String prefix, String url) {
		return getReferenceURL(prefix, url, null);
	}

	private String getReferenceURL(String prefix, String url, String label) {
		if (label == null)
			label= "http://" + prefix + url;
		return "<a href='http://" + prefix + url + "'>" + label + "</a><br/>";
	}

	private String getString(List<String> strings, int index) {
		try {
			return strings.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}