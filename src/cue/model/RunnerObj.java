package cue.model;

public abstract class RunnerObj {
	
	public boolean began = false;
	
	public void execute() {
	}
	public void executeOnThread() {
	}
	public boolean isAlive()
	{
		return false;
	}
	public String getCommandType()
	{
		return null;
	}
}
