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

public class initPane extends JPanel
{
	//===================================================
	// initPane
	// 	The login pane with the get username and pass
	// 	functionality.
	//===================================================

	// Variables
	private JLabel userlabel;
	private JLabel passlabel;
	private JLabel hostlabel;
	private JLabel systemMSG;

	private JTextField user;
	private JTextField pass;
	private JTextField host;

	private String username;
	private String password;
	private String hostname;
		
	private JButton logBtn;

	public initPane()
	{
		int rows = 20;
		int columns = 4;
		JPanel[][] template = new JPanel[rows][columns];

		setLayout(new GridLayout(rows, columns));

		userlabel = new JLabel("Username: ");
		passlabel = new JLabel("Password: ");
		hostlabel = new JLabel("Server: ");
		systemMSG = new JLabel("");

		user = new JTextField(12);
		pass = new JTextField(12);
		host = new JTextField(12);

		username = "locked";
		password = "nopass";
		hostname = "localhost";
			
		logBtn = new JButton("Login");
		
		// Add empty panels to grid layout to allow
		// adding above elements to specific grid.
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				template[i][j] = new JPanel();
				add(template[i][j]);
			}	
		}
		
		template[8][1].add(userlabel);
		template[8][2].add(user);
		template[9][1].add(passlabel);
		template[9][2].add(pass);
		template[10][1].add(hostlabel);
		template[10][2].add(host);
		template[11][2].add(logBtn);
		template[12][2].add(systemMSG);	
	}

	public void addListener(ActionListener lis)
	{
		logBtn.addActionListener(lis);
	}

	public Session login(Session sesh)
	{
		// Variable Initialization
		Gson gson = new Gson();
		String jsonOutput = "{\"msg\": \"connection error\", \"code\": -1, \"token\": \"none\"}";
		String jsonInput = "{\"user\": \"" + user.getText() + "\", \"pass\": \""
				+ pass.getText() + "\", \"cmd\": \"login\"}";
		
		// Try the connection
		try
		{ 
			Connector con = new Connector(host.getText());
			jsonOutput = con.postJSON(jsonInput); 
		}
		catch(Exception e) {}
		Credentials cred = gson.fromJson(jsonOutput, Credentials.class);

		// Fill in the Session Variable
		if(cred.code > 0)
		{
			// Successful connection
			sesh.setUsername(user.getText());
			sesh.setReturnCode(cred.code);
			sesh.setServer(host.getText());
			sesh.setToken(cred.token);
		}

		systemMSG.setText(cred.msg);
		// Print to CLI for debug.
		//System.out.println(cred.msg);
		
		return sesh;
	}

	private class Credentials
	{
		String msg;
		String token;
		int code;
	}
}

