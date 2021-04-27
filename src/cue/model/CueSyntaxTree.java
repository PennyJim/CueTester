package cue.model;

import java.util.ArrayList;
import java.util.List;

public class CueSyntaxTree
{
	private Node<Keyword> root;
	private Node<Keyword> curBuild;
	private Node<Keyword> curSel;
	
	public CueSyntaxTree()
	{
		root = new Node<Keyword>();
		root.children = new ArrayList<Node<Keyword>>();
		curBuild = root;
	}
	
	public CueSyntaxTree add(Keyword word)
	{
		Node<Keyword> newNode = new Node<Keyword>();
		newNode.data = word;
		
		if (word.getClass().equals(RepeatRunner.class))
		{
			newNode.parent = root;
			newNode.children = new ArrayList<Node<Keyword>>();
			
			curBuild = newNode;
		}
		else
		{
			if (word.getClass().equals(RepeatRunner.class))
			{
				curBuild = root;
			}
			
			newNode.parent = curBuild;
		}
		newNode.parent.children.add(newNode);
		
		return this;
	}
	
	public Keyword next()
	{
		if (curSel != null)
		{
			curSel.selIndex++;
			if (curSel.selIndex >= curSel.children.size())
			{
				if (curSel.parent == null) { return null; }
				curSel = curSel.parent;
				return next();
			}
			else
			{
				Node<Keyword> child = curSel.children.get(curSel.selIndex);
				
				if (child.children != null) { curSel = child; }
				
				return child.data;
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
	
	public boolean hasNext()
	{
		if (curSel != null)
		{
			int index = curSel.selIndex + 1;
			if (index >= curSel.children.size())
			{
				if (curSel.parent == null) { return false; }
				return hasNext(curSel.parent);
			}
			else
			{ return true; }
		}
		else if (root.children.size() > 0)
		{ return true; }
		
		return false;
	}
	
	private boolean hasNext(Node<Keyword> testSel)
	{
		if (testSel != null)
		{
			int index = testSel.selIndex + 1;
			if (index >= testSel.children.size())
			{
				if (testSel.parent == null) { return false; }
				return hasNext(testSel.parent);
			}
			else { return true; }
		}
		return false;
	}
	
	public CueSyntaxTree clear()
	{
		root.children.clear();
		root = new Node<Keyword>();
		curBuild = root;
		curSel = null;
		
		return this;
	}
	
	public static class Node<T> {
		private T data;
		private Node<T> parent;
		private List<Node<T>> children;
		private int selIndex = -1;
	}
}
