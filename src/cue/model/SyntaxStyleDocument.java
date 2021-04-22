package cue.model;

import java.awt.Color;
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
		Style numberStyle = addStyle("numberStyle", null);
		StyleConstants.setForeground(numberStyle, Color.ORANGE);
		
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
		
		Scanner lines = new Scanner(code);
		int cPosition = 0;
		
		
		Style testStyle = addStyle("testStyle", null);
		StyleConstants.setForeground(testStyle, Color.MAGENTA);
		
		setCharacterAttributes(0, code.length(), testStyle, true);
		while (lines.hasNext())
		{
			String word = lines.next();
			System.out.println("GOOD:" + word + cPosition);
			System.out.println("G00D:" + getStyle("numberStyle").toString());
			System.out.println("8008:" + getCharacterElement(cPosition + 1).getAttributes().containsAttributes(testStyle));
//			setCharacterAttributes(cPosition, word.length(), styleList.get("numberStyle"), true);
			cPosition += word.length() + 1;
		} lines.close();
	}
}
