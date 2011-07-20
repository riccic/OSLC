/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.logic;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Finder<T> {

	/**
	 * @param toList the list to put instances in
	 * @param packageName the package in which to find the classes
	 */
	public void addSubclassInstances(List<T> toList, String packageName) {
		// Get a File object for the package.
		URL url= getPackageURL(packageName);
		if (url != null) {
			File directory= new File(url.getFile());
			// Get the classes
			if (directory.exists())
				getInstancesFromDirectory(packageName, toList, directory);
			else
				getInstancesFromJar(toList, url);
		}
	}

	/**
	 * @param packageName the package name
	 * @return the URL for the package or null if not found
	 */
	private URL getPackageURL(String packageName) {
		String packageURL= packageName;
		if (!packageURL.startsWith("/"))
			packageURL= "/" + packageURL;
		packageURL= packageURL.replace('.', '/');
		URL url= Finder.class.getResource(packageURL);
		if (url != null) {
			String path= url.toExternalForm();
			path= path.replaceAll("%20", " ");
			try {
				url= new URL(path);
			} catch (MalformedURLException e) {
			}
		}
		return url;
	}

	/**
	 * @param packageName the package in which to find the classes
	 * @param toList the list to put instances in
	 * @param directory the directory corresponding to the package
	 * @throws ClassNotFoundException
	 */
	private void getInstancesFromDirectory(String packageName, List<T> toList, File directory) {
		// Examine each .class file
		String[] files= directory.list();
		for (int i= 0; i < files.length; i++) {
			if (files[i].endsWith(".class")) {
				String className= files[i].substring(0, files[i].length() - 6);
				className= packageName + "." + className;
				T instance= instantiateSubclass(className);
				if (instance != null)
					toList.add(instance);
			}
		}
		return;
	}

	/**
	 * @param toList the list to put instances in
	 * @param url the URL of the jar to search
	 */
	private void getInstancesFromJar(List<T> toList, URL url) {
		String starts= null;
		Enumeration<JarEntry> enumerator= null;
		try {
			// Get the jar entries
			URLConnection urlConn= url.openConnection();
			if (urlConn instanceof JarURLConnection) {
				JarURLConnection conn= (JarURLConnection) urlConn;
				starts= conn.getEntryName();
				JarFile jfile= conn.getJarFile();
				enumerator= jfile.entries();
			} else {
				// Could not open the jar. It was probably
				// a directory that does not exist. The
				// space in the classpath problem can
				// cause this.
				return;
			}
		} catch (IOException ioe) {
			// Could not open the jar.
			return;
		}
		while (enumerator.hasMoreElements()) {
			// Candidates are entries that start with the entry for
			// our package and are class files.
			String entryname= (enumerator.nextElement()).getName();
			if (entryname.startsWith(starts) && (entryname.lastIndexOf('/') <= starts.length()) && entryname.endsWith(".class")) {
				String className= entryname.substring(0, entryname.length() - 6);
				if (className.startsWith("/"))
					className= className.substring(1);
				className= className.replace('/', '.');
				T instance= instantiateSubclass(className);
				if (instance != null)
					toList.add(instance);
			}
		}
		return;
	}

	/**
	 * @param className the fully qualified name of the class to validate
	 * @return an instance of T or null in case of an error.
	 */
	@SuppressWarnings("unchecked")
	public T instantiateSubclass(String className) {
		T result= null;
		try {
			/*
			 * If we can create an instance and it is of our type, we have one.
			 */
			Class<?> subClass= Class.forName(className);
			Object o= subClass.newInstance();
			try {
				/*
				 * T.class.isInstance(o) does not compile. Neither does
				 * T.isInstance(o).
				 */
				result= (T) o;
			} catch (ClassCastException cce) {
				System.out.println("The \"" + className + "\" is not a of the specified base type.");
			}
		} catch (ClassNotFoundException cnfex) {
			// Subclass did not load
			System.out.println("The \"" + className + "\" could not be found.");
		} catch (InstantiationException iex) {
			// Subclass does not have a default constructor
			System.out.println("The \"" + className + "\" could not be created.");
		} catch (IllegalAccessException iaex) {
			// The class is not public
			System.out.println("The \"" + className + "\" class is private.");
		} catch (NoClassDefFoundError ncdfe) {
			// The name probably has a case mismatch problem
			System.out.println("The \"" + className + "\" may have a case sensitivity problem.");
		}
		return result;
	}
}