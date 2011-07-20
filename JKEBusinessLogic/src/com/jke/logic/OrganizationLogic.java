/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.logic;

import com.jke.organization.Organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Class OrganizationLogic.
 */
public class OrganizationLogic {

	/** The organizations. */
	private Map<String, Organization> fOrganizations= new HashMap<String, Organization>();

	/**
	 * Instantiates a new organization logic.
	 */
	public OrganizationLogic() {
		super();
		List<Organization> organizations= new ArrayList<Organization>();
		Finder<Organization> finder= new Finder<Organization>();
		finder.addSubclassInstances(organizations, "com.jke.organizations");
		for (Organization organization : organizations) {
			fOrganizations.put(organization.getName(), organization);
		}
	}

	/**
	 * Checks if is valid organization.
	 * 
	 * @param orgName the organization name
	 * @return true, if is valid organization
	 */
	public boolean isValidOrganization(String orgName) {
		return fOrganizations.containsKey(orgName);
	}

	/**
	 * Names as array.
	 * 
	 * @return the string[]
	 */
	public String[] namesAsArray() {
		Set<String> names= fOrganizations.keySet();
		String[] result= new String[names.size()];
		names.toArray(result);
		return result;
	}

	/**
	 * As map.
	 * 
	 * @return the map
	 */
	public Map<String, Organization> asMap() {
		Map<String, Organization> result= new HashMap<String, Organization>();
		result.putAll(fOrganizations);
		return result;
	}
}