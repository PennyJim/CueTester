package cue.model;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTextArea;

//TODO: Implement no run while run, chnge popup, accomodate for repeat

public class RunRunner {
	
	private String code;
	private final JTextArea textArea;
	private final JButton stopButton;
	private final JButton proceedButton;
	private final JButton pauseButton;
	
	
	private ArrayList<RunnerIFace> commands;
	public boolean iFailed = false;
	private boolean doProceed = false;
	private Thread t;
	
	public RunRunner(String code, JTextArea textArea, JButton stopButton, JButton proceedButton, JButton pauseButton) {
		this.code = code;
		this.textArea = textArea;
		this.stopButton = stopButton;
		this.proceedButton = proceedButton;
		this.pauseButton = pauseButton;
		
		commands = new ArrayList<>();
//		whatDoINeedToRun();
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public String compile()
	{
		try
		{
			if (!code.substring(0, 5).equals("START")) { return "Needs to start with \"START\""; }
			
			String[] sentences = code.split("\n");
			commands.clear();
			for (int index = 0; index < sentences.length; index++)
			{
				String[] words = sentences[index].split(" ");
				if (words[0].equals("START"))
				{
					int[] newColor = null;
					if (!words[1].equals("NULL"))
					{
						newColor = new int[] {	(int)((double)Double.parseDouble(words[1]) * 2.55),
												(int)((double)Double.parseDouble(words[2]) * 2.55),
												(int)((double)Double.parseDouble(words[3]) * 2.55)};
					}
					commands.add(new StartRunner(textArea, newColor));
					System.out.println("Added START runner");
				}
				else if (words[0].equals("HOLD"))
				{
					commands.add(new HoldRunner());
					System.out.println("Added HOLD runner");
				}
				else if (words[0].equals("WAIT"))
				{
					int length = Integer.parseInt(words[1]);
					commands.add(new WaitRunner(length));
					System.out.println("Added WAIT runner");
				}
				else if (words[0].equals("FADE"))
				{
					int length = (int)Integer.decode(words[1]);
					if(length < 10) { length = 10; }
					
					Color start = textArea.getBackground();
//					int[] startColor = new int[] {start.getRed(), start.getGreen(), start.getBlue()};
					int[] endColor = new int[] {(int)((double)Double.parseDouble(words[2]) * 2.55),
												(int)((double)Double.parseDouble(words[3]) * 2.55),
												(int)((double)Double.parseDouble(words[4]) * 2.55)};
					commands.add(new FadeRunner(textArea, length, endColor));
					System.out.println("Added FADE runner");
				}
				else if (words[0].equals("JUMP"))
				{
					//Fade to the next color in a very short amount of time
				}
				else if (words[0].equals("REPEAT"))
				{
					commands.add(new RepeatRunner());
					System.out.println("Added REPEAT runner");
				}
				else if (words[0].equals("STOP"))
				{
//					textArea.setBackground(Color.WHITE);
					return null;
				}
				else
				{
					iFailed = true;
					return "Incorrect command type at line " + index;
				}
			}

//			textArea.setBackground(Color.WHITE)
			return null;
		}
		catch (Exception e) { iFailed = true; System.out.println(e); return "invalid number type"; }
	}
	
	public String execute()
	{	
//		stop();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					RunnerIFace currentRunner = null;
					int startIndex = -1, repeatIndex = -1;
					for (int curIndex = -1; curIndex < commands.size();)
					{
						if (currentRunner == null || !currentRunner.isAlive())
						{
							if(doProceed && curIndex <= repeatIndex) { curIndex = repeatIndex; doProceed = false; }
							curIndex++;
//							commands.remove(currentRunner);
							if (commands.size() > curIndex && commands.get(curIndex).getCommandType() != null)
							{
								currentRunner = commands.get(curIndex);
//								System.out.println(currentRunner.getCommandType());
								if (currentRunner.getCommandType().equals("START")) { startIndex = curIndex; }
								else if (currentRunner.getCommandType().equals("REPEAT"))
								{ 
									System.out.println(repeatIndex);
									if (curIndex > repeatIndex) {repeatIndex = curIndex; }
									curIndex = startIndex;
								}
								else if (currentRunner.getCommandType().equals("HOLD")) { while (!doProceed) { Thread.sleep(10); } }
								
								currentRunner.executeOnThread();
							} else {
								currentRunner = null;
							}
						}
						else if (doProceed && currentRunner != null)
						{
							if (repeatIndex < curIndex) { doProceed = false; }
							currentRunner.stopEarly();
						}
//						System.out.println((currentRunner == null || !currentRunner.isAlive()) ? "Dead" : "Alive");
					}
				} catch (Exception e)
				{
					System.err.println(e);
				}
				try
				{
					Thread.sleep(3000);
				} catch (InterruptedException e) {}
				finally
				{
					textArea.setBackground(Color.WHITE);
				}
			}
		});
		t.start();
		return null;
	}
	
	public void proceed()
	{
		doProceed = true;
	}
	
	public void stop()
	{
		textArea.setBackground(Color.WHITE);
		if (t.isAlive())
		{
			t.stop();
		}
	}
	
	public void pause()
	{
		t.suspend();
	}
	
	public void play()
	{
		t.resume();
	}
	
	public boolean isPaused()
	{
		return t.isInterrupted();
	}
}
