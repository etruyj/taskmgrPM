//=============================================================================
// taskDetailPane.java
// 	This is the detailed screen for each of the traces. It is where traces
// 	are edited, updated, and created.
//=============================================================================

import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class taskDetailPane extends JPanel
{
	private JComboBox<String> listOfAccounts;
	private JComboBox<String> listOfContacts;
	private JComboBox<String> listOfProjects;
	private JComboBox<String> traceTypeList;

	private JTextField dateField;
	private JTextField timeField;
	private JTextField subjectField;
	private JTextField durationField;

	private JCheckBox completeBox;

	private JTextArea detailsArea;

	private JButton newTask;
	private JButton saveTask;
	private JButton cancelTask;

	// Container for the API call results.
	private traceInfo details; 

	// Drop-Down Box Arrays
	private String[] traceTypes;
	private accountList accounts;
	private contactList contacts;
	private projectList projects;
	private int numTraceTypes;

	public taskDetailPane()
	{
		setLayout(new BorderLayout());

		// Set Drop-Down Box Options
		traceTypes = new String[] {"Email", "Meeting - In-Person", "Meeting - Remote", "Paperwork", "Phone Call"};
		numTraceTypes = 5;

		// Arrange Top Bar
		JPanel topbar = new JPanel();
		topbar.setLayout(new FlowLayout());

		// Configurat the dropdown boxes
		listOfAccounts = new JComboBox<> ();
		listOfAccounts.addItem("Account Name");
		listOfContacts = new JComboBox<> ();
		listOfContacts.addItem("Contact Name");
		listOfProjects = new JComboBox<> ();
		listOfProjects.addItem("Project Name");

		// Add components to topbar
		topbar.add(listOfAccounts);
		topbar.add(listOfContacts);
		topbar.add(listOfProjects);

		add(topbar, BorderLayout.NORTH);

		// Arrange Center Bar
		JPanel centerbar = new JPanel();
		centerbar.setLayout(new FlowLayout());

		dateField = new JTextField(7);
		timeField = new JTextField(7);
		durationField = new JTextField(3);
		traceTypeList = new JComboBox<> (traceTypes);
		completeBox = new JCheckBox("Complete");
		subjectField = new JTextField(35);

		// Add components to centerbar
		centerbar.add(dateField);
		centerbar.add(timeField);
		centerbar.add(durationField);
		centerbar.add(traceTypeList);
		centerbar.add(subjectField);
		centerbar.add(completeBox);

		add(centerbar, BorderLayout.CENTER);

		// Arrange Bottom Bar
		JPanel bottombar = new JPanel();
		bottombar.setLayout(new BorderLayout());
		
		JPanel buttonbar = new JPanel();
		buttonbar.setLayout(new FlowLayout());

		detailsArea = new JTextArea(25, 1);
		detailsArea.setWrapStyleWord(true);
		bottombar.add(detailsArea, BorderLayout.CENTER);

		// Bottom buttons
		newTask = new JButton("New");
		saveTask = new JButton("Save");
		cancelTask = new JButton("Cancel");

		buttonbar.add(newTask);
		buttonbar.add(saveTask);
		buttonbar.add(cancelTask);

		bottombar.add(buttonbar, BorderLayout.SOUTH);

		add(bottombar, BorderLayout.SOUTH);
	}

	//========================================================
	// Functions
	//========================================================

	public void addAccountListener(ActionListener lis)
	{
		listOfAccounts.addActionListener(lis);
	}

	public void addButtonListener(String command, ActionListener lis)
	{
		switch(command)
		{
			case "newTask":
				newTask.addActionListener(lis);
				break;
			case "saveTask":
				saveTask.addActionListener(lis);
				break;
			case "cancelTask":
				cancelTask.addActionListener(lis);
				break;
		}
	}

	public void getAccountContactList(Session sesh, int account)
	{
		Gson gson = new Gson();

		String jsonOutput = "{\"msg\": \"connection error\", \"code\": -1}";
		String jsonInput = "{\"cmd\": \"listAccountContacts\", \"account_id\": \""
					+ account + "\", \"token\": \"" 
					+ sesh.getToken() + "\"}";
		
		// Try the connection
		try
		{
			Connector con = new Connector(sesh.getServer());
			jsonOutput = con.postJSON(jsonInput);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		contacts = gson.fromJson(jsonOutput, contactList.class);
		contacts.count();

		// Load the contacts drop-down

		// Clear any prior artifacts.
		// Using the same method as getAccountList to perform this task
		// for consistency.
		while(listOfContacts.getItemCount()>1)
		{
			listOfContacts.removeItemAt(1);
		}

		// Add all names to the list.
		for(int i=0; i<contacts.getNumContacts(); i++)
		{
			listOfContacts.addItem(contacts.getFullName(i));
		}
	}

	public void getAccountList(Session sesh)
	{
		Gson gson = new Gson();

		String jsonOutput = "{\"msg\": \"connection error\", \"code\": -1}";
		String jsonInput = "{\"cmd\": \"listAccounts\", \"token\": \"" + sesh.getToken() + "\"}";
		
		// Try the connection
		try
		{
			Connector con = new Connector(sesh.getServer());
			jsonOutput = con.postJSON(jsonInput);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		accounts = gson.fromJson(jsonOutput, accountList.class);
		accounts.count();

		// Load the account drop-down
		
		// Clear any prior artifacts.
		// The event listener attached to this combobox prevents the
		// use of removeAllItems(). Using this function results in 
		// runtime errors.
		
		while(listOfAccounts.getItemCount()>1)
		{
			listOfAccounts.removeItemAt(1);
		}

		// Add all account names to the list.
		for(int i=0; i<accounts.getNumAccounts(); i++)
		{
			listOfAccounts.addItem(accounts.getAccountName(i));
		}
	}

	public void getAccountProjectList(Session sesh, int account)
	{
		Gson gson = new Gson();

		String jsonOutput = "{\"msg\": \"connection error\", \"code\": -1}";
		String jsonInput = "{\"cmd\": \"listAccountProjects\", \"account_id\": \""
					+ account + "\", \"token\": \"" 
					+ sesh.getToken() + "\"}";
		
		// Try the connection
		try
		{
			Connector con = new Connector(sesh.getServer());
			jsonOutput = con.postJSON(jsonInput);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		projects = gson.fromJson(jsonOutput, projectList.class);
		projects.count();

		// Load the contacts drop-down

		// Clear any prior artifacts.
		// Using the same method as getAccountList to perform this task
		// for consistency.
		while(listOfProjects.getItemCount()>1)
		{
			listOfProjects.removeItemAt(1);
		}

		// Add all names to the list.
		for(int i=0; i<projects.getNumProjects(); i++)
		{
			listOfProjects.addItem(projects.getProject(i));
		}

	}

	public void loadFreshInfo(Session sesh)
	{
		//===========================================
		// loadFreshInfo
		// 	Load the detail page with an empty template
		// 	for use with the creation of new traces.
		//===========================================

		// Prep the template.
		getAccountList(sesh);

		// Load the page
		listOfAccounts.setSelectedIndex(0);
		listOfContacts.setSelectedIndex(0);
		listOfProjects.setSelectedIndex(0);
		traceTypeList.setSelectedIndex(0);
		dateField.setText(sesh.getCurDate());
		timeField.setText("12:00");
		durationField.setText("5");
		subjectField.setText("");
		detailsArea.setText("");
		completeBox.setSelected(false);

		// Reset the traceInfo (details)
		details.newTrace(sesh);

	}

	public void loadTraceInfo(Session sesh, String trace_id)
	{
		//===========================================
		// loadTraceInfo
		// Get trace details from the database and load
		// them into the template.
		//===========================================

		// Prep the template.
		getAccountList(sesh);

		// Get the trace information.
		Gson gson = new Gson();

		String jsonOutput = "{\"msg\": \"connection error\", \"code\": -1}";
		String jsonInput = "{\"cmd\": \"listTraceDetails\", \"trace_id\": \""
			+ trace_id + "\", \"token\": \"" + sesh.getToken() + "\"}";
		// Try the connection
		try
		{
			Connector con = new Connector(sesh.getServer());
			jsonOutput = con.postJSON(jsonInput);
		}
		catch(Exception e)
		{
			System.out.println(sesh.getServer());
			System.out.println(e);
		}

		// Load the info in the template.
		details = gson.fromJson(jsonOutput, traceInfo.class);

		if(details.getCode()>0)
		{
			// Set drop-down boxes.
			listOfAccounts.setSelectedItem(details.getAccount());
			listOfContacts.setSelectedItem(details.getLastName() + ", " + details.getFirstName());
			listOfProjects.setSelectedItem(details.getProject());
			traceTypeList.setSelectedItem(details.getType());			

			// Set trace information
			dateField.setText(details.getDate());
			timeField.setText(details.getTime());
			durationField.setText(Integer.toString(details.getDuration()));

			completeBox.setSelected(details.getCompleted());

			// Set trace information
			subjectField.setText(details.getSubject());
			detailsArea.setText(details.getText());	
		}
		else
		{
			System.out.println(details.getMsg());
		}
	}

	public boolean saveTrace(Session sesh)
	{
		System.out.println(details.getTraceID());
		boolean result = false;
		/*
		if(validTraceInfo())
		{
			result = uploadTrace(sesh);
		}
		*/
		return result;
	}

	public boolean uploadTrace(Session sesh)
	{
		Gson gson = new Gson();

		String jsonOutput = "{\"msg\": \"connection error\", \"code\": -1}";
		String jsonInput = "{\"cmd\": \"saveTrace\", \"trace_id\": \""
				+ details.getTraceID() + "\", \"token\": \"" + sesh.getToken() + "\"}";
		return true;
	}

	public void updateDropDowns(Session sesh, String selection)
	{
		getAccountContactList(sesh, accounts.getAccountID(selection));
		getAccountProjectList(sesh, accounts.getAccountID(selection));
	}

	public boolean validateTraceInfo()
	{
		boolean testResult = false;

		if(listOfAccounts.getSelectedIndex()>0)
		{
			testResult = true;
		}
		else
		{
			System.out.println("Please choose and account for this trace.");
		}

		if(listOfContacts.getSelectedIndex()>0)
		{
			testResult = true;
		}
		else
		{
			System.out.println("Please assign a contact to this trace.");
		}

		if(listOfProjects.getSelectedIndex()>0)
		{
		//	System.out.
		}
		else
		{
		}

		return true;

	}
}
