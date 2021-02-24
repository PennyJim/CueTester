package cue.model;

public interface RunnerIFace
{
	public boolean began = false;
	public void execute();
	public void executeOnThread();
	public default boolean isAlive()
	{
		return false;
	}
	public default String getCommandType()
	{
		return null;
	}
}
