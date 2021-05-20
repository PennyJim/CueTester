package cue.controller;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Runner {
	
	public static void main(String[] args)
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");
//		System.setProperty("apple.awt.application.appearance", "NSAppearanceNameDarkAwua");
//		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Name"); //Sets the application name?
		
		final Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.ALL);
		final ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
//		consoleHandler.setFormatter(new Formatter() {
//			@Override
//			public String format(LogRecord record) {
//				return "EVENT: " + record.getMessage() + '\n';
//			}
//		});
		final Logger logger = Logger.getLogger("java.awt.event.Component");
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
		logger.addHandler(consoleHandler);
		
		CueController app = new CueController();
		app.start();
	}
}
