package cue.model;

import java.awt.Color;
import java.util.List;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


public class SyntaxStyleDocument extends DefaultStyledDocument
{
	private static final long serialVersionUID = 1L;
	
	public SyntaxStyleDocument()
	{
		super();
		createStyles();
	}
	
	private void createStyles()
	{

		Style testStyle = addStyle(Parser.DEFAULT, null);
		StyleConstants.setForeground(testStyle, Color.BLACK);
		
		Style numberStyle = addStyle(Parser.NUMBER, null);
		StyleConstants.setForeground(numberStyle, Color.BLUE);
		
		Style keywordStyle = addStyle(Parser.KEYWORD, null);
		StyleConstants.setForeground(keywordStyle, new Color(252, 90, 3));
		
		Style variableStyle = addStyle(Parser.VARIABLE, null);
		StyleConstants.setForeground(variableStyle, Color.YELLOW);
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		// TODO Auto-generated method stub
		super.insertString(offs, str, a);
		refreshDocument();
	}
	
	@Override
	public void remove(int offs, int len) throws BadLocationException
	{
		// TODO Auto-generated method stub
		super.remove(offs, len);
		refreshDocument();
	}
	
	private synchronized void refreshDocument() throws BadLocationException
	{
		System.out.println("EVIL");
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
