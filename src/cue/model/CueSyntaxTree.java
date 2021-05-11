package cue.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CueSyntaxTree
{
	private Node<Keyword> root;
	private Node<Keyword> curBuild;
	private Node<Keyword> curSel;
	private boolean isExit = false;
	
	/**
	 * Initializes the tree with only a root node
	 */
	public CueSyntaxTree()
	{
		root = new Node<Keyword>();
		root.children = new ArrayList<Node<Keyword>>();
		curBuild = root;
	}
	
	/**
	 * Adds the Keyword to the tree and automatically creates a new branch if the given Keyword is a repeat
	 * @param word The Keyword to be added to the tree
	 * @return Itself, for chaining methods
	 * @throws IOException If order is incorrect
	 */
	public CueSyntaxTree add(Keyword word) throws IOException
	{
		Node<Keyword> newNode = new Node<Keyword>();
		newNode.data = word;
		
		if (word.getClass().equals(RepeatKeyword.class))
		{
			newNode.parent = root;
			newNode.children = new ArrayList<Node<Keyword>>();
			
			curBuild = newNode;
			newNode.parent.children.add(newNode);
		}
		else if (word.getClass().equals(StopRepeatKeyword.class))
		{
			try
			{
				curBuild = curBuild.parent;
			}
			catch (NullPointerException e)
			{
				throw new IOException("StopRepeat must be after a Repeat");
			}
		}
		else
		{
			newNode.parent = curBuild;
			newNode.parent.children.add(newNode);
		}
		
		return this;
	}
	
	/**
	 * Wipes the tree back down to only a root node
	 * @return Itself, for chaining commands
	 */
	public CueSyntaxTree clear()
	{
		root.children.clear();
		root = new Node<Keyword>();
		curBuild = root;
		curSel = null;
		
		return this;
	}
	
	/**
	 * Moves to the next place on the tree and returns it's Keyword
	 * @return The next Keyword
	 */
	public Keyword next()
	{
		if (curSel != null)
		{
			curSel.selIndex++;
			if (curSel.selIndex >= curSel.children.size() || (isExit && curSel.data != null && curSel.data.getClass().equals(RepeatKeyword.class)))
			{
				if (curSel.data != null && curSel.data.getClass().equals(RepeatKeyword.class) && curSel.data.hasStep() && !isExit)
				{
					curSel.selIndex = -1;
					for (Node<Keyword> node : curSel.children)
					{
						node.data.reset();
					}
					curSel.data.step();
					return next();
				}
				else if (curSel.parent == null) { return null; }
				else
				{
					if (isExit) { isExit = false; }
					curSel = curSel.parent;
					return next();
				}
			}
			else
			{
				Node<Keyword> child = curSel.children.get(curSel.selIndex);
				
				if (child.children != null) { curSel = child; }
				if (!child.data.getClass().equals(RepeatKeyword.class)) { return child.data; }
				return next();
			}
		}
		else if (root.children.size() > 0)
		{
			curSel = root;
			curSel.selIndex = 0;
			return root.children.get(0).data;
		}
		
		return null;
	}
	
	/**
	 * @return Whether or not there is a next keyword in the tree
	 */
	public boolean hasNext()
	{
		return hasNext(curSel);
	}
	private boolean hasNext(Node<Keyword> testSel)
	{
		if (testSel != null)
		{
			int index = testSel.selIndex + 1;
			if (index >= testSel.children.size() || (isExit && testSel.data != null && testSel.data.getClass().equals(RepeatKeyword.class)))
			{
				if (testSel.data != null && testSel.data.getClass().equals(RepeatKeyword.class) && testSel.data.hasStep() && !isExit) { return true; }
				if (testSel.parent == null) { return false; }
				return hasNext(testSel.parent);
			}
			else { return true; }
		}
		else if (root.children.size() > 0) { return true; }
		return false;
	}
	
	public void setExit(boolean isExit)
	{
		this.isExit = isExit;
	}
	
	@Override
	public String toString()
	{
		return toString(root, new StringBuilder(), 0, "").toString();
	}
	/**
	 * A recursive function for building the string returned in {@link #toString()}
	 * @param node The current node to add to the string
	 * @param builder The builder for the string
	 * @param recursion What level of recursion it is
	 * @param childType What to prefix the node with
	 * @return The builder for the string
	 */
	private StringBuilder toString(Node<Keyword> node, StringBuilder builder, int recursion, String childType)
	{
		for (int index = 0; index < recursion - 1; index++)
		{
			builder.append("│  ");
		}
		
		builder.append(childType);
		if (node.data != null)
		{
			builder.append(node.data.getClass().getSimpleName());
		}
		else
		{
			builder.append("Node");
		}
		builder.append('\n');
		
		recursion++;
		if(node.children != null)
		{
			for (int index = 0; index < node.children.size(); index++)
			{
				if (index == node.children.size() - 1)
				{
					toString(node.children.get(index), builder, recursion, "└──");
				}
				else
				{
					toString(node.children.get(index), builder, recursion, "├──");
				}
			}
		}
		
		return builder;
	}
	
	/**
	 * The basic node class used to make the tree structure of CueSyntaxTree
	 * @author char2259
	 *
	 * @param <T> The type of data stored in the node
	 */
	public static class Node<T> {
		private T data;
		private Node<T> parent;
		private List<Node<T>> children;
		private int selIndex = -1;
	}
}
