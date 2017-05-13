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
			conditionNode.left_side_id = left_side.ID;
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
			conditionNode.left_side_id = left_side.ID;
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
						conditionNode.righ_side_1_id = term.ID;
						
						if (!Utils.isArrayOrFunctionAccess(term.ID))
						{
							String aux_0 = analyseFunctionCall(term.getChildren(),function,term.ID);
							conditionNode.right_side_1_function = YAL2JVM.getModule().getFunctionByID(aux_0);
						} 
						else
						{
							conditionNode.other_module_1 = true;
						}
						
						for (int i = 0; i < term.jjtGetNumChildren(); i++)
						{
							conditionNode.right_side_1_args_id.add(i, ((SimpleNode)term.jjtGetChild(i)).ID);
						}
						
						analyseCall(term.ID, term.getChildren(), true, function);
						
					} 
					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
					{
						SimpleNode index = (SimpleNode)term.jjtGetChild(0);
						
						conditionNode.righ_side_1_id = term.ID;
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
						conditionNode.righ_side_1_id = term.ID;
						conditionNode.rhs1Access = "scalar";
						
						analyseScalarAccess(term.ID, function);
					}
				} 
				else 
				{
					conditionNode.righ_side_1_id = right_side_child.ID;
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
					conditionNode.righ_side_1_id = child.ID;
					
					if (!Utils.isArrayOrFunctionAccess(child.ID)) 
					{
						String aux_1 = analyseFunctionCall(child.getChildren(),function,child.ID);
						System.out.println("Testing1: "+aux_1);
						conditionNode.right_side_1_function = YAL2JVM.getModule().getFunctionByID(aux_1);
					} 
					else 
					{
						conditionNode.other_module_1 = true;
					}
					
					for (int i = 0; i < child.jjtGetNumChildren(); i++)
					{
						conditionNode.right_side_1_args_id.add(i, ((SimpleNode)child.jjtGetChild(i)).ID);
					}
					
					analyseCall(child.ID, child.getChildren(), true, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					SimpleNode index = (SimpleNode)child.jjtGetChild(0);
					
					conditionNode.righ_side_1_id = child.ID;
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
					conditionNode.righ_side_1_id = child.ID;
					conditionNode.rhs1Access = "scalar";
					
					analyseScalarAccess(child.ID, function);
				}
			} 
			else
			{
				conditionNode.righ_side_1_id = left_term.ID;
				conditionNode.rhs1Access = "integer";
			}

			if (right_term.ID == null) 
			{
				SimpleNode child = (SimpleNode)right_term.jjtGetChild(0);

				if (child.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
				{
					conditionNode.rhs2Access = "call";
					conditionNode.righ_side_2_id = child.ID;
					
					if (!Utils.isArrayOrFunctionAccess(child.ID)) 
					{
						String aux_2 = analyseFunctionCall(child.getChildren(),function,child.ID);
						conditionNode.right_side_2_function = YAL2JVM.getModule().getFunctionByID(aux_2);
					} 
					else 
					{
						conditionNode.other_module_2 = true;
					}
					
					for (int i = 0; i < child.jjtGetNumChildren(); i++)
					{
						conditionNode.right_side_2_args_id.add(i, ((SimpleNode)child.jjtGetChild(i)).ID);
					}
					
					analyseCall(child.ID, child.getChildren(), true, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					SimpleNode index = (SimpleNode)child.jjtGetChild(0);
					
					conditionNode.righ_side_2_id = child.ID;
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
					conditionNode.righ_side_2_id = child.ID;
					conditionNode.rhs2Access = "scalar";
					
					analyseScalarAccess(child.ID, function);
				}
			} 
			else 
			{
				conditionNode.righ_side_2_id = right_term.ID;
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
		AST assignmentNode = new AST(Constants.ASSIGNMENT,function);		
		boolean isOperation = false;
		
		//ANALYSE LEFT SIDE
		if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
		{
			assignmentNode.lhsAccess = "array";
			assignmentNode.left_side_id = left_side.ID;
			
			SimpleNode arrayIndex = (SimpleNode)(left_side.jjtGetChild(0));
			
			try
			{
				Integer.parseInt(arrayIndex.ID);
				assignmentNode.lhsArrayIndexId = arrayIndex.ID;
				assignmentNode.lhsArrayAccessType = "integer";
			} 
			catch (NumberFormatException e)
			{
				assignmentNode.lhsArrayIndexId  = arrayIndex.ID;
				assignmentNode.lhsArrayAccessType = "scalar";
			}
		} 
		else if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
		{
			if(Utils.isArrayOrFunctionAccess((left_side.ID)))
			{
				assignmentNode.lhsAccess = "size";
				assignmentNode.left_side_id = left_side.ID.split(".")[0];
				System.out.println(assignmentNode.left_side_id);
			}
			else
			{
				assignmentNode.lhsAccess = "scalar";
				assignmentNode.left_side_id = left_side.ID;
			}
		}
		
		//ANALYSE RIGTH SIDE 1
		
		SimpleNode right_side_1 = (SimpleNode)right_side.jjtGetChild(0);
		
		if (right_side_1.getOriginalId() == YAL2JVMTreeConstants.JJTTERM) 
		{
			if (right_side_1.ID != null)
			{
				assignmentNode.righ_side_1_id = right_side_1.ID;
				assignmentNode.rhs1Access = "integer";
			} 
			else 
			{
				SimpleNode term = (SimpleNode)right_side_1.jjtGetChild(0);
				
				if (term.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
				{
					assignmentNode.rhs1Access = "call";
					assignmentNode.righ_side_1_id = term.ID;
					
					for (int i = 0; i < term.jjtGetNumChildren(); i++)
					{
						assignmentNode.right_side_1_args_id.add(((SimpleNode)term.jjtGetChild(i)).ID);
					}
				}
				else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					assignmentNode.rhs1Access = "array";
					assignmentNode.righ_side_1_id = term.ID;
					
					SimpleNode arrayIndex = (SimpleNode)(term.jjtGetChild(0));
					
					try
					{
						Integer.parseInt(arrayIndex.ID);
						assignmentNode.rhs1ArrayIndexId = arrayIndex.ID;
						assignmentNode.rhs1ArrayAccess = "integer";
					}
					catch (NumberFormatException e)
					{
						assignmentNode.rhs1ArrayIndexId = arrayIndex.ID;
						assignmentNode.rhs1ArrayAccess = "scalar";
					}
				}
				else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS) 
				{
					if(Utils.isArrayOrFunctionAccess(term.ID))
					{
						assignmentNode.rhs1Access = "size";
						assignmentNode.righ_side_1_id = term.ID.split(".")[0];
					}
					else
					{
						assignmentNode.rhs1Access = "scalar";
						assignmentNode.righ_side_1_id = term.ID;
					}
				} 
			}
		} 
		else if (right_side_1.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYSIZE) 
		{
			assignmentNode.rhs1Access = "arraysize";
			assignmentNode.righ_side_1_id = right_side_1.ID;
		}
		
		//ANALYSE RIGTH SIDE 2
		
		if (right_side.jjtGetNumChildren() == 2) 
		{
			assignmentNode.operation = right_side.ID;
			assignmentNode.isOperation = true;
			
			System.out.println(assignmentNode.operation);
			
			SimpleNode right_side_2 = (SimpleNode)right_side.jjtGetChild(1);
			
			if (right_side_2.getOriginalId() == YAL2JVMTreeConstants.JJTTERM)
			{
				if (right_side_2.ID != null)
				{
					assignmentNode.righ_side_2_id = right_side_2.ID;
					assignmentNode.rhs2Access = "integer";
				}
				else 
				{
					SimpleNode term = (SimpleNode)right_side_2.jjtGetChild(0);
					
					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
					{
						assignmentNode.rhs2Access = "call";
						assignmentNode.righ_side_2_id = term.ID;
						
						for (int i = 0; i < term.jjtGetNumChildren(); i++)
						{
							assignmentNode.right_side_2_args_id.add(((SimpleNode)term.jjtGetChild(i)).ID);
						}
					} 
					else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
					{
						assignmentNode.rhs2Access = "array";
						assignmentNode.righ_side_2_id = term.ID;
						
						SimpleNode arrayIndex = (SimpleNode)(term.jjtGetChild(0));
						
						try
						{
							Integer.parseInt(arrayIndex.ID);
							assignmentNode.rhs2ArrayIndexId = arrayIndex.ID;
							assignmentNode.rhs2ArrayAccess = "integer";
						} 
						catch (NumberFormatException e) 
						{
							assignmentNode.rhs2ArrayIndexId = arrayIndex.ID;
							assignmentNode.rhs2ArrayAccess = "scalar";
						}
					} 
					else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
					{
						if(Utils.isArrayOrFunctionAccess(term.ID))
						{
							assignmentNode.rhs2Access = "size";
							assignmentNode.righ_side_2_id = term.ID.split(".")[0];
						}
						else
						{
							assignmentNode.rhs2Access = "scalar";
							assignmentNode.righ_side_2_id = term.ID;
						}
					} 
				}
			}
			else if (right_side_2.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYSIZE)
			{
				assignmentNode.rhs2Access = "arraysize";
				assignmentNode.righ_side_2_id = right_side_2.ID;
			}
		}
	
		findErrors(function,assignmentNode);
		
		assignmentNode.left_side_scope = function.getScopes(assignmentNode.left_side_id);
		assignmentNode.right_side_1_scope = function.getScopes(assignmentNode.righ_side_1_id);
		assignmentNode.right_side_2_scope = function.getScopes(assignmentNode.righ_side_2_id);
		
		return assignmentNode;
	}
	
	public void findErrors(Function function, AST assignmentNode)
	{
		//LEFT SIDE
		boolean new_var = false;
		
		if(!function.findVariable(assignmentNode.left_side_id))
		{
			new_var=true;
		}
		else
		{
			Variable v = function.returnVarById(assignmentNode.left_side_id);
			
			switch (assignmentNode.lhsAccess) 
			{	
			case "array":
			
				if (!v.getType().equals(Constants.ARRAY))
				{
					String error_message = "Na função "+function+" a variável "+assignmentNode.lhsAccess+
							 " do lado esquerdo do assignment não é um array!";
					
					Utils.error(error_message);
				}
				else 
				{
					if (assignmentNode.lhsArrayIndexId != null && !assignmentNode.lhsArrayAccessType.equals(Constants.INTEGER_ACCESS))
					{
						if (!function.findVariable(assignmentNode.lhsArrayIndexId))
						{
							String error_message = "Na função "+function+" a variável "+assignmentNode.lhsArrayIndexId+
									" do lado esquerdo do assignment usada como index da variável "+ assignmentNode.left_side_id +
									" não existe!";
							
							Utils.error(error_message);
						}
						else 
						{
							Variable index = function.returnVarById(assignmentNode.lhsArrayIndexId);
							
							if (!index.getType().equals(Constants.SCALAR)) 
							{
								String error_message = "Na função "+function+" a variável usada como index da variável "+
										assignmentNode.left_side_id + " do lado esquerdo do assignment não é do tipo Scalar!";
								
								Utils.error(error_message);
							}
						}
					}
				}
				break;
			
			case "scalar":
				
				if(!assignmentNode.rhs1Access.equals(Constants.ARRAY_SIZE))
				{
					Variable var = function.returnVarById(assignmentNode.left_side_id);
					
					if(var.getType().equals(Constants.ARRAY))
					{
						String error_message = "Na função "+function+" a variável "+assignmentNode.left_side_id+
								" do lado esquerdo do assignment não é do tipo Scalar!";
						
						Utils.error(error_message);
					}
				}
				
				break;
			}

		}
		
		//RIGTH SIDE 1
		
		if(assignmentNode.rhs1Access.equals(Constants.ARRAY_SIZE))
		{
			if(!new_var)
			{
				Variable lhsVar = function.returnVarById(assignmentNode.left_side_id);
				
				if(!lhsVar.getType().equals(Constants.ARRAY))
				{
					String error_message = "Na função "+function+" a variável "+assignmentNode.left_side_id+
							" do lado esquerdo do assignment não é do tipo Array!";
					
					Utils.error(error_message);
				}
			}
		}
		else if (!assignmentNode.rhs1Access.equals(Constants.INTEGER_ACCESS) && !assignmentNode.rhs1Access.equals(Constants.CALL)) 
		{
			if (!function.findVariable(assignmentNode.righ_side_1_id))
			{
				String error_message = "Na função "+function+" a variável "+assignmentNode.righ_side_1_id+
						" do lado direito do assignment da variável " +assignmentNode.left_side_id+
						" não existe!";
				
				Utils.error(error_message);
			}
			else 
			{
				assignmentNode.rhs1Type = function.returnVarById(assignmentNode.righ_side_1_id).getType();
				
				if (assignmentNode.rhs1Access.equals(Constants.ARRAY_ACCESS)) 
				{
					if (assignmentNode.rhs1ArrayIndexId != null && !assignmentNode.rhs1ArrayAccess.equals(Constants.INTEGER_ACCESS))
					{
						if (!function.findVariable(assignmentNode.rhs1ArrayIndexId)) 
						{
							String error_message = "Na função "+function+" a variável "+assignmentNode.rhs1ArrayIndexId+
									" do lado direito do assignment da variável "+assignmentNode.left_side_id+
									" não existe!";
							
							Utils.error(error_message);
						} 
						else 
						{
							Variable v = function.returnVarById(assignmentNode.rhs1ArrayIndexId);
							
							if (!v.getType().equals(Constants.SCALAR)) 
							{
								String error_message = "Na função "+function+" o index usado para a variável "+
										assignmentNode.righ_side_1_id+ " do lado direito do assignment da variável "+
										assignmentNode.left_side_id+" não é um Scalar!";
								
								Utils.error(error_message);
							}
						}
					}
				} 
				else if (assignmentNode.rhs1Access.equals(Constants.SCALAR_ACCESS)) 
				{
					if (assignmentNode.rhs1Type.equals(Constants.ARRAY)) 
					{
						String error_message = "Na função "+function+" a variável "+
								assignmentNode.righ_side_1_id+ " do lado direito do assignment da variável "+
								assignmentNode.left_side_id+" não é um Scalar!";
						
						Utils.error(error_message);
					}
				} 
				else if (assignmentNode.rhs1Access.equals(Constants.SIZE_ACCESS)) 
				{
					Variable v = function.returnVarById(assignmentNode.righ_side_1_id);
					
					if (!v.getType().equals(Constants.ARRAY)) 
					{
						String error_message = "Na função "+function+" a variável "+
								assignmentNode.righ_side_1_id+ " do lado direito do assignment da variável "+
								assignmentNode.left_side_id+" não é um Array!";
						
						Utils.error(error_message);
					}
				}
			}
		}
		else if(assignmentNode.rhs1Access.equals(Constants.CALL))
		{
			if (!Utils.isArrayOrFunctionAccess(assignmentNode.righ_side_1_id))
			{
				if(!YAL2JVM.getModule().findFunctioByName(assignmentNode.righ_side_1_id))
				{
					String error_message = "Na função "+function+" a chamada à função "+assignmentNode.righ_side_1_id+
							" do lado direito do assignment da variável "+assignmentNode.left_side_id+" não pertence ao módulo!";
					
					Utils.error(error_message);
				}
				else
				{
					String declaration = Utils.buildFunctionDeclaration(function, assignmentNode.righ_side_1_id, assignmentNode.right_side_1_args_id,assignmentNode.left_side_id);
					
					System.out.println(declaration);
					
					if(!YAL2JVM.getModule().functionExists(declaration))
					{
						String error_message = "Na função "+function+" os argumentos da chamada à função "+
								assignmentNode.righ_side_1_id + " do lado direito do assignment da variável "+assignmentNode.left_side_id+
								" não correspondem aos argumentos da função com o mesmo nome!";
						
						Utils.error(error_message);
					}
					else
					{
						assignmentNode.right_side_1_function = YAL2JVM.getModule().getFunctionByID(declaration);
					}
				}
			}
			else
			{
				assignmentNode.other_module_1 = true;
			}
		}
		
		//RIGHT SIDE 2
		
		if (assignmentNode.isOperation)
		{
			if (!assignmentNode.rhs2Access.equals(Constants.INTEGER_ACCESS) && !assignmentNode.rhs1Access.equals(Constants.CALL)) 
			{
				if (!function.findVariable(assignmentNode.righ_side_2_id))
				{
					String error_message = "Na função "+function+" a variável "+assignmentNode.righ_side_2_id+
							" do lado direito do assignment da variável " +assignmentNode.left_side_id+
							" não existe!";
					
					Utils.error(error_message);
				} 
				else 
				{
					assignmentNode.rhs2Type = function.returnVarById(assignmentNode.righ_side_2_id).getType();
					
					if (assignmentNode.rhs2Access.equals("array"))
					{
						if (assignmentNode.rhs2ArrayIndexId != null && !assignmentNode.rhs2ArrayAccess.equals(Constants.INTEGER_ACCESS))
						{
							if (!function.findVariable(assignmentNode.rhs2ArrayIndexId)) 
							{
								String error_message = "Na função "+function+" a variável "+assignmentNode.rhs2ArrayIndexId+
										" do lado direito do assignment da variável "+assignmentNode.left_side_id+
										" não existe!";
								
								Utils.error(error_message);
							} 
							else 
							{
								Variable v = function.returnVarById(assignmentNode.rhs2ArrayIndexId);
								
								if (!v.getType().equals(Constants.SCALAR))
								{
									String error_message = "Na função "+function+" o index usado para a variável "+
											assignmentNode.righ_side_2_id+ " do lado direito do assignment da variável "+
											assignmentNode.left_side_id+" não é um Scalar!";
									
									Utils.error(error_message);
								}
							}
						}
					} 
					else if (assignmentNode.rhs2Access.equals(Constants.SCALAR_ACCESS)) 
					{
						if (assignmentNode.rhs2Type.equals(Constants.ARRAY))
						{
							String error_message = "Na função "+function+" a variável "+
									assignmentNode.righ_side_2_id+ " do lado direito do assignment da variável "+
									assignmentNode.left_side_id+" não é um Scalar!";
							
							Utils.error(error_message);
						}
					} 
					else if (assignmentNode.rhs2Access.equals(Constants.SIZE_ACCESS))
					{
						Variable v = function.returnVarById(assignmentNode.righ_side_2_id);
						
						if (!v.getType().equals(Constants.ARRAY)) 
						{	
							String error_message = "Na função "+function+" a variável "+
									assignmentNode.righ_side_2_id+ " do lado direito do assignment da variável "+
									assignmentNode.left_side_id+" não é um Array!";
							
							Utils.error(error_message);
						}
					}
				}
			}
			else if(assignmentNode.rhs2Access.equals(Constants.CALL))
			{
				if (!Utils.isArrayOrFunctionAccess(assignmentNode.righ_side_2_id))
				{
					if(!YAL2JVM.getModule().findFunctioByName(assignmentNode.righ_side_2_id))
					{
						String error_message = "Na função "+function+" a chamada à função "+assignmentNode.righ_side_2_id+
								" do lado direito do assignment da variável "+assignmentNode.left_side_id+" não pertence ao módulo!";
						
						Utils.error(error_message);
					}
					else
					{
						String declaration = Utils.buildFunctionDeclaration(function, assignmentNode.righ_side_2_id, assignmentNode.right_side_2_args_id,assignmentNode.left_side_id);
						
						System.out.println(declaration);
						
						if(!YAL2JVM.getModule().functionExists(declaration))
						{
							String error_message = "Na função "+function+" os argumentos da chamada à função "+
									assignmentNode.righ_side_2_id + " do lado direito do assignment da variável "+assignmentNode.left_side_id+
									" não correspondem aos argumentos da função com o mesmo nome!";
							
							Utils.error(error_message);
						}
						else
						{
							assignmentNode.right_side_1_function = YAL2JVM.getModule().getFunctionByID(declaration);
						}
					}
				}
			}
			else
			{
				assignmentNode.other_module_2 = true;
			}
		}
		
		if(new_var)
		{
			function.addVariable(assignmentNode.rhs1Access, assignmentNode.left_side_id, assignmentNode.righ_side_1_id);
		}
	}

	public AST analyseFunctionBuildAST(Function function, AST root, Node[] children)
	{
		if(children == null)
		{
			return root;
		}
		
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
