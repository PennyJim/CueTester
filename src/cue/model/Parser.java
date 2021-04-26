package cue.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser
{
	private static HashMap<String,Class<Keyword>> keywords; //Create Custom Objects for each keyword all following the abstract Keyword.
	static {
		keywords = new HashMap<String,Class<Keyword>>();
		keywords.put("VAR", Keyword.class);
		keywords.put("FADE", Keyword.class);
		keywords.put("HOLD", Keyword.class);
		keywords.put("WAIT", Keyword.class);
		keywords.put("REPEAT", Keyword.class);
	}
	
	public static String DEFAULT = "defaultStyle";
	public static String NUMBER = "numberStyle";
	public static String VARIABLE = "variableStyle";
	public static String KEYWORD = "keywordStyle";
	
	public static List<String> parseVariables(String code)
	{
		ArrayList<String> variables = new ArrayList<String>();
		
		Scanner lines = new Scanner(code);
		while (lines.hasNext())
		{
			String line = lines.nextLine();
			String[] words = line.split(" ");
			
			if (words.length >= 3 && words[0].equalsIgnoreCase("VAR"))
			{
				variables.add(words[1].toUpperCase());
			}
		}
		
		return variables;
	}
	
	public static String wordType(String word, List<String> variableList)
	{
		word = word.toUpperCase();
		
		try
		{
			Double.parseDouble(word);
			return NUMBER;
		}
		catch (Exception e) {}
		
		if (keywords.get(word) != null)
		{
			return KEYWORD;
		}
		
		if (variableList != null && variableList.contains(word))
		{
			return VARIABLE;
		}
		
		return DEFAULT;
	}
	
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
			Class<Keyword> word = keywords.get(keyword.toUpperCase());
			
			
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
		lines.close();
		
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
