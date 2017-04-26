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
						YAL2JVM.incErrors();
						System.out.println("Existe mais que uma variável global com o nome " + name);
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
	
	public void analyseScalarAccess(String scalarAccess, Function parentFunction) 
	{
		String scalar;

		if (Utils.isArrayOrFunctionAccess(scalarAccess)) 
		{
			scalar = scalarAccess.split("\\.")[0];

			if (YAL2JVM.getModule().isGlobalVariable(scalar)) 
			{
				if (YAL2JVM.getModule().getGlobalVariableById(scalar) instanceof Scalar)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho");
				}
			}  
			if (parentFunction.isLocalVariable(scalar)) 
			{
				if (parentFunction.getVariableById(scalar) instanceof Scalar)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho");
				}
			} 
			if (parentFunction.checkArguments(scalar)) 
			{
				if (parentFunction.getArgumentsById(scalar) instanceof Scalar)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho");
				}
			} 
			if (parentFunction.isReturnValue(scalar))
			{
				if (parentFunction.getReturnValue() instanceof Scalar)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho");
				}
			} 
			else 
			{
				System.out.println("Na função "+parentFunction.getFunctionId() + " a variavel "+scalar+" ainda não foi declarada");
			}
		} 
		else
		{
			if (YAL2JVM.getModule().isGlobalVariable(scalarAccess)) 
			{
				if (YAL2JVM.getModule().getGlobalVariableById(scalarAccess) instanceof Array)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess);
				}
			} 
			if (parentFunction.isLocalVariable(scalarAccess)) 
			{
				if (parentFunction.getVariableById(scalarAccess) instanceof Array)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess);
				}
			} 
			if (parentFunction.checkArguments(scalarAccess))
			{
				if (parentFunction.getArgumentsById(scalarAccess) instanceof Array)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess);
				}
			} 
			if (parentFunction.isReturnValue(scalarAccess))
			{
				if (parentFunction.getReturnValue() instanceof Array)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess);
				}
			}
			else 
			{
				System.out.println("Na função "+parentFunction.getFunctionId() + " a variavel "+scalarAccess+" ainda não foi declarada");
			}
		}
	}
	
	public String analyseFunctionCall(Node[] args, Function parentFunction, String name)
	{	
		String temp = "";
		temp += name;
		temp += "(";
		if (args == null)
		{
			temp += ")";
			return temp;
		}

		for (int i = 0; i < args.length; i++) 
		{
			SimpleNode arg = (SimpleNode)args[i];
			String var = arg.ID;
			try 
			{
				Integer.parseInt(var);
				temp += " scalar";
			} 
			catch (NumberFormatException e) 
			{
				if (YAL2JVM.getModule().isGlobalVariable(var)) 
				{
					if (YAL2JVM.getModule().getGlobalVariableById(var) instanceof Scalar) 
					{
						temp += " scalar";
						analyseScalarAccess(var, parentFunction);
					} 
					else 
					{
						temp += " array";
						analyseArrayAccess(var, "0", parentFunction);
					}
				}  
				if (parentFunction.isLocalVariable(var)) 
				{
					if (parentFunction.getVariableById(var) instanceof Scalar)
					{
						temp += " scalar";
						analyseScalarAccess(var, parentFunction);
					} 
					else 
					{
						temp += " array";
						analyseArrayAccess(var, "0", parentFunction);
					}
				}  
				if (parentFunction.checkArguments(var)) 
				{
					if (parentFunction.getArgumentsById(var) instanceof Scalar) 
					{
						temp += " scalar";
						analyseScalarAccess(var, parentFunction);
					} 
					else 
					{
						temp += " array";
						analyseArrayAccess(var, "0", parentFunction);
					}
				} 
				if (parentFunction.isReturnValue(var)) 
				{
					if (parentFunction.getReturnValue() instanceof Scalar) 
					{
						temp += " scalar";
						analyseScalarAccess(var, parentFunction);
					} 
					else 
					{
						temp += " array";
						analyseArrayAccess(var, "0", parentFunction);
					}
				} 
				else 
				{
					temp += " " + var;
					System.out.println("Na função "+parentFunction+"a variável "+var+" ainda  não foi declarada.");
				}
			}
		}

		temp += ")";
		return temp;
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
		
		if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
		{
			conditionNode.lhsId = left_side.ID;
			conditionNode.lhsAccess = "scalar";
			analyseScalarAccess(left_side.ID,function);
		}
		
		if(right_side.jjtGetNumChildren()==1)
		{
			conditionNode.isOperation = false;
			SimpleNode right_side_child = (SimpleNode)right_side.jjtGetChild(0);
			
			if (right_side_child.getOriginalId() == YAL2JVMTreeConstants.JJTTERM) 
			{
				if (right_side_child.ID == null) 
				{
					@SuppressWarnings("unused")
					SimpleNode term = (SimpleNode)right_side_child.jjtGetChild(0);

					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTCALL)
					{
						conditionNode.rhs1Access = "call";
						conditionNode.rhs1Id = term.ID;
						
						if (!Utils.isArrayOrFunctionAccess(term.ID))
						{
							conditionNode.rhs1Call = YAL2JVM.getModule().getFunctionByID(term.ID);
						} 
						else
						{
							conditionNode.rhs1OtherModule = true;
						}
						
						for (int i = 0; i < term.jjtGetNumChildren(); i++)
						{
							conditionNode.rhs1Args.add(i, ((SimpleNode)term.jjtGetChild(i)).ID);
						}
						
						analyseCall(term.ID, term.getChildren(), true, function);
						
					} 
					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
					{
						SimpleNode index = (SimpleNode)term.jjtGetChild(0);
						
						conditionNode.rhs1Id = term.ID;
						conditionNode.rhs1Access = "array";
						try
						{
							Integer.parseInt(index.ID);
							conditionNode.rhs1ArrayAccess = "integer";
						}
						catch(NumberFormatException e)
						{
							conditionNode.rhs1ArrayAccess = "scalar";
						}
						conditionNode.rhs1ArrayIndexId = index.ID;
						
						analyseArrayAccess(term.ID, index.ID, function);
					} 
					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS) 
					{
						conditionNode.rhs1Id = term.ID;
						conditionNode.rhs1Access = "scalar";
						
						analyseScalarAccess(term.ID, function);
					}
				} 
				else 
				{
					conditionNode.rhs1Id = right_side_child.ID;
					conditionNode.rhs1Access = "integer";
				}
			} 
		}
		
		return conditionNode;
	}
	
	public IntermediateRepresentation analyseCall(String call, Node[] args, boolean isCondition, Function parentFunction) 
	{
		boolean dot = true;
		String declaration = null;
		
		if (!Utils.isArrayOrFunctionAccess(call)) 
		{
			dot = false;
			declaration = analyseFunctionCall(args, parentFunction,call);

			if (YAL2JVM.getModule().functionExists(declaration))
			{
				if (isCondition)
				{
					if (YAL2JVM.getModule().getFunctionByID(declaration).getReturnValue() != null) 
					{
						if (YAL2JVM.getModule().getFunctionByID(declaration).getReturnValue() instanceof Array)
						{
							YAL2JVM.errorFound();
							YAL2JVM.incErrors();
							System.out.println("A função "+declaration+" retorna um array.");
							
						}
					} 
					else
					{
						YAL2JVM.errorFound();
						YAL2JVM.incErrors();
						System.out.println("A função "+declaration+" não retorna nenhuma variável.");
					}
				}
			} 
			else
			{
				YAL2JVM.errorFound();
				YAL2JVM.incErrors();
				System.out.println("A função "+declaration+" não foi declarada.");
			}
		}
		
		IntermediateRepresentation callFunction = new IntermediateRepresentation("call",parentFunction);
		
		int numArgs;
		
		if(args == null)
		{
			numArgs = 0;
		}
		else
		{
			numArgs = args.length;
		}
		
		String[] arguments = new String[numArgs];
		
		for (int i = 0; i < numArgs; i++) 
		{
			arguments[i] = ((SimpleNode)args[i]).ID;
		}
		
		callFunction.callArgs= arguments;
		callFunction.dot = dot;
		callFunction.callFunctionName = call;
		callFunction.callFunctionDeclaration = declaration;
		
		return callFunction;
	}
	
	public IntermediateRepresentation analyseAssignment(SimpleNode lhs, SimpleNode rhs, Function function) 
	{
		boolean isOperation = false;
		return null;
	}

	public IntermediateRepresentation analyseFunction(Function function, IntermediateRepresentation parentNode)
	{
		for(int i=0; i<sn.getChildren().length;i++)
		{
			SimpleNode child = (SimpleNode) sn.getChildren()[i];
			
			switch(child.getOriginalId())
			{
			case YAL2JVMTreeConstants.JJTIF:
				SimpleNode if_node = (SimpleNode) child.jjtGetChild(0);
				SimpleNode if_left_side = (SimpleNode) if_node.jjtGetChild(0);
				SimpleNode if_right_side = (SimpleNode) if_node.jjtGetChild(1);
				
				IntermediateRepresentation conditionNodeIf = analyseCondition(if_left_side, if_right_side, function,"if");
				break;
			case YAL2JVMTreeConstants.JJTWHILE:
				break;
			}
		}
		return null;
	}
}
