package cue.model;

import cue.controller.CueController;

public class WaitKeyword extends Keyword
{
	private boolean hasStepped = false;
	
	private long startMilis;
	private int duration;
	
	/**
	 * Sets up the duration based on the input
	 * 
	 * @param controller Used to get variables values
	 * @param inputs a non-negative number either plainly or within a variable
	 */
	public WaitKeyword(CueController controller, String inputs)
	{
		super(controller, inputs);
		String durationStr = inputs.split(" ")[0];
		try
		{
			duration = (int)Double.parseDouble(durationStr);
			if (duration < 0) { validateString = "Duration cannot be negative"; }
			else { validateString = ""; }
		}
		catch (NumberFormatException e)
		{
			Variable var = controller.getVariable(durationStr);
			
			if (var == null) { validateString = "Variable for duration does not exist"; }
			else if (var.isThree()) { validateString = "Variable for duration can't have 3 values"; }
			else
			{
				duration = (int)var.getValue()[0];
				if (duration < 0) { validateString = "Duration cannot be negative"; }
				else { validateString = ""; }
			}
		}
	}

	/**
	 * When first called, sets startMilis to current time<br>
	 * It otherwise does nothing.
	 */
	@Override
	public void step()
	{
		if (!hasStepped)
		{
			startMilis = System.currentTimeMillis();
			hasStepped = true;
		}
	}

	/**
	 * Returns whether or not the time passed since<br>
	 * first step, exceeds the duration given.
	 */
	@Override
	public boolean hasStep()
	{
		if (!hasStepped) { return true; }
		return System.currentTimeMillis() < startMilis + duration;
	}

}
