//============================================================================
// Taskmrg.java
// 	The primary class of the task manager program and the GUI.
// 	Implements Session and Connection for maintaining state and 
// 	querying the remote server.
//============================================================================

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TaskGui extends JFrame
{
	// Variables
	private Session sesh;
	private JPanel screens;
	private initPane loginScreen;
	private taskPane taskScreen;
	private taskDetailPane taskDetailScreen;
	private ActionListener logListen;
	private loginBTNListener lis;
	private taskBTNListener taskLis;
	private taskMouseListener taskMouseLis;
	private dropDownListener taskDetailDropLis;
	private taskMenu menubar;
	private traceList traces;

	// Contructor
	public TaskGui()
	{
		// Variable initialization
		sesh = new Session();

		// Initialize Application Screens
		screens = new JPanel(new CardLayout());
		loginScreen = new initPane();
		taskScreen = new taskPane();
		taskDetailScreen = new taskDetailPane();
		screens.add(loginScreen, "Login");
		screens.add(taskScreen, "Tasks");
		screens.add(taskDetailScreen, "taskDetails");
		menubar = new taskMenu();

		// Add event listeners.
		lis = new loginBTNListener();
		taskLis = new taskBTNListener();
		taskMouseLis = new taskMouseListener();
		taskDetailDropLis = new dropDownListener();
		loginScreen.addListener(lis);
		taskScreen.addListener("changeBtn", taskLis);
		taskScreen.addListener("newBtn", taskLis);
		taskDetailScreen.addAccountListener(taskDetailDropLis);
		taskDetailScreen.addButtonListener("newTask", taskLis);
		taskDetailScreen.addButtonListener("saveTask", taskLis);	
		taskDetailScreen.addButtonListener("cancelTask", taskLis);

		add(screens);
		
		// Exit the program functionality
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Window Configuration
		setTitle("Task Manager");
		setSize(600, 600);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() 
			{
				new TaskGui();
			}
		});

	}

	//===========================================================
	// MENU BAR
	// 	This is the menu bar for the gui. It only becomes
	// 	visible after logging in.
	//===========================================================
	
	private class taskMenu extends JMenuBar
	{
		private taskMenu()
		{
			// ActionListener Initializaton
			taskBTNListener taskList = new taskBTNListener();

			// File Menu
			JMenu menu = new JMenu("File");
			menu.setMnemonic(KeyEvent.VK_F);
			add(menu);

			JMenuItem mitem = new JMenuItem("Quit");
			mitem.setMnemonic(KeyEvent.VK_Q);
			menu.add(mitem);

			// Task Menu
			menu = new JMenu("Tasks");
			menu.setMnemonic(KeyEvent.VK_T);
			add(menu);

			mitem = new JMenuItem("View Tasks");
			mitem.addActionListener(taskList);
			mitem.setMnemonic(KeyEvent.VK_V);
			menu.add(mitem);

			mitem = new JMenuItem("New Task");
			mitem.addActionListener(taskList);
			mitem.setMnemonic(KeyEvent.VK_N);
			menu.add(mitem);

			// Accounts Menu
			menu = new JMenu("Accounts");
			menu.setMnemonic(KeyEvent.VK_A);
			add(menu);

			mitem = new JMenuItem("List Accounts");
			mitem.setMnemonic(KeyEvent.VK_L);
			menu.add(mitem);

			mitem = new JMenuItem("New Account");
			mitem.setMnemonic(KeyEvent.VK_E);
			menu.add(mitem);

			// Projects Menu
			menu = new JMenu("Projects");
			menu.setMnemonic(KeyEvent.VK_P);
			add(menu);

			mitem = new JMenuItem("List Projects");
			mitem.setMnemonic(KeyEvent.VK_I);
			menu.add(mitem);

			mitem = new JMenuItem("New Project");
			mitem.setMnemonic(KeyEvent.VK_J);
			menu.add(mitem);

			// Reports Menu
			menu = new JMenu("Reports");
			menu.setMnemonic(KeyEvent.VK_R);
			add(menu);
		}
	}
	
	//===========================================================
	// LISTENERS
	// 	These are the action listeners for the program. They
	//	define the code of the program to be executed when
	//	buttons or other activities are clicked.
	//
	//	screenBTN : is the listener that is used for changing
	//			between screens. Right now it's only
	//			implemented for login functionality. 
	//===========================================================

	private class dropDownListener implements ActionListener
	{
		// Fills in the contacts and project dropdown boxes with the contacts
		// and projects specific to the account. It queries based on the account
		// name picked from the first dropdown box.

		@Override
		public void actionPerformed(ActionEvent evt)
		{
			JComboBox<String> source = (JComboBox<String>) evt.getSource();
			String selectedValue = source.getSelectedItem().toString();
			taskDetailScreen.updateDropDowns(sesh, selectedValue);

			revalidate();
			repaint();
		}
	}

	private class loginBTNListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			String btnLabel = evt.getActionCommand();
			// Parse action based on the clicked button.
			if(btnLabel.equals("Login"))
			{
				// Process Login Scripts.
				sesh = loginScreen.login(sesh);
		
				// DEBUG print to CLI
				//System.out.println(sesh.getReturnCode());	
				if(sesh.getReturnCode()>0)
				{
					// Change to the next screen
					CardLayout c1 = (CardLayout)(screens.getLayout());
					c1.show(screens, "Tasks");

					// Make menu bar visible
					setJMenuBar(menubar);

					// Pass Updated session variables to taskscreen
					traces = taskScreen.initializeWindow(sesh, taskMouseLis);
					taskScreen.sessionTest(sesh);
				}

			}
		}
	}

	private class taskBTNListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			CardLayout c1;
			String btnLabel = evt.getActionCommand();
			
			System.out.println(btnLabel);

			switch(btnLabel)
			{
				case "Change Date":
					traces = taskScreen.updateWindow(sesh, taskMouseLis);
					break;
				case "View Tasks":
				case "Cancel":
					// Return to taskPane
					c1 = (CardLayout)(screens.getLayout());

					c1.show(screens, "Tasks");
					break;
				case "Save":
					taskDetailScreen.saveTrace(sesh);
				case "New":
				case "New Task":
					// Open a clear task detail screen.
					taskDetailScreen.loadFreshInfo(sesh);
					
					// Change to the next screen
					c1 = (CardLayout)(screens.getLayout());

					c1.show(screens, "taskDetails");
					break;
			}

		}
	}

	private class taskMouseListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			JLabel lbl = (JLabel)evt.getSource();

			// Change to the next screen
			CardLayout c1 = (CardLayout)(screens.getLayout());

			c1.show(screens, "taskDetails");
			
			taskDetailScreen.loadTraceInfo(sesh, lbl.getName());
		}

		// Blank necessary to implement the listener
		@Override public void mousePressed(MouseEvent evt) {}
		@Override public void mouseReleased(MouseEvent evt) {}
		@Override public void mouseEntered(MouseEvent evt) {}
		@Override public void mouseExited(MouseEvent evt) {}
	}
}
