//=============================================================================================
// AccountSearchPane.java
// 	This pane is the screen that allows for the searching of accounts.
//=============================================================================================

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class AccountSearchPane extends JPanel
{
	// GUI Variables
	private JTextField searchBar;
	private JPanel resultsPanel;
	private JButton searchButton;

	// System Variables
	private AccountAPI accountList;	

	public AccountSearchPane()
	{
		// Configure Panel
		setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		// The panels below are just for controlling the layout.
		// 	Without them, the center panel will consume all
		// 	the space in an ugly mess. 
		JPanel bottomPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();

		// Search bar configuration
		searchBar = new JTextField(20);
		searchButton = new JButton("Search");

		topPanel.setLayout(new FlowLayout());
		topPanel.add(searchBar);
		topPanel.add(searchButton);
		add(topPanel, BorderLayout.NORTH);
		
		// Search Results bar
		resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridLayout(30,1));
		resultsPanel.setBackground(Color.white);
		resultsPanel.setSize(100, 200);
		add(resultsPanel, BorderLayout.CENTER);

		// Spacing bars
		bottomPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		add(bottomPanel, BorderLayout.SOUTH);
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);

	}

	//=====================================================================
	// Functions
	//=====================================================================

	public void addButtonListener(ActionListener lis)
	{
		searchButton.addActionListener(lis);
	}

	public int getAccountID(int i)
	{
		return accountList.getAccountID(i);
	}

	public void refreshScreen(MouseListener mis, Session sesh, Connector cx)
	{
		// Get the accounts
		String cmdString;
		String output;
		String query;
		JLabel name;

		query = searchBar.getText();

		cmdString = cx.generateAccountSearchString(query, sesh.getToken());
		output = cx.postAPIRequest(cmdString);
		accountList = cx.decodeAllAccountsCall(output);
		accountList.count();

		System.out.println(cmdString);
		resultsPanel.removeAll();

		// Populate the search bar
		if(accountList.getCode()>0)
		{
			for(int i=0; i<accountList.getNum(); i++)
			{
				name = new JLabel();
				name.setText(accountList.getAccountName(i));
				name.setForeground(Color.blue);
				name.addMouseListener(mis);
				name.setName(Integer.toString(accountList.getAccountID(i)));
				resultsPanel.add(name);
			}
		}
		else
		{
			name = new JLabel();
			name.setText(accountList.getMsg());
			name.setForeground(Color.gray);
			resultsPanel.add(name);
		}

	}
}
