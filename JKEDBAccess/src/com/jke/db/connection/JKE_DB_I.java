/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.db.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A singleton of this interface will exist for the application. The singleton
 * is obtained via JKEDBFactory.getFactory().getDB(); The default implementation
 * provides a simple single connection interface to the JKE database.
 */
public interface JKE_DB_I {
	/**
	 * Returns the connection to the database or throws an SQLException.
	 * 
	 * @return connection to the JKE database
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException;

	/**
	 * Shuts down the JKE database. All connections will be invalid.
	 */
	public void shutdown();

	/**
	 * Ensures that the table exists. Creates it, if needed.
	 * 
	 * @param tableName the name of the table
	 * @param columnsClause the columns clause, e.g.
	 * "(id BIGINT PRIMARY KEY, name VARCHAR(40))"
	 * @return true if the table was created or false if it already existed.
	 * @throws SQLException
	 */
	public boolean createTable(String tableName, String columnsClause) throws SQLException;

	/**
	 * Drops the table if it exists.
	 * 
	 * @param tableName the name of the table
	 * @return true if the table was dropped or false if it did not exist.
	 */
	public boolean dropTable(String tableName);
}