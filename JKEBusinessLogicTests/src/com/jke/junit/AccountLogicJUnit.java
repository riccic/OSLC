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
import com.jke.beans.AccountTypeBean;
import com.jke.beans.UserBean;
import com.jke.db.data.BeanLoader;
import com.jke.db.data.GenerateData;
import com.jke.logic.AccountLogic;

import java.util.List;

import junit.framework.TestCase;

public class AccountLogicJUnit extends TestCase {
	private BeanLoader fLoader;
	private AccountLogic fLogic;

	protected void setUp() throws Exception {
		GenerateData.resetDatabaseToInitialState();
		fLoader= new BeanLoader();
		fLogic= new AccountLogic();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAccountLogic() {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		AccountBean bean= fLogic.determineAcct(user, AccountTypeBean.Checking);
		assertEquals(200, bean.getAccountNumber());
		bean= fLogic.getAccount(202);
		assertEquals(202, bean.getAccountNumber());

		AccountTypeBean type= AccountTypeBean.valueOf("IRA"); //$NON-NLS-1$
		assertEquals("IRA", type.toString()); //$NON-NLS-1$
	}

	public void testAccountTypesArray() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$
		String[] accountTypes= AccountLogic.accountTypesArray(user);

		assertEquals(accountTypes.length, AccountTypeBean.values().length);
	}

	public void testGetAccountNumber() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		int accountNumber= fLogic.getAccountNumber(user, AccountTypeBean.IRA);
		assertTrue(accountNumber != -1);
	}

	public void testGetUserAccountTypes() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		List<AccountTypeBean> userAccountTypes= fLogic.getUserAccountTypes(user);
		assertEquals(userAccountTypes.size(), AccountTypeBean.values().length);
	}

	public void testDetermineAcct() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		AccountBean account= fLogic.determineAcct(user, AccountTypeBean.Checking);
		assertNotNull(account);
	}

	public void testGetAccount() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		int accountNumber= fLogic.getAccountNumber(user, AccountTypeBean.Checking);
		AccountBean account= fLogic.getAccount(accountNumber);
		assertNotNull(account);
	}

	public void testGetUserAccounts() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		List<AccountBean> userAccounts= fLogic.getUserAccounts(user);
		assertNotNull(userAccounts);
	}
}