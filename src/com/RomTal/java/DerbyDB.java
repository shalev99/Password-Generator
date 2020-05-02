package com.RomTal.java;
import javax.swing.*;
import java.sql.*;

import static com.RomTal.java.PassGeneratorDemo.logger;

/***
 *Responsible Manage database
 */
public class DerbyDB{
	private Connection conn;
	public static int flag=0;
	private boolean hasData = false;
	public static String driver = "org.apache.derby.jdbc.ClientDriver";
	public static String protocol = "jdbc:derby://localhost:1527/gagamoDB;create=true";
	private void getConnection() {

		try
		{
			Class.forName(driver); //loads class with this name
			conn = DriverManager.getConnection(protocol);
			System.out.println("Established Connection to DB");
			initialize();
		}
		catch(ClassNotFoundException | SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	private void initialize() {
		if (!hasData) {
			hasData = true;
			try
			{
				Statement state = conn.createStatement();
				//state.execute("DROP TABLE data");//clean data if needed
					if(flag==0) {//check if its first login
						System.out.println("Checking database for table");
						DatabaseMetaData databaseMetadata = conn.getMetaData();
						ResultSet resultSet = databaseMetadata.getTables(null, null, "DATA", null);//checking if table data exist
						if (resultSet.next()) {
							System.out.println("TABLE ALREADY EXISTS");
						} else {
							state.execute("create table data(account varchar(60) ,username varchar(60) ,url varchar(120) ,password varchar(60),KeyVal varchar(60))");
						}
						flag++;
					}
				ResultSet res = state.executeQuery("SELECT account FROM data");
				if(!res.next()) {
					System.out.println("Building data table.");
					logger.info(this.getClass().getName() + " Building data table");
				}
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

	/***
	 * Close connection to database
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		if (conn == null) {
			getConnection();
		}
		conn.close();
		conn=null;
		System.out.println("Layout panel successfully close connection.");
	}

	/***
	 * get a reference to all account saved in database
	 * @return reference to all account saved in database
	 * @throws SQLException if connection fail
	 */
	public ResultSet displayUsers() throws SQLException {
		if (conn == null) {
			getConnection();
		}
		Statement state = conn.createStatement();
		ResultSet res = state.executeQuery("SELECT account, username, url, password, KeyVal FROM data");
		return res;
	}

	/***
	 * get a reference to account match key in database
	 * @param KeyVal key of account
	 * @return
	 * @throws SQLException
	 */
	public ResultSet displayInfo(String KeyVal) throws SQLException {
		if (conn == null) {
			getConnection();
		}
		Statement state = conn.createStatement();
		ResultSet res = state.executeQuery("SELECT account, username, url, password FROM data WHERE KeyVal = '" + KeyVal + "'");
		return res;
	}

	/***
	 *
	 * @return get amount of account saved in database
	 * @throws SQLException
	 */
	public int displayAmount() throws SQLException {
		if (conn == null) {
			getConnection();
		}
		int count = 0;
		PreparedStatement state = conn.prepareStatement("SELECT * FROM data");
		ResultSet res = state.executeQuery();
		while (res.next()) {
			count++;
		}
		return count;
	}

	/***
	 * Add a user to database
	 * @param account name of account
	 * @param username name of username
	 * @param url value of url
	 * @param password url of password
	 * @param KeyVal url of KeyVal
	 */
	public void addUser(String account, String username,String url, String password, String KeyVal) {
		if (conn == null) {
			getConnection();
		}
		try
		{
			PreparedStatement prep = conn.prepareStatement("INSERT INTO data VALUES(?,?,?,?,?)");
			prep.setString(1, account);
			prep.setString(2, username);
			prep.setString(3, url);
			prep.setString(4, password);
			prep.setString(5, KeyVal);
			prep.execute();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/***
	 * Change username of account match key in database
	 * @param KeyVal key of account
	 * @param newUsername New username to change for
	 */
	public void changeUser(String KeyVal, String newUsername) {
		if (conn == null) {
			getConnection();
		}
		try
		{
			PreparedStatement prep = conn.prepareStatement("UPDATE data SET username = '" + newUsername + "' WHERE KeyVal = '" + KeyVal + "'");
			prep.execute();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/***
	 * Change password of account match key in database
	 * @param KeyVal key of account
	 * @param newPassword New password to change for
	 */
	public void changePass(String KeyVal, String newPassword) {
		if (conn == null) {
			getConnection();
		}
		try
		{
			PreparedStatement prep = conn.prepareStatement("UPDATE data SET password = '" + newPassword + "' WHERE KeyVal = '" + KeyVal + "'");
			prep.execute();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	/***
	 * Change url of account match key in database
	 * @param KeyVal key of account
	 * @param newUrl New url to change for
	 */
	public void changeSite(String KeyVal, String newUrl) {
		if (conn == null) {
			getConnection();
		}
		try
		{
			PreparedStatement prep = conn.prepareStatement("UPDATE data SET url = '" + newUrl + "' WHERE KeyVal = '" + KeyVal + "'");
			prep.execute();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/***
	 * deleting password of account match key in database
	 * @param KeyVal key of account
	 */
	public void deleteUser(String KeyVal) {
		if (conn == null) {
			getConnection();
		}
		try
		{
			PreparedStatement prep = conn.prepareStatement("DELETE FROM data WHERE KeyVal = '" + KeyVal + "'");
			prep.execute();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}
}
