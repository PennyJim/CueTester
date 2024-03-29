package cue.model.keyword;

import cue.controller.CueController;
import cue.model.CueThread;

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

	@Override
	public void reset() {}

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
