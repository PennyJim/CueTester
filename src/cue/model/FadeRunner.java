package cue.model;

import java.awt.Color;

import javax.swing.JTextArea;

public class FadeRunner extends RunnerObj {
	
	private final JTextArea panel;
	private final int time;
	private final int[] endColor;
	private Thread t;
	
	public FadeRunner(JTextArea panel, int time, int[] endColor) {
		this.panel = panel;
		this.time = time;
		this.endColor = endColor;
	}
	
	public void execute()
	{
		t = new Thread(new Runnable() {
			
			private long startMilis = System.currentTimeMillis();
			private long endMilis = startMilis + time;
			private Color start = panel.getBackground();
			private int[] startColor = new int[] {start.getRed(), start.getGreen(), start.getBlue()};
			
			@Override
			public void run() {
				
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
				panel.setBackground(new Color(endColor[0], endColor[1], endColor[2]));
				
			}
		});
		t.start();
	}
	
	public boolean isAlive()
	{
		return t.isAlive();
	}
	
}
