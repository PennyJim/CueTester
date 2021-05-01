package cue.model;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class VariableKeyword extends Keyword
{
	private boolean hasStepped = false;
	
	private String varName;
	private Variable varValue;
	
	public VariableKeyword(Parser parser, String inputs)
	{
		super(parser, inputs);
	}

	@Override
	public void step()
	{
		// TODO Auto-generated method stub
		parser.addVariable(varName, varValue);
		
		hasStepped = true;
	}

	@Override
	public boolean hasStep()
	{
		return !hasStepped;
	}
}
