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
		
		else if (function.isLocalVariable(array)) 
		{
			if (function.getVariableById(array) instanceof Scalar)
			{
				System.out.println("Na função "+function.getFunctionId() + " a variavel "+array+" não é um array");
			}
		} 
		
		else if (function.checkArguments(array))
		{
			if (function.getArgumentsById(array) instanceof Scalar)
			{
				System.out.println("Na função "+function.getFunctionId() + " a variavel "+array+" não é um array");
			}
		}  
		
		else if (function.isReturnValue(array)) 
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
			else if (function.isLocalVariable(index)) 
			{
				if (function.getVariableById(index) instanceof Array)
				{
					System.out.println("Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar");
				}
			} 
			else if (function.checkArguments(index)) 
			{
				if (function.getArgumentsById(index) instanceof Array)
				{
					System.out.println("Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar");
				}
			}  
			else if (function.isReturnValue(index))
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
			else if (parentFunction.isLocalVariable(scalar)) 
			{
				if (parentFunction.getVariableById(scalar) instanceof Scalar)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho");
				}
			} 
			else if (parentFunction.checkArguments(scalar)) 
			{
				if (parentFunction.getArgumentsById(scalar) instanceof Scalar)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho");
				}
			} 
			else if (parentFunction.isReturnValue(scalar))
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
			else if (parentFunction.isLocalVariable(scalarAccess)) 
			{
				if (parentFunction.getVariableById(scalarAccess) instanceof Array)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess);
				}
			} 
			else if (parentFunction.checkArguments(scalarAccess))
			{
				if (parentFunction.getArgumentsById(scalarAccess) instanceof Array)
				{
					System.out.println("Na função "+parentFunction.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess);
				}
			} 
			else if (parentFunction.isReturnValue(scalarAccess))
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
				else if (parentFunction.isLocalVariable(var)) 
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
				else if (parentFunction.checkArguments(var)) 
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
				else if (parentFunction.isReturnValue(var)) 
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
					YAL2JVM.errorFound();
					YAL2JVM.incErrors();
					System.out.println("Na função "+parentFunction.getFunctionDeclaration()+"a variável "+var+" ainda não foi declarada.");
				}
			}
		}

		temp += ")";
		return temp;
	}
	
	public AST analyseCondition(SimpleNode left_side, SimpleNode right_side, Function function, String type)
	{
		AST conditionNode = new AST(type,function);
		
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
							String aux_0 = analyseFunctionCall(term.getChildren(),function,term.ID);
							conditionNode.rhs1Call = YAL2JVM.getModule().getFunctionByID(aux_0);
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
		else if (right_side.jjtGetNumChildren() == 2) 
		{
			conditionNode.isOperation = true;
			conditionNode.compOperator = right_side.ID;
			
			SimpleNode left_term = (SimpleNode)right_side.jjtGetChild(0);
			SimpleNode right_term = (SimpleNode)right_side.jjtGetChild(1);

			if (left_term.ID == null) 
			{
				SimpleNode child = (SimpleNode)left_term.jjtGetChild(0);

				if (child.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
				{
					conditionNode.rhs1Access = "call";
					conditionNode.rhs1Id = child.ID;
					
					if (!Utils.isArrayOrFunctionAccess(child.ID)) 
					{
						String aux_1 = analyseFunctionCall(child.getChildren(),function,child.ID);
						System.out.println("Testing1: "+aux_1);
						conditionNode.rhs1Call = YAL2JVM.getModule().getFunctionByID(aux_1);
					} 
					else 
					{
						conditionNode.rhs1OtherModule = true;
					}
					
					for (int i = 0; i < child.jjtGetNumChildren(); i++)
					{
						conditionNode.rhs1Args.add(i, ((SimpleNode)child.jjtGetChild(i)).ID);
					}
					
					analyseCall(child.ID, child.getChildren(), true, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					SimpleNode index = (SimpleNode)child.jjtGetChild(0);
					
					conditionNode.rhs1Id = child.ID;
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
					
					analyseArrayAccess(child.ID, index.ID, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS) 
				{
					conditionNode.rhs1Id = child.ID;
					conditionNode.rhs1Access = "scalar";
					
					analyseScalarAccess(child.ID, function);
				}
			} 
			else
			{
				conditionNode.rhs1Id = left_term.ID;
				conditionNode.rhs1Access = "integer";
			}

			if (right_term.ID == null) 
			{
				SimpleNode child = (SimpleNode)right_term.jjtGetChild(0);

				if (child.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
				{
					conditionNode.rhs2Access = "call";
					conditionNode.rhs2Id = child.ID;
					
					if (!Utils.isArrayOrFunctionAccess(child.ID)) 
					{
						String aux_2 = analyseFunctionCall(child.getChildren(),function,child.ID);
						conditionNode.rhs2Call = YAL2JVM.getModule().getFunctionByID(aux_2);
					} 
					else 
					{
						conditionNode.rhs2OtherModule = true;
					}
					
					for (int i = 0; i < child.jjtGetNumChildren(); i++)
					{
						conditionNode.rhs2Args.add(i, ((SimpleNode)child.jjtGetChild(i)).ID);
					}
					
					analyseCall(child.ID, child.getChildren(), true, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					SimpleNode index = (SimpleNode)child.jjtGetChild(0);
					
					conditionNode.rhs2Id = child.ID;
					conditionNode.rhs2Access = "array";
					
					try
					{
						Integer.parseInt(index.ID);
						conditionNode.rhs2ArrayAccess = "integer";
					}
					catch(NumberFormatException e)
					{
						conditionNode.rhs2ArrayAccess = "scalar";
					}
					
					conditionNode.rhs2ArrayIndexId = index.ID;
					
					analyseArrayAccess(child.ID, index.ID, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
				{
					conditionNode.rhs2Id = child.ID;
					conditionNode.rhs2Access = "scalar";
					
					analyseScalarAccess(child.ID, function);
				}
			} 
			else 
			{
				conditionNode.rhs2Id = right_term.ID;
				conditionNode.rhs2Access = "integer";
			}
		}
		return conditionNode;
	}
	
	public AST analyseCall(String call, Node[] args, boolean isCondition, Function parentFunction) 
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
		
		AST callFunction = new AST(Constants.CALL,parentFunction);
		
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
	
	public AST analyseAssignment(SimpleNode left_side, SimpleNode right_side, Function function) 
	{
		AST assigmentNode = new AST(Constants.ASSIGNMENT,function);		
		boolean isOperation = false;
		
		//ANALYSE LEFT SIDE
		if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
		{
			assigmentNode.lhsAccess = "array";
			assigmentNode.lhsId = left_side.ID;
			
			SimpleNode arrayIndex = (SimpleNode)(left_side.jjtGetChild(0));
			
			try
			{
				Integer.parseInt(arrayIndex.ID);
				assigmentNode.lhsArrayIndexId = arrayIndex.ID;
				assigmentNode.lhsArrayAccessType = "integer";
			} 
			catch (NumberFormatException e)
			{
				assigmentNode.lhsArrayIndexId  = arrayIndex.ID;
				assigmentNode.lhsArrayAccessType = "scalar";
			}
		} 
		else if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
		{
			if(Utils.isArrayOrFunctionAccess((left_side.ID)))
			{
				assigmentNode.lhsAccess = "size";
				assigmentNode.lhsId = left_side.ID.split(".")[0];
				System.out.println(assigmentNode.lhsId);
			}
			else
			{
				assigmentNode.lhsAccess = "scalar";
				assigmentNode.lhsId = left_side.ID;
			}
		}
		
		//ANALYSE RIGTH SIDE 1
		
		SimpleNode right_side_1 = (SimpleNode)right_side.jjtGetChild(0);
		
		if (right_side_1.getOriginalId() == YAL2JVMTreeConstants.JJTTERM) 
		{
			if (right_side_1.ID != null)
			{
				assigmentNode.rhs1Id = right_side_1.ID;
				assigmentNode.rhs1Access = "integer";
			} 
			else 
			{
				SimpleNode term = (SimpleNode)right_side_1.jjtGetChild(0);
				
				if (term.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
				{
					assigmentNode.rhs1Access = "call";
					assigmentNode.rhs1Id = term.ID;
					
					for (int i = 0; i < term.jjtGetNumChildren(); i++)
					{
						assigmentNode.rhs1Args.add(((SimpleNode)term.jjtGetChild(i)).ID);
					}
				}
				else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					assigmentNode.rhs1Access = "array";
					assigmentNode.rhs1Id = term.ID;
					
					SimpleNode arrayIndex = (SimpleNode)(term.jjtGetChild(0));
					
					try
					{
						Integer.parseInt(arrayIndex.ID);
						assigmentNode.rhs1ArrayIndexId = arrayIndex.ID;
						assigmentNode.rhs1ArrayAccess = "integer";
					}
					catch (NumberFormatException e)
					{
						assigmentNode.rhs1ArrayIndexId = arrayIndex.ID;
						assigmentNode.rhs1ArrayAccess = "scalar";
					}
				}
				else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS) 
				{
					if(Utils.isArrayOrFunctionAccess(term.ID))
					{
						assigmentNode.rhs1Access = "size";
						assigmentNode.rhs1Id = term.ID.split(".")[0];
					}
					else
					{
						assigmentNode.rhs1Access = "scalar";
						assigmentNode.rhs1Id = term.ID;
					}
				} 
			}
		} 
		else if (right_side_1.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYSIZE) 
		{
			assigmentNode.rhs1Access = "arraysize";
			assigmentNode.rhs1Id = right_side_1.ID;
		}
		
		//ANALYSE RIGTH SIDE 2
		
		if (right_side.jjtGetNumChildren() == 2) 
		{
			assigmentNode.operation = right_side.ID;
			assigmentNode.isOperation = true;
			System.out.println(assigmentNode.operation);
			
			SimpleNode right_side_2 = (SimpleNode)right_side.jjtGetChild(1);
			
			if (right_side_2.getOriginalId() == YAL2JVMTreeConstants.JJTTERM)
			{
				if (right_side_2.ID != null)
				{
					assigmentNode.rhs2Id = right_side_2.ID;
					assigmentNode.rhs2Access = "integer";
				}
				else 
				{
					SimpleNode term = (SimpleNode)right_side_2.jjtGetChild(0);
					
					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
					{
						assigmentNode.rhs2Access = "call";
						assigmentNode.rhs2Id = term.ID;
						
						for (int i = 0; i < term.jjtGetNumChildren(); i++)
						{
							assigmentNode.rhs2Args.add(((SimpleNode)term.jjtGetChild(i)).ID);
						}
					} 
					else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
					{
						assigmentNode.rhs2Access = "array";
						assigmentNode.rhs2Id = term.ID;
						
						SimpleNode arrayIndex = (SimpleNode)(term.jjtGetChild(0));
						
						try
						{
							Integer.parseInt(arrayIndex.ID);
							assigmentNode.rhs2ArrayIndexId = arrayIndex.ID;
							assigmentNode.rhs2ArrayAccess = "integer";
						} 
						catch (NumberFormatException e) 
						{
							assigmentNode.rhs2ArrayIndexId = arrayIndex.ID;
							assigmentNode.rhs2ArrayAccess = "scalar";
						}
					} 
					else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
					{
						if(Utils.isArrayOrFunctionAccess(term.ID))
						{
							assigmentNode.rhs2Access = "size";
							assigmentNode.rhs2Id = term.ID.split(".")[0];
						}
						else
						{
							assigmentNode.rhs2Access = "scalar";
							assigmentNode.rhs2Id = term.ID;
						}
					} 
				}
			}
			else if (right_side_2.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYSIZE)
			{
				assigmentNode.rhs2Access = "arraysize";
				assigmentNode.rhs2Id = right_side_2.ID;
			}
		}
		
		findErrors(function,assigmentNode);
		
		return assigmentNode;
	}
	
	public void findErrors(Function function, AST assigmentNode)
	{
		
	}

	public AST analyseFunction(Function function, AST root, Node[] children)
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
				
				AST if_cond = analyseCondition(if_left_side, if_right_side, function,"if");
				break;
			case YAL2JVMTreeConstants.JJTWHILE:
				break;
			case YAL2JVMTreeConstants.JJTCALL:
				AST call = analyseCall(child.ID, child.getChildren(), false, function);
				break;
			case YAL2JVMTreeConstants.JJTASSIGNEMENT:
				SimpleNode ass_left_side = (SimpleNode)child.jjtGetChild(0); 
				SimpleNode ass_right_side = (SimpleNode)child.jjtGetChild(1); 
				
				AST assignment = analyseAssignment(ass_left_side, ass_right_side, function);
				break;
			}
		}
		return null;
	}
}
