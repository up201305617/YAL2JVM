public class SemanticAnalysis
{
	private SimpleNode sn;
	
	public SemanticAnalysis(SimpleNode sn)
	{
		this.sn = sn;
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
						String error_message = "Existe mais que uma variável global com o nome " + name+".";
						Utils.error(error_message);
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
				String error_message = "Na função "+function.getFunctionId() + " a variavel "+array+" não é um array.";
				Utils.error(error_message);
			}
		}
		
		else if (function.isLocalVariable(array)) 
		{
			if (function.getVariableById(array) instanceof Scalar)
			{
				String error_message = "Na função "+function.getFunctionId() + " a variavel "+array+" não é um array.";
				Utils.error(error_message);
			}
		} 
		
		else if (function.checkArguments(array))
		{
			if (function.getArgumentsById(array) instanceof Scalar)
			{
				String error_message = "Na função "+function.getFunctionId() + " a variavel "+array+" não é um array.";
				Utils.error(error_message);
			}
		}  
		
		else if (function.isReturnValue(array)) 
		{
			if (function.getReturnValue() instanceof Scalar)
			{
				String error_message = "Na função "+function.getFunctionId() + " a variavel "+array+" não é um array.";
				Utils.error(error_message);
			}
		} 
		else 
		{
			String error_message = "Na função "+function.getFunctionId() + " a variavel "+array+" ainda não foi declarada.";
			Utils.error(error_message);
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
					String error_message = "Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar.";
					Utils.error(error_message);
				}
			}  
			else if (function.isLocalVariable(index)) 
			{
				if (function.getVariableById(index) instanceof Array)
				{
					String error_message = "Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar.";
					Utils.error(error_message);
				}
			} 
			else if (function.checkArguments(index)) 
			{
				if (function.getArgumentsById(index) instanceof Array)
				{
					String error_message = "Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar.";
					Utils.error(error_message);
				}
			}  
			else if (function.isReturnValue(index))
			{
				if (function.getReturnValue() instanceof Array)
				{
					String error_message = "Na função "+function.getFunctionId() +" para aceder ao array " + index + " deve usar um scalar.";
					Utils.error(error_message);
				}
			} 
			else 
			{
				String error_message = "Na função "+function.getFunctionId() + " a variavel "+index+" ainda não foi declarada.";
				Utils.error(error_message);
			}
		}
	}
	
	public void analyseScalarAccess(String scalarAccess, Function function) 
	{
		String scalar;

		if (Utils.isArrayOrFunctionAccess(scalarAccess)) 
		{
			scalar = scalarAccess.split("\\.")[0];

			if (YAL2JVM.getModule().isGlobalVariable(scalar)) 
			{
				if (YAL2JVM.getModule().getGlobalVariableById(scalar) instanceof Scalar)
				{
					String error_message = "Na função "+function.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho.";
					Utils.error(error_message);
				}
			}  
			else if (function.isLocalVariable(scalar)) 
			{
				if (function.getVariableById(scalar) instanceof Scalar)
				{
					String error_message = "Na função "+function.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho.";
					Utils.error(error_message);
				}
			} 
			else if (function.checkArguments(scalar)) 
			{
				if (function.getArgumentsById(scalar) instanceof Scalar)
				{
					String error_message = "Na função "+function.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho.";
					Utils.error(error_message);
				}
			} 
			else if (function.isReturnValue(scalar))
			{
				if (function.getReturnValue() instanceof Scalar)
				{
					String error_message = "Na função "+function.getFunctionId()+" a variável "+scalar+" não é um array e não possui o atributo tamanho.";
					Utils.error(error_message);
				}
			} 
			else 
			{
				String error_message = "Na função "+function.getFunctionId() + " a variavel "+scalar+" ainda não foi declarada.";
				Utils.error(error_message);
			}
		} 
		else
		{
			if (YAL2JVM.getModule().isGlobalVariable(scalarAccess)) 
			{
				if (YAL2JVM.getModule().getGlobalVariableById(scalarAccess) instanceof Array)
				{
					String error_message = "Na função "+function.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess+".";
					Utils.error(error_message);
				}
			} 
			else if (function.isLocalVariable(scalarAccess)) 
			{
				if (function.getVariableById(scalarAccess) instanceof Array)
				{
					String error_message = "Na função "+function.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess+".";
					Utils.error(error_message);
				}
			} 
			else if (function.checkArguments(scalarAccess))
			{
				if (function.getArgumentsById(scalarAccess) instanceof Array)
				{
					String error_message = "Na função "+function.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess+".";
					Utils.error(error_message);
				}
			} 
			else if (function.isReturnValue(scalarAccess))
			{
				if (function.getReturnValue() instanceof Array)
				{
					String error_message = "Na função "+function.getFunctionId()+" é necessário um index para aceder à variavél "+scalarAccess+".";
					Utils.error(error_message);
				}
			}
			else 
			{
				String error_message = "Na função "+function.getFunctionId() + " a variavel "+scalarAccess+" ainda não foi declarada.";
				Utils.error(error_message);
			}
		}
	}
	
	public String analyseFunctionCall(Node[] args, Function function, String name)
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
						analyseScalarAccess(var, function);
					} 
					else 
					{
						temp += " array";
						analyseArrayAccess(var, "0", function);
					}
				}  
				else if (function.isLocalVariable(var)) 
				{
					if (function.getVariableById(var) instanceof Scalar)
					{
						temp += " scalar";
						analyseScalarAccess(var, function);
					} 
					else 
					{
						temp += " array";
						analyseArrayAccess(var, "0", function);
					}
				}  
				else if (function.checkArguments(var)) 
				{
					if (function.getArgumentsById(var) instanceof Scalar) 
					{
						temp += " scalar";
						analyseScalarAccess(var, function);
					} 
					else 
					{
						temp += " array";
						analyseArrayAccess(var, "0", function);
					}
				} 
				else if (function.isReturnValue(var)) 
				{
					if (function.getReturnValue() instanceof Scalar) 
					{
						temp += " scalar";
						analyseScalarAccess(var, function);
					} 
					else 
					{
						temp += " array";
						analyseArrayAccess(var, "0", function);
					}
				} 
				else 
				{
					temp += " " + var;
					String error_message = "Na função "+function.getFunctionDeclaration()+"a variável "+var+" ainda não foi declarada.";
					Utils.error(error_message);
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
			conditionNode.left_side.id = left_side.ID;
			conditionNode.left_side.access = "array";
			
			try
			{
				Integer.parseInt(index.ID);
				conditionNode.left_side.array_access_type = "integer";
			}
			catch(NumberFormatException e)
			{
				conditionNode.left_side.array_access_type = "scalar";
			}
			
			conditionNode.left_side.array_index = index.ID;
			
			analyseArrayAccess(left_side.ID, index.ID, function);
		}
		
		if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
		{
			conditionNode.left_side.id = left_side.ID;
			conditionNode.left_side.access = "scalar";
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
					SimpleNode term = (SimpleNode)right_side_child.jjtGetChild(0);

					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTCALL)
					{
						conditionNode.right_side_1.access = "call";
						conditionNode.right_side_1.id = term.ID;
						
						if (!Utils.isArrayOrFunctionAccess(term.ID))
						{
							String aux_0 = analyseFunctionCall(term.getChildren(),function,term.ID);
							conditionNode.right_side_1.function = YAL2JVM.getModule().getFunctionByID(aux_0);
						} 
						else
						{
							conditionNode.right_side_1.other_module = true;
						}
						
						for (int i = 0; i < term.jjtGetNumChildren(); i++)
						{
							conditionNode.right_side_1.args_id.add(i, ((SimpleNode)term.jjtGetChild(i)).ID);
						}
						
						analyseCall(term.ID, term.getChildren(), true, function);
						
					} 
					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
					{
						SimpleNode index = (SimpleNode)term.jjtGetChild(0);
						
						conditionNode.right_side_1.id = term.ID;
						conditionNode.right_side_1.access = "array";
						try
						{
							Integer.parseInt(index.ID);
							conditionNode.right_side_1.array_access_type = "integer";
						}
						catch(NumberFormatException e)
						{
							conditionNode.right_side_1.array_access_type = "scalar";
						}
						conditionNode.right_side_1.array_index = index.ID;
						
						analyseArrayAccess(term.ID, index.ID, function);
					} 
					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS) 
					{
						if(Utils.isArrayOrFunctionAccess(term.ID))
						{
							conditionNode.right_side_1.access = Constants.SIZE_ACCESS;
							conditionNode.right_side_1.id = term.ID.split("\\.")[0];
							System.out.println("size");
						}
						else
						{
							conditionNode.right_side_1.access = "scalar";
							conditionNode.right_side_1.id = term.ID;
							System.out.println(term.ID);
						}
						
						//conditionNode.right_side_1.id = term.ID;
						//conditionNode.right_side_1.access = "scalar";
						
						analyseScalarAccess(term.ID, function);
					}
				} 
				else 
				{
					conditionNode.right_side_1.id = right_side_child.ID;
					conditionNode.right_side_1.access = "integer";
				}
			} 
		}
		else if (right_side.jjtGetNumChildren() == 2) 
		{
			conditionNode.isOperation = true;
			
			SimpleNode left_term = (SimpleNode)right_side.jjtGetChild(0);
			SimpleNode right_term = (SimpleNode)right_side.jjtGetChild(1);

			if (left_term.ID == null) 
			{
				SimpleNode child = (SimpleNode)left_term.jjtGetChild(0);

				if (child.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
				{
					conditionNode.right_side_1.access = "call";
					conditionNode.right_side_1.id = child.ID;
					
					if (!Utils.isArrayOrFunctionAccess(child.ID)) 
					{
						String aux_1 = analyseFunctionCall(child.getChildren(),function,child.ID);
						conditionNode.right_side_1.function = YAL2JVM.getModule().getFunctionByID(aux_1);
					} 
					else 
					{
						conditionNode.right_side_1.other_module = true;
					}
					
					for (int i = 0; i < child.jjtGetNumChildren(); i++)
					{
						conditionNode.right_side_1.args_id.add(i, ((SimpleNode)child.jjtGetChild(i)).ID);
					}
					
					analyseCall(child.ID, child.getChildren(), true, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					SimpleNode index = (SimpleNode)child.jjtGetChild(0);
					
					conditionNode.right_side_1.id = child.ID;
					conditionNode.right_side_1.access = "array";
					
					try
					{
						Integer.parseInt(index.ID);
						conditionNode.right_side_1.array_access_type = "integer";
					}
					catch(NumberFormatException e)
					{
						conditionNode.right_side_1.array_access_type = "scalar";
					}
					conditionNode.right_side_1.array_index = index.ID;
					
					analyseArrayAccess(child.ID, index.ID, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS) 
				{
					conditionNode.right_side_1.id = child.ID;
					conditionNode.right_side_1.access = "scalar";
					
					analyseScalarAccess(child.ID, function);
				}
			} 
			else
			{
				conditionNode.right_side_1.id = left_term.ID;
				conditionNode.right_side_1.access = "integer";
			}

			if (right_term.ID == null) 
			{
				SimpleNode child = (SimpleNode)right_term.jjtGetChild(0);

				if (child.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
				{
					conditionNode.right_side_2.access = "call";
					conditionNode.right_side_2.id = child.ID;
					
					if (!Utils.isArrayOrFunctionAccess(child.ID)) 
					{
						String aux_2 = analyseFunctionCall(child.getChildren(),function,child.ID);
						conditionNode.right_side_2.function = YAL2JVM.getModule().getFunctionByID(aux_2);
					} 
					else 
					{
						conditionNode.right_side_2.other_module = true;
					}
					
					for (int i = 0; i < child.jjtGetNumChildren(); i++)
					{
						conditionNode.right_side_2.args_id.add(i, ((SimpleNode)child.jjtGetChild(i)).ID);
					}
					
					analyseCall(child.ID, child.getChildren(), true, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					SimpleNode index = (SimpleNode)child.jjtGetChild(0);
					
					conditionNode.right_side_2.id = child.ID;
					conditionNode.right_side_2.access = "array";
					
					try
					{
						Integer.parseInt(index.ID);
						conditionNode.right_side_2.array_access_type = "integer";
					}
					catch(NumberFormatException e)
					{
						conditionNode.right_side_2.array_access_type = "scalar";
					}
					
					conditionNode.right_side_2.array_index = index.ID;
					
					analyseArrayAccess(child.ID, index.ID, function);
				} 
				else if (child.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
				{
					conditionNode.right_side_2.id = child.ID;
					conditionNode.right_side_2.access = "scalar";
					
					analyseScalarAccess(child.ID, function);
				}
			} 
			else 
			{
				conditionNode.right_side_2.id = right_term.ID;
				conditionNode.right_side_2.access = "integer";
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
							String error_message = "A função "+declaration+" retorna um array.";
							Utils.error(error_message);	
						}
					} 
					else
					{
						String error_message = "A função "+declaration+" não retorna nenhuma variável.";
						Utils.error(error_message);
					}
				}
			} 
			else
			{
				String error_message = "A função "+declaration+" não existe.";
				Utils.error(error_message);
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
		
		callFunction.call.args= arguments;
		callFunction.call.other_module = dot;
		callFunction.call.functionName = call;
		callFunction.call.functionDeclaration = declaration;
		
		return callFunction;
	}
	
	public AST analyseAssignment(SimpleNode left_side, SimpleNode right_side, Function function) 
	{
		AST assignmentNode = new AST(Constants.ASSIGNMENT,function);		
		
		boolean isOperation = false;
		
		//ANALYSE LEFT SIDE
		if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
		{
			assignmentNode.left_side.access = "array";
			assignmentNode.left_side.id = left_side.ID;
			
			SimpleNode arrayIndex = (SimpleNode)(left_side.jjtGetChild(0));
			
			try
			{
				Integer.parseInt(arrayIndex.ID);
				assignmentNode.left_side.array_index = arrayIndex.ID;
				assignmentNode.left_side.array_access_type = "integer";
			} 
			catch (NumberFormatException e)
			{
				assignmentNode.left_side.array_index  = arrayIndex.ID;
				assignmentNode.left_side.array_access_type = "scalar";
			}
		} 
		else if(left_side.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
		{
			if(Utils.isArrayOrFunctionAccess((left_side.ID)))
			{
				assignmentNode.left_side.access = "size";
				assignmentNode.left_side.id = left_side.ID.split("\\.")[0];
			}
			else
			{
				assignmentNode.left_side.access = "scalar";
				assignmentNode.left_side.id = left_side.ID;
			}
		}
		
		//ANALYSE RIGTH SIDE 1
		
		SimpleNode right_side_1 = (SimpleNode)right_side.jjtGetChild(0);
	
		if (right_side_1.getOriginalId() == YAL2JVMTreeConstants.JJTTERM) 
		{
			if (right_side_1.ID != null)
			{
				assignmentNode.right_side_1.id = right_side_1.ID;
				assignmentNode.right_side_1.access = "integer";
			} 
			else 
			{
				SimpleNode term = (SimpleNode)right_side_1.jjtGetChild(0);
				
				if (term.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
				{
					assignmentNode.right_side_1.access = "call";
					assignmentNode.right_side_1.id = term.ID;
					
					for (int i = 0; i < term.jjtGetNumChildren(); i++)
					{
						assignmentNode.right_side_1.args_id.add(((SimpleNode)term.jjtGetChild(i)).ID);
					}
				}
				else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS)
				{
					assignmentNode.right_side_1.access = "array";
					assignmentNode.right_side_1.id = term.ID;
					
					SimpleNode arrayIndex = (SimpleNode)(term.jjtGetChild(0));
					
					try
					{
						Integer.parseInt(arrayIndex.ID);
						assignmentNode.right_side_1.array_index = arrayIndex.ID;
						assignmentNode.right_side_1.array_access_type = "integer";
					}
					catch (NumberFormatException e)
					{
						assignmentNode.right_side_1.array_index = arrayIndex.ID;
						assignmentNode.right_side_1.array_access_type = "scalar";
					}
				}
				else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS) 
				{
					if(Utils.isArrayOrFunctionAccess(term.ID))
					{
						assignmentNode.right_side_1.access = "size";
						assignmentNode.right_side_1.id = term.ID.split("\\.")[0];
					}
					else
					{
						assignmentNode.right_side_1.access = "scalar";
						assignmentNode.right_side_1.id = term.ID;
						System.out.println(term.ID);
					}
				} 
			}
		} 
		else if (right_side_1.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYSIZE) 
		{
			assignmentNode.right_side_1.access = "arraysize";
			
			if(((SimpleNode)right_side_1.children[0]).toString().equals("ScalarAccess"))
			{
				assignmentNode.right_side_1.id = ((SimpleNode)right_side_1.children[0]).ID; 
				assignmentNode.right_side_1.isScalar = true;
			}
			else
			{
				assignmentNode.right_side_1.id = right_side_1.ID;
				assignmentNode.right_side_1.isScalar = false;
			}
		}
		
		//ANALYSE RIGTH SIDE 2
		
		if (right_side.jjtGetNumChildren() == 2) 
		{
			assignmentNode.operation = right_side.ID;
			isOperation = true;
			
			SimpleNode right_side_2 = (SimpleNode)right_side.jjtGetChild(1);
			
			if (right_side_2.getOriginalId() == YAL2JVMTreeConstants.JJTTERM)
			{
				if (right_side_2.ID != null)
				{
					assignmentNode.right_side_2.id = right_side_2.ID;
					assignmentNode.right_side_2.access = "integer";
				}
				else 
				{
					SimpleNode term = (SimpleNode)right_side_2.jjtGetChild(0);
					
					if (term.getOriginalId() == YAL2JVMTreeConstants.JJTCALL) 
					{
						assignmentNode.right_side_2.access = "call";
						assignmentNode.right_side_2.id = term.ID;
						
						for (int i = 0; i < term.jjtGetNumChildren(); i++)
						{
							assignmentNode.right_side_2.args_id.add(((SimpleNode)term.jjtGetChild(i)).ID);
						}
					} 
					else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYACCESS) 
					{
						assignmentNode.right_side_2.access = "array";
						assignmentNode.right_side_2.id = term.ID;
						
						SimpleNode arrayIndex = (SimpleNode)(term.jjtGetChild(0));
						
						try
						{
							Integer.parseInt(arrayIndex.ID);
							assignmentNode.right_side_2.array_index = arrayIndex.ID;
							assignmentNode.right_side_2.array_access_type = "integer";
						} 
						catch (NumberFormatException e) 
						{
							assignmentNode.right_side_2.array_index = arrayIndex.ID;
							assignmentNode.right_side_2.array_access_type = "scalar";
						}
					} 
					else if (term.getOriginalId() == YAL2JVMTreeConstants.JJTSCALARACCESS)
					{
						if(Utils.isArrayOrFunctionAccess(term.ID))
						{
							assignmentNode.right_side_2.access = "size";
							assignmentNode.right_side_2.id = term.ID.split("\\.")[0];
						}
						else
						{
							assignmentNode.right_side_2.access = "scalar";
							assignmentNode.right_side_2.id = term.ID;
						}
					} 
				}
			}
			else if (right_side_2.getOriginalId() == YAL2JVMTreeConstants.JJTARRAYSIZE)
			{
				assignmentNode.right_side_2.access = "arraysize";
				assignmentNode.right_side_2.id = right_side_2.ID;
			}
		}
	
		findErrors(function,assignmentNode,isOperation);
		
		assignmentNode.isOperation = isOperation;
		assignmentNode.left_side.scope = function.getScopes(assignmentNode.left_side.id);
		assignmentNode.right_side_1.scope = function.getScopes(assignmentNode.right_side_1.id);
		assignmentNode.right_side_2.scope = function.getScopes(assignmentNode.right_side_2.id);
		
		return assignmentNode;
	}
	
	public void findErrors(Function function, AST assignmentNode, boolean isOperation)
	{
		//LEFT SIDE
		boolean new_var = false;
		
		if(!function.findVariable(assignmentNode.left_side.id))
		{
			new_var=true;
		}
		else
		{
			Variable v = function.returnVarById(assignmentNode.left_side.id);
			
			switch (assignmentNode.left_side.access) 
			{	
			case "array":
			
				if (!v.getType().equals(Constants.ARRAY))
				{
					String error_message = "Na função "+function+" a variável "+assignmentNode.left_side.access+
							 " do lado esquerdo do assignment não é um array!";
					
					Utils.error(error_message);
				}
				else 
				{
					if (assignmentNode.left_side.array_index != null && !assignmentNode.left_side.array_access_type.equals(Constants.INTEGER_ACCESS))
					{
						if (!function.findVariable(assignmentNode.left_side.array_index))
						{
							String error_message = "Na função "+function+" a variável "+assignmentNode.left_side.array_index+
									" do lado esquerdo do assignment usada como index da variável "+ assignmentNode.left_side.id +
									" não existe!";
							
							Utils.error(error_message);
						}
						else 
						{
							Variable index = function.returnVarById(assignmentNode.left_side.array_index);
							
							if (!index.getType().equals(Constants.SCALAR)) 
							{
								String error_message = "Na função "+function+" a variável usada como index da variável "+
										assignmentNode.left_side.id + " do lado esquerdo do assignment não é do tipo Scalar!";
								
								Utils.error(error_message);
							}
						}
					}
				}
				break;
			
			case "scalar":
				
				if(!assignmentNode.right_side_1.access.equals(Constants.ARRAY_SIZE))
				{
					Variable var = function.returnVarById(assignmentNode.left_side.id);
					
					if(var.getType().equals(Constants.ARRAY))
					{
						String error_message = "Na função "+function+" a variável "+assignmentNode.left_side.id+
								" do lado esquerdo do assignment não é do tipo Scalar!";
						
						Utils.error(error_message);
					}
				}
				
				break;
			}

		}
		
		//RIGTH SIDE 1
		
		if(assignmentNode.right_side_1.access.equals(Constants.ARRAY_SIZE))
		{
			if(!new_var)
			{
				Variable lhsVar = function.returnVarById(assignmentNode.left_side.id);
				
				if(!lhsVar.getType().equals(Constants.ARRAY))
				{
					String error_message = "Na função "+function+" a variável "+assignmentNode.left_side.id+
							" do lado esquerdo do assignment não é do tipo Array!";
					
					Utils.error(error_message);
				}
			}
		}
		else if (!assignmentNode.right_side_1.access.equals(Constants.INTEGER_ACCESS) && !assignmentNode.right_side_1.access.equals(Constants.CALL)) 
		{
			if (!function.findVariable(assignmentNode.right_side_1.id))
			{
				String error_message = "Na função "+function+" a variável "+assignmentNode.right_side_1.id+
						" do lado direito do assignment da variável " +assignmentNode.left_side.id+
						" não existe!";
				
				Utils.error(error_message);
			}
			else 
			{
				assignmentNode.right_side_1.type = function.returnVarById(assignmentNode.right_side_1.id).getType();
				
				if (assignmentNode.right_side_1.access.equals(Constants.ARRAY_ACCESS)) 
				{
					if (assignmentNode.right_side_1.array_index != null && !assignmentNode.right_side_1.array_access_type.equals(Constants.INTEGER_ACCESS))
					{
						if (!function.findVariable(assignmentNode.right_side_1.array_index)) 
						{
							String error_message = "Na função "+function+" a variável "+assignmentNode.right_side_1.array_index+
									" do lado direito do assignment da variável "+assignmentNode.left_side.id+
									" não existe!";
							
							Utils.error(error_message);
						} 
						else 
						{
							Variable v = function.returnVarById(assignmentNode.right_side_1.array_index);
							
							if (!v.getType().equals(Constants.SCALAR)) 
							{
								String error_message = "Na função "+function+" o index usado para a variável "+
										assignmentNode.right_side_1.id+ " do lado direito do assignment da variável "+
										assignmentNode.left_side.id+" não é um Scalar!";
								
								Utils.error(error_message);
							}
						}
					}
				} 
				else if (assignmentNode.right_side_1.access.equals(Constants.SCALAR_ACCESS)) 
				{
					if (assignmentNode.right_side_1.type.equals(Constants.ARRAY)) 
					{
						String error_message = "Na função "+function+" a variável "+
								assignmentNode.right_side_1.id+ " do lado direito do assignment da variável "+
								assignmentNode.left_side.id+" não é um Scalar!";
						
						Utils.error(error_message);
					}
				} 
				else if (assignmentNode.right_side_1.access.equals(Constants.SIZE_ACCESS)) 
				{
					Variable v = function.returnVarById(assignmentNode.right_side_1.id);
					
					if (!v.getType().equals(Constants.ARRAY)) 
					{
						String error_message = "Na função "+function+" a variável "+
								assignmentNode.right_side_1.id+ " do lado direito do assignment da variável "+
								assignmentNode.left_side.id+" não é um Array!";
						
						Utils.error(error_message);
					}
				}
			}
		}
		else if(assignmentNode.right_side_1.access.equals(Constants.CALL))
		{
			if (!Utils.isArrayOrFunctionAccess(assignmentNode.right_side_1.id))
			{
				if(!YAL2JVM.getModule().findFunctioByName(assignmentNode.right_side_1.id))
				{
					String error_message = "Na função "+function+" a chamada à função "+assignmentNode.right_side_1.id+
							" do lado direito do assignment da variável "+assignmentNode.left_side.id+" não pertence ao módulo!";
					
					Utils.error(error_message);
				}
				else
				{
					String declaration = Utils.buildFunctionDeclaration(function, assignmentNode.right_side_1.id, assignmentNode.right_side_1.args_id,assignmentNode.left_side.id);
					
					if(!YAL2JVM.getModule().functionExists(declaration))
					{
						String error_message = "Na função "+function+" os argumentos da chamada à função "+
								assignmentNode.right_side_1.id + " do lado direito do assignment da variável "+assignmentNode.left_side.id+
								" não correspondem aos argumentos da função com o mesmo nome!";
						
						Utils.error(error_message);
					}
					else
					{
						assignmentNode.right_side_1.function = YAL2JVM.getModule().getFunctionByID(declaration);
					}
				}
			}
			else
			{
				assignmentNode.right_side_1.other_module = true;
			}
		}
		
		//RIGHT SIDE 2
		
		if (isOperation)
		{
			if (!assignmentNode.right_side_2.access.equals(Constants.INTEGER_ACCESS) && !assignmentNode.right_side_1.access.equals(Constants.CALL)) 
			{
				if (!function.findVariable(assignmentNode.right_side_2.id))
				{
					String error_message = "Na função "+function+" a variável "+assignmentNode.right_side_2.id+
							" do lado direito do assignment da variável " +assignmentNode.left_side.id+
							" não existe!";
					
					Utils.error(error_message);
				} 
				else 
				{
					assignmentNode.right_side_2.type = function.returnVarById(assignmentNode.right_side_2.id).getType();
					
					if (assignmentNode.right_side_2.access.equals("array"))
					{
						if (assignmentNode.right_side_2.array_index != null && !assignmentNode.right_side_2.array_access_type.equals(Constants.INTEGER_ACCESS))
						{
							if (!function.findVariable(assignmentNode.right_side_2.array_index)) 
							{
								String error_message = "Na função "+function+" a variável "+assignmentNode.right_side_2.array_index+
										" do lado direito do assignment da variável "+assignmentNode.left_side.id+
										" não existe!";
								
								Utils.error(error_message);
							} 
							else 
							{
								Variable v = function.returnVarById(assignmentNode.right_side_2.array_index);
								
								if (!v.getType().equals(Constants.SCALAR))
								{
									String error_message = "Na função "+function+" o index usado para a variável "+
											assignmentNode.right_side_2.id+ " do lado direito do assignment da variável "+
											assignmentNode.left_side.id+" não é um Scalar!";
									
									Utils.error(error_message);
								}
							}
						}
					} 
					else if (assignmentNode.right_side_2.access.equals(Constants.SCALAR_ACCESS)) 
					{
						if (assignmentNode.right_side_2.type.equals(Constants.ARRAY))
						{
							String error_message = "Na função "+function+" a variável "+
									assignmentNode.right_side_2.id+ " do lado direito do assignment da variável "+
									assignmentNode.left_side.id+" não é um Scalar!";
							
							Utils.error(error_message);
						}
					} 
					else if (assignmentNode.right_side_2.access.equals(Constants.SIZE_ACCESS))
					{
						Variable v = function.returnVarById(assignmentNode.right_side_2.id);
						
						if (!v.getType().equals(Constants.ARRAY)) 
						{	
							String error_message = "Na função "+function+" a variável "+
									assignmentNode.right_side_2.id+ " do lado direito do assignment da variável "+
									assignmentNode.left_side.id+" não é um Array!";
							
							Utils.error(error_message);
						}
					}
				}
			}
			else if(assignmentNode.right_side_2.access.equals(Constants.CALL))
			{
				if (!Utils.isArrayOrFunctionAccess(assignmentNode.right_side_2.id))
				{
					if(!YAL2JVM.getModule().findFunctioByName(assignmentNode.right_side_2.id))
					{
						String error_message = "Na função "+function+" a chamada à função "+assignmentNode.right_side_2.id+
								" do lado direito do assignment da variável "+assignmentNode.left_side.id+" não pertence ao módulo!";
						
						Utils.error(error_message);
					}
					else
					{
						String declaration = Utils.buildFunctionDeclaration(function, assignmentNode.right_side_2.id, assignmentNode.right_side_2.args_id,assignmentNode.left_side.id);
						
						if(!YAL2JVM.getModule().functionExists(declaration))
						{
							String error_message = "Na função "+function+" os argumentos da chamada à função "+
									assignmentNode.right_side_2.id + " do lado direito do assignment da variável "+assignmentNode.left_side.id+
									" não correspondem aos argumentos da função com o mesmo nome!";
							
							Utils.error(error_message);
						}
						else
						{
							assignmentNode.right_side_1.function = YAL2JVM.getModule().getFunctionByID(declaration);
						}
					}
				}
			}
			else
			{
				assignmentNode.right_side_2.other_module = true;
			}
		}
		
		if(new_var)
		{
			if(assignmentNode.right_side_1.isScalar == true)
			{
				function.addVariable(assignmentNode.right_side_1.access, assignmentNode.left_side.id, assignmentNode.right_side_1.id,true);
			}
			else
			{
				function.addVariable(assignmentNode.right_side_1.access, assignmentNode.left_side.id, assignmentNode.right_side_1.id,false);
			}	
		}
	}

	public AST analyseFunctionBuildAST(Function function, AST root, Node[] children)
	{
		if(children == null)
		{
			return root;
		}
		
		AST current = root;
		
		for(int i=0; i<children.length;i++)
		{
			SimpleNode child = (SimpleNode) children[i];
			
			switch(child.getOriginalId())
			{
			case YAL2JVMTreeConstants.JJTIF:
				
				SimpleNode if_node = (SimpleNode) child.jjtGetChild(0);
				SimpleNode if_left_side = (SimpleNode) if_node.jjtGetChild(0);
				SimpleNode if_right_side = (SimpleNode) if_node.jjtGetChild(1);
				SimpleNode if_node_body;
				SimpleNode else_node_body;
				
				AST if_cond;
				AST last_if;
				AST end_if;
				AST last_else;
				
				if_cond = analyseCondition(if_left_side, if_right_side, function,"if");
				if_cond.conditional_op = if_node.ID;
				
				current.children.add(if_cond);
				if_cond.parents.add(current);
				
				if_node_body = (SimpleNode) child.jjtGetChild(1);
				last_if = analyseFunctionBuildAST(function,if_cond,if_node_body.getChildren());
				end_if = new AST("endif",function);
				
				if (child.jjtGetNumChildren() == 3) 
				{
					else_node_body = (SimpleNode) child.jjtGetChild(2);
					last_else = analyseFunctionBuildAST(function,if_cond,else_node_body.getChildren());
				
					if(if_cond != last_else && last_if != if_cond)
					{
						end_if.parents.add(last_if);
						last_if.children.add(end_if);
						end_if.parents.add(last_else);
						last_else.children.add(end_if);
					}
				}
				else
				{
					if(last_if != if_cond)
					{
						end_if.parents.add(last_if);
						last_if.children.add(end_if);
					}
					
					end_if.parents.add(if_cond);
					if_cond.children.add(end_if);
				}
				
				current = end_if;
				
				break;
			
			case YAL2JVMTreeConstants.JJTWHILE:
				
				SimpleNode while_node = (SimpleNode) child.jjtGetChild(0);
				SimpleNode while_left_side = (SimpleNode) while_node.jjtGetChild(0);
				SimpleNode while_right_side = (SimpleNode) while_node.jjtGetChild(1);
				
				AST while_ast = analyseCondition(while_left_side, while_right_side, function,Constants.WHILE);
				
				while_ast.conditional_op = while_node.ID;
				current.children.add(while_ast);
				while_ast.parents.add(current);
				current = while_ast;
				
				SimpleNode while_body = (SimpleNode) child.jjtGetChild(1);
				AST last_while = analyseFunctionBuildAST(function,while_ast,while_body.getChildren());
				
				current.parents.add(last_while);
				last_while.children.add(current);
				
				break;
			
			case YAL2JVMTreeConstants.JJTCALL:
				
				AST call = analyseCall(child.ID, child.getChildren(), false, function);
				
				call.parents.add(current);
				current.children.add(call);
				current = call;
				
				break;
			
			case YAL2JVMTreeConstants.JJTASSIGNEMENT:
				
				SimpleNode ass_left_side = (SimpleNode)child.jjtGetChild(0);
				SimpleNode ass_right_side = (SimpleNode)child.jjtGetChild(1); 
				
				AST assignment = analyseAssignment(ass_left_side, ass_right_side, function);
				
				current.children.add(assignment);
				assignment.parents.add(current);
				
				current = assignment;
				
				break;
			}
		}
		
		return current;
		
	}
}
