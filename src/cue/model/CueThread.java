package cue.model;

import java.util.concurrent.atomic.AtomicBoolean;

import cue.controller.CueController;
import cue.model.keyword.Keyword;

public class CueThread extends Thread
{
	private AtomicBoolean exitEarly;
	private AtomicBoolean stopEarly;
	private AtomicBoolean isPaused;
	
	private CueController controller;
	private CueSyntaxTree runTree;
	private Keyword currentWord;
	
	/**
	 * Initializes the CueThread
	 * @param name The name of the thread created
	 * @param parser The connection to the panel
	 */
	public CueThread(String name, CueController controller)
	{
		super(name);
		this.controller = controller;
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
						runTree.setExit(exitEarly.get());
						exitEarly.set(false);
					}
					stopEarly.set(false);
					runTree = null; //May regret
					try { wait(2000); }
					catch (InterruptedException e) {}
					controller.resetPanel();
				}
			}
			try { Thread.sleep(100); }
			catch (InterruptedException e) {}
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
	 * Resumes execution of the current syntax tree
	 */
	public synchronized void play()
	{
		isPaused.set(false);
	}
	
	/**
	 * @return Whether or not execution is paused
	 */
	public synchronized boolean isPaused()
	{
		return isPaused.get();
	}
	
	/**
	 * Moves onto the next item in the syntax tree without regard of whether or not the current one is done
	 */
	public synchronized void moveForward()
	{
		exitEarly.set(true);
	}
}
