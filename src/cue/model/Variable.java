package cue.model;

public class Variable //TODO: make an abstract type, just for the fun of it?
{
	private double a;
	private double b;
	private double c;
	
	private boolean isThree;
	
	/**
	 * Initializes a variable with a single value
	 * @param a the single value held
	 */
	public Variable (double a)
	{
		this.a = a;
		this.isThree = false;
	}
	
	/**
	 * Initializes a variable with 3 values
	 * @param a the first value
	 * @param b the second value
	 * @param c the third value
	 */
	public Variable (double a, double b, double c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.isThree = true;
	}
	
	/**
	 * @return an array of either 1 or 3 doubles based on what it contains
	 */
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
	
	/**
	 * @return Whether or not this variable contains 3 values
	 */
	public boolean isThree()
	{
		return isThree;
	}

	@Override
	public String toString() {
		if (isThree)
		{
			return "[" + a + ", " + b + ", " + c + "]";
		}
		else
		{
			return "" + a;
		}
	}
}
