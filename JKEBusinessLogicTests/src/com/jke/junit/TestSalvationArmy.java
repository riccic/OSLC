/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010, 2011. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.junit;

import com.jke.organizations.SalvationArmy;

import junit.framework.TestCase;

public class TestSalvationArmy extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testOrganization() {
		SalvationArmy org= new SalvationArmy();
		assertEquals("Salvation Army", org.getName());
		assertTrue(org.getWebPage().toString().length() > 0);
	}
}