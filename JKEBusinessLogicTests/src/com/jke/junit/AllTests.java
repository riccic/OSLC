/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010, 2011. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite= new TestSuite("Test for com.jke.junit");
		//$JUnit-BEGIN$
		suite.addTestSuite(OrganizationFinderJUnit.class);
		suite.addTestSuite(AccountLogicJUnit.class);
		suite.addTestSuite(BeanModelJUnit.class);
		suite.addTestSuite(DBJunit.class);
		suite.addTestSuite(OrganizationLogicJUnit.class);
		suite.addTestSuite(TransactionLogicJUnit.class);
		suite.addTestSuite(TestSalvationArmy.class);
		suite.addTestSuite(TestRedCross.class);
		suite.addTestSuite(TestCare.class);
		suite.addTestSuite(TestAmericanCancerSociety.class);
		//$JUnit-END$
		return suite;
	}
}