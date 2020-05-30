import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class testGui extends JFrame
{
	JFrame frame;
	taskDetailPane tasks;

	public static void main(String[] args)
	{
		new testGui();
	}

	public testGui()
	{
		tasks = new taskDetailPane();
		add(tasks);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,600);
		setTitle("Tests");
		setVisible(true);

	}

}
