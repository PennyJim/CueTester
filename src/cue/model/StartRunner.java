package cue.model;

import java.awt.Color;

import javax.swing.JTextArea;

public class StartRunner implements RunnerIFace
{
	private final JTextArea panel;
	private final int[] newColor;
	private boolean isRunning = false;
	private boolean stopEarly = false;
	private Thread t;
	
	public StartRunner(JTextArea panel, int[] newColor)
	{
		this.panel = panel;
		this.newColor = newColor;
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
	
	public void executeOnThread()
	{
		if (newColor != null)
		{
			isRunning = true;
			long startMilis = System.currentTimeMillis();
			long endMilis = startMilis + 100;
			Color start = panel.getBackground();
			int[] startColor = new int[] {start.getRed(), start.getGreen(), start.getBlue()};
			while (System.currentTimeMillis() < endMilis && !stopEarly)
			{
				double factor = (double)(System.currentTimeMillis() - startMilis) / 100.0;
				panel.setBackground(new Color(	startColor[0] + (int)(factor * (double)(newColor[0] - startColor[0])),
												startColor[1] + (int)(factor * (double)(newColor[1] - startColor[1])),
												startColor[2] + (int)(factor * (double)(newColor[2] - startColor[2]))));
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!stopEarly) { panel.setBackground(new Color(newColor[0], newColor[1], newColor[2])); }
			isRunning = false;
		}
	}
	
	public void stopEarly()
	{
		stopEarly = true;
	}
	
	public boolean isAlive() { return isRunning; }
	public boolean isThreadAlive() { return t.isAlive(); }
	public String getCommandType() { return "START"; }
}