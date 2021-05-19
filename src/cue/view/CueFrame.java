package cue.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.event.MouseInputListener;

import cue.controller.CueController;

@SuppressWarnings("serial")
public class CueFrame extends JFrame
{
	private CuePanel panel;
	
	/**
	 * Initializes the panel and {@link #setupFrame() sets up the frame}
	 * @param controller Passes down to the panel
	 */
	public CueFrame(CueController controller)
	{
		super();
		this.panel 		= new CuePanel(controller, this);
		
		setupFrame();
	}
	
	/**
	 * Makes the constructed frame visible
	 */
	public void display()
	{
		this.setVisible(true);
	}
	
	/**
	 * Sets up the necessary values of the frame
	 */
	private void setupFrame()
	{
	    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    this.addWindowListener(new WindowAdapter()
		{
	    	@Override
	    	public void windowClosing(WindowEvent event)
	    	{
	    		JFrame frame = (JFrame)event.getSource();
	    		if (!panel.isSaved())
	    		{
		    		if (panel.confirmSave())
		    		{
		    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    		}
	    		}
	    		else
	    		{
	    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    		}
	    	}
		});
	    
//	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		this.setSize(800, 600);
		this.setTitle("Cue Tester");
		this.setResizable(true);
		this.setLocationRelativeTo(null);
	}
	
	public void setupMenuBar(JMenuBar menuBar)
	{
		this.setUndecorated(true);
//		this.getRootPane().setWindowDecorationStyle(this.getRootPane().FRAME);
		
		JButton minimize = new JButton("_");
		JButton close = new JButton("X");
		
		minimize.setBorderPainted(false);
		minimize.setBackground(null);
		close.setBorderPainted(false);
		close.setBackground(null);

		menuBar.add(new JMenu("Cue Tester"), 0);
		menuBar.getMenu(0).setEnabled(false);
		menuBar.add(new JSeparator());
		menuBar.add(minimize);
		menuBar.add(close);

		Component[] comps = menuBar.getComponents();
		for(int i = 0; i < comps.length; i++)
		{
			if (comps[i] != null)
			{
				comps[i].setBackground(null);
				comps[i].setForeground(Color.WHITE);
			}
		}
		comps[comps.length - 3].setForeground(null);
		
		
		JFrame frame = this;
		minimize.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.setExtendedState(JFrame.ICONIFIED);
			}
		});
		
		close.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
	}
}
