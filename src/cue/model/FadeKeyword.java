package cue.model;

import java.awt.Color;

import javax.swing.SwingUtilities;

public class FadeKeyword extends Keyword
{
	private Parser parser;
	private String validateString;
	
	private int[] endColor;
	private int duration;
	
	private long startMilis = 0;
	private int[] startColor;
	
	public FadeKeyword(Parser parser, String inputs) //TODO: Define endColor and length
	{
		super(parser, inputs);
		this.parser = parser;
		
		String[] input = inputs.split(" ");
		Boolean usedVariable = null;
		
		try
		{
			Double colorR = Double.parseDouble(input[0]);
			Double colorG = Double.parseDouble(input[1]);
			Double colorB = Double.parseDouble(input[2]);
			
			
			if (colorR > 100.0 || colorR < 0.0) { validateString = "Input 1 is out of bounds"; }
			else if (colorG > 100.0 || colorG < 0.0) { validateString = "Input 2 is out of bounds"; }
			else if (colorB > 100.0 || colorB < 0.0) { validateString = "Input 3 is out of bounds"; } 
			else
			{
				validateString = "";
				usedVariable = false;
				
				endColor = new int[] {	(int)(colorR * 2.55),
										(int)(colorG * 2.55),
										(int)(colorB * 2.55)};
			}
		}
		catch (Exception e)
		{
			if (parser.isVariable(input[0]))
			{
				Variable var = parser.getVariable(input[0]); //Might need secondary checks for variable type. At least for single/multiple value variables
				if (var.isThree())
				{
					double[] values = var.getValue();
					
					if (values[0] > 100.0 || values[0] < 0.0) { validateString = "Variable '" + input[0] + "', value 1 is out of bounds"; }
					else if (values[1] > 100.0 || values[1] < 0.0) { validateString = "Variable '" + input[0] + "', value 2 is out of bounds"; }
					else if (values[2] > 100.0 || values[2] < 0.0) { validateString = "Variable '" + input[0] + "', value 3 is out of bounds"; } 
					else
					{
						validateString = "";
						usedVariable = true;
						
						endColor = new int[] {	(int)(values[0] * 2.55),
												(int)(values[1] * 2.55),
												(int)(values[2] * 2.55)};
					}
				}
				else
				{
					validateString = "Color variable needs 3 values";
				}
			}
			else
			{
				validateString = "A color input is neither a number or variable";
			}
		}
		
		if (usedVariable == null) { return; }
		try
		{
			Double duration;
			if (usedVariable)
			{
				duration = Double.parseDouble(input[1]);
			}
			else
			{
				duration = Double.parseDouble(input[3]);
			}
			
			if (duration < 0.0) { validateString = "Value of duration cannot be negative"; }
			else
			{
				this.duration = duration.intValue();
			}
		}
		catch (Exception e)
		{
			Variable var = null;
			if (usedVariable)
			{
				if (parser.isVariable(input[1]))
				{
					var = parser.getVariable(input[1]);
				}
				else
				{
					validateString = "Duration input is neither a number or variable";
				}
			}
			else
			{
				if (parser.isVariable(input[3]))
				{
					var = parser.getVariable(input[3]);
				}
				else
				{
					validateString = "Duration input is neither a number or variable";
				}
			}
			
			if (var == null) { return; }
			if (var.isThree())
			{
				validateString = "Duration variable cannot hold 3 values";
			}
			else
			{
				Double duration = var.getValue()[0];
				if (duration < 0.0) { validateString = "Value of duration cannot be negative"; }
				else
				{
					this.duration = duration.intValue();
				}
			}
		}
	}

	@Override
	public void step()
	{
		if (startMilis == 0 || startColor == null)
		{
			startMilis = System.currentTimeMillis();
			Color start = parser.panel.getBackground();
			startColor = new int[] {start.getRed(), start.getGreen(), start.getBlue()};
			
			
			
		}
		
		double factor = (double)(System.currentTimeMillis() - startMilis) / (double)duration;
		System.out.println(parser.isPaused());
		SwingUtilities.invokeLater(() -> {
			parser.panel.setBackground(new Color(	startColor[0] + (int)(factor * (double)(endColor[0] - startColor[0])),
					startColor[1] + (int)(factor * (double)(endColor[1] - startColor[1])),
					startColor[2] + (int)(factor * (double)(endColor[2] - startColor[2]))));
		});
	}

	@Override
	public boolean hasStep()
	{
		if (startMilis == 0) { return true; }
		return System.currentTimeMillis() < startMilis + duration;
	}
	
	@Override
	public String validateInputs()
	{
		
		return validateString;
	}

}
