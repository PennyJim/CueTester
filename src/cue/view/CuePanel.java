package cue.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cue.controller.CueController;

public class CuePanel extends JPanel {

	private CueController controller;
	private SpringLayout layout;
	private Font font;
	
	private JScrollPane textPane;
	private JTextArea textArea;
	private JButton pickColor;
	private JButton runButton;
	
	public CuePanel(CueController controller)
	{
		super();
		this.controller = controller;
		this.layout = new SpringLayout();
		
		this.textPane = new JScrollPane();
		this.textArea = new JTextArea();
		this.pickColor = new JButton("Pick Color");
		this.runButton = new JButton("Run");
		
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
