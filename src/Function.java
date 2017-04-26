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
	private IntermediateRepresentation initialNode;
	
	public Function(String id, Variable returnValue, ArrayList<Variable> a, SimpleNode body)
	{
		this.functionId = id;
		this.returnValue = returnValue;
		this.arguments = a;
		this.body = body;
		this.nodes = new ArrayList<IntermediateRepresentation>();
		this.variables = new HashMap<String,Variable>();
	}
	
	public void initializeInitialNode(Function f)
	{
		this.initialNode = new IntermediateRepresentation("start",f);
	}
	
	public SimpleNode getBody()
	{
		return this.body;
	}

	public void setBody(SimpleNode body) 
	{
		this.body = body;
	}

	public IntermediateRepresentation getInitialNode()
	{
		return this.initialNode;
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
	
	public void printFuntion()
	{
		System.out.println("Arguments");
		
		for(int i=0; i<this.arguments.size();i++)
		{
			System.out.println(this.arguments.get(i).getVariableID()+" - "+this.arguments.get(i).getType());
		}
		
		System.out.println("Return Value");
		System.out.println(this.returnValue.getVariableID()+" - "+this.returnValue.getType());
	}
	
	public String getFunctionDeclaration()
	{
		String s = functionId;
		
		s += "(";
		
		for (int i = 0; i < arguments.size(); i++)
		{
			if(arguments.get(i) instanceof Scalar)
				s += " scalar";
			else if(arguments.get(i) instanceof Array)
				s += " array";
		}
		
		s += ")";
		
		return s;
	}
}
