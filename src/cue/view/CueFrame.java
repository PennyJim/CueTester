package cue.view;

import java.awt.event.WindowAdapter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cue.controller.CueController;
import javafx.stage.WindowEvent;

public class CueFrame extends JFrame
{
	private CueController 	controller;
	private CuePanel 		panel;
	
	public CueFrame(CueController controller)
	{
		super();
		this.controller = controller;
		this.panel 		= new CuePanel(controller);
		
		setupFrame();
	}
	
	private void setupFrame()
	{
//	    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    this.addWindowListener(new WindowAdapter()
		{
	    	public void windowClosing(WindowEvent event)
	    	{
	    		JFrame frame = (JFrame)event.getSource();
	    			    		
	    		int result = JOptionPane.showConfirmDialog(frame,
	    				"Do you want to save before closing?",
	    				"Exit Application",
	    				JOptionPane.YES_NO_CANCEL_OPTION,
	    				JOptionPane.WARNING_MESSAGE);
	    		
	    		if (result == JOptionPane.YES_OPTION)
	    		{
	    			String name = (String)JOptionPane.showInputDialog(frame,
	    					"It will be saved in your downloads folder\nWhat do you want to name it?",
	    					"Save Code",
	    					JOptionPane.INFORMATION_MESSAGE,
	    					null,
	    					null,
	    					"SavedCues.txt");
	    			try
	    			{
	    				FileWriter savedCode = new FileWriter(System.getProperty("user.home") + "/Downloads/" + name);
	    				savedCode.write(panel.getCode());
	    				savedCode.close();
	    				System.out.println("Saved the code");
	    			}
	    			catch (IOException e)
	    			{
	    				JOptionPane.showMessageDialog(frame, "Failed to create the file", "Failed to Save", JOptionPane.ERROR_MESSAGE, null);
	    				e.printStackTrace();
	    			}
	    			
	    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    		}
	    		else if (result == JOptionPane.NO_OPTION)
	    		{
	    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    		}
	    	}
		});
	    
		this.setContentPane(panel);
		this.setSize(800, 600);
		this.setTitle("Cue Tester");
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
