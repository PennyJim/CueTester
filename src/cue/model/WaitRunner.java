package cue.model;

public class WaitRunner implements RunnerIFace
{
	private boolean isRunning = false;
	private int length;
	private Thread t;

	public WaitRunner(int length)
	{
		this.length = length;
	}
	
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

	public void executeOnThread() {try { Thread.sleep(length); } catch (Exception e) {} }
	public void stopEarly() {}
	
	public boolean isAlive() { return isRunning; }
	public boolean isThreadAlive() { return t.isAlive(); }
	public String getCommandType() { return "WAIT"; }
}
