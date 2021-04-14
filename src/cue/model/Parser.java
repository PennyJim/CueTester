package cue.model;

import java.util.HashMap;

public class Parser
{
	private static HashMap<String,Object> keywords = new HashMap<String,Object>(); //Create Custom Objects for each keyword all following an interface. Likely similar to current Runner's but static.
	
	//Basic list of keywords wanted:
	//ColorVar - create a static color variable
	//Var - Creates a static variable
	//Fade - Fades to a color over x time
	//Hold - Waits for a user input to move on
	//Wait - Waits x amount of time and moves on
	//Repeat - Starts (and ends) a loop that loops x times or moves on on go
	
	private Object syntaxTree;	//Custom tree to implement Abstract Tree Syntax for loops?
								//Otherwise I'll use 
								//Need to learn how to implement properly
	
	public boolean initParse(String code)
	{
		
		
		return false;
	}
	
	public boolean pause()
	{
		return false;
	}
	
	public boolean play()
	{
		return false;
	}
	
	public boolean moveForward()	//For fade cues. Decide to either jump to end,
	{								//take current color and move on, //Likely this one
		return false;				//or dynamically continue fading
	}
	
	
}
