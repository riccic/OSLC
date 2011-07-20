/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2005, 2010. All Rights Reserved.
 * 
 * Note to U.S. Government Users Restricted Rights:
 * Use, duplication or disclosure restricted by GSA ADP Schedule
 * Contract with IBM Corp. 
 *******************************************************************************/
package com.ibm.team.junit.examples;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Vector extends TestCase {

	public void testEmpty() {
		java.util.Vector< Object> v = new java.util.Vector<Object>();
		Assert.assertTrue(v.isEmpty());
		v.add(new Object());
		Assert.assertFalse(v.isEmpty());
	}
	
	public void testAddItem() {
		java.util.Vector< Object> v = new java.util.Vector<Object>();
		v.add(new Object());
		Assert.assertFalse(v.isEmpty());
		Assert.assertTrue(v.size() == 1);
		v.add(new Object());
		Assert.assertTrue(v.size() == 2);
	}
	
	public void testAddMultipleItems() {
		java.util.Vector< Object> v = new java.util.Vector<Object>();
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		v.add(o1);
		v.add(o2);
		v.add(o3);
		Assert.assertTrue(v.size() == 3);
	}
	
	public void testAddAll() {
		java.util.Vector< Object> v = new java.util.Vector<Object>();
		List<Object> c = new ArrayList<Object>();
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		c.add(o1);
		c.add(o2);
		c.add(o3);
		v.addAll(c);
		Assert.assertTrue(v.size() == 3);
	}
	
	public void testSize() {
		java.util.Vector< Object> v = new java.util.Vector<Object>();		
		Object o = new Object();
		v.add(o);
		Assert.assertTrue(v.remove(o));
		Assert.assertTrue(v.isEmpty());
	}
	
	public void testFirstElement() {
		java.util.Vector< Object> v = new java.util.Vector<Object>();		
		Object o = new Object();
		v.add(o);
		Assert.assertTrue(v.remove(o));
		Assert.assertTrue(v.isEmpty());
	}
	
	public void testIndexOf() {
		java.util.Vector< Object> v = new java.util.Vector<Object>();		
		Object o = new Object();
		v.add(o);
		Assert.assertTrue(v.remove(o));
		Assert.assertTrue(v.isEmpty());
	}
	
	public void testRemoveAll() {
		java.util.Vector< Object> v = new java.util.Vector<Object>();		
		Object o = new Object();
		v.add(o);
		Assert.assertTrue(v.remove(o));
		Assert.assertTrue(v.isEmpty());
	}
}
