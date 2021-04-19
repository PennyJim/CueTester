package cue.model;

public abstract class Keyword
{
	public Keyword(String code) {}		//Default constructor
	public abstract boolean go();		//Starts the cue
	public abstract boolean stop();		//Cancels the cue
	public abstract boolean pause();		//Pauses the cue
	public abstract boolean play();		//Continues the cue
}
