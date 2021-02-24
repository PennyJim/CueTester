package cue.model;

public class RepeatRunner implements RunnerIFace
{
	private boolean isRunning = false;
	private Thread t;

	public void execute()
	{
		t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				executeOnThread();
			}
		});
		t.start();
	}

	public void executeOnThread() {}
	
	public boolean isAlive() { return isRunning; }
	public boolean isThreadAlive() { return t.isAlive(); }
	public String getCommandType() { return "REPEAT"; }
}
