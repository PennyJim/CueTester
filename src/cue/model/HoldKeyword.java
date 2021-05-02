package cue.model;

public class HoldKeyword extends Keyword
{

	public HoldKeyword(Parser parser, String inputs)
	{
		super(parser, inputs);
		validateString = "";
	}

	@Override
	public void step() {}

	@Override
	public boolean hasStep()
	{
		return true;
	}

}
