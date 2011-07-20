/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010, 2011. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.db.data;

import com.jke.beans.AccountBean;
import com.jke.beans.AccountTypeBean;
import com.jke.beans.ContributionBean;
import com.jke.beans.TransactionBean;
import com.jke.beans.UserBean;
import com.jke.db.connection.JKE_DB_Factory;
import com.jke.db.connection.JKE_DB_I;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class BeanLoader.
 */
public class BeanLoader {

	/** The db. */
	private JKE_DB_I db;

	/**
	 * Instantiates a new bean loader.
	 */
	public BeanLoader() {
		db= JKE_DB_Factory.getFactory().getDB();
	}

	/**
	 * Push user bean.
	 * 
	 * @param b the b
	 * @param pass the pass
	 * @throws SQLException the sQL exception
	 */
	public void pushUserBean(UserBean b, String pass) throws SQLException {
		String sql= "INSERT INTO " + GenerateData.userTable + " (FirstName, LastName, UserName, Password)" + " values (?, ?, ?, ?)";
		PreparedStatement ps= db.getConnection().prepareStatement(sql);
		ps.setString(1, b.getFirstName());
		ps.setString(2, b.getLastName());
		ps.setString(3, b.getUserName());
		ps.setString(4, pass);
		ps.executeUpdate();
		ps.close();
		System.out.println("User created: " + b.getFirstName() + " " + b.getLastName());
	}

	/**
	 * Push account bean.
	 * 
	 * @param b the b
	 * @throws SQLException the sQL exception
	 */
	public void pushAccountBean(AccountBean b) throws SQLException {
		String sql= "INSERT INTO " + GenerateData.acctTable + " (AccountNumber, Balance, Dividends, DividendsETD, Contributions, ContributionsETD, UserName, AccountType)" + " values (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps= db.getConnection().prepareStatement(sql);
		ps.setInt(1, b.getAccountNumber());
		ps.setDouble(2, b.getBalance());
		ps.setDouble(3, b.getDividends());
		ps.setDouble(4, b.getDividendsETD());
		ps.setDouble(5, b.getContributions());
		ps.setDouble(6, b.getContributionsETD());
		ps.setString(7, b.getUserName());
		ps.setString(8, b.getType());
		ps.executeUpdate();
		ps.close();
		System.out.println("Account Created: " + b.getAccountNumber());
	}

	/**
	 * Returns the account for the given user and type. Returns null if it does
	 * not exist.
	 * 
	 * @param user
	 * @param acctType
	 * @return
	 * @throws SQLException
	 */
	public AccountBean pullAccountBean(UserBean user, AccountTypeBean acctType) throws SQLException {
		AccountBean bean= null;
		String sql= "SELECT * FROM " + GenerateData.acctTable + " WHERE UserName = '" + user.getUserName() + "' AND AccountType = '" + acctType.toString() + "'";
		Statement s= db.getConnection().createStatement();
		ResultSet rs= s.executeQuery(sql);
		if (rs.next()) {
			bean= createAccountBean(rs);
		}
		return bean;
	}

	/**
	 * Returns the account for the given account number. Returns null if it does
	 * not exist.
	 * 
	 * @param acctNumber
	 * @return
	 * @throws SQLException
	 */
	public AccountBean pullAccountBean(int acctNumber) throws SQLException {
		AccountBean bean= null;
		String sql= "SELECT * FROM " + GenerateData.acctTable + " WHERE AccountNumber = " + acctNumber;
		Statement s= db.getConnection().createStatement();
		ResultSet rs= s.executeQuery(sql);
		if (rs.next()) {
			bean= createAccountBean(rs);
		}
		return bean;
	}

	/**
	 * Pull account beans for the given user.
	 * 
	 * @param user the user
	 * @return the list
	 * @throws SQLException the sQL exception
	 */
	public List<AccountBean> pullUserAccountBeans(UserBean user) throws SQLException {
		String sql= "SELECT * FROM " + GenerateData.acctTable + " WHERE UserName = " + "'" + user.getUserName() + "'";
		Statement s= db.getConnection().createStatement();
		ResultSet rs= s.executeQuery(sql);
		List<AccountBean> list= new ArrayList<AccountBean>();

		while (rs.next()) {
			list.add(createAccountBean(rs));
		}
		return list;
	}

	private AccountBean createAccountBean(ResultSet rs) throws SQLException {
		return new AccountBean(rs.getInt("AccountNumber"), rs.getDouble("Balance"), rs.getDouble("Dividends"), rs.getDouble("DividendsETD"), rs.getDouble("Contributions"), rs.getDouble("ContributionsETD"), rs.getString("UserName"), rs.getString("AccountType"));
	}

	/**
	 * Update account.
	 * 
	 * @param b the b
	 * @throws SQLException the sQL exception
	 */
	public void updateAccount(AccountBean b) throws SQLException {
		String sql= "UPDATE " + GenerateData.acctTable + " SET Balance = " + b.getBalance() + ", Dividends = " + b.getDividends() + ", DividendsETD = " + b.getDividendsETD() + ", Contributions = " + b.getContributions() + ", ContributionsETD = " + b.getContributionsETD() + " WHERE AccountNumber = " + b.getAccountNumber();
		Statement s= db.getConnection().createStatement();
		s.executeUpdate(sql);
	}

	/**
	 * Push transaction bean for a normal, non-Donation, transaction.
	 * 
	 * @param trans the transaction
	 * @throws SQLException the sQL exception
	 */
	public void pushTransactionBean(TransactionBean trans) throws SQLException {
		String sql= "INSERT INTO " + GenerateData.transTable + " (Type, AccountNumber, Source, Amount, PostBalance, Date)" + " values (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps= db.getConnection().prepareStatement(sql);
		ps.setString(1, trans.getTransactionType());
		ps.setInt(2, trans.getAccountNumber());
		ps.setString(3, trans.getSource());
		ps.setDouble(4, trans.getAmount());
		ps.setDouble(5, trans.getPostBalance());
		ps.setString(6, trans.getDate());
		ps.executeUpdate();
		ps.close();
		System.out.println("Transaction created: " + trans.getAccountNumber());
	}

	/**
	 * Push transaction bean for a contribution. For repeating contributions,
	 * multiple transaction records will refer to the same contribution. For one
	 * time contributions, only one transaction will refer to the contribution.
	 * 
	 * @param trans the transaction
	 * @throws SQLException the sQL exception
	 */
	public void pushTransactionBean(TransactionBean trans, int contributionID) throws SQLException {
		String sql= "INSERT INTO " + GenerateData.transTable + " (Type, AccountNumber, Source, Amount, PostBalance, Date, ContributionID)" + " values (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps= db.getConnection().prepareStatement(sql);
		ps.setString(1, trans.getTransactionType());
		ps.setInt(2, trans.getAccountNumber());
		ps.setString(3, trans.getSource());
		ps.setDouble(4, trans.getAmount());
		ps.setDouble(5, trans.getPostBalance());
		ps.setString(6, trans.getDate());
		ps.setInt(7, contributionID);
		ps.executeUpdate();
		ps.close();
		trans.setTransactionID(countTransactionBeans());
		System.out.println("Transaction created: " + trans.getAccountNumber());
	}

	/**
	 * Pull transaction beans for the specified account.
	 * 
	 * @param accountNumber the account number to pull from
	 * @return a list of transactions
	 * @throws SQLException the sQL exception
	 */
	public List<TransactionBean> pullTransactionBeans(int accountNumber) throws SQLException {
		String sql= "SELECT * FROM " + GenerateData.transTable + " WHERE AccountNumber = " + accountNumber;
		Statement s= db.getConnection().createStatement();
		ResultSet rs= s.executeQuery(sql);
		List<TransactionBean> list= new ArrayList<TransactionBean>();

		while (rs.next()) {
			list.add(new TransactionBean(rs.getInt("TransactionID"), rs.getString("Type"), rs.getInt("AccountNumber"), rs.getString("Source"), rs.getDouble("Amount"), rs.getDouble("PostBalance"), rs.getString("Date")));
		}
		return list;
	}
	
	public int countTransactionBeans() throws SQLException {
		String sql= "SELECT * FROM " + GenerateData.transTable;
		Statement s= db.getConnection().createStatement();
		ResultSet rs= s.executeQuery(sql);
		int count= 0;

		while (rs.next()) {
			count++;
		}
		return count;
	}

	/**
	 * @param b
	 * @throws SQLException
	 */
	public int pushContributionBean(ContributionBean b) throws SQLException {
		int myKey= getKey(GenerateData.contribKeyRowId);
		String sql= "INSERT INTO " + GenerateData.contribTable + " (ContributionID, AccountNumber, Organization, Percentage, Date)" + " values (?, ?, ?, ?, ?)";
		PreparedStatement ps= db.getConnection().prepareStatement(sql);
		ps.setInt(1, myKey);
		ps.setInt(2, b.getAccountNumber());
		ps.setString(3, b.getOrganization());
		ps.setDouble(4, b.getPercentage());
		ps.setString(5, b.getDate());
		ps.executeUpdate();
		ps.close();
		return myKey;
	}

	/*
	 * Retrieves the next integer key for the named key type. The next key value
	 * in the database is incremented for the next caller.
	 */
	private synchronized int getKey(String keyID) throws SQLException {
		PreparedStatement tps= db.getConnection().prepareStatement("SELECT KeyID, NextKey FROM " + GenerateData.keysTable + " WHERE KeyID = '" + keyID + "'", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		ResultSet trs= tps.executeQuery();
		trs.next();
		int myKey= trs.getInt(2);
		int nextKey= myKey + 1;
		trs.updateInt(2, nextKey);
		trs.updateRow();
		return myKey;
	}

	public UserBean pullUserBean(String u, String p) {
		UserBean b= null;
		String sql= "SELECT * FROM " + GenerateData.userTable + " WHERE UserName = '" + u + "' ";
		if (p != null) {
			sql+= " AND Password = '" + p + "'";
		}
		try {
			Statement s= db.getConnection().createStatement();
			ResultSet rs= s.executeQuery(sql);
			if (rs.next()) {
				b= new UserBean(rs.getString("UserName"), rs.getString("FirstName"), rs.getString("LastName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return b;
	}
}