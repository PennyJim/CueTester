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
	
	/**
	 * Initializes the CueThread
	 * @param name The name of the thread created
	 * @param parser The connection to the panel
	 */
	public CueThread(String name, Parser parser)
	{
		super(name);
		this.parser = parser;
		exitEarly = new AtomicBoolean();
		stopEarly = new AtomicBoolean();
		isPaused = new AtomicBoolean();
	}
	
	/**
	 * Sets what run tree is going to be ran
	 * @param runTree The run tree to be ran
	 */
	public void setRunTree(CueSyntaxTree runTree)
	{
		this.runTree = runTree;
	}
	
	/**
	 * An infinite loop always attempting to run any given cueTree.<br>
	 * Has control functions to control the execution of the cueTree
	 * @see #stopCues()
	 * @see #pause()
	 * @see #play()
	 * @see #isPaused()
	 * @see #moveForward()
	 */
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
							
							if (end - start < 25) { try { wait(25 - (end - start)); }
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
					try { wait(2000); }
					catch (InterruptedException e) {}
					parser.resetPanel();
				}
			}
			try { Thread.sleep(100); }
			catch (InterruptedException e) {}
//			System.out.println("Die");
		}
	}
	
	/**
	 * Stops execution of the current syntax tree
	 */
	public synchronized void stopCues()
	{
		stopEarly.set(true);
		exitEarly.set(true);
		isPaused.set(false);
	}
	
	/**
	 * Pauses execution of the current syntax tree
	 */
	public synchronized void pause()
	{
		isPaused.set(true);
	}
	
	/**
	 * @return Whether or not execution is paused
	 */
	public synchronized boolean isPaused()
	{
		return isPaused.get();
	}
	
	/**
	 * Resumes execution of the current syntax tree
	 */
	public synchronized void play()
	{
		isPaused.set(false);
	}
	
	/**
	 * Moves onto the next item in the syntax tree without regard of whether or not the current one is done
	 */
	public synchronized void moveForward()
	{
		exitEarly.set(true);
	}
}
