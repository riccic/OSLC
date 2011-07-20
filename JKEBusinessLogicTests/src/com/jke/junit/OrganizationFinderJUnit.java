/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.junit;

import com.jke.logic.Finder;
import com.jke.organization.Organization;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class OrganizationFinderJUnit extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*public void testAddSubclassInstances() {
		Finder<Organization> orgFinder= new Finder<Organization>();
		List<Organization> toList= new ArrayList<Organization>();
		orgFinder.addSubclassInstances(toList, "com.jke.organizations");
		assertTrue(toList.size() == 2);
		assertEquals("RedCross", toList.get(0).getClass().getSimpleName());
		assertEquals("American Red Cross", toList.get(0).getName());
		assertEquals("SalvationArmy", toList.get(1).getClass().getSimpleName());
		assertEquals("Salvation Army", toList.get(1).getName());
	}*/

	public void testAddSubclassInstancesBad() {
		Finder<Organization> orgFinder= new Finder<Organization>();
		List<Organization> toList= new ArrayList<Organization>();
		orgFinder.addSubclassInstances(toList, "com.JKE.organizations");
		assertTrue(toList.size() == 0);
	}
}