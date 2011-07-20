/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.server;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.jke.logic.JKE_Util;

import java.io.File;

/**
 * This is a standalone server that is run using the jetty libraries. Other apps
 * can call this to embedded the jetty server into their app, which can make
 * things simpler when showing simple examples .
 */
public class JKEServer {

	/**
	 * Location of html and js files for web ui, relative to the application jar
	 * (jke.jar) If this folder doesn't exist, server falls back to
	 * {@link #developmentWebFolder} jke.jar web/index.html web/..... other js
	 * files
	 */
	public static final File productionWebFolder= new File("./web");

	/**
	 * Location of html and js files for web ui, relative to this plugin in the
	 * eclipse workspace. This folder exists in development mode and is used if
	 * {@link #productionWebFolder} isn't available.
	 */
	public static final File developmentWebFolder= new File("../JKEWebUI/WebContent/");

	private static Server server;

	public static void main(String[] args) {
		server= new Server(8080);
		HandlerList handlers= new HandlerList();

		Context context= new Context(Context.SESSIONS);
		context.setContextPath("/");
		context.addServlet(new ServletHolder(new UserResource()), "/user/*");
		context.addServlet(new ServletHolder(new AdminResource()), "/admin/*");
		context.addServlet(new ServletHolder(new OrganizationsResource()), "/organizations/*");
		context.addServlet(new ServletHolder(new TransactionsResource()), "/transactions/*");

		/**
		 * Serve Web UI files from either their location in production or
		 * development environment http://localhost:8080/ maps to ./web/index.html
		 * or JKEWebUI/WebContent/index.html
		 */
		File webFolder= productionWebFolder.exists() ? productionWebFolder : developmentWebFolder;
		ResourceHandler resourceHandler= new ResourceHandler();
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		resourceHandler.setResourceBase(webFolder.toString());

		handlers.addHandler(resourceHandler);
		handlers.addHandler(context);

		server.setHandler(handlers);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JKE_Util.shutdown();
		}
	}

	public static void stop() {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				System.out.println("Unable to stop embedded Jetty server");
			}
		}
	}
}