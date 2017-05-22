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
	private int nodeCount;
	private AST initialNode;
	private HashMap<String,Integer> allVariables;
	private int varNum;
	
	public Function(String id, Variable returnValue, ArrayList<Variable> a, SimpleNode body)
	{
		this.functionId = id;
		this.returnValue = returnValue;
		this.arguments = a;
		this.body = body;
		this.nodeCount = 0;
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
		int index = -1;
		
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
	
	public void setVarNum()
	{
		int temp_num=0;
		temp_num = this.arguments.size() + this.variables.size();
		
		if(this.returnValue != null)
		{
			temp_num++;
		}
		
		this.varNum = temp_num;
	}
	
	public int getVarNum()
	{
		return this.varNum;
	}
	
	public Variable returnVarById(String id)
	{
		Variable var;
		
		if (this.returnValue != null && this.isReturnValue(id)) 
		{
			var = this.returnValue;
		} 
		else if (this.checkArguments(id)) 
		{
			var = this.getArgumentsById(id);
		}
		else if (this.isLocalVariable(id))
		{
			var = this.getVariableById(id);
		} 
		else if (YAL2JVM.getModule().isGlobalVariable(id))
		{
			var = YAL2JVM.getModule().getGlobalVariableById(id);
		}
		else 
		{
			var = null;
		}
		
		return var;
	}
	
	public boolean findVariable(String variable)
	{
		if (this.returnValue != null && this.isReturnValue(variable)) 
		{
			return true;
		} 
		else if (this.checkArguments(variable)) 
		{
			return true;
		} 
		else if (this.isLocalVariable(variable)) 
		{
			return true;
		}
		else if (YAL2JVM.getModule().isGlobalVariable(variable)) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void incNodeCount()
	{
		this.nodeCount++;
	}
	
	public int getNodeCount()
	{
		return this.nodeCount;
	}

	public HashMap<String, Integer> getAllVariables()
	{
		return allVariables;
	}
	
	public void addVariable(String access, String id, String size)
	{
		if(access.equals(Constants.ARRAY_SIZE))
		{
			this.variables.put(id, new Array(id,Integer.parseInt(size)));
		}
		else
		{
			this.variables.put(id, new Scalar(id));
		}
	}
	
	public String getScopes(String id) 
	{
		if (this.returnValue != null && this.isReturnValue(id)) 
		{
			return Constants.RETURN;
		}
		else if (this.checkArguments(id))
		{
			return Constants.ARGUMENT;
		}
		else if (this.isLocalVariable(id))
		{
			return Constants.LOCAL;
		} 
		else if (YAL2JVM.getModule().isGlobalVariable(id)) 
		{
			return Constants.GLOBAL;
		} 
		else 
		{
			return Constants.NEW;
		}
	}
}
