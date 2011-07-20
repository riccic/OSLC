/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010, 2011. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.junit;

import com.ibm.team.json.JSONObject;
import com.jke.beans.AccountBean;
import com.jke.beans.ContributionBean;
import com.jke.beans.TransactionBean;
import com.jke.beans.UserBean;

import java.io.StringReader;

import junit.framework.TestCase;

public class BeanModelJUnit extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAccountBeanSerializer() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setAccountNumber(5);
		bean.setBalance(500d);
		bean.setContributions(200d);
		bean.setContributionsETD(100d);
		bean.setDividends(3d);
		bean.setType("Savings"); //$NON-NLS-1$
		bean.setUserName("jbrown"); //$NON-NLS-1$
		JSONObject json= bean.toJson();

		AccountBean otherBean= new AccountBean();
		otherBean.fromJson(json);

		assertEquals(bean.getAccountNumber(), otherBean.getAccountNumber());
		assertEquals(bean.getBalance(), otherBean.getBalance());
		assertEquals(bean.getContributions(), otherBean.getContributions());
		assertEquals(bean.getContributionsETD(), otherBean.getContributionsETD());
		assertEquals(bean.getDividends(), otherBean.getDividends());
		assertEquals(bean.getType(), otherBean.getType());
		assertEquals(bean.getUserName(), otherBean.getUserName());
	}

	public void testTransactionBeanSerializer() throws Exception {
		TransactionBean bean= new TransactionBean();
		bean.setAccountNumber(5);
		bean.setAmount(100d);
		bean.setDate("12.12.2010"); //$NON-NLS-1$
		bean.setPostBalance(20d);
		bean.setSource("foo"); //$NON-NLS-1$
		bean.setTransactionID(44);
		bean.setTransactionType("bar"); //$NON-NLS-1$

		JSONObject json= bean.toJson();

		TransactionBean otherBean= new TransactionBean();
		otherBean.fromJson(json);

		assertEquals(bean.getAccountNumber(), otherBean.getAccountNumber());
		assertEquals(bean.getAmount(), otherBean.getAmount());
		assertEquals(bean.getDate(), otherBean.getDate());
		assertEquals(bean.getPostBalance(), otherBean.getPostBalance());
		assertEquals(bean.getSource(), otherBean.getSource());
		assertEquals(bean.getTransactionID(), otherBean.getTransactionID());
		assertEquals(bean.getTransactionType(), otherBean.getTransactionType());
	}

	public void testUserBeanSerializer() throws Exception {
		UserBean bean= new UserBean();
		bean.setFirstName("Markus"); //$NON-NLS-1$
		bean.setLastName("Foo"); //$NON-NLS-1$
		bean.setUserName("mfoo"); //$NON-NLS-1$

		JSONObject json= bean.toJson();
		UserBean otherBean= new UserBean();
		otherBean.deserializeFromJson(new StringReader(json.toString()));

		assertEquals(bean.getFirstName(), otherBean.getFirstName());
		assertEquals(bean.getLastName(), otherBean.getLastName());
		assertEquals(bean.getUserName(), otherBean.getUserName());
	}

	public void testUserBeanUserName() throws Exception {
		UserBean bean= new UserBean();
		bean.setUserName("mfoo");
		assertEquals("mfoo", bean.getUserName());
	}

	public void testUserBeanFirstName() throws Exception {
		UserBean bean= new UserBean();
		bean.setFirstName("Markus");
		assertEquals("Markus", bean.getFirstName());
	}

	public void testUserBeanLastName() throws Exception {
		UserBean bean= new UserBean();
		bean.setLastName("Foo");
		assertEquals("Foo", bean.getLastName());
	}

	public void testTransactionBeanID() throws Exception {
		TransactionBean bean= new TransactionBean();
		bean.setTransactionID(5);
		assertEquals(5, bean.getTransactionID());
	}

	public void testTransactionBeanType() throws Exception {
		TransactionBean bean= new TransactionBean();
		bean.setTransactionType("Foo");
		assertEquals("Foo", bean.getTransactionType());
	}

	public void testTransactionBeanAccountNumber() throws Exception {
		AccountBean account= new AccountBean();
		account.setAccountNumber(5);

		TransactionBean bean= new TransactionBean(account, "foo", "source", 3d, 2d);
		assertEquals(5, bean.getAccountNumber());
	}

	public void testTransactionSource() throws Exception {
		AccountBean account= new AccountBean();
		account.setAccountNumber(5);

		TransactionBean bean= new TransactionBean(account, "foo", "source", 3d, 2d);
		assertEquals("source", bean.getSource());
	}

	public void testTransactionAmount() throws Exception {
		AccountBean account= new AccountBean();
		account.setAccountNumber(5);

		TransactionBean bean= new TransactionBean(account, "foo", "source", 3d, 2d);
		assertEquals(3d, bean.getAmount());
	}

	public void testTransactionDate() throws Exception {
		TransactionBean bean= new TransactionBean();
		bean.setDate("12.12.2010");
		assertEquals("12.12.2010", bean.getDate());
	}

	public void testContributionAccountNumber() throws Exception {
		ContributionBean bean= new ContributionBean();
		bean.setAccountNumber(5);
		assertEquals(5, bean.getAccountNumber());
	}

	public void testContributionOrganization() throws Exception {
		ContributionBean bean= new ContributionBean();
		bean.setOrganization("Red Cross");
		assertEquals("Red Cross", bean.getOrganization());
	}

	public void testContributionPercentage() throws Exception {
		ContributionBean bean= new ContributionBean();
		bean.setPercentage(5d);
		assertEquals(5d, bean.getPercentage());
	}

	public void testContributionDate() throws Exception {
		ContributionBean bean= new ContributionBean();
		bean.setDate("12.12.2010");
		assertEquals("12.12.2010", bean.getDate());
	}

	public void testAccountBalance() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setBalance(4d);
		assertEquals(4d, bean.getBalance());
	}

	public void testAccountDividends() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setDividends(1d);
		assertEquals(1d, bean.getDividends());
	}

	public void testAccountDividendsETD() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setDividendsETD(8d);
		assertEquals(8d, bean.getDividendsETD());
	}

	public void testAccountContributions() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setContributions(9d);
		assertEquals(9d, bean.getContributions());
	}

	public void testAccountContributionsETD() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setContributionsETD(3d);
		assertEquals(3d, bean.getContributionsETD());
	}

	public void testAccountAccountNumber() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setAccountNumber(6);
		assertEquals(6, bean.getAccountNumber());
	}

	public void testAccountUsername() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setUserName("mfoo");
		assertEquals("mfoo", bean.getUserName());
	}

	public void testAccountType() throws Exception {
		AccountBean bean= new AccountBean();
		bean.setType("Foo");
		assertEquals("Foo", bean.getType());
	}
}