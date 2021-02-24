package cue.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import cue.controller.CueController;
import cue.model.FadeRunner;
import cue.model.RunRunner;

public class CuePanel extends JPanel {

	private CueController controller;
	private SpringLayout layout;
	private Font font;
	
	private JScrollPane textPane;
	private JTextArea textArea;
	private JButton pickColor;
	private JButton runButton;
	private RunRunner runner;
	
	public CuePanel(CueController controller)
	{
		super();
		this.controller = controller;
		this.layout = new SpringLayout();
		
		this.textPane = new JScrollPane();
		this.textArea = new JTextArea();
		this.pickColor = new JButton("Pick Color");
		this.runButton = new JButton("Run");
		this.runner = new RunRunner("", textArea);
		
		setupPanel();
		setupListeners();
		setupLayout();
	}
	
	private void setupPanel()
	{
		this.setBackground(Color.DARK_GRAY);
		this.setSize(800,600);
		this.setLayout(layout);
		
		font = this.getFont();
		font = new Font(font.getFontName(), font.getStyle(), 20);

		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setFont(font);
		textArea.setBackground(Color.WHITE);
		textPane.setViewportView(textArea);
		this.add(textPane);
		
		pickColor.setFont(font);
		this.add(pickColor);
		
		runButton.setFont(font);
		this.add(runButton);
	}
	
	private void setupListeners()
	{
		pickColor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent click)
			{
				Color color = Color.white;
			    JFrame frame = new JFrame();
			    frame.setAlwaysOnTop(true);
			    color = JColorChooser.showDialog(frame, "Pick a color", color);
			    
				int position = textArea.getCaretPosition();
				String codeFull = textArea.getText();
				String codeFHalf = codeFull.substring(0, position);
				String codeSHalf = codeFull.substring(position);
				System.out.println(codeFHalf + "|" + codeSHalf);
				
				double red = ((double)color.getRed() / 255.0) * 100;
				double green = ((double)color.getGreen() / 255.0) * 100;
				double blue = ((double)color.getBlue() / 255.0) * 100;
				
				textArea.setText(codeFHalf + String.format("%.2f", red) + " " + String.format("%.2f", green) + " " + String.format("%.2f", blue) + "\n" + codeSHalf);
			}
		});
		
		runButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent click)
			{
//				String output = run(textArea.getText().toUpperCase());
				String output = run2(textArea.getText().toUpperCase());
				if (output != null)
				{
					textArea.setBackground(Color.WHITE);
					JOptionPane.showMessageDialog(null, "Program Failed:\n" + output , "Cue Tester", JOptionPane.INFORMATION_MESSAGE, null);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Program Succeded", "Cue Tester", JOptionPane.INFORMATION_MESSAGE, null);
				}
			}
		});
	}
	
	private String run2(String code)
	{
		runner.setCode(code);
		runner.whatDoINeedToRun();
		if (runner.iFailed) return "Failure to execute code";
		return runner.execute();
	}
	
	private String run(String code)
	{
		try
		{
			if (!code.substring(0, 5).equals("START")) { return "Needs to start with \"START\""; }
			
			String[] sentences = code.split("\n");
			
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
					runner.execute();
					
//					long startMilis = System.currentTimeMillis();
//					long endMilis = startMilis + length;
//					while (System.currentTimeMillis() < endMilis)
//					{
//						double factor = (double)(System.currentTimeMillis() - startMilis) / (double)length;
//						textArea.setBackground(new Color(	startColor[0] + (int)(factor * (double)(endColor[0] - startColor[0])),
//															startColor[1] + (int)(factor * (double)(endColor[1] - startColor[1])),
//															startColor[2] + (int)(factor * (double)(endColor[2] - startColor[2]))));
//						Thread.sleep(100);
//					}
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
					return "Incorrect command type";
				}
			}

//			textArea.setBackground(Color.WHITE);
			return null;
		}
		catch (Exception e) { System.out.println(e); return "invalid number type"; }
	}
	
	private void setupLayout()
	{
		layout.putConstraint(SpringLayout.WEST, textPane, 25, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, textPane, -25, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, textPane, 25, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, textPane, -12, SpringLayout.NORTH, pickColor);

		layout.putConstraint(SpringLayout.WEST, pickColor, 0, SpringLayout.WEST, textPane);
		layout.putConstraint(SpringLayout.EAST, pickColor, 0, SpringLayout.EAST, textPane);
		layout.putConstraint(SpringLayout.NORTH, pickColor, -33, SpringLayout.NORTH, runButton);
		layout.putConstraint(SpringLayout.SOUTH, pickColor, -8, SpringLayout.NORTH, runButton);
		
		layout.putConstraint(SpringLayout.WEST, runButton, 0, SpringLayout.WEST, textPane);
		layout.putConstraint(SpringLayout.EAST, runButton, 0, SpringLayout.EAST, textPane);
		layout.putConstraint(SpringLayout.NORTH, runButton, -50, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, runButton, -25, SpringLayout.SOUTH, this);
	}
}
