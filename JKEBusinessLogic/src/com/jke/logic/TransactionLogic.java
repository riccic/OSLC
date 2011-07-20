/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.logic;

import com.jke.beans.AccountBean;
import com.jke.beans.AccountTypeBean;
import com.jke.beans.ContributionBean;
import com.jke.beans.TransactionBean;
import com.jke.beans.UserBean;
import com.jke.db.data.BeanLoader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class TransactionLogic.
 */
public class TransactionLogic {

	/** The loader. */
	private BeanLoader loader;

	/** The account logic. */
	private AccountLogic acctLog;

	public class ProposedContributionResults {
		public TransactionBean trans;
		public AccountBean acct;
	}

	/**
	 * Instantiates a new transaction logic.
	 */
	public TransactionLogic() {
		loader= new BeanLoader();
		acctLog= new AccountLogic();
	}

	/**
	 * Gets the transaction history.
	 * 
	 * @param user the user
	 * @param acctType the account type
	 * @return the transaction history
	 */
	public List<TransactionBean> getTransactionHistory(UserBean user, AccountTypeBean acctType) {
		int accountNum= acctLog.getAccountNumber(user, acctType);
		List<TransactionBean> sendList= new ArrayList<TransactionBean>();
		try {
			sendList.addAll(loader.pullTransactionBeans(accountNum));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendList;
	}

	/**
	 * Calculates the proposed results for the contribution. The database is not
	 * updated.
	 * 
	 * @param cont the contribution
	 * @return the proposed transaction and the proposed account update
	 * @throws SQLException
	 */
	public ProposedContributionResults getProposedContributionTransaction(ContributionBean cont) {
		ProposedContributionResults results= new ProposedContributionResults();
		AccountLogic acctBL= new AccountLogic();
		results.acct= acctBL.getAccount(cont.getAccountNumber());
		double amt= (results.acct.getDividends() * (cont.getPercentage() / 100));
		results.trans= new TransactionBean(results.acct, "Donation", cont.getOrganization(), amt, results.acct.getBalance() - amt);
		results.acct.setBalance(results.acct.getBalance() - amt);
		results.acct.setContributions(results.acct.getContributions() + amt);
		results.acct.setContributionsETD(results.acct.getContributionsETD() + amt);
		return results;
	}

	/**
	 * Calculates and executes the contribution against its target account.
	 * 
	 * @param cont the contribution
	 * @return the executed transaction
	 * @throws SQLException
	 */
	public TransactionBean performContribution(ContributionBean cont) throws SQLException {
		ProposedContributionResults proposed= getProposedContributionTransaction(cont);
		int contributionID= loader.pushContributionBean(cont);
		loader.pushTransactionBean(proposed.trans, contributionID);
		loader.updateAccount(proposed.acct);
		return proposed.trans;
	}
}