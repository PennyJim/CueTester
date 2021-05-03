package cue.model;

import java.util.NoSuchElementException;
import java.util.Scanner;

import cue.controller.CueController;

public class VariableKeyword extends Keyword
{
	
	public VariableKeyword(CueController controller, String inputs)
	{
		super(controller, inputs);
		Scanner input = new Scanner(inputs);
		
		String varName = input.next();
		Variable varValue = null;
		
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

		if (varValue != null)
		{
			controller.addVariable(varName, varValue);
			System.out.println("Added " + varName + ": " + varValue);
		}
		input.close();
	}

	@Override
	public void step() {}

	@Override
	public boolean hasStep()
	{
		return false;
	}
}
