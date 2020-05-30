//=============================================================================
// taskView.java
//	This is a view of the task items as a simple clickable list of JLables.
//	This panel is embedded in the JScrollBar on the taskPane. It requires
//	the traceList class.
//
//	It prints a list of JLabels with the start time and the subject of the
//	task in sequencial order. Tasks that are completed are grayed out while
//	incomplete tasks are green.
//
//	Clicking on a task brings up the detailed info window.
//=============================================================================

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class taskView extends JPanel
{
	public taskView()
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setSize(400, 800);
	}

	public void updateTasks(traceList traces, MouseListener lis)
	{
		removeAll();

		JLabel lbl;
		if(traces.getCode()>0)
		{
			for(int i=0; i<traces.getTraceCount(); i++)
			{
				lbl = new JLabel();
				
				if(traces.getCompleteStatus(i))
				{
					lbl.setForeground(Color.LIGHT_GRAY);
				}
				else
				{
					lbl.setForeground(Color.BLUE);
				}

				lbl.setText(traces.getTime(i).substring(0, traces.getTime(i).length()-3) + " " + traces.getSubject(i));
				lbl.setName(Integer.toString(i));
				lbl.addMouseListener(lis);
				add(lbl);
			}
		}
		else
		{
			lbl = new JLabel(traces.getMSG());
			lbl.setForeground(Color.DARK_GRAY);
			add(lbl);
		}
		
		revalidate();
		repaint();
	}


}
