/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010, 2011. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.junit;

import com.jke.logic.OrganizationLogic;
import com.jke.organization.Organization;
import com.jke.organizations.AmericanCancerSociety;
import com.jke.organizations.Care;
import com.jke.organizations.RedCross;
import com.jke.organizations.SalvationArmy;

import java.util.Map;

import junit.framework.TestCase;

public class OrganizationLogicJUnit extends TestCase {
	private OrganizationLogic fOrganizationLogic;

	protected void setUp() throws Exception {
		fOrganizationLogic= new OrganizationLogic();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testIsValidOrganization() throws Exception {
		assertTrue(fOrganizationLogic.isValidOrganization(new RedCross().getName()));
		assertTrue(fOrganizationLogic.isValidOrganization(new Care().getName()));
		assertTrue(fOrganizationLogic.isValidOrganization(new SalvationArmy().getName()));
		assertTrue(fOrganizationLogic.isValidOrganization(new AmericanCancerSociety().getName()));

		assertFalse(fOrganizationLogic.isValidOrganization("foobar_foo")); //$NON-NLS-1$
	}

	public void testNamesAsArray() throws Exception {
		String[] namesAsArray= fOrganizationLogic.namesAsArray();
		assertEquals(fOrganizationLogic.asMap().size(),  namesAsArray.length);
	}

	public void testAsMap() throws Exception {
		Map<String, Organization> asMap= fOrganizationLogic.asMap();
		assertEquals(asMap.size(), asMap.size());
	}
}