/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.junit;

import com.jke.beans.AccountTypeBean;
import com.jke.beans.ContributionBean;
import com.jke.beans.TransactionBean;
import com.jke.beans.UserBean;
import com.jke.db.data.BeanLoader;
import com.jke.db.data.GenerateData;
import com.jke.logic.AccountLogic;
import com.jke.logic.TransactionLogic;
import com.jke.logic.TransactionLogic.ProposedContributionResults;
import com.jke.organizations.Care;

import java.util.List;

import junit.framework.TestCase;

public class TransactionLogicJUnit extends TestCase {
	private TransactionLogic fTransactionLogic;
	private BeanLoader fLoader;
	private AccountLogic fAccountLogic;

	protected void setUp() throws Exception {
		GenerateData.resetDatabaseToInitialState();
		fLoader= new BeanLoader();
		fTransactionLogic= new TransactionLogic();
		fAccountLogic= new AccountLogic();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTransactionHistory() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		List<TransactionBean> history= fTransactionLogic.getTransactionHistory(user, AccountTypeBean.Checking);
		assertTrue(!history.isEmpty());
	}

	public void testGetProposedContributionTransaction() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		ContributionBean bean= new ContributionBean();
		bean.setAccountNumber(fAccountLogic.getAccountNumber(user, AccountTypeBean.Checking));
		bean.setDate("12.12.2010"); //$NON-NLS-1$
		bean.setOrganization(new Care().getName());
		bean.setPercentage(12d);

		ProposedContributionResults result= fTransactionLogic.getProposedContributionTransaction(bean);
		assertNotNull(result);
	}

	public void testPerformContribution() throws Exception {
		UserBean user= fLoader.pullUserBean("jbrown", "jbrown"); //$NON-NLS-1$ //$NON-NLS-2$

		ContributionBean bean= new ContributionBean();
		bean.setAccountNumber(fAccountLogic.getAccountNumber(user, AccountTypeBean.Checking));
		bean.setDate("12.12.2010"); //$NON-NLS-1$
		bean.setOrganization(new Care().getName());
		bean.setPercentage(13d);

		TransactionBean tBean= fTransactionLogic.performContribution(bean);
		assertNotNull(tBean);
	}
}