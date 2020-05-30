//=============================================================================
// plannerPanel.java
// 	This panel is the day planner layout for the taskPane
//=============================================================================

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class plannerPanel extends JPanel
{
	int scale; // the conversion between duration and time. 
	int panelHeight; 

	//private traceList traces;
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g.drawLine(20, 5, 20, 795);
		
		// Draw Hours
		String hour;

		for(int i=0; i<12; i++)
		{
			System.out.println(i);
			g2.draw(new Line2D.Double(10, (i*scale*6)+5, 350, (i*scale*6)+5));
			hour = Integer.toString(6+i);
			g2.drawString(hour, 5, (i*scale*6)+20);
		}

		drawTask(20, 5, g2);
		drawTask(100, 10, g2); 
	}

	public plannerPanel()
	{
		scale = 10 * 3; // 10 minutes = 30 pixels
		panelHeight = 12 * 60 * scale; // 12 hours * 60 minutes * scale.
		
		// Set Dimensions
		setSize(new Dimension(400, panelHeight));
		setBackground(Color.white);

		// Render traces.
		// traces = tasks;
	}

	public void refresh()
	{
		repaint();
	}

	public void drawTask(int start, int duration, Graphics2D g2)
	{
		int y; // starting point/time.
		int h; // height/duration

		// RGB Values: 
		//  Open Task (107, 179, 201)
		//  Closed Task (162, 167, 168)
		Color open = new Color(107, 179, 201);
		Color closed = new Color(162, 167, 168);

		y = start;
		h = (duration * 3) - 2;

		if(duration % 2 ==0)
		{
			g2.setPaint(open);
		}
		else
		{
			g2.setPaint(closed);
		}

		g2.fill(new RoundRectangle2D.Double(25, y, 325, h, 10, 10));
	}	
}
