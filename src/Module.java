
import java.util.HashMap;

public class Module
{
	private String moduleID;
	private HashMap<String,Function> functions;
	private HashMap<String,Variable> globalVariables;
	private SimpleNode root;

	public Module(String id, SimpleNode root)
	{
		this.moduleID = id;
		this.functions = new HashMap<String,Function>();
		this.globalVariables = new HashMap<String,Variable>();
		this.root = root;
	}
	
	public boolean addGlobalVariableToModule(Variable globalVariable) 
	{
		//Se a variavel ainda não está no HashMap
		if (!this.globalVariables.containsKey(globalVariable.getVariableID()))
		{
			//Adiciona-a ao HashMap e retorna true
			this.globalVariables.put(globalVariable.getVariableID(),globalVariable);
			return true;
		}
		else
		{
			//Se já existir retorna false;
			return false;
		}
	}
	
	public boolean isGlobalVariable(String id) 
	{
		return globalVariables.containsKey(id);
	}
	
	public Variable getGlobalVariableById(String id)
	{
		return globalVariables.get(id);
	}

	public boolean addFunction(Function function)
	{
		String key = function.getFunctionId();
		if (!functions.containsKey(key))
		{
			functions.put(key,function);
			return true;
		} 
		else 
		{
			System.out.println("Already exist a function with the name " + function.getFunctionId());
			return false;
		}
	}
	
	public void printAllModule()
	{
		System.out.println(this.moduleID + " Functions");
		
		for(String key : functions.keySet()) 
		{
			Function f = functions.get(key);
			System.out.println(f.getFunctionId());
	 	}
		
		System.out.println(this.moduleID + " Globals");
		
		for(String key : globalVariables.keySet())
		{
			Variable var = globalVariables.get(key);
			System.out.println(var.getVariableID());
		}
	}
}
