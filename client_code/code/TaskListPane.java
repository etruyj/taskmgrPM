//======================================================================================
// TaskListPane.java
//	This is the display screen for the task list. Tasks are displayed in
//	chronological order for the date selected. Completed tasks are grayed out and
//	incomplete tasks are printed in blue. Clicking on the task will bring up the
//	taskDetailPane for the specific task.
//======================================================================================

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class TaskListPane extends JPanel
{
	// GUI Variables
	private JLabel welcomeMessage;
	private JButton changeDateBTN;
	private JButton newTaskBTN;
	private JTextField dateField;
	private JPanel scheduleArea;

	// System Variables
	private TracesAPI traceList;
	private String cmdString;
	private String output;

	public TaskListPane(Session sesh)
	{
		// Initial Values
		welcomeMessage = new JLabel();
		welcomeMessage.setText("Welcome " + sesh.getUsername() + ", "
				+ "Today's date is " + sesh.getToday() + ".");
		dateField = new JTextField();
		changeDateBTN = new JButton("Change Date");
		newTaskBTN = new JButton("New Task");
		dateField.setText(sesh.getCurrentDate());
		scheduleArea = new JPanel();
		scheduleArea.setSize(100, 200);
		scheduleArea.setBackground(Color.white);
		scheduleArea.setLayout(new GridLayout(30,1));

		// Configure Panel
		// Primary Panel Layout
		setLayout(new BorderLayout());

		// Sub-Panels for Better Layout Control
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		// These two blank panels are needed to prevent the
		// 	center panel from consuming the full width
		// 	of the task manager.
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		leftPanel.setSize(50, 200);
		rightPanel.setSize(50, 200);

		// Configure topPanel
		JPanel topSub1 = new JPanel();
		JPanel topSub2 = new JPanel();
		topPanel.setLayout(new GridLayout(2, 0));
		topSub1.setLayout(new FlowLayout());
		topSub2.setLayout(new FlowLayout());
		topSub1.add(welcomeMessage);
		topSub2.add(dateField);
		topSub2.add(changeDateBTN);
		topPanel.add(topSub1);
		topPanel.add(topSub2);
		add(topPanel, BorderLayout.NORTH);

		// Configure centerPanel

		// Option 1
	//	centerPanel.setLayout(new FlowLayout());
	//	centerPanel.add(leftPanel); // Blank Panel
	//	centerPanel.add(scheduleArea);
	//	centerPanel.add(rightPanel); // Blank Panel
	//	add(centerPanel, BorderLayout.CENTER);
	
		// Option 2
		add(scheduleArea, BorderLayout.CENTER);
		add(leftPanel, BorderLayout.WEST); // Blank Panel
		add(rightPanel, BorderLayout.EAST); // Blank Panel

		// Configure bottomPanel
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(newTaskBTN);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	//=====================================================================
	// Functions
	//=====================================================================
	
	public void addBTNListener(ActionListener lis)
	{
		changeDateBTN.addActionListener(lis);
		newTaskBTN.addActionListener(lis);
	}

	public void refreshWelcomeMessage(Session sesh)
	{
		welcomeMessage.setText("Welcome " + sesh.getUsername() + ", "
				+ "Today's date is " + sesh.getToday() + ".");
		
	}

	public void updateDateField(Session sesh)
	{
		dateField.setText(sesh.getCurrentDate());
	}

	public void updateTraceList(Session sesh, MouseListener mis, Connector cx)
	{
		// Get Traces From Database
		sesh.setCurrentDate(dateField.getText());
		
		cmdString = cx.generateTracebyDateString(sesh.getCurrentDate(), sesh.getToken());
		output = cx.postAPIRequest(cmdString);

		traceList = cx.decodeTracesCall(output);
		traceList.count();

		scheduleArea.removeAll();

		// Update the text area
		if(traceList.getCode()>0)
		{
			for(int i=0; i<traceList.getNum(); i++)
			{
				// Create and format the JLabel
				JLabel traceData = new JLabel();
				traceData.setText(traceList.getTime(i) + " " + traceList.getSubject(i));
				traceData.setName(Integer.toString(traceList.getTraceID(i)));
				traceData.addMouseListener(mis);

				if(traceList.getCompleted(i))
				{
					traceData.setForeground(Color.gray);
				}
				else
				{
					traceData.setForeground(Color.blue);
				}
				
				scheduleArea.add(traceData, i);

			}
		}
	}

}
