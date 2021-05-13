package cue.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import cue.controller.CueController;
import cue.controller.IOController;
import cue.model.SyntaxStyleDocument;

@SuppressWarnings("serial")
public class CuePanel extends JPanel {

	private CueController controller;
	private JFrame frame;
	private UndoManager undoManager;
	private SpringLayout layout;
	private Font font;
	private String pathName = null;
	
	private JScrollPane textPane;
	private JTextPane textArea;
	private JPanel buttonPane;
	private JButton stopButton;
	private JButton proceedButton;
	private JButton pauseButton;
	private JButton runButton;
	private JButton pickColor;
	
	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem newFile;
	private MenuItem openFile;
	private MenuItem save;
	private MenuItem saveAs;
	private Menu editMenu;
	private MenuItem undo;
	private MenuItem redo;
	
	/**
	 * Initializes and sets up the UI elements.
	 * @param controller
	 */
	public CuePanel(CueController controller, JFrame frame)
	{
		super();
		this.controller = controller;
		this.frame = frame;
		this.undoManager = new UndoManager();
		this.layout = new SpringLayout();
		this.font = getFont();
		this.font = new Font(font.getFontName(), font.getStyle(), 20);
		
		this.textPane = new JScrollPane();
		this.textArea = new JTextPane(new SyntaxStyleDocument());
		this.buttonPane = new JPanel(new GridLayout(1, 0, 8, 0));
		this.stopButton = new JButton("Stop");
		this.proceedButton = new JButton("Proceed");
		this.pauseButton = new JButton("Pause");
		this.runButton = new JButton("Run");
		this.pickColor = new JButton("Pick Color");
		
		this.menuBar = new MenuBar();
		this.fileMenu = new Menu("File");
		this.newFile = new MenuItem("New File");
		this.openFile = new MenuItem("Open File");
		this.save = new MenuItem("Save");
		this.saveAs = new MenuItem("Save As…");
		this.editMenu = new Menu("Edit");
		this.undo = new MenuItem("Undo");
		this.redo = new MenuItem("Redo");
		
		setupPanel();
		setupMenu();
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
		
		controller.setDefaultBG(new Color(50, 50, 50));
		controller.setPanel(textArea);

		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setBackground(new Color(50, 50, 50));
		textArea.setText("Var flashColor 0.00 0.00 82.75\n" + 
					"Var holdTime 2000\n" + 
					"Var rainbowTime 1000\n" + 
					"\n" + 
					"Fade 100   100 100    1000\n" + 
					"Hold\n" + 
					"Fade flashColor 200\n" + 
					"Wait holdTime\n" + 
					"Fade 0 0 0 1000\n" + 
					"REPEAT 5\n" + 
					"	Fade 100 0 0 rainbowTime\n" + 
					"	Fade 100 100 0 rainbowTime\n" + 
					"	Fade 0 100 0 rainbowTime\n" + 
					"	Fade 0 100 100 rainbowTime\n" + 
					"	Fade 0 0 100 rainbowTime\n" + 
					"	Fade 100 0 100 rainbowTime\n" + 
					"CloseRepeat\n" + 
					"Fade 100.00 50.98 27.06 10000\n" + 
				"");
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
	 * Sets up the JMenu components
	 */
	private void setupMenu()
	{
		fileMenu.add(newFile);
		fileMenu.add(openFile);
		fileMenu.addSeparator();
		fileMenu.add(save);
		fileMenu.add(saveAs);
		menuBar.add(fileMenu);
		
		editMenu.add(undo);
		editMenu.add(redo);
		menuBar.add(editMenu);
		
		
		frame.setMenuBar(menuBar);
	}
	
	/**
	 * Sets up the listeners to the buttons.
	 */
	private void setupListeners()
	{
		textArea.getDocument().addUndoableEditListener(new UndoableEditListener()
		{
			@Override
			public void undoableEditHappened(UndoableEditEvent e)
			{
				UndoableEdit edit = e.getEdit();
				if (!edit.getPresentationName().equals("style change"))
				{
					undoManager.addEdit(edit);
					System.out.println(edit.getPresentationName());
				}
				else
				{
					edit.die();
				}
			}
		});
		
		InputMap inputMask = textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMask = textArea.getActionMap();
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		//New File Button
		newFile.setShortcut(new MenuShortcut(KeyStroke.getKeyStroke('N', toolkit.getMenuShortcutKeyMask()).getKeyCode()));
		//New File Action
		newFile.addActionListener(click -> newFile());
		
		//Open File Button
		openFile.setShortcut(new MenuShortcut(KeyStroke.getKeyStroke('O', toolkit.getMenuShortcutKeyMask()).getKeyCode()));
		//Open File Action
		openFile.addActionListener(click -> openFile());
		
		//Save Button
		save.setShortcut(new MenuShortcut(KeyStroke.getKeyStroke('S', toolkit.getMenuShortcutKeyMask()).getKeyCode()));
		//Save Action
		save.addActionListener(click -> saveFile());
		
		//Save As Button
		saveAs.setShortcut(new MenuShortcut(KeyStroke.getKeyStroke('S', toolkit.getMenuShortcutKeyMask()).getKeyCode(), true));
		//Save As Action
		saveAs.addActionListener(click -> saveFileAs());
		
		//Undo Button
		undo.setShortcut(new MenuShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_Z, toolkit.getMenuShortcutKeyMask()).getKeyCode())); //inputMask.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, toolkit.getMenuShortcutKeyMask()), "Undo");
		//Undo Action
		AbstractAction undoAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent ev)
			{
				try
				{
					if(undoManager.canUndo())
					{
						undoManager.undo();
						style.refreshStyle();
					}
				}
				catch (CannotUndoException | BadLocationException er)
				{
					er.printStackTrace();
				}
			}
		};
		undo.addActionListener(undoAction);
		actionMask.put("Undo", undoAction);
		
		//Redo Buttons
		inputMask.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, toolkit.getMenuShortcutKeyMask()), "Redo");
		redo.setShortcut(new MenuShortcut(KeyStroke.getKeyStroke('Z', toolkit.getMenuShortcutKeyMask()).getKeyCode(), true)); //inputMask.put(KeyStroke.getKeyStroke('Z', Event.SHIFT_MASK + toolkit.getMenuShortcutKeyMask()), "Redo");
		//Undo Action
		AbstractAction redoAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					if(undoManager.canRedo())
					{
						undoManager.redo();
					}
				}
				catch (CannotRedoException er) {}
			}
		};
		redo.addActionListener(redoAction);
		actionMask.put("Redo", redoAction);
		
		stopButton.addActionListener(click -> controller.stop());
		proceedButton.addActionListener(click -> controller.moveForward());
		pauseButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (controller.isPaused())
				{
					controller.play();
					pauseButton.setText("Pause");
				}
				else
				{
					controller.pause();
					pauseButton.setText("Play");
				}
			}
		});
		runButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ev)
			{
				try
				{
					controller.parse(textArea.getText());
				}
				catch (IOException er)
				{
					controller.handleErrors(er);
				}
			}
		});
		pickColor.addActionListener(click -> pickColor());
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
	
	/**
	 * Method called by the pickColor button that places the<br>
	 * rgb value of the color picked in 0-100 percentages<br>
	 * at the location of the cursor in the text pane
	 */
	private void pickColor()
	{
		Color color = Color.white;
	    JFrame frame = new JFrame();
	    frame.setAlwaysOnTop(true);
	    color = JColorChooser.showDialog(frame, "Pick a color", color);
	    
	    if (color != null)
	    {
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
	}

	private void saveFile()
	{
		if (pathName != null)
		{
			IOController.save(controller, pathName, textArea.getText());
		}
		else { saveFileAs(); }
		
	}
	
	private void saveFileAs()
	{
		JFileChooser fileChooser = new JFileChooser();
//		fileChooser.setFileFilter(new FileFilter()
//		{
//			
//			@Override
//			public String getDescription()
//			{
//				return "Basic Light Cue Language (*.blcl)";
//			}
//			
//			@Override
//			public boolean accept(File f)
//			{
//				if (f.isDirectory()) { return true; }
//				boolean temp = f.getName().trim().toLowerCase().endsWith(".blcl");
//				System.out.println(f.getName() + " : " + temp);
//				return temp;
//			}
//		});
		fileChooser.setFileFilter(new FileNameExtensionFilter("test", "blcl"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		int option = fileChooser.showSaveDialog(null);
		if (option == JFileChooser.APPROVE_OPTION)
		{
			pathName = fileChooser.getSelectedFile().getPath();
			if (!pathName.endsWith(".blcl")) { pathName += ".blcl"; }
			saveFile();
		}
		else if (option == JFileChooser.ERROR_OPTION)
		{
			controller.handleErrors(new Exception("File Chooser returned \"ERROR_OPTION\""));
		}
	}

	private void newFile()
	{
	}
	
	private void openFile()
	{
	}
}
