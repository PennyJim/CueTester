package cue.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser
{
	private static HashMap<String,Class<Keyword>> keywords = new HashMap<String,Class<Keyword>>(); //Create Custom Objects for each keyword all following an interface.
	
	//Basic list of keywords wanted:
	//ColorVar - create a static color variable
	//Var - Creates a static variable			//Entirely new for me to implement
	//Fade - Fades to a color over x time
	//Hold - Waits for a user input to move on
	//Wait - Waits x amount of time and moves on
	//Repeat - Starts (and ends) a loop that loops x times or moves on on go
	
	private CueSyntaxTree syntaxTree;	//Custom tree to implement Abstract Tree Syntax for loops?
								//Otherwise I'll use an array
								//Need to learn how to implement properly
	
	public boolean initParse(String code) throws IOException
	{
		Scanner lines = new Scanner(code);
		
		while (lines.hasNext())
		{
			String line = lines.nextLine();
			int splitIndex = line.indexOf(' ');
			String keyword,inputs;
			
			if (splitIndex == -1)
			{
				keyword = line;
				inputs = "";
			}
			else
			{
				keyword = line.substring(0, splitIndex);
				inputs = line.substring(splitIndex + 1);
			}
			Class<Keyword> word = keywords.get(keyword);
			
			
			if (word != null)
			{
				String error;
				try
				{
					error = (String) word.getMethod("validateInputs", null).invoke(inputs);
				
					if (error.equals(""))
					{
						syntaxTree.add(word.getConstructor(null).newInstance(inputs));
					}
					else
					{
						throw new IOException(error);
					}
				}
				catch (IOException e)
				{
					throw e;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				throw new IOException("Keyword '" + keyword + "' not found");
			}
		}
		
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
