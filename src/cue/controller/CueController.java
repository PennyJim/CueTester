package cue.controller;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import cue.model.*;
import cue.model.keyword.*;
import cue.view.CueFrame;

public class CueController {

	private static final HashMap<String,Class<? extends Keyword>> KEYWORDS;
	static {
		KEYWORDS = new HashMap<String,Class<? extends Keyword>>();
		KEYWORDS.put(KEY.VARIABLE,	VariableKeyword.class);
		KEYWORDS.put(KEY.FADE,		FadeKeyword.class);
		KEYWORDS.put(KEY.HOLD,		HoldKeyword.class);
		KEYWORDS.put(KEY.WAIT,		WaitKeyword.class);
		KEYWORDS.put(KEY.REPEAT,	RepeatKeyword.class);
		KEYWORDS.put(KEY.CLOSE,		StopRepeatKeyword.class);
	}
	
	public static final String DEFAULT = "defaultStyle";
	public static final String NUMBER = "numberStyle";
	public static final String VARIABLE = "variableStyle";
	public static final String KEYWORD = "keywordStyle";
	
	//Basic list of keywords wanted:
	//Var - Creates a static variable
	//Fade - Fades to a color over x time
	//Hold - Waits for a user input to move on
	//Wait - Waits x amount of time and moves on
	//Repeat - Starts (and ends) a loop that loops x times or moves on on go
	/**
	 * A static class containing static strings defining the word for each keyword
	 * @author char2259
	 *
	 */
	public static final class KEY {
		public static final String VARIABLE	= "VAR";
		public static final String FADE		= "FADE";
		public static final String HOLD		= "HOLD";
		public static final String WAIT		= "WAIT";
		public static final String REPEAT	= "REPEAT";
		public static final String CLOSE	= "CLOSEREPEAT";
	}
	
	/**
	 * Returns a list of variables found in the given code
	 * @param code The code it's looking for variables in
	 * @return A list of the found variables
	 */
	public static List<String> parseVariables(String code)
	{
		ArrayList<String> variables = new ArrayList<String>();
		
		Scanner lines = new Scanner(code);
		while (lines.hasNext())
		{
			String line = lines.nextLine();
			String[] words = line.split(" ");
			
			if (words.length >= 3 && words[0].equalsIgnoreCase(KEY.VARIABLE))
			{
				variables.add(words[1].toUpperCase());
			}
		}
		lines.close();
		
		return variables;
	}
	
	/**
	 * Returns what type of word the given word is and if it is in the variableList
	 * @param word The word that you're testing
	 * @param variableList The list of 'known' variables
	 * @return One of 4 static strings indicating word type
	 */
	public static String wordType(String word, List<String> variableList)
	{
		word = word.toUpperCase();
		
		try
		{
			Double.parseDouble(word);
			return NUMBER;
		}
		catch (Exception e) {}
		
		if (KEYWORDS.get(word.trim()) != null)
		{
			return KEYWORD;
		}
		
		if (variableList != null && variableList.contains(word))
		{
			return VARIABLE;
		}
		
		return DEFAULT;
	}
	
	private CueFrame frame;
	private CueSyntaxTree syntaxTree;
	
	public JComponent panel;
	private Color defaultBG;
	
	private CueThread thread;
	private HashMap<String,Variable> variables;
	
	public CueController()
	{
		this.panel = null;
		this.defaultBG = Color.WHITE;
		this.frame = new CueFrame(this);
		this.variables = new HashMap<String,Variable>();
		this.thread = new CueThread("Cues", this);
	}
	
	public void start()
	{
		System.out.println("Program Started");
		
		thread.start();
		frame.display();
	}
	
	/**
	 * Initializes the syntax tree and puts it within the thread running the cue
	 * @param code The code that's parsed into a syntax tree
	 * @throws IOException An error defining what is wrong with the given code. Currently does not give line numbers
	 */
	public void parse(String code) throws IOException
	{
		int lineNumber = 0;
		Scanner lines = new Scanner(code);
		syntaxTree = new CueSyntaxTree();
		this.variables = new HashMap<String,Variable>();
		
		while (lines.hasNext())
		{
			lineNumber++;
			String line = lines.nextLine();
			int splitIndex = line.indexOf(' ');
			String keyword,inputs;
			
			if (line.equals("")) { continue; }
			
			if (splitIndex == -1)
			{
				keyword = line.trim();
				inputs = "";
			}
			else
			{
				keyword = line.substring(0, splitIndex).trim();
				inputs = line.substring(splitIndex + 1);
			}
			Class<? extends Keyword> word = KEYWORDS.get(keyword.toUpperCase());
			
			
			if (word != null)
			{
				String error;
				try
				{
					Class<?> args[] = new Class[] {CueController.class, String.class};
					
					Keyword constructedWord = word.getConstructor(args).newInstance(this, inputs);
					
					error = constructedWord.validateInputs();
					if (error.equals(""))
					{
						syntaxTree.add(constructedWord);
					}
					else
					{
						throw new IOException(lineNumber + ": " + error);
					}
				}
				catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | SecurityException e)
				{
					lines.close();
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Keyword didn't create properly.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				lines.close();
				throw new IOException("Keyword '" + keyword + "' not found");
			}
		}
		
		thread.play();
		System.out.println(syntaxTree);
		thread.setRunTree(syntaxTree);
		
		listVariables();
		
		lines.close();
	}
	
	/**
	 * Resets the panel to the given default background
	 */
	public void resetPanel()
	{
		panel.setBackground(defaultBG);
	}
	public void setPanel(JComponent panel)
	{
		this.panel = panel;
	}
	public JComponent getPanel()
	{
		return panel;
	}
	public void setDefaultBG(Color newBG)
	{
		if (newBG == null) { throw new IllegalArgumentException("Cannot be null"); }
		defaultBG = newBG;
	}
	
	/**
	 * @see CueThread#stopCues()
	 */
	public void stop()
	{
		synchronized (thread)
		{
			thread.stopCues();
			thread.notifyAll();
		}
	}
	/**
	 * @see CueThread#pause()
	 */
	public void pause()
	{
		synchronized (thread)
		{
			thread.pause();
			thread.notifyAll();
		}
	}
	/**
	 * @see CueThread#play()
	 */
	public void play()
	{
		synchronized (thread)
		{
			thread.play();
			thread.notifyAll();
		}
	}
	/**
	 * @return whether or not the cue is paused
	 * @see CueThread#isPaused()
	 */
	public boolean isPaused()
	{
		return thread.isPaused();
	}
	/**
	 * @see CueThread#moveForward()
	 */
	public void moveForward()
	{
		synchronized(thread)
		{
			thread.moveForward();
			thread.notifyAll();
		}
	}

	/**
	 * Checks whether the given variable name is in use
	 * @param variable The variable name being tested
	 * @return Whether or not the variable name is in use
	 */
	public boolean isVariable(String variable)
	{
		return variables.get(variable) != null;
	}
	/**
	 * Adds a variable of the given value with the given name to the HashMap
	 * @param variable The name of the new variable
	 * @param value The value of the new variable
	 * @return Whether or not it was successful in adding a new variable
	 */
	public boolean addVariable(String variable, Variable value)
	{
		if (isVariable(variable)) { return false; }
		variables.put(variable, value);
		return true;
	}
	/**
	 * Retrieves a variable with the given name
	 * @param variable The name of the variable you're looking for
	 * @return The found variable or null if it doesn't exist
	 */
	public Variable getVariable(String variable)
	{
		return variables.get(variable);
	}
	/**
	 * @return a string of all the variables and their values
	 */
	public String listVariables()
	{
		String variableList = "";
		for (Entry<String,Variable> variable : variables.entrySet())
		{
			variableList += variable.getKey() + ": " + variable.getValue().toString() + "\n";
		}
		return variableList;
	}
	
	public void handleErrors(Exception e)
	{
		JOptionPane.showMessageDialog(null, e.getMessage(), "Cue Tester", JOptionPane.WARNING_MESSAGE, null);
	}
}
