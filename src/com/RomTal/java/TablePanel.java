package com.RomTal.java;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * GUI Manager
 */
public class TablePanel extends JFrame implements IView {
    private static final long serialVersionUID = 125631468412L;
    private DerbyDB database = new DerbyDB();
    private String[] columnNames = {"Account Name", "Username", "URL", "Password", "Key"};
    private String[][] data;
    private int amountAccounts;
    private static String selectedAccount,selectedKey;
    private JTable table;
    private JPanel buttonPanel; // To hold a button
    private JPanel mainPanel;

    public TablePanel() {
        setTitle("PassGenerator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        buildButtonPanel();        // Build the panels.
        buildPanel();
        add(buttonPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        pack();
        setSize(800,600);
        setVisible(true);
    }
    private void buildPanel() {
        buildDataArray();
        mainPanel = new JPanel();
        table = new JTable(new TableModel());
        mainPanel.add(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel listSelectionModel = table.getSelectionModel();
        listSelectionModel.addListSelectionListener(new TableListener());
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);
        table.getColumnModel().getColumn(4).setWidth(0);
        mainPanel.add(scrollPane);
        mainPanel.setLayout(new GridLayout(1,0,0,0));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Data"));
    }
    @Override
    public void rebuildPanel(){
        remove(mainPanel);
        buildPanel();
        this.add(mainPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        System.gc();
    }
    private void buildDataArray() {
    try {
        amountAccounts = database.displayAmount();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }

    data = new String[amountAccounts][5];
    ResultSet res;
        int row = 0;
    try {
        res = database.displayUsers();
        while(res.next()) {
            data[row][0] = res.getString("Account");
            data[row][1] = res.getString("Username");
            data[row][2] = res.getString("URL");
            data[row][3] = "****************";
            //data[row][3] = res.getString("Password");
            data[row][4] = res.getString("KeyVal");
            row++;
        }
        res.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    private void buildButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6,1,0,10));
        //finalize buttons
        JButton addPass = new JButton("Add Account");
        JButton delPass = new JButton("Delete Account");
        JButton seePass = new JButton("Copy/View Credentials");
        JButton altUser = new JButton("Change Username");
        JButton altPass = new JButton("Change Password");
        JButton altSite = new JButton("Change Website");
        addPass.addActionListener(new PassGeneratorDemo.AddPassListener());
        delPass.addActionListener(new PassGeneratorDemo.DelPassListener());
        seePass.addActionListener(new PassGeneratorDemo.SeePassListener());
        altUser.addActionListener(new PassGeneratorDemo.AltUserListener());
        altPass.addActionListener(new PassGeneratorDemo.AltPassListener());
        altSite.addActionListener(new PassGeneratorDemo.AltSiteListener());
        //set border around buttons
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Operation "));
        //adds buttons to the panel
        buttonPanel.add(addPass);
        buttonPanel.add(delPass);
        buttonPanel.add(seePass);
        buttonPanel.add(altUser);
        buttonPanel.add(altPass);
        buttonPanel.add(altSite);
    }
    @Override
    public String getSelection() {
        return selectedAccount;
    }
    @Override
    public String getSelectionKey() {
        return selectedKey;
    }
    public void setSelection(){
        selectedAccount=null;
    }
    @Override
    public void displayCopy(String password){
        JPanel display = new JPanel();
        JLabel passText;
        JButton copyUsername = new JButton("Copy Username");
        JButton copyPassword = new JButton("Copy Password");
        JButton copyUrl = new JButton("Copy Url");
        JButton toWebsite = new JButton("Go to Site");
        copyPassword.addActionListener(new PassGeneratorDemo.CopyPassListener());
        copyUsername.addActionListener(new PassGeneratorDemo.CopyUserListener());
        copyUrl.addActionListener(new PassGeneratorDemo.CopyUrlListener());
        passText = new JLabel("Password:");
        display.add(passText);
        passText = new JLabel(password);
        display.add(passText);
        display.add(copyUsername);
        display.add(copyPassword);
        display.add(copyUrl);
        JOptionPane.showMessageDialog(this, display);
    };
    @Override
    public void displayWarning(){
        JOptionPane.showMessageDialog(this, "Warning no account was selected, retry after selecting an account");
    };
    private class TableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1454874863221235L;
        public int getColumnCount() {
            return columnNames.length;
        }
        public int getRowCount() {
            return data.length;
        }
        public String getColumnName(int col) {
            return columnNames[col];
        }
        public String getValueAt(int rowNum, int colNum) {
            return data[rowNum][colNum];
        }
    }
    private class TableListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            selectedAccount = (String) table.getValueAt(table.getSelectedRow(), 0);
            selectedKey = (String) table.getValueAt(table.getSelectedRow(), 4);
        }
    }
}