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
import com.jke.beans.UserBean;
import com.jke.db.data.BeanLoader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class AccountLogic.
 */
public class AccountLogic {

	/** The loader. */
	private BeanLoader loader;

	/**
	 * Instantiates a new account logic.
	 */
	public AccountLogic() {
		loader= new BeanLoader();
	}

	/**
	 * Account types array.
	 * 
	 * @param user the user
	 * @return the string[]
	 */
	public static String[] accountTypesArray(UserBean user) {
		List<AccountTypeBean> list= new AccountLogic().getUserAccountTypes(user);
		String[] types= new String[list.size()];
		int i= 0;
		for (AccountTypeBean b : list) {
			types[i++]= b.toString();
		}
		return types;
	}

	/**
	 * Gets the account number for a given user and account type. Return -1 if the
	 * user does not have this account type.
	 * 
	 * @param user the user
	 * @param acctType the account type
	 * @return the account number or -1 if not found
	 */
	public int getAccountNumber(UserBean user, AccountTypeBean acctType) {
		AccountBean a= determineAcct(user, acctType);
		if (a != null)
			return a.getAccountNumber();
		else
			return -1;
	}

	/**
	 * Gets the user's account types.
	 * 
	 * @param user the user
	 * @return the user accounts
	 */
	public List<AccountTypeBean> getUserAccountTypes(UserBean user) {
		List<AccountTypeBean> acctTypes= new ArrayList<AccountTypeBean>();
		try {
			List<AccountBean> ab= loader.pullUserAccountBeans(user);
			for (AccountBean a : ab) {
				acctTypes.add(a.getTypeBean());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return acctTypes;

	}

	/**
	 * Determine the account for a given user and account type. Return null if the
	 * user does not have this type of account.
	 * 
	 * @param user the user
	 * @param acctType the account type
	 * @return the account bean
	 */
	public AccountBean determineAcct(UserBean user, AccountTypeBean acctType) {
		AccountBean bean= null;
		try {
			bean= loader.pullAccountBean(user, acctType);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * Return the account for the given account number. Return null if the account
	 * does not exist.
	 * 
	 * @param acct the account number
	 * @return
	 */
	public AccountBean getAccount(int acct) {
		AccountBean bean= null;
		try {
			bean= loader.pullAccountBean(acct);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * Return a list of accounts for the specified user. The list may be empty but
	 * it will not be null.
	 * 
	 * @param user the user
	 * @return
	 */
	public List<AccountBean> getUserAccounts(UserBean user) {
		List<AccountBean> accts= new ArrayList<AccountBean>();
		try {
			accts.addAll(loader.pullUserAccountBeans(user));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accts;
	}
}