package cue.view;

import javax.swing.JFrame;

import cue.controller.CueController;

@SuppressWarnings("serial")
public class CueFrame extends JFrame
{
	private CuePanel 		panel;
	
	public CueFrame(CueController controller)
	{
		super();
		this.panel 		= new CuePanel(controller);
		
		setupFrame();
	}
	
	public void display()
	{
		this.setVisible(true);
	}
	
	private void setupFrame()
	{
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		this.setSize(800, 600);
		this.setTitle("Cue Tester");
		this.setResizable(true);
		this.setLocationRelativeTo(null);
	}
}
