package cue.model;

import java.awt.Color;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;


public class SyntaxStyleDocument extends DefaultStyledDocument
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes this style document
	 */
	public SyntaxStyleDocument()
	{
		super();
		createStyles();
	}
	
	/**
	 * Initializes the styles used in this style document
	 */
	private void createStyles()
	{

		Style defaultStyle = addStyle(Parser.DEFAULT, null);
		StyleConstants.setForeground(defaultStyle, Color.WHITE);
		StyleConstants.setFontSize(defaultStyle, 20);
		
		Style numberStyle = addStyle(Parser.NUMBER, null);
		StyleConstants.setForeground(numberStyle, new Color(80, 135, 188));
		StyleConstants.setFontSize(numberStyle, 20);
		
		Style keywordStyle = addStyle(Parser.KEYWORD, null);
		StyleConstants.setForeground(keywordStyle, new Color(206, 106, 64));
		StyleConstants.setFontSize(keywordStyle, 20);
		
		Style variableStyle = addStyle(Parser.VARIABLE, null);
		StyleConstants.setForeground(variableStyle, new Color(249, 242, 114));
		StyleConstants.setFontSize(variableStyle, 20);
	}

	/** 
	 * {@inheritDoc}
	 * <br>(Adds a call to a private function: refreshDocument)
	 * @see SytaxStyleDocument#refreshDocument()
	 */
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		super.insertString(offs, str, a);
		refreshDocument();
	}
	
	/**
	 * {@inheritDoc}
	 * <br>(Adds a call to a private function: refreshDocument)
	 * @see SyntaxStyleDocument#refreshDocument()
	 */
	@Override
	public void remove(int offs, int len) throws BadLocationException
	{
		// TODO Auto-generated method stub
		super.remove(offs, len);
		refreshDocument();
	}
	
	/**
	 * A private function used to style the current document according to the predefined rules and defined styles
	 * @throws BadLocationException Should never throw exception (0, getLength())
	 */
	private synchronized void refreshDocument() throws BadLocationException
	{
		String code = getText(0, getLength());
		List<String> variables = Parser.parseVariables(code);
		
		Scanner lines = new Scanner(code);
		int cPosition = 0;
		
		setCharacterAttributes(0, code.length(), getStyle(Parser.DEFAULT), true);
		while (lines.hasNext())
		{
			String word = lines.next();
			setCharacterAttributes(cPosition, word.length(), getStyle(Parser.wordType(word, variables)), true);
			cPosition += word.length() + 1;
		} lines.close();
	}
}
