package cue.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class IOController
{
	/**
	 * Saves a string to a file on disk
	 * @param app used to handle errors
	 * @param filePath where to save
	 * @param contents what to save
	 */
	public static void save(CueController app, String filePath, String contents)
	{
		String filename = filePath;
		File saveFile = new File(filename);
		
		try (Scanner textScanner = new Scanner(contents); PrintWriter saveText = new PrintWriter(saveFile))
		{
			if (!saveFile.isFile()) { throw new IOException("Given file path is not a file"); }
			
			while(textScanner.hasNext())
			{
				String currentLine = textScanner.nextLine();
				saveText.println(currentLine);
			}
		}
		catch(IOException e)
		{
			app.handleErrors(e);
		}
		catch(Exception e)
		{
			app.handleErrors(e);
		}
	}
	
	/**
	 * A function that returns the text of a file on disk
	 * @param app used to handle errors
	 * @param path location of the file
	 * @return text of the file
	 */
	public static String loadFile(CueController app, String path)
	{
		String results = "";
		
		File source = new File(path);
		
		try (Scanner sourceScanner = new Scanner(source))
		{
			while (sourceScanner.hasNext())
			{
				results += sourceScanner.nextLine() + "\n";
			}
		}
		catch (IOException fileError)
		{
			app.handleErrors(fileError);
		}
		catch (Exception randomError)
		{
			app.handleErrors(randomError);
		}
		
		return results;
	}
}
