package cue.model;

import java.awt.Color;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cue.controller.CueController;

public class FadeKeyword extends Keyword
{
	
	private int[] endColor;
	private int duration;
	
	private long startMilis = 0;
	private int[] startColor;
	
	/**
	 * Sets up the required variables and checks if the inputs are valid:<br>
	 * <pre>
	 * Variable one/first 3 numbers:	must be 0-100
	 * Variable two/last number:	must be non-negative
	 * </pre>
	 * 
	 * @param controller Used to access Panel to change its color
	 * @param inputs 4 numbers or 2 variables (3 and 1 number)
	 * @see Keyword#Keyword(Parser, String)
	 */
	public FadeKeyword(CueController controller, String inputs)
	{
		super(controller, inputs);
		
		Scanner input = new Scanner(inputs);
		String testInput = input.next();
		try
		{
			double colorR = Double.parseDouble(testInput);
			testInput = null;
			double colorG = Double.parseDouble(input.next());
			double colorB = Double.parseDouble(input.next());
			
			if (colorR > 100.0 || colorR < 0.0) { validateString = "Red value is out of bounds"; }
			else if (colorG > 100.0 || colorG < 0.0) { validateString = "Green value is out of bounds"; }
			else if (colorB > 100.0 || colorB < 0.0) { validateString = "Blue value is out of bounds"; } 
			else
			{
				endColor = new int[] {	(int)(colorR * 2.55),
										(int)(colorG * 2.55),
										(int)(colorB * 2.55)};
			}
		}
		catch (NumberFormatException e)
		{
			if (testInput == null) { validateString = "Not all color values are numbers"; }
			else
			{
				Variable var = controller.getVariable(testInput);
				
				if (var == null) { validateString = "Variable for color does not exist"; }
				else if (!var.isThree()) { validateString = "Variable for color needs 3 values"; }
				else
				{
					double[] values = var.getValue();
					if (values[0] > 100.0 || values[0] < 0.0) { validateString = "Red value is out of bounds"; }
					else if (values[1] > 100.0 || values[1] < 0.0) { validateString = "Green value is out of bounds"; }
					else if (values[2] > 100.0 || values[2] < 0.0) { validateString = "Blue value is out of bounds"; } 
					else
					{
						endColor = new int[] {	(int)(values[0] * 2.55),
												(int)(values[1] * 2.55),
												(int)(values[2] * 2.55)};
					}
				}
			}
		}
		catch (NoSuchElementException e)
		{
			validateString = "Not enough input values";
			input.close();
			return;
		}
		
		if (validateString == null)
		{
			try
			{
				testInput = input.next();
				double length = Double.parseDouble(testInput);
				if (length < 0)
				{
					validateString = "Duration cannot be negative";
				}
				else
				{
					duration = (int)length;
					validateString = "";
				}
			}
			catch (NumberFormatException e)
			{
				Variable var = controller.getVariable(testInput);
				
				if (var == null) { validateString = "Variable for duration does not exist"; }
				else if (var.isThree()) { validateString = "Variable for duration can't have 3 values"; }
				else
				{
					double length = var.getValue()[0];
					if (length < 0)
					{
						validateString = "Duration cannot be negative";
					}
					else
					{
						duration = (int)length;
						validateString = "";
					}
				}
			}
			catch (NoSuchElementException e)
			{
				validateString = "Not enough input values";
			}
		}
		input.close();
	}

	/**
	 * {@inheritDoc}<br><br>
	 * Proportionally alters the color of the panel, which is<br>
	 * Proportional to the time since first step and the duration
	 */
	@Override
	public void step()
	{
		if (startMilis == 0 || startColor == null)
		{
			startMilis = System.currentTimeMillis();
			Color start = controller.getPanel().getBackground();
			startColor = new int[] {start.getRed(), start.getGreen(), start.getBlue()};
			
			
			
		}
		
		double factor = (double)(System.currentTimeMillis() - startMilis) / (double)duration;
		controller.panel.setBackground(new Color(	startColor[0] + (int)(factor * (double)(endColor[0] - startColor[0])),
												startColor[1] + (int)(factor * (double)(endColor[1] - startColor[1])),
												startColor[2] + (int)(factor * (double)(endColor[2] - startColor[2]))));
	}

	/**
	 * Sets the panel to the final color given.<br>
	 * Used to avoid cases where the last step is only<br>
	 * 70% complete due to extremely small duration<br>
	 * <br>
	 * Called in {@link #hasStep()} when it returns false
	 */
	private void finalStep()
	{
		controller.panel.setBackground(new Color(endColor[0], endColor[1], endColor[2]));
	}
	
	/**
	 * Returns whether or not the time passed since first step exceeds the duration found in the inputs
	 */
	@Override
	public boolean hasStep()
	{
		if (startMilis == 0) { return true; }
		if (System.currentTimeMillis() > startMilis + duration)
		{
			finalStep();
			return false;
		}
		else { return true; }
	}
}
