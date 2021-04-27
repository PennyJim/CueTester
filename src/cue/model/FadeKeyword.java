package cue.model;

public class FadeKeyword extends Keyword
{
	private Parser parser;
	private String validateString;
	
	private int[] endColor;
	private int length;
	
	public FadeKeyword(Parser parser, String inputs)
	{
		super(parser, inputs);
		this.parser = parser;
		
		String[] input = inputs.split(" ");
		
		try
		{
			Double colorR = Double.parseDouble(input[0]);
			Double colorG = Double.parseDouble(input[1]);
			Double colorB = Double.parseDouble(input[2]);
			
			
			if (colorR > 100.0 || colorR < 0.0) { validateString = "Input 1 is out of bounds"; }
			else if (colorG > 100.0 || colorG < 0.0) { validateString = "Input 2 is out of bounds"; }
			else if (colorB > 100.0 || colorB < 0.0) { validateString = "Input 3 is out of bounds"; } 
			else { validateString = ""; }
		}
		catch (Exception e)
		{
			if (parser.isVariable(input[0]))
			{
				parser.getVariable(input[0]); //Might need secondary checks for variable type. At least for single/multiple value variables
				validateString = "";
			}
			else
			{
				validateString = input[0] + " is not a number or variable";
			}
		}
	}

	@Override
	public void step()
	{
	}

	@Override
	public boolean hasStep()
	{
		return false;
	}
	
	@Override
	public String validateInputs()
	{
		
		return validateString;
	}

}
