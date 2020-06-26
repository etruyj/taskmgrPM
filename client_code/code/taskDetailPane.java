//======================================================================================
// TaskDetailPane.java
//	This screen shows the detailed information for the trace. It also the screen
//	where traces are created. 
//======================================================================================

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;		// Just for Error Dialog
import javax.swing.JFrame;		// Just for Error Dialog
import javax.swing.JLabel;		// Just for Error Dialog
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;	// Just for Error Dialog

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;		// Just for Error Dialog

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;	// Just for Error Dialog
import java.awt.event.ItemListener;

public class TaskDetailPane extends JPanel
{
	// GUI Components
	private JComboBox listOfAccounts;
	private JComboBox listOfContacts;
	private JComboBox listOfProjects;
	private JComboBox listOfTypes;
	private JTextField dateField;
	private JTextField timeField;
	private JTextField durationField;
	private JTextField subjectField;
	private JTextArea detailArea;
	private JCheckBox completeBox;
	
	private JButton newTrace;
	private JButton saveTrace;
	private JButton cancelTrace;

	// API Containers
	private AccountAPI accountList;
	private ContactsAPI contactList;
	private ProjectsAPI projectList;
	private TraceInfoAPI details;


	public TaskDetailPane()
	{
		// Variable Definition
		details = new TraceInfoAPI();

		// Component Definitions
		String[] traceTypes = {"Email", "Meeting - In Person", "Meeting - Virtual", "On-Site Work", "Phone Call", "Project Work", "Support"};

		listOfAccounts = new JComboBox();
		listOfAccounts.addItem("Account Name");
		listOfContacts = new JComboBox();
		listOfContacts.addItem("Contact Name");
		listOfProjects = new JComboBox();
		listOfProjects.addItem("Project Name");
		listOfTypes = new JComboBox(traceTypes);

		dateField = new JTextField(8);
		timeField = new JTextField(5);
		durationField = new JTextField(2);
		subjectField = new JTextField(20);

		detailArea = new JTextArea(20, 30);
		detailArea.setLineWrap(true);

		completeBox = new JCheckBox("Completed");

		newTrace = new JButton("New Trace");
		saveTrace = new JButton("Save");
		cancelTrace = new JButton("Cancel");

		// JPanel Layout
		setLayout(new BorderLayout());
		
		// Configure Top Panel
		JPanel topPanel = new JPanel();
		JPanel topSub1 = new JPanel();
		JPanel topSub2 = new JPanel();
		JPanel topSub3 = new JPanel();

		topPanel.setLayout(new GridLayout(3, 1));
		topSub1.setLayout(new FlowLayout());
		topSub2.setLayout(new FlowLayout());
		topSub3.setLayout(new FlowLayout());

		topSub1.add(listOfAccounts);
		topSub1.add(listOfContacts);
		topSub1.add(listOfProjects);

		topSub2.add(dateField);
		topSub2.add(timeField);
		topSub2.add(durationField);
		topSub2.add(listOfTypes);

		topSub3.add(subjectField);
		topSub3.add(completeBox);

		topPanel.add(topSub1);
		topPanel.add(topSub2);
		topPanel.add(topSub3);

		add(topPanel, BorderLayout.NORTH);

		// Configure Center Panel
		add(detailArea, BorderLayout.CENTER);

		// Configure Bottom Panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());

		bottomPanel.add(newTrace);
		bottomPanel.add(saveTrace);
		bottomPanel.add(cancelTrace);

		add(bottomPanel, BorderLayout.SOUTH);
	}
	
	//======================================================
	// Functions
	//======================================================

	public void addDropDownListener(ActionListener lis)
	{
		listOfAccounts.addActionListener(lis);
	}

	public void addBTNListener(ActionListener lis)
	{
		newTrace.addActionListener(lis);
		saveTrace.addActionListener(lis);
		cancelTrace.addActionListener(lis);
	}

	private void errorDialog(String errMSG)
	{
		JDialog erm = new JDialog((JFrame)SwingUtilities.windowForComponent(this), "Errror: Invalid Input");
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

	public int getAccountID(String acc_name)
	{
		return accountList.getAccountIDbyName(acc_name);
	}

	public void getAccounts(Session sesh, Connector cx)
	{
		// Get accounts from database
		String cmdString;
	       	String output;
	
		cmdString = cx.generateAllAccountsString(sesh.getToken());
		output = cx.postAPIRequest(cmdString);
		accountList = cx.decodeAllAccountsCall(output);
		accountList.count();

		// Load the JComboBox with the accounts. This has to be
		// done in a weird way to ensure there is always data in
		// the combobox. Since there is a listener attached to
		// the combobox, a removeAll commands results in an error.
		while(listOfAccounts.getItemCount()>1)
		{
			listOfAccounts.removeItemAt(1);
		}

		for(int i=0; i<accountList.getNum(); i++)
		{
			listOfAccounts.addItem(accountList.getAccountName(i));
		}

	}

	public void getContacts(int acc_id, Session sesh, Connector cx)
	{
		// Get contacts from database
		String cmdString;
		String output;

		cmdString = cx.generateTraceContactsString(acc_id, sesh.getToken());
	       	output = cx.postAPIRequest(cmdString);
		contactList = cx.decodeContactsCall(output);
		contactList.count();	

		// Load the JComboBox with the contacts. This method is used
		// instead of removeAll() to keep things in line with loading
		// the Accounts JComboBox

		while(listOfContacts.getItemCount()>1)
		{
			listOfContacts.removeItemAt(1);
		}
		for(int i=0; i<contactList.getNum(); i++)
		{	
			listOfContacts.addItem(contactList.getFullName(i));
		}
	}

	public void getProjects(int acc_id, Session sesh, Connector cx)
	{
		// get projects from database
		String cmdString;
		String output;

		cmdString = cx.generateProjectString(acc_id, sesh.getToken());
		output = cx.postAPIRequest(cmdString);
		projectList = cx.decodeProjectCall(output);
		projectList.count();

		// Load the JComboBox with the projects. This method is used
		// instead of removeAll() to keep things in line with loading
		// the Accounts JComboBox

		while(listOfProjects.getItemCount()>1)
		{
			listOfProjects.removeItemAt(1);
		}

		for(int i=0; i<projectList.getNum(); i++)
		{
			listOfProjects.addItem(projectList.getProjectName(i));
		}
	}

	public void loadExistingTrace(int trace_id, Session sesh, Connector cx)
	{
		// Get trace details
		String cmdString;
		String output;

		cmdString = cx.generateTraceInfoString(trace_id, sesh.getToken());
		output = cx.postAPIRequest(cmdString);
		details = cx.decodeTraceInfoCall(output);

		// Load dropdowns
		getAccounts(sesh, cx);
		listOfAccounts.setSelectedItem(details.getAccount());
		listOfContacts.setSelectedItem(details.getContact());
		listOfProjects.setSelectedItem(details.getProject());
		listOfTypes.setSelectedItem(details.getType());

		// Populate fields
		dateField.setText(details.getDate());
		timeField.setText(details.getTime());
		durationField.setText(details.getDuration());
		subjectField.setText(details.getSubject());
		detailArea.setText(details.getText());
		completeBox.setSelected(details.getCompleted());	
	}
	
	public void loadFreshTrace(Session sesh, Connector cx)
	{
		getAccounts(sesh, cx);
	
		details.newDetails();

		dateField.setText(sesh.getCurrentDate());
		timeField.setText(details.getTime());
		durationField.setText(details.getDuration());
		subjectField.setText(details.getSubject());
		detailArea.setText(details.getText());
		completeBox.setSelected(details.getCompleted());
	}

	public int saveTrace(Session sesh, Connector cx)
	{
		// If/Else If Statements to parse a couple of error causing states.
		// 	This structure will avoid pushing the indentations off the
		// 	edge of the screen like nested ifs.
		// 1.) The first dropdown boxes aren't selected.
		// 2.) Date or time are blank
		// 3.) Duration is blank or <=0
		// 4.) Subject is blank
		if(listOfAccounts.getSelectedItem().equals("Account Name") || listOfContacts.getSelectedItem().equals("Contact Name") || listOfProjects.getSelectedItem().equals("Project Name"))
		{
			errorDialog("Please assign trace to company, contact, and project.");
		}
		else if(dateField.getText().equals("") || timeField.getText().equals(""))
		{
			errorDialog("Please pick a date and time.");
		}
		else if(durationField.getText().equals("") || Integer.parseInt(durationField.getText())<=0)
		{
			errorDialog("Please pick a valid duration.");
		}
		else if(subjectField.getText().equals(""))
		{
			errorDialog("Please enter a subject/short description.");
		}
		else
		{
			// Okay, testing done. We're ready to save.
			String cmdString;
			String output;

			writeToDetails();

			cmdString = cx.generateSaveTraceString(details.getTraceID(), accountList.getAccountIDbyName(details.getAccount()), contactList.getContactIDbyName(details.getContact()), projectList.getProjectIDbyName(details.getProject()), Integer.valueOf(details.getDuration()), details.getDate(), details.getTime(), details.getType(), details.getSubject(), details.getText(), details.getCompleted(), sesh.getToken());

			output = cx.postAPIRequest(cmdString);
		
			// FUTURE VERSION
			// Write code to assign the traceID back on here
			// This way new traces can be created from the trace
			// detail screen. A pop-up for saved successfully is
			// also probably a good idea.

			return cx.decodeSaveCode(output); // Completed successfully
		}
		
		return -1; // One of the error conditions were triggered
	}

	public int writeToDetails()
	{
		// Write the field values to the details variable.
		// This allows for consolidating changes to the traces as
		// all updates and refreshing comes from this variable.
		// The goal is to provide consistent behavior
		
		details.setAccount((String)listOfAccounts.getSelectedItem());
		details.setContact((String)listOfContacts.getSelectedItem());
		details.setProject((String)listOfProjects.getSelectedItem());
		details.setDate(dateField.getText());
		details.setTime(timeField.getText());
		details.setDuration(durationField.getText());
		details.setType((String)listOfTypes.getSelectedItem());
		details.setSubject(subjectField.getText());
		details.setText(detailArea.getText());
		details.setComplete(completeBox.isSelected());
	
		return 1;
	}
}	
