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
		Scanner input = new Scanner(inputs);
		
		varName = input.next();
		
		try
		{
			double a = Double.parseDouble(input.next());
			
			if (input.hasNext())
			{
				try
				{
					double b = Double.parseDouble(input.next());
					double c = Double.parseDouble(input.next());
					
					varValue = new Variable(a, b, c);
					validateString = "";
				}
				catch (NumberFormatException e)
				{
					validateString = "Not all vaulues are valid numbers";
				}
				catch (NoSuchElementException e)
				{
					validateString = "Need one or three values, not two";
				}
			}
			else
			{
				varValue = new Variable(a);
				validateString = "";
			}
		}
		catch (NumberFormatException e)
		{
			validateString = "Value is not a valid number";
		}
		
		input.close();
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
