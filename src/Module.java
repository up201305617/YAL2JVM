
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
	
	public String getModuleID() 
	{
		return moduleID;
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
		String declaration = function.getFunctionDeclaration();
		
		if (!functions.containsKey(declaration))
		{
			functions.put(declaration,function);
			return true;
		} 
		else 
		{
			YAL2JVM.incErrors();
			YAL2JVM.errorFound();
			System.out.println("Já existe uma função com o nome " + declaration);
			return false;
		}
	}
	
	public boolean functionExists(String functionID)
	{
		return functions.containsKey(functionID);
	}
	
	public HashMap<String, Variable> getGlobalVariables()
	{
		return globalVariables;
	}

	public Function getFunctionByID(String functionID) 
	{
		return functions.get(functionID);
	}
	
	public void analyseFunctions()
	{
		for (String id : functions.keySet()) 
		{
			Function f = functions.get(id);
			f.initializeInitialNode(f);
			AST last = functions.get(id).getBody().analyseFunction(f,f.getInitialNode());
			AST end = new AST("end",f);
			last.children.add(end);
			end.parents.add(last);
			f.buildVariablesIndex();
		}
	}
	
	public void printAllModule()
	{
		System.out.println("///////////////////////////////////////");
		System.out.println(this.moduleID + " Função");
		System.out.println("///////////////////////////////////////");
		
		for(String key : functions.keySet()) 
		{
			Function f = functions.get(key);
			System.out.println(f.getFunctionId());
			f.printFuntion();
	 	}
		
		System.out.println("///////////////////////////////////////");
		System.out.println(this.moduleID + " Variáveis Globais");
		System.out.println("///////////////////////////////////////");
		
		for(String key : globalVariables.keySet())
		{
			Variable var = globalVariables.get(key);
			System.out.println(var.getVariableID());
		}
	}

	public HashMap<String, Function> getAllFunctions() 
	{
		return functions;
	}
	
	public boolean findFunctioByName(String name)
	{
		for (String key : functions.keySet())
		{
			if(Utils.splitByArgumets(key).equals(name))
			{
				return true;
			}
		}
		return false;
	}
}
