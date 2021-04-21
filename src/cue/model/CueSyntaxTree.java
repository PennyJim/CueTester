package cue.model;

import java.util.ArrayList;
import java.util.List;

public class CueSyntaxTree
{
	private Node<Keyword> root;
	private Node<Keyword> curBuild;
	private Node<Keyword> curRun;
	
	public CueSyntaxTree()
	{
		root = new Node<Keyword>();
		root.children = new ArrayList<Node<Keyword>>();
	}
	
	public CueSyntaxTree add(Keyword word)
	{
		Node<Keyword> newNode = new Node<Keyword>();
		if (word.getClass().equals(RepeatRunner.class))
		{
			newNode.data = word;
			newNode.parent = root;
			newNode.children = new ArrayList<Node<Keyword>>();
		}
		
		return this;
	}
	
	public CueSyntaxTree clear()
	{
		root.children.clear();
		root = new Node<Keyword>();
		
		return this;
	}
	
	public static class Node<T> {
		private T data;
		private Node<T> parent;
		private List<Node<T>> children;
	}
}
