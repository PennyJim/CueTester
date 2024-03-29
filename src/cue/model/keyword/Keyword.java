package cue.model.keyword;

import cue.controller.CueController;

public abstract class Keyword
{
	protected CueController controller;
	protected String validateString;
	/**
	 * The required constructor to any inherited keyword 
	 * @param parser The object running the cues
	 * @param inputs A string of what is given along with the keyword
	 */
	public Keyword(CueController controller, String inputs)
	{
		this.controller = controller;
	}
	/**
	 * Reset the keyword as if it was never run
	 */
	public abstract void reset();
	/**
	 * Take a step forward in the keyword's execution
	 */
	public abstract void step();
	/**
	 * @return Whether or not there are any steps left to take
	 */
	public abstract boolean hasStep();
	
	/**
	 * Returns a string of what is wrong with the constructed inputs
	 * @return Either an empty string or a string describing what went wrong
	 */
	public String validateInputs()
	{
		return validateString;
	}
}
