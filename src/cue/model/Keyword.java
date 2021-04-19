package cue.model;

public interface Keyword
{
	public boolean go();		//Starts the cue
	public boolean stop();		//Cancels the cue
	public boolean pause();		//Pauses the cue
	public boolean play();		//Continues the cue
}
