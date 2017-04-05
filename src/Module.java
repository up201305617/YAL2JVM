
import java.util.HashMap;

public class Module
{
	private String moduleID;
	private HashMap<String,Function> functions;
	private HashMap<String,Variable> globalVariables;

	public Module(String id, SimpleNode root)
	{
		this.moduleID = id;
		this.functions = new HashMap<String,Function>();
		this.globalVariables = new HashMap<String,Variable>();
	}
	
	public boolean addGlobalVariableToModule(Variable globalVariable) 
	{
		//Se a variavel ainda n�o est� no HashMap
		if (!this.globalVariables.containsKey(globalVariable.getVariableID()))
		{
			//Adiciona-a ao HashMap e retorna true
			this.globalVariables.put(globalVariable.getVariableID(),globalVariable);
			return true;
		}
		else
		{
			//Se j� existir retorna false;
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
			System.out.println("J� existe uma fun��o com o nome " + function.getFunctionId());
			return false;
		}
	}
	
	public void printAllModule()
	{
		System.out.println("///////////////////////////////////////");
		System.out.println(this.moduleID + " Fun��o");
		System.out.println("///////////////////////////////////////");
		
		for(String key : functions.keySet()) 
		{
			Function f = functions.get(key);
			System.out.println(f.getFunctionId());
			f.printFuntion();
	 	}
		
		System.out.println("///////////////////////////////////////");
		System.out.println(this.moduleID + " Vari�veis Globais");
		System.out.println("///////////////////////////////////////");
		
		for(String key : globalVariables.keySet())
		{
			Variable var = globalVariables.get(key);
			System.out.println(var.getVariableID());
		}
	}
}
