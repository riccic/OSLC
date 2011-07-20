/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.junit;

import com.jke.beans.AccountBean;
import com.jke.beans.TransactionBean;
import com.jke.beans.UserBean;
import com.jke.db.data.BeanLoader;
import com.jke.db.data.GenerateData;
import com.jke.logic.JKE_Util;

import java.util.List;

import junit.framework.TestCase;

public class DBJunit extends TestCase {
	private BeanLoader fLoader;

	protected void setUp() throws Exception {
		GenerateData.resetDatabaseToInitialState();
		fLoader= new BeanLoader();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateLoadUsers() throws Exception {
		UserBean user= fLoader.pullUserBean("Markus", "Foo"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(user);

		UserBean bean= new UserBean();
		bean.setFirstName("Markus"); //$NON-NLS-1$
		bean.setLastName("Foo"); //$NON-NLS-1$
		bean.setUserName("mfoo"); //$NON-NLS-1$

		fLoader.pushUserBean(bean, "pass"); //$NON-NLS-1$

		user= fLoader.pullUserBean("mfoo", "pass"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(user);

		assertEquals(bean.getFirstName(), user.getFirstName());
		assertEquals(bean.getLastName(), user.getLastName());
		assertEquals(bean.getUserName(), user.getUserName());
	}

	public void testCreateLoadAccountBean() throws Exception {
		AccountBean account= fLoader.pullAccountBean(6);
		assertNull(account);

		AccountBean bean= new AccountBean();
		bean.setAccountNumber(6);
		bean.setBalance(5d);
		bean.setContributions(3d);
		bean.setContributionsETD(2d);
		bean.setDividends(8d);
		bean.setDividendsETD(10d);
		bean.setType("foo"); //$NON-NLS-1$
		bean.setUserName("mfoo"); //$NON-NLS-1$

		fLoader.pushAccountBean(bean);

		account= fLoader.pullAccountBean(6);
		assertNotNull(account);

		assertEquals(bean.getAccountNumber(), account.getAccountNumber());
		assertEquals(bean.getBalance(), account.getBalance());
		assertEquals(bean.getContributions(), account.getContributions());
		assertEquals(bean.getContributionsETD(), account.getContributionsETD());
		assertEquals(bean.getDividends(), account.getDividends());
		assertEquals(bean.getDividendsETD(), account.getDividendsETD());
		assertEquals(bean.getType(), account.getType());
		assertEquals(bean.getUserName(), account.getUserName());
	}

	public void testCreateLoadTransactionBeans() throws Exception {
		List<TransactionBean> list= fLoader.pullTransactionBeans(7);
		assertTrue(list.isEmpty());

		TransactionBean bean= new TransactionBean();
		bean.setAccountNumber(7);
		bean.setAmount(111d);
		bean.setDate("12.12.2010"); //$NON-NLS-1$
		bean.setPostBalance(12d);
		bean.setSource("foo"); //$NON-NLS-1$
		bean.setTransactionType("bar"); //$NON-NLS-1$

		fLoader.pushTransactionBean(bean);

		list= fLoader.pullTransactionBeans(7);
		assertEquals(1, list.size());
		TransactionBean tBean= list.get(0);

		assertEquals(tBean.getAccountNumber(), bean.getAccountNumber());
		assertEquals(tBean.getAmount(), bean.getAmount());
		assertEquals(tBean.getDate(), bean.getDate());
		assertEquals(tBean.getPostBalance(), bean.getPostBalance());
		assertEquals(tBean.getSource(), bean.getSource());
		assertEquals(tBean.getTransactionType(), bean.getTransactionType());
	}

	public void testLogin() throws Exception {
		UserBean user= JKE_Util.login("jbrown", "jbrown"); //$NON-NLS-1$//$NON-NLS-2$
		assertNotNull(user);
	}
}