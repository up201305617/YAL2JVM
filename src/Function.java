import java.util.ArrayList;
import java.util.HashMap;

public class Function 
{
	private String functionId;
	private SimpleNode body;
	private Variable returnValue;
	private ArrayList<Variable> arguments;
	private ArrayList<IntermediateRepresentation> nodes;  
	private HashMap<String,Variable> variables;
	public static int cfgNodeCount;
	
	public Function(String id, Variable returnValue, ArrayList<Variable> a, SimpleNode body)
	{
		this.functionId = id;
		this.returnValue = returnValue;
		this.arguments = a;
		this.body = body;
		this.nodes = new ArrayList<IntermediateRepresentation>();
		this.variables = new HashMap<String,Variable>();
	}

	public ArrayList<IntermediateRepresentation> getNodes() 
	{
		return nodes;
	}

	public String getFunctionId() 
	{
		return functionId;
	}
	
	public boolean isLocalVariable(String id)
	{
		return variables.containsKey(id);
	}
	
	public Variable getVariableById(String id)
	{
		return variables.get(id);
	}
	
	public boolean checkArguments(String varID)
	{
		for (int i = 0; i < arguments.size(); i++)
		{	
			if (arguments.get(i).getVariableID().equals(varID))
				return true;
		}
		return false;
	}

	public ArrayList<Variable> getArguments() 
	{
		return arguments;
	}
	
	public Variable getArgumentsById(String id)
	{
		for (int i = 0; i < arguments.size(); i++)
		{
			if (arguments.get(i).getVariableID().equals(id))
			{
				return arguments.get(i);
			}
		}
		return null;
	}

	public Variable getReturnValue()
	{
		return returnValue;
	}
	
	public boolean isReturnValue(String id) 
	{
		if (returnValue != null) 
		{
			if (returnValue.getVariableID().equals(id))
				return true;
		}
		return false;
	}
}
