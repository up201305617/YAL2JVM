import java.util.ArrayList;
import java.util.HashMap;

public class Function 
{
	private String functionId;
	private SimpleNode body;
	private Variable returnValue;
	private ArrayList<Variable> arguments;
	private ArrayList<AST> nodes;  
	private HashMap<String,Variable> variables;
	public static int cfgNodeCount;
	private AST initialNode;
	private HashMap<String,Integer> allVariables;
	
	public Function(String id, Variable returnValue, ArrayList<Variable> a, SimpleNode body)
	{
		this.functionId = id;
		this.returnValue = returnValue;
		this.arguments = a;
		this.body = body;
		this.nodes = new ArrayList<AST>();
		this.variables = new HashMap<String,Variable>();
		this.allVariables = new HashMap<String,Integer>();
	}
	
	public void initializeInitialNode(Function f)
	{
		this.initialNode = new AST("start",f);
	}
	
	public SimpleNode getBody()
	{
		return this.body;
	}

	public void setBody(SimpleNode body) 
	{
		this.body = body;
	}

	public AST getInitialNode()
	{
		return this.initialNode;
	}

	public ArrayList<AST> getNodes() 
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
		
		if(this.returnValue!=null)
		{
			System.out.println("Return Value");
			System.out.println(this.returnValue.getVariableID()+" - "+this.returnValue.getType());
		}
		else
		{
			System.out.println("Não tem valor de retorno." );
		}
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
	
	public void buildVariablesIndex()
	{
		int index = 0;
		
		for (int i = 0; i < arguments.size(); i++)
		{
			index++;
			allVariables.put(arguments.get(i).getVariableID(),index);
		}
		
		if(returnValue != null)
		{
			index++;
			allVariables.put(returnValue.getVariableID(),index);
		}
		
		for (String key : variables.keySet())
		{
			index++;
			allVariables.put(variables.get(key).getVariableID(),index);
		}
	}
}
