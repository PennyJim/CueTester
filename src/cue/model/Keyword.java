package cue.model;

public abstract class Keyword
{
	public Keyword(Parser parser, String inputs) {}		//Default constructor
	public abstract void step();
	public abstract boolean hasStep();
	
	public abstract String validateInputs();
}
