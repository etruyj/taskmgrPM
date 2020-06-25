//==============================================================================
// AccountDetailPane.java
// 	This is the detailed representation of the account screen. This is 
// 	the screen where accounts, contacts, and projects are created and 
// 	managed.
//==============================================================================

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;

public class AccountDetailPane extends JPanel
{
	// GUI Components
	// Panes
	private JTabbedPane bodyPane;
	
	// Account Pane
	private JTextField accountTitle;
	private JTextArea accountDetails;
	private JButton accCancelButton;
	private JButton accNewButton;
	private JButton accSaveButton;
	
	// Contacts Pane
	private JPanel contactWindow;
	private JTextField conFirstName;
	private JTextField conLastName;
	private JTextField conPosition;
	private JTextField conPhone;
	private JTextField conEmail;
	private JButton conCancelButton;
	private JButton conNewButton;
	private JButton conSaveButton;
	
	// Projects Pane
	private JPanel projectWindow;
	private JTextField projectName;
	private JButton projCancelButton;
	private JButton projNewButton;
	private JButton projSaveButton;
	
	// Variables
	private ContactsAPI contactList;
	private ProjectsAPI projectList;
	private int accountID;
	private int contactID;
	private int projectID;
	private String accountName;

	public AccountDetailPane()
	{
		accountName = "None";
		accountID = -1;
		contactID = -1;
		projectID = -1;

		// Declare Components
		// Arrange Panel
		setLayout(new BorderLayout());
		
		accountTitle = new JTextField();
		accountTitle.setText("Empty Account");
		add(accountTitle, BorderLayout.NORTH);

		bodyPane = new JTabbedPane();
		add(bodyPane, BorderLayout.CENTER);

		//=================================
		// Account Tab
		//=================================
		
		JPanel accPanel = new JPanel();
		JPanel accBTNPane = new JPanel();
		accPanel.setLayout(new BorderLayout());
		accBTNPane.setLayout(new FlowLayout());
		
		accountDetails = new JTextArea();
		accountDetails.setText("We'll add this functionality later.");
		accCancelButton = new JButton("Cancel");
		accCancelButton.setName("Exit");
		accNewButton = new JButton("New");
		accNewButton.setName("New Account");
		accSaveButton = new JButton("Save");
		accSaveButton.setName("Save Account");
		accBTNPane.add(accNewButton);
		accBTNPane.add(accSaveButton);
		accBTNPane.add(accCancelButton);
		
		accPanel.add(accountDetails, BorderLayout.CENTER);
		accPanel.add(accBTNPane, BorderLayout.SOUTH);
		bodyPane.add("Account", accPanel);
		
		//=================================
		// Contacts Tab
		//=================================
	
		JPanel conPanel = new JPanel();
		contactWindow = new JPanel();
		JPanel contactDetails = new JPanel();
		JPanel conBTNPane = new JPanel();
		conPanel.setLayout(new BorderLayout());
		contactWindow.setBackground(Color.white);
		contactWindow.setLayout(new GridLayout(30,1));
		contactWindow.setPreferredSize(new Dimension(100, 100));
		contactDetails.setSize(200, 100);
		contactDetails.setLayout(new GridLayout(10, 1));
		conBTNPane.setLayout(new FlowLayout());

		// Contact Pane
		JLabel emptyMSG = new JLabel();
		emptyMSG.setText("No contacts");
		emptyMSG.setForeground(Color.gray);
		contactWindow.add(emptyMSG);

		// Detail Pane
		// Define components
		JLabel firstNamel = new JLabel();
		JLabel lastNamel = new JLabel();
		JLabel positionl = new JLabel();
		JLabel phonel = new JLabel();
		JLabel emaill = new JLabel();
		firstNamel.setText("First Name:");
		lastNamel.setText("Last Name:");
		positionl.setText("Position:");
		phonel.setText("Phone:");
		emaill.setText("Email:");

		conFirstName = new JTextField(10);
		conLastName = new JTextField(10);
		conPosition = new JTextField(15);
		conPhone = new JTextField(10);
		conEmail = new JTextField(10);

		contactDetails.add(firstNamel);
		contactDetails.add(conFirstName);
		contactDetails.add(lastNamel);
		contactDetails.add(conLastName);
		contactDetails.add(positionl);
		contactDetails.add(conPosition);
		contactDetails.add(phonel);
		contactDetails.add(conPhone);
		contactDetails.add(emaill);
		contactDetails.add(conEmail);

		// Button Pane
		conCancelButton = new JButton("Cancel");
		conCancelButton.setName("Exit");
		conNewButton = new JButton("New");
		conNewButton.setName("New Contact");
		conSaveButton = new JButton("Save");
		conSaveButton.setName("Save Contact");

		conBTNPane.add(conNewButton);
		conBTNPane.add(conSaveButton);
		conBTNPane.add(conCancelButton);

		conPanel.add(contactWindow, BorderLayout.WEST);
		conPanel.add(contactDetails, BorderLayout.CENTER);
		conPanel.add(conBTNPane, BorderLayout.SOUTH);
		bodyPane.add("Contacts", conPanel);
		
		//=================================
		// Projects Tab
		//=================================

		JPanel projPanel = new JPanel();
		projectWindow = new JPanel();
		JPanel projDetails = new JPanel();
		JPanel projBTNPane = new JPanel();
		projPanel.setLayout(new BorderLayout());
		projectWindow.setLayout(new GridLayout(30,1));
		projDetails.setLayout(new GridLayout(10,2));
		projBTNPane.setLayout(new FlowLayout());

		// Project List
		projectWindow.setBackground(Color.white);
		projectWindow.setSize(100, 100);
		// Defining and adding this JLabel is necessary
		// 	in order to force the panel to render.
		JLabel projMSG = new JLabel();
		projMSG.setText("No Projects");
		projMSG.setForeground(Color.gray);
		projectWindow.add(projMSG);

		// Project Details
		projDetails.setSize(200,100);
		JLabel projNamel = new JLabel();
		projNamel.setText("Project Name:");
		projectName = new JTextField(7);
		projDetails.add(projNamel);
		projDetails.add(projectName);

		// Button Panel
		projCancelButton = new JButton("Cancel");
		projCancelButton.setName("Exit");
		projNewButton = new JButton("New");
		projNewButton.setName("New Project");
		projSaveButton = new JButton("Save");
		projSaveButton.setName("Save Project");
		projBTNPane.add(projNewButton);
		projBTNPane.add(projSaveButton);
		projBTNPane.add(projCancelButton);
		
		projPanel.add(projectWindow, BorderLayout.WEST);
		projPanel.add(projDetails, BorderLayout.CENTER);
		projPanel.add(projBTNPane, BorderLayout.SOUTH);

		bodyPane.add("Projects", projPanel);
	}

	//=========================================================
	// FUNCTIONS
	//=========================================================

	public void addBTNListener(ActionListener lis)
	{
		accCancelButton.addActionListener(lis);
		accNewButton.addActionListener(lis);
		accSaveButton.addActionListener(lis);
		conCancelButton.addActionListener(lis);
		conNewButton.addActionListener(lis);
		conSaveButton.addActionListener(lis);
		projCancelButton.addActionListener(lis);
		projNewButton.addActionListener(lis);
		projSaveButton.addActionListener(lis);
	}

	public int getAccountID()
	{
		return accountID;
	}

	public void loadAccount(int acc_id, String acc_name)
	{
		accountID = acc_id;
		accountName = acc_name;

		accountTitle.setText(acc_name);
	}

	public void loadContacts(int acc_id, MouseListener mis, Session sesh, Connector cx)
	{
		// Get Contacts from database
		String cmdString;
		String output;
		
		cmdString = cx.generateContactsString(acc_id, sesh.getToken());
		output = cx.postAPIRequest(cmdString);
		contactList = cx.decodeContactsCall(output);
		contactList.count();

		// Populate the contacts window
		JLabel contact;
		contactWindow.removeAll();
		
		if(contactList.getNum()>0)
		{
			for(int i=0; i<contactList.getNum(); i++)
			{
				contact = new JLabel();
				contact.setText(contactList.getFullName(i));
				contact.setName(Integer.toString(contactList.getContactID(i)));
				contact.addMouseListener(mis);
				contact.setForeground(Color.blue);
				// Add Listener here
				contactWindow.add(contact);
			}
		}
		else
		{
			contact = new JLabel();
			contact.setText("No Contacts");
			contact.setForeground(Color.gray);
			contactWindow.add(contact);
		}


	}

	public void loadProjects(int acc_id, MouseListener mis, Session sesh, Connector cx)
	{
		// Get the project list
		String cmdString;
		String output;

		cmdString = cx.generateProjectString(acc_id, sesh.getToken());
		output = cx.postAPIRequest(cmdString);
		projectList = cx.decodeProjectCall(output);
		projectList.count();

		// Load the projects window
		projectWindow.removeAll();
		JLabel project;

		if(projectList.getNum()>0)
		{
			for(int i=0; i<projectList.getNum(); i++)
			{
				project = new JLabel();
				project.setText(projectList.getProjectName(i));
				project.setName(Integer.toString(projectList.getProjectID(i)));
				project.addMouseListener(mis);
				project.setForeground(Color.blue);
				projectWindow.add(project);
			}
		}
		else
		{
			project = new JLabel();
			project.setText("No Projects");
			project.setForeground(Color.gray);
			projectWindow.add(project);
		}
	}

	public void refreshContactClear()
	{
		contactID = -1;
		conFirstName.setText("");
		conLastName.setText("");
		conPosition.setText("");
		conEmail.setText("");
		conPhone.setText("");
	}

	public void refreshContactDetails(int id)
	{
		for(int i=0; i<contactList.getNum(); i++)
		{
			if(id == contactList.getContactID(i))
			{
				conFirstName.setText(contactList.getFirstName(i));
				conLastName.setText(contactList.getLastName(i));
				conPosition.setText(contactList.getPosition(i));
				conEmail.setText(contactList.getEmail(i));
				conPhone.setText(contactList.getPhone(i));
				contactID = contactList.getContactID(i);
			}
		}
	}

	public void refreshProjectClear()
	{
		projectID = -1;
		projectName.setText("");
	}

	public void refreshProjectDetails(int id)
	{
		for(int i=0; i<projectList.getNum(); i++)
		{
			if(id == projectList.getProjectID(i))
			{
				projectName.setText(projectList.getProjectName(i));
				projectID = projectList.getProjectID(i);
			}
		}
	}

	public void saveAccount(Session sesh, Connector cx)
	{
		// Check to ensure valid input
		if(accountTitle.equals(""))
		{
			errorDialog("Please input an account name.");
		}
		else
		{
			// Copy values to 
			accountName = accountTitle.getText();

			// Write to save
			String cmdString;
			String output;

			cmdString = cx.generateSaveAccountString(accountID, accountName, sesh.getToken());
			output = cx.postAPIRequest(cmdString);

			// Necessary to prevent saving duplicates if the save
			// button is pressed more than once.
			accountID = cx.decodeSaveID(output);
		}
	}
		
	public void saveContact(Session sesh, Connector cx)
	{
		// Error handling. Using If/Else If statements to keep this cleaner
		// 	than using nested ifs.
		if(accountID<0)
		{
			errorDialog("Please save the account befored adding a contact.");
		}
		// Ensure both first and last name were saved as a
		// minimal input.
		else if(conFirstName.getText().equals("") || conLastName.getText().equals(""))
		{
			errorDialog("Please input both a first and last name.");
		}
		else
		{
			String cmdString;
			String output;

			cmdString = cx.generateSaveContactString(contactID, accountID, conFirstName.getText(), conLastName.getText(), conPosition.getText(), conEmail.getText(), conPhone.getText(), false, sesh.getToken());
			output = cx.postAPIRequest(cmdString);

			// Necessary to prevent saving duplicates if the save
			// button is pressed more than once.
			contactID = cx.decodeSaveID(output);
		}
	}

	public void saveProject(Session sesh, Connector cx)
	{
		// Error handling. Using If/Else If statements to keep this cleaner
		// 	than using nested ifs.
		if(accountID<0)
		{
			errorDialog("Please save the account befored adding a contact.");
		}
		else if(projectName.getText().equals(""))
		{
			errorDialog("Please input a project name.");
		}
		else
		{
			String cmdString;
			String output;

			cmdString = cx.generateSaveProjectString(projectID, accountID, projectName.getText(), sesh.getToken());
			output = cx.postAPIRequest(cmdString);

			// Necessary to prevent saving duplicates if the save
			// button is pressed more than once.
			projectID = cx.decodeSaveID(output);
		}
	}

	//==================================================================
	// Inner Class
	// 	This is just for the error dialog box
	//==================================================================
	
	private void errorDialog(String errMSG)
	{
			JDialog erm = new JDialog((JFrame)SwingUtilities.windowForComponent(this), "Error: Invalid Input");
			JPanel pan = new JPanel();
			pan.setLayout(new BorderLayout());
			JLabel msg = new JLabel(errMSG);
			JButton closeBTN = new JButton("Ok");
			closeBTN.setPreferredSize(new Dimension(30, 30));
			closeBTN.addActionListener(
						new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								erm.dispose();
							}
						}
					);	
			pan.add(msg, BorderLayout.CENTER);
			pan.add(closeBTN, BorderLayout.SOUTH);
			
			erm.add(pan);
			erm.setPreferredSize(new Dimension(300, 200));
			erm.pack();
			erm.setLocationRelativeTo((JFrame)SwingUtilities.windowForComponent(this));
			erm.setVisible(true);
			erm.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}
	
}
