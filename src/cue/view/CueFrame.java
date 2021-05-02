package cue.view;

import javax.swing.JFrame;

import cue.controller.CueController;

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
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		this.setSize(800, 600);
		this.setTitle("Cue Tester");
		this.setResizable(true);
		this.setLocationRelativeTo(null);
	}
	
	public void display()
	{
		this.setVisible(true);
	}
}
