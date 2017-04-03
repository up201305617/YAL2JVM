

public class Variable 
{
	private String variableID;
	
	public Variable(String id) 
	{
		this.variableID = id;
	}

	public String getVariableID()
	{
		return this.variableID;
	}

	@Override
	public boolean equals(Object object) 
	{
		Variable variable = (Variable) object;
		if (variableID.equals(variable.getVariableID()))
			return true;
		else
			return false;
	}
	
	public String getType()
	{
		return "variable";
	}
	
	public boolean whatIs(String type)
	{
		if(type.equals("Variable"))
			return true;
		else
			return false;
	}
}
