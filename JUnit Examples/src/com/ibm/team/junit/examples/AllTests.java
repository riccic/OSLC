/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2005, 2010. All Rights Reserved.
 * 
 * Note to U.S. Government Users Restricted Rights:
 * Use, duplication or disclosure restricted by GSA ADP Schedule
 * Contract with IBM Corp. 
 *******************************************************************************/
package com.ibm.team.junit.examples;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * JUnit examples class that runs all the tests in the sample.
 */
public class AllTests {
	/**
	 * Build a test suite.
	 */
	public static Test suite() {
		TestSuite suite= new TestSuite(AllTests.class.getName());
		suite.addTestSuite(Vector.class);
		suite.addTestSuite(Math.class);
		return suite;
	}
}
