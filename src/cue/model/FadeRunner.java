package cue.model;

import java.awt.Color;

import javax.swing.JTextArea;

public class FadeRunner extends RunnerObj {
	
	private final JTextArea panel;
	private final int time;
	private final int[] endColor;
	private boolean isRunning = false;
	private Thread t;
	
	public FadeRunner(JTextArea panel, int time, int[] endColor) {
		this.panel = panel;
		this.time = time;
		this.endColor = endColor;
	}
	
	public void execute()
	{
		t = new Thread(new Runnable() {
			
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
		long startMilis = System.currentTimeMillis();
		long endMilis = startMilis + time;
		Color start = panel.getBackground();
		int[] startColor = new int[] {start.getRed(), start.getGreen(), start.getBlue()};
		isRunning = true;
		while (System.currentTimeMillis() < endMilis)
		{
			double factor = (double)(System.currentTimeMillis() - startMilis) / (double)time;
			panel.setBackground(new Color(	startColor[0] + (int)(factor * (double)(endColor[0] - startColor[0])),
											startColor[1] + (int)(factor * (double)(endColor[1] - startColor[1])),
											startColor[2] + (int)(factor * (double)(endColor[2] - startColor[2]))));
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		isRunning = false;
		panel.setBackground(new Color(endColor[0], endColor[1], endColor[2]));
	}
	
	public boolean isAlive()
	{
		return isRunning;
	}
	
	public boolean isThreadAlive()
	{
		return t.isAlive();
	}
	
}
