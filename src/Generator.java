import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map.Entry;

public class Generator 
{
	private Module module;
	private PrintWriter write;
	private File folder;
	private File file;
	private String moduleName;
	private String newFileName;
	private AST endIfNode;
	
	public Generator(Module m, String name)
	{
		this.module = m;
		this.moduleName = name;
		this.newFileName = name+".j";
	}
	
	public AST getEndIfNode() 
	{
		return endIfNode;
	}

	public void initiateGeneration()
	{
		try 
		{
			this.folder = new File("./j");
			this.folder.mkdirs();
			this.file = new File(folder, this.newFileName);
			this.write = new PrintWriter(file,"UTF-8");
			generate();
			this.write.close();
		} 
		catch (FileNotFoundException | UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void generate()
	{
		generateHeader();
		generateNewLine();
		generateGlobalVariables();
		generateNewLine();
		generateFunctions();
	}
	
	public void generateNewLine()
	{
		this.write.print("\n");
	}
	
	public void generateHeader()
	{
		this.write.println(".class public " + this.moduleName);
		this.write.println(".super java/lang/Object");
	}
	
	@SuppressWarnings("unused")
	public void generateGlobalVariables()
	{
		for(Entry<String,Variable> entry : this.module.getGlobalVariables().entrySet())
		{
			String name = null;
			String type = null;
			String value = null;
			
			if(entry.getValue() instanceof Scalar)
			{
				name = entry.getKey();
				type = Constants.JVM_SCALAR;
				Scalar newScalar = (Scalar) entry.getValue();
				
				if(newScalar.isAssign())
				{
					value = newScalar.getValue()+"";
				}
			}
			
			if(entry.getValue() instanceof Array)
			{
				name = entry.getKey();
				type = Constants.JVM_ARRAY;
				Array newArray = (Array) entry.getValue();
			}
			
			if(value != null && !type.equals(Constants.JVM_ARRAY))
			{
				this.write.println(".field static "+name+" "+type+" = "+value);
			}
			else
			{
				this.write.println(".field static "+name+" "+type);
			}
		}
	}
	
	public void generateFunctionDeclaration(Function f)
	{
		ArrayList<Variable> arguments = f.getArguments();
		Variable returnValue = f.getReturnValue();
		f.setVarNum();
		int num = f.getVarNum();
		
		if(f.getFunctionId().equals("main"))
		{
			num++;
			this.write.println(".method public static main([Ljava/lang/String;)V");
			this.write.println(".limit locals " + num);
			this.write.println(".limit stack 5");
		}
		else
		{
			this.write.print(".method public static " + f.getFunctionId() + "(");
			
			for (int i = 0; i < arguments.size(); i++)
			{
				if(arguments.get(i).getType().equals(Constants.SCALAR))
				{
					this.write.print("I");
				}
				if(arguments.get(i).getType().equals(Constants.ARRAY)) 
				{
					this.write.print("[I");
				}
			}
			
			this.write.print(")");
			
			if(returnValue != null)
			{
				if(returnValue.getType().equals(Constants.SCALAR))
				{
					this.write.println("I");
				}
				if(returnValue.getType().equals(Constants.ARRAY))
				{
					this.write.println("[I");
				}
			}
			else
			{
				this.write.println("V");
			}
			
			this.write.println(".limit locals " + num);
			this.write.println(".limit stack 2");
			this.write.println();
		}
	}
	
	public void generateReturn(Function f)
	{
		if(f.getReturnValue() != null)
		{
			Variable retVar = f.getReturnValue();
			int var_index = f.getAllVariables().get(retVar.getVariableID());
			
			if(retVar.getType() == "Scalar")
			{
				if(var_index <= 3)
				{
					this.write.println("iload_" + var_index);
				}
				else
				{
					this.write.println("iload " + var_index);
				}
				
				this.write.println("ireturn");
			}
			
			if(retVar.getType() == "Array")
			{
				if(var_index <= 3)
				{
					this.write.println("aload_" + var_index);
				}
				else
				{
					this.write.println("aload " + var_index);
				}
				
				this.write.println("areturn");
			}
		}
		else
		{
			this.write.println("return");
		}
		
		this.write.println(".end method");
	}
	
	public void generateConstructor()
	{
		this.write.println(".method static public <clinit>()V ");
		
		if(this.module.getGlobalVariables().size() != 0)
		{
			
		}
		else
		{
			this.write.println(".limit stack 0");
			this.write.println(".limit locals 0");
		}
		
		this.write.println("return");
		this.write.println(".end method");
	}
	
	public void pushIntToStack(String n)
	{
		int value = Integer.parseInt(n);
		
		if(value == -1)
		{
			this.write.println("iconst_m1");
		}
		else if(value <= 5 && value >= 0)
		{
			this.write.println("iconst_" + value);
		}
		else
		{
			this.write.println("ldc "+value);
		}
	}
	
	public void loadScalarFromStack(String varId, int varNum, String scope)
	{
		if(scope.equals(Constants.GLOBAL))
		{
			this.write.println("getstatic " + moduleName + "/" + varId + " I");
		}
		else
		{
			if(varNum <= 3)
			{
				this.write.println("iload_" + varNum);
			}
			else
			{
				this.write.println("iload " + varNum);
			}
		}
	}
	
	public void loadArrayFromStack(String varId, int varNum, String scope)
	{
		if(scope.equals("global"))
		{
			this.write.println("getstatic " + moduleName + "/" + varId + " [I");
		}
		else
		{
			if(varNum <= 3)
			{
				this.write.println("aload_" + varNum);
			}
			else
			{
				this.write.println("aload " + varNum);
			}
		}
	}
	
	public void storeScalarToStack(String id, int varNum, String scope)
	{
		if(scope.equals("global"))
		{
			this.write.println("putstatic " + moduleName + "/" + id + " I");
		}
		else
		{
			if(varNum <= 3)
			{
				this.write.println("istore_" + varNum);
			}
			else
			{
				this.write.println("istore " + varNum);
			}
		}
	}
	
	public void storeArrayToStack(String id, int varNum, String scope)
	{
		if(scope.equals("global"))
		{
			this.write.println("putstatic " + moduleName + "/" + id + " [I");
		}
		else
		{
			if(varNum <= 3)
			{
				this.write.println("astore_" + varNum);
			}
			else
			{
				this.write.println("astore " + varNum);
			}
		}
	}
	
	public void makeOperation(String op)
	{
		switch (op) 
		{
		case "+":
			this.write.println("iadd");
			break;
		case "-":
			this.write.println("isub");
			break;
		case "*":
			this.write.println("imul");
			break;
		case "/":
			this.write.println("idiv");
			break;
		}
	}
	
	public String buildFunctionInvocationOtherModule(Function f, String name, ArrayList<String> args)
	{
		Variable var;
		
		String invocation = "";
		
		invocation += name;
		invocation += "(";
		
		for(int i = 0; i<args.size();i++)
		{
			try
			{
				Integer.parseInt(args.get(i));
				invocation += "I";
			}
			catch(NumberFormatException e)
			{
				var = f.returnVarById(args.get(i));
				
				if(var.getType().equals(Constants.SCALAR))
				{
					invocation += "I";
				}
				else if(var.getType().equals(Constants.ARRAY))
				{
					invocation += "[I";
				}
			}
		}
		
		invocation += ")";
		invocation += "I";
		
		return invocation;
	}
	
	public String buildFunctionInvocationSameModule(Function f, String name, ArrayList<Variable> args)
	{
		String invocation = "";
		
		invocation += name;
		invocation += "(";
		
		for (int i = 0; i < args.size(); i++) 
		{
			if(f.getArguments().get(i).getType().equals(Constants.SCALAR))
			{
				invocation += "I";
			}
			else if(f.getArguments().get(i).getType().equals(Constants.ARRAY))
			{
				invocation += "[I";
			}
		}
		
		invocation += ")";
		
		Variable ret = f.getReturnValue();
		
		if(ret == null)
		{
			invocation += "V";
		}
		else
		{
			if(ret.getType().equals(Constants.SCALAR))
			{
				invocation += "I";
			}
			else if(ret.getType().equals(Constants.ARRAY))
			{
				invocation += "[I";
			}
		}
		
		return invocation;
	}
	
	public void generateCall(Function f, AST ast)
	{
		for (int i = 0; i < ast.call.args.length; i++) 
		{
			try
			{
				Integer.parseInt(ast.call.args[i]);
				pushIntToStack(ast.call.args[i]);
			}
			catch(NumberFormatException e)
			{
				Variable var;
				String scope;
				int varNum;
				System.out.println(ast.call.args[i]);
				System.out.println(ast.call.functionName);
				
				if(!ast.call.functionName.equals("io.println"))
				{	
					var = f.returnVarById(ast.call.args[i]);
					scope = f.getScopes(ast.call.args[i]);
					varNum = f.getAllVariables().get(var.getVariableID());
					
					if(var.getType().equals(Constants.SCALAR))
					{
						loadScalarFromStack(ast.call.args[i], varNum, scope);
					}
					else if(var.getType().equals(Constants.ARRAY))
					{
						loadArrayFromStack(ast.call.args[i], varNum, scope);
					}
				}
				else
				{
					var = f.returnVarById(ast.call.args[i]);
					
					if(var!=null)
					{
						if(var.getType().equals(Constants.SCALAR))
						{
							scope = f.getScopes(ast.call.args[i]);
							varNum = f.getAllVariables().get(var.getVariableID());
							loadScalarFromStack(ast.call.args[i], varNum, scope);
						}
						else if(var.getType().equals(Constants.ARRAY))
						{
							scope = f.getScopes(ast.call.args[i]);
							varNum = f.getAllVariables().get(var.getVariableID());
							loadArrayFromStack(ast.call.args[i], varNum, scope);
						}
					}
					else
					{
						this.write.println("ldc "+ast.call.args[i]);
					}
				}
			}
		}
		
		Function call = null;
		
		if(ast.call.other_module)
		{
			this.write.print("invokestatic " + Utils.splitByDotModule(ast.call.functionName)+ "/"+
					Utils.splitByDotFunction(ast.call.functionName));
		}
		else
		{
			this.write.print("invokestatic " + moduleName + "/" + ast.call.functionName);
			call = YAL2JVM.getModule().getFunctionByID(ast.call.functionDeclaration);
		}
		
		this.write.print("(");
		
		if(call!=null)
		{
			for (int i = 0; i < call.getArguments().size(); i++) 
			{
				Variable var = call.getArguments().get(i);
				
				if(var.getType().equals(Constants.SCALAR))
				{
					this.write.print("I");
				}
				else if(var.getType().equals(Constants.ARRAY))
				{
					this.write.print("[I");
				}
			}
			
			Variable returnVar = call.getReturnValue();
			
			if(returnVar == null)
			{
				this.write.println("V");
			}
			else
			{
				if(returnVar.getType().equals(Constants.SCALAR))
				{
					this.write.print("I");
				}
				else if(returnVar.getType().equals(Constants.ARRAY))
				{
					this.write.print("[I");
				}
			}
			
			this.write.print(")");	
		}
		else if(ast.call.functionName.equals("io.println"))
		{
			for(int i=0; i<ast.call.args.length;i++)
			{
				Variable var = f.returnVarById(ast.call.args[i]);
				
				if(var!=null)
				{
					if(var.getType().equals(Constants.SCALAR))
					{
						this.write.print("I");
					}
					else if(var.getType().equals(Constants.ARRAY))
					{
						this.write.print("[I");
					}
				}
				else
				{
					this.write.print(Constants.STRING);
				}
				
			}
			
			this.write.print(")");	
			this.write.print("V");
		}
		
		this.write.println("");
	}
	
	public void generateBody(Function f, AST ast)
	{
		switch(ast.type)
		{
		case "call":
			//System.out.println("Call");
			generateCall(f,ast);
			break;
		case "assignment":
			//System.out.println("Assignment");
			generateAssignment(f, ast);
			break;
		default:
			break;
		}
		nextNode(f,ast);
	}
	
	public void nextNode(Function f, AST ast)
	{
		ast.visited = true;
		
		for (int i = 0; i < ast.children.size(); i++)
		{
			if(!ast.children.get(i).visited)
			{
				if(!ast.children.get(i).type.equals(Constants.END_IF))
				{
					System.out.println(ast.children.get(i).type);
					generateBody(f, ast.children.get(i));
				}
				else
				{
					this.endIfNode = ast.children.get(i);
				}
			}
		}
	}
	
	public void generateAssignment(Function f, AST ast)
	{
		int righ_side_1_var_index = -1;
		int righ_side_2_var_index = -1;
		int left_side_var_index = -1;
		
		//RIGHT_SIDE_1
		
		if(ast.right_side_1.access.equals(Constants.INTEGER_ACCESS))
		{ 
			pushIntToStack(ast.right_side_1.id);
			
		}
		else if(ast.right_side_1.access.equals(Constants.SCALAR_ACCESS))
		{ 
			righ_side_1_var_index = f.getAllVariables().get(ast.right_side_1.id);
			loadScalarFromStack(ast.right_side_1.id, righ_side_1_var_index, ast.right_side_1.scope);
		}
		else if(ast.right_side_1.access.equals(Constants.ARRAY_ACCESS))
		{
			righ_side_1_var_index = f.getAllVariables().get(ast.right_side_1.id);
			loadArrayFromStack(ast.right_side_1.id, righ_side_1_var_index, ast.right_side_1.scope);
			
			if(ast.right_side_1.array_access_type.equals(Constants.INTEGER_ACCESS))
			{
				pushIntToStack(ast.right_side_1.array_index);
			}
			else if(ast.right_side_1.array_access_type.equals(Constants.SCALAR_ACCESS))
			{
				loadScalarFromStack(ast.right_side_1.array_index,f.getAllVariables().get(ast.right_side_1.array_index),f.getScopes(ast.right_side_1.array_index));
			}
			this.write.println("iaload");
		}
		else if(ast.right_side_1.access.equals(Constants.CALL))
		{
			String invocation = "";
			
			if(ast.right_side_1.other_module)
			{
				invocation = buildFunctionInvocationOtherModule(f, Utils.splitByDotFunction(ast.right_side_1.id), ast.right_side_1.args_id);
			}
			else
			{
				invocation = buildFunctionInvocationSameModule(ast.right_side_1.function, ast.right_side_1.id, ast.right_side_1.function.getArguments());
			}
			
			for (int i = 0; i < ast.right_side_1.args_id.size(); i++) 
			{
				Variable var;
				String atual_var = ast.right_side_1.args_id.get(i);
				
				try 
				{
					Integer.parseInt(atual_var);
					pushIntToStack(atual_var);
				} 
				catch (NumberFormatException e)
				{
					var = f.returnVarById(atual_var);
					String scope = f.getScopes(atual_var);
					int varNum = f.getAllVariables().get(atual_var);
					
					if(var.getType().equals(Constants.SCALAR))
					{
						loadScalarFromStack(atual_var, varNum, scope);
					}
					else if(var.getType().equals(Constants.ARRAY))
					{
						loadArrayFromStack(atual_var, varNum, scope);
					}
				}
			}
			
			if(ast.right_side_1.other_module)
			{
				this.write.println("invokestatic "+Utils.splitByDotModule(ast.right_side_1.id)+"/"+invocation);
			}
			else
			{
				this.write.println("invokestatic "+moduleName+"/"+invocation);
			}
		}
		else if(ast.right_side_1.access.equals(Constants.SIZE_ACCESS))
		{ 
			if(righ_side_1_var_index!=-1)
			{
				loadArrayFromStack(ast.right_side_1.id, righ_side_1_var_index,ast.right_side_1.scope);
				this.write.println("arraylength");
			}
		}
		else if(ast.right_side_1.access.equals(Constants.ARRAY_SIZE))
		{
			pushIntToStack(ast.right_side_1.id);
		}
		
		if(ast.isOperation)
		{
			//RIGHT_SIDE_2
			
			if(ast.right_side_2.access.equals(Constants.INTEGER_ACCESS))
			{
				pushIntToStack(ast.right_side_2.id);
				
			}
			else if(ast.right_side_2.access.equals(Constants.SCALAR_ACCESS))
			{
				righ_side_2_var_index = f.getAllVariables().get(ast.right_side_2.id);
				loadScalarFromStack(ast.right_side_1.id, righ_side_2_var_index, ast.right_side_2.scope);
			}
			else if(ast.right_side_2.access.equals(Constants.ARRAY_ACCESS))
			{
				righ_side_2_var_index = f.getAllVariables().get(ast.right_side_2.id);
				loadArrayFromStack(ast.right_side_2.id, righ_side_2_var_index, ast.right_side_2.scope);
				
				if(ast.right_side_2.array_access_type.equals(Constants.INTEGER_ACCESS))
				{
					pushIntToStack(ast.right_side_2.array_index);
				}
				else if(ast.right_side_2.array_access_type.equals(Constants.SCALAR_ACCESS))
				{
					loadScalarFromStack(ast.right_side_2.array_index,f.getAllVariables().get(ast.right_side_2.array_index),f.getScopes(ast.right_side_2.array_index));
				}
				this.write.println("iaload");
			}
			else if(ast.right_side_2.access.equals(Constants.CALL))
			{
				String invocation = "";
				
				if(ast.right_side_2.other_module)
				{
					invocation = buildFunctionInvocationOtherModule(f, Utils.splitByDotFunction(ast.right_side_2.id), ast.right_side_2.args_id);
				}
				else
				{
					invocation = buildFunctionInvocationSameModule(ast.right_side_2.function, ast.right_side_2.id, ast.right_side_2.function.getArguments());
				}
				
				for (int i = 0; i < ast.right_side_2.args_id.size(); i++) 
				{
					Variable var;
					String atual_var = ast.right_side_2.args_id.get(i);
					
					try 
					{
						Integer.parseInt(atual_var);
						pushIntToStack(atual_var);
					} 
					catch (NumberFormatException e)
					{
						var = f.returnVarById(atual_var);
						String scope = f.getScopes(atual_var);
						int varNum = f.getAllVariables().get(atual_var);
						
						if(var.getType().equals(Constants.SCALAR))
						{
							loadScalarFromStack(atual_var, varNum, scope);
						}
						else if(var.getType().equals(Constants.ARRAY))
						{
							loadArrayFromStack(atual_var, varNum, scope);
						}
					}
				}
				
				if(ast.right_side_2.other_module)
				{
					this.write.println("invokestatic "+Utils.splitByDotModule(ast.right_side_2.id)+"/"+invocation);
				}
				else
				{
					this.write.println("invokestatic "+moduleName+"/"+invocation);
				}
			}
			else if(ast.right_side_2.access.equals(Constants.SIZE_ACCESS))
			{
				if(righ_side_2_var_index!=-1)
				{
					loadArrayFromStack(ast.right_side_2.id, righ_side_2_var_index,ast.right_side_2.scope);
					this.write.println("arraylength");
				}
			}
			
			//OPERATION
			
			makeOperation(ast.operation);
		}
		
		//LEFT_SIDE
		left_side_var_index = f.getAllVariables().get(ast.left_side.id);
		
		if(ast.left_side.access.equals(Constants.SCALAR_ACCESS))
		{
			if(ast.right_side_1.access.equals(Constants.ARRAY_SIZE))
			{ 
				this.write.println("newarray int");
				storeArrayToStack(ast.left_side.id, left_side_var_index, ast.left_side.scope);
			}
			else
			{
				storeScalarToStack(ast.left_side.id, left_side_var_index, ast.left_side.scope);
			}
		}
		else if(ast.left_side.access.equals(Constants.ARRAY_ACCESS))
		{
			loadArrayFromStack(ast.left_side.id,left_side_var_index, ast.left_side.scope);
			
			this.write.println("swap");
			
			if(ast.left_side.array_access_type.equals(Constants.INTEGER_ACCESS))
			{
				pushIntToStack(ast.left_side.array_index);
			}
			else if(ast.left_side.array_access_type.equals(Constants.SCALAR_ACCESS))
			{
				loadScalarFromStack(ast.left_side.array_index, f.getAllVariables().get(ast.left_side.array_index), f.getScopes(ast.left_side.array_index));
			}
			
			this.write.println("swap");
			this.write.println("iastore");
		}
	}

	public void generateFunctions()
	{
		for(Function f : module.getAllFunctions().values())
		{
			f.buildVariablesIndex();
			generateFunctionDeclaration(f);
			generateBody(f,f.getInitialNode());
			generateReturn(f);
			generateNewLine();
		}
	}
}
