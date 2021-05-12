package cue.model;

import cue.controller.CueController;

public class StopRepeatKeyword extends Keyword
{

	public StopRepeatKeyword(CueController controller, String inputs)
	{
		super(controller, inputs);
		validateString = "";
	}

	@Override
	public void reset() {}

	@Override
	public void step() {}

	@Override
	public boolean hasStep()
	{
		return false;
	}

}
