//============================================================================
// Taskmrg.java
// 	The primary class of the task manager program and the GUI.
// 	Implements Session and Connection for maintaining state and 
// 	querying the remote server.
//============================================================================

import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class taskPane extends JPanel
{
	//============================================================
	// taskPane
	// 	Prints all the tasks on a specific date.
	//============================================================
		
	private JLabel testMsg;
	private JButton changeBtn;
	private JButton newBtn;
	private JButton saveBtn;
	private JTextField dateField;

	private int rows;
	private int columns;
	private JPanel[][] template;
	
	private taskView taskPlan;
	private traceList traces;
	

	public taskPane()
	{
		// Configure Window
		setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		JPanel bodyPanel = new JPanel();
		JPanel btnPanel = new JPanel();

		topPanel.setLayout(new FlowLayout());
		bodyPanel.setLayout(new FlowLayout());
		btnPanel.setLayout(new FlowLayout());

		add(topPanel, BorderLayout.NORTH);
		add(bodyPanel, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);


		// The date window
		testMsg = new JLabel();
		dateField = new JTextField();
		topPanel.add(testMsg);
		topPanel.add(dateField);	

		// The task window and scrollbar.
		taskPlan = new taskView();
		JScrollPane taskScrollPane = new JScrollPane(taskPlan);
		taskScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		taskScrollPane.setPreferredSize(new Dimension(370, 480));
		bodyPanel.add(taskScrollPane);

		changeBtn = new JButton("Change Date");
		newBtn = new JButton("New Task");
		saveBtn = new JButton("Save");
		
		topPanel.add(changeBtn, BorderLayout.NORTH);
		btnPanel.add(newBtn, BorderLayout.SOUTH);
		btnPanel.add(saveBtn, BorderLayout.SOUTH);

	}

	public void addListener(String buttonName, ActionListener lis)
	{
		switch (buttonName)
		{
			case "changeBtn":
				changeBtn.addActionListener(lis);
				break;
			case "newBtn":
				newBtn.addActionListener(lis);
				break;
			case "saveBtn":
				saveBtn.addActionListener(lis);
				break;
		}
	}

	public traceList initializeWindow(Session sesh, MouseListener mouseLis)
	{
		//============================================================
		// initializeWindow(session)
		//	For use with the initial login. Updates the variables
		//	using the session variable. All values in the
		//	constructor are locked in at initialization and don't
		//	update with variable changes occuring at login. 
		// 
		// 	Functions:
		// 	-- set CurDate
		// 	-- set testMessage
		// 	-- query remote db for tasks.
		//============================================================
		
		sesh.setCurDate(sesh.getDate());
		testMsg.setText("Welcome " + sesh.getUsername() + ", Today is " + sesh.getDate());
		dateField.setText(sesh.getCurDate());
		queryTraces(sesh);
		taskPlan.updateTasks(traces, mouseLis);

		return traces;
	}

	public traceList updateWindow(Session sesh, MouseListener mouseLis)
	{
		//============================================================
		// updateWindow(Session, mouseListener)
		// 	Updates the curDate variable and re-queries the remote
		// 	DB for that day's tasks. For use after login when the
		// 	user wants to change the date of the task list.
		//============================================================
	
		sesh.setCurDate(dateField.getText());
		queryTraces(sesh);
		taskPlan.updateTasks(traces, mouseLis);

		return traces;
	}

	public void queryTraces(Session sesh)
	{
		Gson gson = new Gson();

		String jsonOutput = "{\"msg\": \"connection error\", \"code\": -1}";
		String jsonInput = "{\"cmd\": \"listTraces\", \"day\": \"" 
				+ sesh.getCurDate() + "\", \"token\": \"" 
				+ sesh.getToken() + "\"}";

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
		
		traces = gson.fromJson(jsonOutput, traceList.class);
		traces.countTraces(); // Determine how many traces were downloaded.
	}

	public void sessionTest(Session sesh)
	{
		System.out.println("Today is " + sesh.getDate());
		System.out.println("Username updated to " + sesh.getUsername());
	}
}
