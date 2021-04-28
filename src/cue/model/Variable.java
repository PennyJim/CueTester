package cue.model;

public class Variable
{
	private double a;
	private double b;
	private double c;
	
	private boolean isThree;
	
	public Variable (double a)
	{
		this.a = a;
		this.isThree = false;
	}
	
	public Variable (double a, double b, double c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.isThree = true;
	}
	
	public double[] getValue()
	{
		if (isThree)
		{
			return new double[] {a, b, c};
		}
		else
		{
			return new double[] {a};
		}
	}
	
	public boolean isThree()
	{
		return isThree;
	}
}
