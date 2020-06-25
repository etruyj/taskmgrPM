//=============================================================================
// LoginPane.java
// 	The login screen
//=============================================================================

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;

import java.awt.event.ActionListener;

public class LoginPane extends JPanel
{
	private int x_dimension;
	private int y_dimension;

	private JPasswordField passBox;
	private JTextField userBox;
	private JTextField hostBox;
	private JButton submitBTN;
	private JLabel errorMessage;

	public LoginPane()
	{
		JLabel userPrompt = new JLabel("Username:");
		JLabel passPrompt = new JLabel("Password:");
		JLabel hostPrompt = new JLabel("Server:");
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.red);

		userBox = new JTextField(10);
		hostBox = new JTextField(10);
		passBox = new JPasswordField(10);

		submitBTN = new JButton("Submit");

		y_dimension = 18;
		x_dimension = 4;

		JPanel[][] layout = new JPanel[y_dimension][x_dimension];
		
		setLayout(new GridLayout(y_dimension, x_dimension));
	
		for(int y=0; y<y_dimension; y++)
		{
			for(int x=0; x<x_dimension; x++)
			{
				layout[y][x] = new JPanel();
				add(layout[y][x]);
			}
		}

		layout[6][1].add(userPrompt);
		layout[6][2].add(userBox);
		layout[7][1].add(passPrompt);
		layout[7][2].add(passBox);
		layout[8][1].add(hostPrompt);
		layout[8][2].add(hostBox);
		layout[9][2].add(submitBTN);
		layout[10][2].add(errorMessage);
	}

	public void addBTNListener(ActionListener lis)
	{
		submitBTN.addActionListener(lis);
	}

	public int loginToServer(Session sesh, Connector cx)
	{
		String cmdString;
		String output;
		
		sesh.setUsername(userBox.getText());
		sesh.setServerAddress(hostBox.getText());

		cx.setServer(sesh.getServerAddress());
	
		cmdString = cx.generateLoginString(sesh.getUsername(), passBox.getText());
		output = cx.postAPIRequest(cmdString);
		sesh.setToken(cx.decodeLoginCall(output));

		if(cx.decodeSaveCode(output)>0)
		{
			errorMessage.setText("");
		}
		else
		{
			errorMessage.setText(cx.decodeSaveMSG(output));
		}
	
		return cx.decodeSaveCode(output); // Using this function which
					// outputs the same value instead of 
					// coding a duplicate function with a 
					// different name.
	}
}
