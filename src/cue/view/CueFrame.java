package cue.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
}
