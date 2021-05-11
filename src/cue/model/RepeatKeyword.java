package cue.model;

import cue.controller.CueController;

public class RepeatKeyword extends Keyword
{

	private int maxRuns;
	private int curRuns = 0;
	
	private boolean isInfinite;
	
	public RepeatKeyword(CueController controller, String inputs)
	{
		super(controller, inputs);
		String durationStr = inputs.split(" ")[0];
		if (durationStr.length() == 0)
		{
			isInfinite = true;
			validateString = "";
		}
		else
		{
			isInfinite = false;
			try
			{
				maxRuns = (int)Double.parseDouble(durationStr);
				if (maxRuns < 0) { validateString = "Repeat count cannot be negative"; }
				else { validateString = ""; }
			}
			catch (NumberFormatException e)
			{
				Variable var = controller.getVariable(durationStr);
				
				if (var == null) { validateString = "Variable for repeat count does not exist"; }
				else if (var.isThree()) { validateString = "Variable for repeat count can't have 3 values"; }
				else
				{
					maxRuns = (int)var.getValue()[0];
					if (maxRuns < 0) { validateString = "Repeat count cannot be negative"; }
					else { validateString = ""; }
				}
			}
		}
	}

	@Override
	public void step()
	{
		curRuns++;
		System.out.println("'Repeat'");
	}

	@Override
	public boolean hasStep()
	{
		return curRuns < maxRuns || isInfinite;
	}

	public boolean isInfinite()
	{
		return isInfinite;
	}

	@Override
	public void reset()
	{
		curRuns = 0;
	}

}
