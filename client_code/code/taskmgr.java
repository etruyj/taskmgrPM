//=======================================================================================
// taskmgr.java
// 	
// 	Description: This is the main executable for the program and an extension of the
// 	JPANEL. Main() exists in this class. The different screens (dubbed panes) are
// 	defined in separate classes, but executed from here. All listeners are defined
// 	in this class and passed down to their respective panes. The menu bar is defined
// 	here as well.
//
//=======================================================================================

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import java.awt.CardLayout;
import java.awt.Toolkit;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class taskmgr extends JFrame
{
	// Program Variables
	private Session sesh;
	private Connector conn;
	private String cmdString;
	private String output;

	// Screen Variables
	private CardLayout cardLayout;
	private navBar navi;
	private JPanel screenCards;
	private TaskListPane taskScreen;
	private TaskDetailPane taskDetailScreen;
	private AccountDetailPane accountDetailScreen;
	private AccountSearchPane accountSearchScreen;
	private LoginPane loginScreen;

	// Action Listeners
	private accBTNListener abtns;
	private btnListener btns;
	private traceListener mse;
	private accountListener act;
	private accountMouseListener amis;
	private contactMouseListener cmis;
	private projectMouseListener pmis;

	public taskmgr()
	{
		// Variable Initialization
		sesh = new Session();
		conn = new Connector("192.168.72.23");

		// Action Listener
		abtns = new accBTNListener();
		btns = new btnListener();
		mse = new traceListener();
		act = new accountListener();
		amis = new accountMouseListener();
		cmis = new contactMouseListener();
		pmis = new projectMouseListener();

		// The display panes
		cardLayout = new CardLayout();
		screenCards = new JPanel();
		screenCards.setLayout(new CardLayout());
		loginScreen = new LoginPane();
		loginScreen.addBTNListener(btns);
		taskScreen = new TaskListPane(sesh);
		taskScreen.addBTNListener(btns);
		taskDetailScreen = new TaskDetailPane();
		taskDetailScreen.addDropDownListener(act);
		taskDetailScreen.addBTNListener(btns);
		accountDetailScreen = new AccountDetailPane();
		accountDetailScreen.addBTNListener(abtns);
		accountSearchScreen = new AccountSearchPane();
		accountSearchScreen.addButtonListener(btns);
		// projectPane
		// reportsPane

		screenCards.add(loginScreen, "Login");
		screenCards.add(taskScreen, "TraceList");
		screenCards.add(taskDetailScreen, "TraceDetails");
		screenCards.add(accountDetailScreen, "AccountDetails");
		screenCards.add(accountSearchScreen, "AccountSearch");
		add(screenCards);

		// Test Initialization II
		showCard("LoginScreen");

		// Configure Window
		setTitle("Task Manager");
		setSize(500, 600);
		setResizable(false);
		setVisible(true);

		// Exit Behavoir
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args)
	{
		new taskmgr();
	}

	private void showCard(String screen)
	{
		CardLayout clay = (CardLayout)(this.screenCards.getLayout());
		clay.show(this.screenCards, screen);
	}

	//==============================================================================
	// Menu Bar
	//==============================================================================

	private class navBar extends JMenuBar
	{
		public navBar(ActionListener traceLis, ActionListener accLis)
		{
			// Create the menu and add the respective action listeners.

			JMenuItem menu = new JMenu("File");
	//		menu.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			JMenuItem mitem = new JMenuItem("Quit");
	//		mitem.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menu.add(mitem);
			add(menu);

			menu = new JMenu("Traces");
			mitem = new JMenuItem("View List");
			mitem.addActionListener(traceLis);
			menu.add(mitem);
			mitem = new JMenuItem("New Trace");
			mitem.addActionListener(traceLis);
			menu.add(mitem);
			add(menu);

			menu = new JMenu("Accounts");
			mitem = new JMenuItem("Search Accounts");
			mitem.addActionListener(traceLis);
			menu.add(mitem);
			mitem = new JMenuItem("Create Account");
			mitem.addActionListener(traceLis);
			menu.add(mitem);
			add(menu);
		}
	}

	//==============================================================================
	// Action Listeners
	//==============================================================================

	private class accountListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			JComboBox tempBox = (JComboBox)evt.getSource();
			String selection = (String)tempBox.getSelectedItem();
			int acc_id = taskDetailScreen.getAccountID((selection));

			taskDetailScreen.getContacts(acc_id, sesh, conn);
			taskDetailScreen.getProjects(acc_id, sesh, conn);

			revalidate();
			repaint();
		}
	}

	private class accBTNListener implements ActionListener
	{
		// This action listener is for parsing the buttons in the
		// account menu as they have the same labels as the original
		// button listner.

		@Override
		public void actionPerformed(ActionEvent evt)
		{
			JButton btn = (JButton)evt.getSource();
			
			switch(btn.getName())
			{
				case "New Account":
					accountDetailScreen.refreshAccountClear();
					break;
				case "Save Account":
					accountDetailScreen.saveAccount(sesh, conn);
					break;
				case "New Contact":
					accountDetailScreen.refreshContactClear();
					break;
				case "Save Contact":
					accountDetailScreen.saveContact(sesh, conn);
					accountDetailScreen.loadContacts(accountDetailScreen.getAccountID(), cmis, sesh, conn);
					break;
				case "New Project":
					accountDetailScreen.refreshProjectClear();
					break;
				case "Save Project":
					accountDetailScreen.saveProject(sesh, conn);
					accountDetailScreen.loadProjects(accountDetailScreen.getAccountID(), pmis, sesh, conn);
					break;
				case "Exit": // Cancel button on Account Details.
					accountSearchScreen.refreshScreen(amis, sesh, conn);
					showCard("AccountSearch");
					break;
			}	
			revalidate();
			repaint();
		}
	}
	
	private class btnListener implements ActionListener
	{
		// This button listener is used exclusively for navigating between the
		// trace screens. I mistakenly didn't anticipate the fact that the accounts,
		// contacts, projects, and reports screen would probably also warrant "new",
		// "save", and "cancel" buttons requiring different functionality. As this
		// discovery occurred towards the end of development of this section of the
		// program, the trace buttons get the btnListener and a different naming
		// convention will be used for the other screens.

		@Override
		public void actionPerformed(ActionEvent evt)
		{
			int errCode = 0; // for error testing.

			String btnLabel = evt.getActionCommand();

			switch(btnLabel)
			{
				// Save, Cancel, and Change Date all have the same
				// function to 'return' to the task list screen after a
				// refresh. Save requires extra code to actually save the
				// trace, but all three switch options can dribble down
				// through the same logic.
				case "Save":
					errCode = taskDetailScreen.saveTrace(sesh, conn);
					if(errCode<=0)
					{
						break;
					}
		//			break; // Comment out if saving should
						// return to the task list pane
					taskScreen.updateDateField(sesh);
				case "View List":
				case "Cancel":
				case "Change Date":
					taskScreen.updateTraceList(sesh, mse, conn);
					showCard("TraceList");
				       	break;
			       	case "New Task":
				case "New Trace":
					taskDetailScreen.loadFreshTrace(sesh, conn);
					showCard("TraceDetails");	
					break;
				case "Search": // Search For Accounts
					accountSearchScreen.refreshScreen(amis, sesh, conn);
				case "Search Accounts": // Open Search Accounts Screen
					showCard("AccountSearch");
					break;
				case "Create Account": // Open a blank create account screen
					accountDetailScreen.refreshAccountClear();
					showCard("AccountDetails");
					break;
				case "Submit": // This is the login call
					// If the login returns a code of 1, login.
					if(loginScreen.loginToServer(sesh, conn) == 1)
					{
						// Initialize MenuBar
						navi = new navBar(btns, abtns);
						setJMenuBar(navi);

						// Switch to the first screen
						taskScreen.refreshWelcomeMessage(sesh);
						taskScreen.updateTraceList(sesh, mse, conn);
						showCard("TraceList");
					}
					break;
			}
			validate();
			repaint();
		}
	}

	private class accountMouseListener implements MouseListener
	{
		// Allows accounts to be opened by clicking on their name
		// in the search bar.
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			JLabel event = (JLabel) evt.getSource();
			int acc_id = Integer.parseInt(event.getName());
			String acc_name = event.getText();
		
			accountDetailScreen.loadAccount(acc_id, acc_name);
			accountDetailScreen.loadContacts(acc_id, cmis, sesh, conn);
			accountDetailScreen.refreshContactClear();
			accountDetailScreen.loadProjects(acc_id, pmis, sesh, conn);
			accountDetailScreen.refreshProjectClear();
			showCard("AccountDetails");
		}
		// Not used, but necessary to compile
		@Override public void mousePressed(MouseEvent evt) {}
		@Override public void mouseReleased(MouseEvent evt) {}
		@Override public void mouseEntered(MouseEvent evt) {}
		@Override public void mouseExited(MouseEvent evt) {}
	}
	
	private class contactMouseListener implements MouseListener
	{
		// Allows accounts to be opened by clicking on their name
		// in the search bar.
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			JLabel event = (JLabel) evt.getSource();
			int contact_id = Integer.parseInt(event.getName());
			accountDetailScreen.refreshContactDetails(contact_id);
		}

		// Not used, but necessary to compile
		@Override public void mousePressed(MouseEvent evt) {}
		@Override public void mouseReleased(MouseEvent evt) {}
		@Override public void mouseEntered(MouseEvent evt) {}
		@Override public void mouseExited(MouseEvent evt) {}
	}

	private class projectMouseListener implements MouseListener
	{
		// Allows accounts to be opened by clicking on their name
		// in the search bar.
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			JLabel event = (JLabel) evt.getSource();
			int project_id = Integer.parseInt(event.getName());
			accountDetailScreen.refreshProjectDetails(project_id);
		}
		// Not used, but necessary to compile
		@Override public void mousePressed(MouseEvent evt) {}
		@Override public void mouseReleased(MouseEvent evt) {}
		@Override public void mouseEntered(MouseEvent evt) {}
		@Override public void mouseExited(MouseEvent evt) {}
	}

	private class traceListener implements MouseListener
	{
		// Allows traces to be opened by clicking on them in the
		// trace list screen.
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			JLabel event = (JLabel) evt.getSource();
			taskDetailScreen.loadExistingTrace(Integer.parseInt(event.getName()), sesh, conn);
			showCard("TraceDetails");
		}

		// Not used, but necessary to compile
		@Override public void mousePressed(MouseEvent evt) {}
		@Override public void mouseReleased(MouseEvent evt) {}
		@Override public void mouseEntered(MouseEvent evt) {}
		@Override public void mouseExited(MouseEvent evt) {}
	}

}
