package cue.controller;

public class Runner {
	
	public static void main(String[] args)
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");
//		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Name"); //Sets the application name?
		
		CueController app = new CueController();
		app.start();
	}
}
