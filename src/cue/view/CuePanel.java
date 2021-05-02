package cue.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.*;

import cue.controller.CueController;
import cue.model.SyntaxStyleDocument;

@SuppressWarnings("serial")
public class CuePanel extends JPanel {

	private CueController controller;
	private SpringLayout layout;
	private Font font;
	
	private JScrollPane textPane;
	private JTextPane textArea;
	private JPanel buttonPane;
	private JButton stopButton;
	private JButton proceedButton;
	private JButton pauseButton;
	private JButton runButton;
	private JButton pickColor;
	
	/**
	 * Initializes and sets up the UI elements.
	 * @param controller
	 */
	public CuePanel(CueController controller)
	{
		super();
		this.controller = controller;
		this.layout = new SpringLayout();
		
		this.textPane = new JScrollPane();
		this.textArea = new JTextPane(new SyntaxStyleDocument());
		this.buttonPane = new JPanel(new GridLayout(1, 0, 8, 0));
		this.stopButton = new JButton("Stop");
		this.proceedButton = new JButton("Proceed");
		this.pauseButton = new JButton("Pause");
		this.runButton = new JButton("Run");
		this.pickColor = new JButton("Pick Color");
		
		setupPanel();
		setupListeners();
		setupLayout();
	}
	
	/**
	 * Sets up the UI elements.
	 */
	private void setupPanel()
	{
		this.setBackground(Color.DARK_GRAY);
		this.setSize(800,600);
		this.setLayout(layout);
		
		font = this.getFont();
		font = new Font(font.getFontName(), font.getStyle(), 20);
		controller.setDefaultBG(new Color(50, 50, 50));
		controller.setPanel(textArea);

		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setBackground(new Color(50, 50, 50));
		textPane.setViewportView(textArea);
		textPane.setBorder(null);
		this.add(textPane);
		
		stopButton.setFont(font);
		proceedButton.setFont(font);
		pauseButton.setFont(font);
		runButton.setFont(font);
		pickColor.setFont(font);
		
		buttonPane.setBackground(new Color (0,0,0,0));
		buttonPane.add(stopButton);
		buttonPane.add(proceedButton);
		buttonPane.add(pauseButton);
		buttonPane.add(runButton);
		buttonPane.add(pickColor);
		this.add(buttonPane);
	}
	
	/**
	 * Sets up the listeners to the buttons.
	 */
	private void setupListeners()
	{
		stopButton.addActionListener(click -> controller.stop());
		
		proceedButton.addActionListener(click -> controller.moveForward());
		
		pauseButton.addActionListener(click ->
		{
			if (controller.isPaused())
			{
				controller.play();
			}
			else
			{
				controller.pause();
			}
		});
		
		runButton.addActionListener(click ->
		{
			try
			{
				controller.initParse(textArea.getText());
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Cue Tester", JOptionPane.WARNING_MESSAGE, null);
			}
		});
		
		pickColor.addActionListener(click ->
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
		});
	}
	
	/**
	 * Sets up the relational positions of the UI elements.
	 */
	private void setupLayout()
	{
		layout.putConstraint(SpringLayout.WEST, textPane, 25, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, textPane, -25, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, textPane, 25, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, textPane, -12, SpringLayout.NORTH, buttonPane);
		
		layout.putConstraint(SpringLayout.WEST, buttonPane, 0, SpringLayout.WEST, textPane);
		layout.putConstraint(SpringLayout.EAST, buttonPane, 0, SpringLayout.EAST, textPane);
		layout.putConstraint(SpringLayout.NORTH, buttonPane, -80, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, buttonPane, -25, SpringLayout.SOUTH, this);
	}
}
