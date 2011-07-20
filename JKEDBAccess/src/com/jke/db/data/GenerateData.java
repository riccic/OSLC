/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.db.data;

import com.jke.beans.AccountBean;
import com.jke.beans.TransactionBean;
import com.jke.beans.UserBean;
import com.jke.db.connection.JKE_DB_Factory;
import com.jke.db.connection.JKE_DB_I;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The Class GenerateData.
 */
public class GenerateData {

	/** The user table. */
	public static String userTable= "JKEuser";

	/** The acct table. */
	public static String acctTable= "JKEaccounts";

	/** The trans table. */
	public static String transTable= "JKEtransactions";

	/** the contributions table. */
	public static String contribTable= "JKEcontributions";

	/** the unique keys table. */
	public static String keysTable= "JKEKeys";
	public static String contribKeyRowId= "Contrib";

	/** The db. */
	private JKE_DB_I db;

	/** The loader. */
	private BeanLoader loader;

	/**
	 * Instantiates a new generate data.
	 */
	public GenerateData() {
		db= JKE_DB_Factory.getFactory().getDB();
		loader= new BeanLoader();
	}

	/**
	 * Drop all database tables.
	 * 
	 * @return true, if successful
	 */
	public static boolean resetDatabaseToInitialState() {
		GenerateData gen= new GenerateData();
		gen.dropTables();
		return true;
	}

	/**
	 * Clear data.
	 */
	private void dropTables() {
		db.dropTable(userTable);
		db.dropTable(acctTable);
		db.dropTable(transTable);
		db.dropTable(contribTable);
		db.dropTable(keysTable);
	}

	/**
	 * Generate tables and data for tables that do not exist yet.
	 * 
	 * @return true, if successful
	 */
	public boolean generateTablesAndInitialData() {
		try {
			generateKeys();
			generateUsers();
			generateAccounts();
			generateTransactions();
			generateContributions();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void generateKeys() throws SQLException {
		if (db.createTable(keysTable, "(KeyID CHAR(8) PRIMARY KEY, NextKey INTEGER)")) {
			// Key row for contributions
			String sql= "INSERT INTO JKEKeys (KeyID, NextKey) values ('" + contribKeyRowId + "', 1)";
			PreparedStatement ps= db.getConnection().prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
		} else
			System.out.println("Table " + keysTable + " already exists.");
	}

	/**
	 * Generate users.
	 * 
	 * @throws SQLException the sQL exception
	 */
	private void generateUsers() throws SQLException {
		if (db.createTable(userTable, "(FirstName varchar(40), LastName varchar(40), UserName varchar(40), Password varchar(40), PRIMARY KEY (UserName))")) {
			loader.pushUserBean(new UserBean("jbrown", "Julie", "Brown"), "jbrown");
			loader.pushUserBean(new UserBean("rbetts", "Rita", "Betts"), "rbetts");
		} else
			System.out.println("Table " + userTable + " already exists.");
	}

	/**
	 * Generate accounts.
	 * 
	 * @throws SQLException the sQL exception
	 */
	private void generateAccounts() throws SQLException {
		if (db.createTable(acctTable, "(AccountNumber INTEGER PRIMARY KEY, Balance DOUBLE, Dividends DOUBLE, DividendsETD DOUBLE, Contributions DOUBLE, ContributionsETD DOUBLE, UserName varchar(40) NOT NULL, AccountType VARCHAR(30) NOT NULL, UNIQUE(UserName, AccountType))")) {
			loader.pushAccountBean(new AccountBean(200, 2000.00, 1000.00, 5500.00, 0.00, 0.00, "jbrown", "Checking"));
			loader.pushAccountBean(new AccountBean(201, 12500.00, 500.00, 3500.00, 0.00, 0.00, "jbrown", "Savings"));
			loader.pushAccountBean(new AccountBean(202, 25.00, 500.00, 5.00, 0.00, 0.00, "jbrown", "IRA"));
			loader.pushAccountBean(new AccountBean(203, 500.00, 500.00, 35.00, 0.00, 0.00, "jbrown", "Money_Market"));

			loader.pushAccountBean(new AccountBean(300, 500.00, 500.00, 1000.00, 0.00, 0.00, "rbetts", "Checking"));
			loader.pushAccountBean(new AccountBean(301, 3400.00, 500.00, 100.00, 0.00, 0.00, "rbetts", "Savings"));
			loader.pushAccountBean(new AccountBean(302, 5500.00, 500.00, 50.00, 0.00, 0.00, "rbetts", "IRA"));
			loader.pushAccountBean(new AccountBean(303, 75.00, 500.00, 5.00, 0.00, 0.00, "rbetts", "Money_Market"));
		} else
			System.out.println("Table " + acctTable + " already exists.");
	}

	/**
	 * Generate transactions.
	 * 
	 * @throws SQLException the sQL exception
	 */
	private void generateTransactions() throws SQLException {
		if (db.createTable(transTable, "(TransactionID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), Type VARCHAR(30), AccountNumber INTEGER, Source VARCHAR(50), Amount DOUBLE, PostBalance DOUBLE, Date VARCHAR(20), ContributionID INTEGER, CHECK(((Type = 'Donation') AND (ContributionID IS NOT NULL)) OR ((Type <> 'Donation') AND (ContributionID IS NULL))), PRIMARY KEY (TransactionID))")) {
			loader.pushTransactionBean(new TransactionBean("Withdrawl", 200, "Bills", 5.75, 6790.00, "5/03/2010"));
			loader.pushTransactionBean(new TransactionBean("Withdrawl", 200, "Bills", 600.00, 6175.00, "5/05/2010"));
			loader.pushTransactionBean(new TransactionBean("Deposit", 200, "Pay", 3000.00, 9175.00, "5/14/2010"));
			loader.pushTransactionBean(new TransactionBean("Withdrawl", 200, "Food", 175.00, 9000.00, "5/22/2010"));
			loader.pushTransactionBean(new TransactionBean("Withdrawl", 200, "Car", 7000.00, 2000.00, "5/25/2010"));
		} else
			System.out.println("Table " + transTable + " already exists.");
	}

	private void generateContributions() throws SQLException {
		if (db.createTable(contribTable, "(ContributionID INTEGER PRIMARY KEY, AccountNumber INTEGER, Organization VARCHAR(50), Percentage DOUBLE, Date VARCHAR(20))")) {
			// No Existing Data
		} else
			System.out.println("Table " + contribTable + " already exists.");
	}
}