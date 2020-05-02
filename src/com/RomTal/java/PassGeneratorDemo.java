package com.RomTal.java;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * Main Application
 */
public class PassGeneratorDemo implements IViewModel{
	public static Logger logger;
	static {
		String file = "logs.txt";
		logger = Logger.getLogger(Class.class.getName());
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.print("");
			writer.close();
			logger.addAppender(new FileAppender(new PatternLayout("%-5p [%t]: %m%n"), file));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static DerbyDB database = new DerbyDB();
	private static TablePanel table= new TablePanel();
	private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	private static Password password;
	private static Password key;
	/***
	 * Add new account (implements listener in Add account button)
	 */
	public static class AddPassListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String accountName = JOptionPane.showInputDialog(table, "Enter Account Name: ");
				while (accountName.contains("'") || accountName.equals("")) {
					accountName = JOptionPane.showInputDialog(table, "Your account name empty, please re-enter account name: ");
				}
				String usernameName = JOptionPane.showInputDialog(table, "Enter Account Username: ");
				while (usernameName.contains("'") || usernameName.equals("")) {
					usernameName = JOptionPane.showInputDialog(table, "Your username name empty, please re-enter username name: ");
				}
				String urlName = JOptionPane.showInputDialog(table, "Enter Website Name: ");
				password = new Password();
				password.generatePassword();
				key = new Password();
				key.generatePassword();
				database.addUser(accountName, usernameName, urlName, password.toString(), key.toString());
				System.out.println(this.getClass().getName() + " Account: " + accountName + " Added Successfully");
				logger.info(this.getClass().getName() + " Account: " + accountName + " Added Successfully");
				table.rebuildPanel();
			} catch (Exception e) {
				System.out.println("Cancel Pressed");
				logger.info("Cancel Pressed");
			}
			password = null;

		}
	}
	/***
	 * deleting account (implements listener in delete account button)
	 */
	public static class DelPassListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ResultSet res;
			if (table.getSelection() == null) {
				table.displayWarning();
			} else {
				try {
					String confirmation = JOptionPane.showInputDialog(table, "Confirm deletion by typing " + table.getSelection() + ":");
					if (confirmation.equalsIgnoreCase(table.getSelection())) {
						res = database.displayInfo(table.getSelectionKey());
						res.next();
						System.out.println(this.getClass() + " Account: " + res.getString("Username") + " Deleted Successfully");
						logger.info(this.getClass() + " Account: " + res.getString("Username") + " Deleted Successfully");
						database.deleteUser(table.getSelectionKey());
						table.setSelection();
						table.rebuildPanel();
					} else {
						JOptionPane.showMessageDialog(table, "Incorrect account name");
					}
				} catch (Exception e) {
					System.out.println("Cancel Pressed");
					logger.info("Cancel Pressed");
				}
			}
		}
	}
	/***
	 * view/copy account credentials (implements listener in copy/view credentials button)
	 */
	public static class SeePassListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ResultSet res;
			try {
				if (table.getSelection() == null) {
					table.displayWarning();
				} else {
					res = database.displayInfo(table.getSelectionKey());
					res.next();
					table.displayCopy(res.getString("Password"));
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	/***
	 * Copy username of selected account (implements listener in copy username button)
	 */
	public static class CopyUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ResultSet res;
			StringSelection stringSelection;
			try {
				res = database.displayInfo(table.getSelectionKey());
				res.next();
				stringSelection = new StringSelection(res.getString("Username"));
				clipboard.setContents(stringSelection, null);
				System.out.println(this.getClass().getName() + " Username: " + res.getString("Username") + " Copped Successfully");
				logger.info(this.getClass().getName() + " Username: " + res.getString("Username") + " Copped Successfully");
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	/***
	 * Copy password of selected account (implements listener in password username button)
	 */
	public static class CopyPassListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ResultSet res;
			StringSelection stringSelection;
			try {
				res = database.displayInfo(table.getSelectionKey());
				res.next();
				stringSelection = new StringSelection(res.getString("Password"));
				clipboard.setContents(stringSelection, null);
				System.out.println(this.getClass().getName() + " Password: " + res.getString("Password") + " Copped Successfully");
				logger.info(this.getClass().getName() + " Password: " + res.getString("Password") + " Copped Successfully");
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	/***
	 * Copy url of selected account (implements listener in url username button)
	 */
	public static class CopyUrlListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ResultSet res;
			StringSelection stringSelection;
			try {
				res = database.displayInfo(table.getSelectionKey());
				res.next();
				stringSelection = new StringSelection(res.getString("Url"));
				clipboard.setContents(stringSelection, null);
				System.out.println(this.getClass().getName() + " Url: " + res.getString("Url") + " Copped Successfully");
				logger.info(this.getClass().getName() + " Url: " + res.getString("Url") + " Copped Successfully");
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	/***
	 * Change username of selected account to new username (implements listener in copy username button)
	 */
	public static class AltUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ResultSet res;
			try {
				if (table.getSelection() != null) {
					String newUsername = JOptionPane.showInputDialog(table, "Enter new username:");
					if (!newUsername.equals("")) {
						res = database.displayInfo(table.getSelectionKey());
						res.next();
						System.out.println(this.getClass() + " Username: " + res.getString("Username") + " Change To New Username: "+ newUsername.toString());
						logger.info(this.getClass() + " Username: " + res.getString("Username") + " Change To New Username: "+ newUsername.toString());
						database.changeUser(table.getSelectionKey(), newUsername);
						table.rebuildPanel();
					} else {
						JOptionPane.showMessageDialog(table, "Warning no replacement was entered, retry after entering a replacement.");
					}
				} else {
					table.displayWarning();
				}
			} catch (Exception e) {
				System.out.println("Cancel Pressed");
				logger.info("Cancel Pressed");
			}
		}
	}
	/***
	 * Change password of selected account to new username (implements listener in copy username button)
	 */
	public static class AltPassListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ResultSet res;
			password = new Password();
			password.generatePassword();
			String newPassword;

			if (table.getSelection() == null) {
				table.displayWarning();
			} else {
				int answer = JOptionPane.showConfirmDialog(table, "Are you sure you would like to regenerate this account's password?", "PassGenerator" ,JOptionPane.YES_NO_OPTION);
				if (answer == 0) {
					int answer2 = JOptionPane.showConfirmDialog(table, "Would you like to enter your own password?", "PassGenerator", JOptionPane.YES_NO_OPTION);
					if (answer2 == 0) {
						newPassword = JOptionPane.showInputDialog(table, "Enter new password:");
						password = new Password(newPassword);
					}
					else
					{
						System.out.println("New Password has Generated");
						logger.info("New Password has Generated");
					}
					try {
						res = database.displayInfo(table.getSelectionKey());
						res.next();
						database.changePass(table.getSelectionKey(), password.toString());
						System.out.println(this.getClass() + " Password: " + res.getString("Password") + " Change To New Password: "+ password.toString());
						logger.info(this.getClass() + " Password: " + res.getString("Password") + " Change To New Password: "+ password.toString());
						table.rebuildPanel();
						} catch (SQLException e) {
						e.printStackTrace();
						}
					JOptionPane.showMessageDialog(table, "Password successfully changed.");
				} else {
					JOptionPane.showMessageDialog(table, "Password not changed");
					logger.info(this.getClass().getName() + "Password not changed");
					System.out.println(this.getClass().getName() + "Password not changed");
				}
			}
			password = null;
		}
	}
	/***
	 * Change url of selected account to new username (implements listener in copy username button)
	 */
	public static class AltSiteListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ResultSet res;
			try {
				if (table.getSelection() != null) {
					String newSite = JOptionPane.showInputDialog(table, "Enter the URL of the new website.");
					if (!newSite.equals("")) {
						res = database.displayInfo(table.getSelectionKey());
						res.next();
						database.changeSite(table.getSelectionKey(), newSite);
						System.out.println(this.getClass() + " URL: " + res.getString("URL") + " Change To New Username: " + newSite.toString());
						logger.info(this.getClass() + " URL: " + res.getString("URL") + " Change To New Username: " + newSite.toString());
						table.rebuildPanel();
					} else {
						 JOptionPane.showMessageDialog(table, "War  ning no replacement was entered, retry after entering a replacement.");
					}
				} else {
					table.displayWarning();
				}
			} catch (Exception e) {
				System.out.println("Cancel Pressed");
				logger.info("Cancel Pressed");
			}
		}

	}

	/***
	 * Start application
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) { new PassGeneratorDemo();}
}
