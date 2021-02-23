package cue.view;

import javax.swing.*;

import cue.controller.CueController;

public class CuePanel extends JPanel {

	private CueController controller;
	private SpringLayout layout;
	
	private JTextField textField;
	private JButton runButton;
	private JButton pickColor;
	
	public CuePanel(CueController controller)
	{
		super();
		this.controller = controller;
		this.layout = new SpringLayout();
		
		this.textField = new JTextField();
		this.runButton = new JButton("Run");
		this.pickColor = new JButton("Pick Color");
		
		setupPanel();
		setupListeners();
		setupLayout();
	}
	
	private void setupPanel()
	{
		this.add(textField);
		this.add(runButton);
		this.add(pickColor);
	}
	
	private void setupListeners()
	{
		
	}
	
	private void setupLayout()
	{
		layout.putConstraint(SpringLayout.WEST, textField, 50, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, textField, -50, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, textField, 50, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, pickColor, 0, SpringLayout.EAST, textField);
		layout.putConstraint(SpringLayout.EAST, pickColor, 0, SpringLayout.EAST, textField);
		layout.putConstraint(SpringLayout.NORTH, pickColor, 20, SpringLayout.SOUTH, textField);
		
		layout.putConstraint(SpringLayout.WEST, runButton, 0, SpringLayout.WEST, textField);
		layout.putConstraint(SpringLayout.EAST, runButton, 0, SpringLayout.EAST, textField);
		layout.putConstraint(SpringLayout.NORTH, runButton, 20, SpringLayout.SOUTH, pickColor);
		layout.putConstraint(SpringLayout.SOUTH, runButton, -50, SpringLayout.SOUTH, this);
	}
}
