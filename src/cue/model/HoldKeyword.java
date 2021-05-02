package cue.model;

import cue.controller.CueController;

public class HoldKeyword extends Keyword
{

	public HoldKeyword(CueController controller, String inputs)
	{
		super(controller, inputs);
		validateString = "";
	}

	@Override
	public void step() {}

	@Override
	public boolean hasStep()
	{
		return true;
	}

}
