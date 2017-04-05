public class SemanticAnalysis
{
	private SimpleNode sn;
	
	public SemanticAnalysis(SimpleNode sn)
	{
		this.sn = sn;
	}
	
	public void initiateAnalysis()
	{
		this.analyseGlobalVariables();
	}
	
	public void analyseGlobalVariables()
	{
		for(int i=0; i<sn.getChildren().length; i++)
		{
			SimpleNode node = (SimpleNode) sn.jjtGetChild(i);
			
			if(node.id == YAL2JVMTreeConstants.JJTFUNCTION)
			{
				return;
			}
			else if(node.id == YAL2JVMTreeConstants.JJTDECLARATION)
			{
				int numChildren = node.jjtGetNumChildren();
				SimpleNode left_side = (SimpleNode) node.jjtGetChild(0);
				String name = left_side.ID;
				Variable var = new Variable(name);
				//Só declração da variável (ex.: a)
				if(numChildren == 1)
				{
					var = new Scalar(name);
					if(!YAL2JVM.getModule().addGlobalVariableToModule(var))
					{
						//Existe mais que uma com o mesmo nome;
						System.out.println("Existe mais que uma variável global com o nome "+name);
					}
				}
				//Declaração e atribuição (ex.: a=1)
				if(numChildren == 2)
				{
					SimpleNode right_side = (SimpleNode) node.jjtGetChild(1);
					int rhsChildrenNum = right_side.jjtGetNumChildren();
					
					//Scalar
					if(rhsChildrenNum == 0)
					{
						int value = Integer.parseInt(right_side.ID);
						var = new Scalar(name,value);
					}
					
					//Array
					if(rhsChildrenNum == 1)
					{
						SimpleNode array = (SimpleNode) right_side.jjtGetChild(0);
						int size = Integer.parseInt(array.ID);
						var = new Array(name, size);
					}
				}
			}
			
		}
	}
	
	public void analyseArrayAccess(String array, String index, Function function)
	{
		if(YAL2JVM.getModule().isGlobalVariable(array))
		{
			if(YAL2JVM.getModule().getGlobalVariableById(array) instanceof Scalar)
			{
				System.out.println("Na função "+function.getFunctionId() + " a variavel "+array+" não é um array");
			}
		}
		
		if (function.isLocalVariable(array)) 
		{
			if (function.getVariableById(array) instanceof Scalar)
			{
				System.out.println("Na função "+function.getFunctionId() + " a variavel "+array+" não é um array");
			}
		} 
		
		if (function.checkArguments(array))
		{
			if (function.getArgumentsById(array) instanceof Scalar)
			{
				System.out.println("Na função "+function.getFunctionId() + " a variavel "+array+" não é um array");
			}
		}  
		
		if (function.isReturnValue(array)) 
		{
			if (function.getReturnValue() instanceof Scalar)
			{
				System.out.println("Na função "+function.getFunctionId() + " a variavel "+array+" não é um array");
			}
		} 
		else 
		{
			System.out.println("Na função "+function.getFunctionId() + " a variavel "+array+" ainda não foi declarada");
		}
		
		try 
		{
			Integer.parseInt(index);
		} 
		catch (NumberFormatException e) 
		{
			if (YAL2JVM.getModule().isGlobalVariable(index)) 
			{
				if (YAL2JVM.getModule().getGlobalVariableById(index)  instanceof Array)
				{
					System.out.println("Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar");
				}
			}  
			if (function.isLocalVariable(index)) 
			{
				if (function.getVariableById(index) instanceof Array)
				{
					System.out.println("Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar");
				}
			} 
			if (function.checkArguments(index)) 
			{
				if (function.getArgumentsById(index) instanceof Array)
				{
					System.out.println("Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar");
				}
			}  
			if (function.isReturnValue(index))
			{
				if (function.getReturnValue() instanceof Array)
				{
					System.out.println("Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar");
				}
			} 
			else 
			{
				System.out.println("Na função "+function.getFunctionId() + " a variavel "+index+" ainda não foi declarada");
			}
		}
	}
	
	public IntermediateRepresentation analyseCondition(SimpleNode left_side, SimpleNode right_side, Function function, String type)
	{
		IntermediateRepresentation conditionNode = new IntermediateRepresentation(type,function);
		
		if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
		{
			SimpleNode index = (SimpleNode) left_side.jjtGetChild(0);
			conditionNode.lhsId = left_side.ID;
			conditionNode.lhsAccess = "array";
			try
			{
				Integer.parseInt(index.ID);
				conditionNode.lhsArrayAccessType = "integer";
			}
			catch(NumberFormatException e)
			{
				conditionNode.lhsArrayAccessType = "scalar";
			}
			conditionNode.lhsArrayIndexId = index.ID;
			
			analyseArrayAccess(left_side.ID, index.ID, function);
		}
		return conditionNode;
	}
	
	public IntermediateRepresentation analyseAssignment(SimpleNode lhs, SimpleNode rhs, Function function) 
	{
		boolean isOperation = false;
		return null;
	}

	public void analyseFunction(Function function)
	{
		for(int i=0; i<sn.getChildren().length;i++)
		{
			SimpleNode child = (SimpleNode) sn.getChildren()[i];
		}
	}
}
