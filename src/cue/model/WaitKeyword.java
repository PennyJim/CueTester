package cue.model;

public class WaitKeyword extends Keyword
{
	private boolean hasStepped = false;
	
	private long startMilis;
	private int duration;
	
	public WaitKeyword(Parser parser, String inputs)
	{
		super(parser, inputs);
		String durationStr = inputs.split(" ")[0];
		try
		{
			duration = (int)Double.parseDouble(durationStr);
			if (duration < 0) { validateString = "Duration cannot be negative"; }
			else { validateString = ""; }
		}
		catch (NumberFormatException e)
		{
			Variable var = parser.getVariable(durationStr);
			
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

	@Override
	public void step()
	{
		if (!hasStepped)
		{
			startMilis = System.currentTimeMillis();
			hasStepped = true;
		}
	}

	@Override
	public boolean hasStep()
	{
		// TODO Auto-generated method stub
		if (!hasStepped) { return true; }
		return System.currentTimeMillis() < startMilis + duration;
	}

}
