package cue.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class CueThread extends Thread
{
	private AtomicBoolean exitEarly;
	private AtomicBoolean stopEarly;
	private AtomicBoolean isPaused;
	
	private Parser parser;
	private CueSyntaxTree runTree;
	private Keyword currentWord;
	
	public CueThread(String name, Parser parser)
	{
		super(name);
		this.parser = parser;
		exitEarly = new AtomicBoolean();
		stopEarly = new AtomicBoolean();
		isPaused = new AtomicBoolean();
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
					while(runTree.hasNext() && !stopEarly.get())
					{
						currentWord = runTree.next();
						while(currentWord.hasStep() && !exitEarly.get())
						{
							long start = System.currentTimeMillis();
							currentWord.step();
							long end = System.currentTimeMillis();
							
							if (end - start < 25) { try { sleep(25 - (end - start)); }
							catch (InterruptedException e) {} }
							
							while(isPaused.get())
							{
								System.out.println("Slep");
								try { wait(); }
								catch (InterruptedException e){}
							}
						}
						exitEarly.set(false);;
					}
					stopEarly.set(false);
					runTree = null; //May regret
					try { Thread.sleep(2000); }
					catch (InterruptedException e) {}
					parser.resetPanel();
				}
			}
			try { Thread.sleep(100); }
			catch (InterruptedException e) {}
//			System.out.println("Die");
		}
	}
	
	public synchronized void stopCues()
	{
		stopEarly.set(true);
		exitEarly.set(true);
	}
	
	public synchronized void pause()
	{
		isPaused.set(true);
	}
	
	public boolean isPaused()
	{
		return isPaused.get();
	}
	
	public synchronized void play()
	{
		isPaused.set(false);
	}
	
	public synchronized void moveForward()
	{
		exitEarly.set(true);
	}
}
