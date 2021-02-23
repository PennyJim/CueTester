package cue.controller;

import cue.view.CueFrame;

public class CueController {

	private CueFrame frame;
	
	public CueController()
	{
		this.frame = new CueFrame(this);
	}
	
	public void start()
	{
		System.out.println("Program Started");
	}
}
