package cue.model;

import cue.controller.CueController;

public class HoldKeyword extends Keyword
{

	/**
	 * Only constructs HoldKeyword
	 * 
	 * @param controller only passed to super
	 * @param inputs ignored
	 */
	public HoldKeyword(CueController controller, String inputs)
	{
		super(controller, inputs);
		validateString = "";
	}

	/**
	 * Empty Function
	 */
	@Override
	public void step() {}

	/**
	 * To keep {@link CueThread}'s attention, returns true.
	 */
	@Override
	public boolean hasStep()
	{
		return true;
	}

}
