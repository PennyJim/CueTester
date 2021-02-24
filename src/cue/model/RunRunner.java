package cue.model;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JTextArea;

//TODO: Implement no run while run, chnge popup, accomodate for repeat

public class RunRunner {
	
	private String code;
	private final JTextArea textArea;
	private ArrayList<RunnerObj> commands;
	public boolean iFailed = false;
	private Thread t;
	
	public RunRunner(String code, JTextArea textArea) {
		this.code = code;
		this.textArea = textArea;
		commands = new ArrayList<>();
//		whatDoINeedToRun();
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public String whatDoINeedToRun()
	{
		try
		{
			if (!code.substring(0, 5).equals("START")) { return "Needs to start with \"START\""; }
			
			String[] sentences = code.split("\n");
			commands.clear();
			int startIndex = 0;
			int loopIndex = -1;
			for (int index = 0; index < sentences.length; index++)
			{
				String[] words = sentences[index].split(" ");
				if (words[0].equals("START"))
				{
					startIndex = index;
					if (!words[1].equals("NULL"))
					{
						textArea.setBackground(new Color(	(int)((double)Double.parseDouble(words[1]) * 2.55),
															(int)((double)Double.parseDouble(words[2]) * 2.55),
															(int)((double)Double.parseDouble(words[3]) * 2.55)));
					}
				}
				else if (words[0].equals("HOLD"))
				{
					//Wait until button press?
				}
				else if (words[0].equals("WAIT"))
				{
					//Wait x milliseconds
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
					FadeRunner runner = new FadeRunner(textArea, length, endColor);
					commands.add(runner);
					System.out.println("Added fade runner");
				}
				else if (words[0].equals("JUMP"))
				{
					//Fade to the next color in a very short amount of time
				}
				else if (words[0].equals("STOP"))
				{
//					textArea.setBackground(Color.WHITE);
					return null;
				}
				else
				{
					iFailed = true;
					return "Incorrect command type";
				}
			}

//			textArea.setBackground(Color.WHITE)
			return null;
		}
		catch (Exception e) { iFailed = true; System.out.println(e); return "invalid number type"; }
	}
	
	public String execute()
	{	
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					RunnerObj currentRunner = null;
					int startIndex = -1, repeatIndex = -1;
					for (int curIndex = -1; curIndex < commands.size();)
					{
						if (currentRunner == null || !currentRunner.isAlive())
						{
							curIndex++;
//							commands.remove(currentRunner);
							if (commands.size() > curIndex && commands.get(curIndex).getCommandType() != null)
							{
								currentRunner = commands.get(curIndex);
								System.out.println(currentRunner.getCommandType());
								if (currentRunner.getCommandType().equals("START")) { startIndex = curIndex; }
								else if (currentRunner.getCommandType().equals("REPEAT"))
								{ 
									repeatIndex = curIndex;
									curIndex = startIndex;
								}
								currentRunner.executeOnThread();
							} else {
								currentRunner = null;
							}
						}
					}
				} catch (Exception e)
				{
					System.err.println(e);
				}
			}
		});
		t.start();
		return null;
		
		
	}
	
}
