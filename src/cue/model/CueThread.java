package cue.model;

public class CueThread extends Thread
{
	private boolean exitEarly;
	private boolean isPaused;
	
	private CueSyntaxTree runTree;
	private Keyword currentWord;
	
	public CueThread(String name)
	{
		super(name);
		exitEarly = false;
		isPaused = true;
	}
	
	public void setRunTree(CueSyntaxTree runTree)
	{
		this.runTree = runTree;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
//			System.out.println("Scree");
			if (runTree != null)
			{
				synchronized(this)
				{
					while(runTree.hasNext() && !exitEarly)
					{
						currentWord = runTree.next();
						while(currentWord.hasStep())
						{
							long start = System.currentTimeMillis();
							currentWord.step();
							long end = System.currentTimeMillis();
							
							if (end - start < 25) { try { sleep(start - end); }
							catch (InterruptedException e) {} }
		
							while(isPaused)
							{
								try { wait(); }
								catch (InterruptedException e){}
							}
						}
					}
				}
			}
			try { Thread.sleep(100); }
			catch (InterruptedException e) {}
		}
	}
	
	public synchronized void pause()
	{
		isPaused = true;
	}
	
	public synchronized void play()
	{
		isPaused = false;
	}
	
	public synchronized void moveForward()
	{
		exitEarly = true;
	}
}
