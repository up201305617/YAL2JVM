import java.util.ArrayList;

public class GetFromSimpleNode 
{
	public static void getGlobalVariables(SimpleNode root, Module m)
	{
		for (int i = 0; i < root.getChildren().length; i++) 
		{
			SimpleNode node = (SimpleNode) root.jjtGetChild(i);

			if (node.id == YAL2JVMTreeConstants.JJTFUNCTION) 
				break;
			
			if (node.id == YAL2JVMTreeConstants.JJTDECLARATION) 
			{ 
				int numGlobalVariables = node.jjtGetNumChildren();
				SimpleNode left_side = (SimpleNode) node.jjtGetChild(0);
				String varName = left_side.ID;
				Variable var = new Variable(varName);

				if (numGlobalVariables == 1) 
				{
					var = new Scalar(varName);
					var.setAssign(false);
					var.setType(Constants.SCALAR);
					
					if (!m.addGlobalVariableToModule(var))
					{
						String error_message = "No m�dulo "+m.getModuleID()+" o atributo "+varName+" j� foi declarada.";
						Utils.error(error_message);
					}
				} 
				if (numGlobalVariables == 2) 
				{
					SimpleNode right_side = (SimpleNode) node.jjtGetChild(1);
					int rhsChildrenNum = right_side.jjtGetNumChildren();

					if(rhsChildrenNum == 0)
					{
						int value = Integer.parseInt(right_side.ID);
						var = new Scalar(varName, value);
						var.setAssign(true);
						var.setType(Constants.SCALAR);
					} 
					
					if(rhsChildrenNum == 1)
					{
						SimpleNode arraySize = (SimpleNode) right_side.jjtGetChild(0);
						int size = Integer.parseInt(arraySize.ID);
						var = new Array(varName, size);
						var.setAssign(true);
						var.setType(Constants.ARRAY);
					}
					
					if (!m.addGlobalVariableToModule(var)) 
					{
						String error_message = "No m�dulo "+m.getModuleID()+" o atributo "+varName+" n�o pode ser redefenida.";
						Utils.error(error_message);
					}
				}
			}
		}
	}
	
	public static void getFunctions(SimpleNode root, Module m) 
	{
		int numChildrenRoot = root.jjtGetNumChildren();
		for (int i = 0; i < numChildrenRoot; i++) 
		{
			SimpleNode node = (SimpleNode)root.jjtGetChild(i);
			if(node.getOriginalId() == YAL2JVMTreeConstants.JJTFUNCTION)
			{
				ArrayList<Variable> arguments = new ArrayList<>();
				Variable returnValue = null;
				int numChildrenNode = node.jjtGetNumChildren();
				
				for (int j = 0; j < numChildrenNode; j++)
				{
					SimpleNode n = (SimpleNode)node.jjtGetChild(j);
					int id = n.getOriginalId();
					if(id == YAL2JVMTreeConstants.JJTRETURN)
					{
						returnValue = getReturnValue(n);
					}
					else if (id == YAL2JVMTreeConstants.JJTVARLIST)
					{
						arguments = getArguments(n,node.ID, returnValue);
					}
				}
				
				String name = node.ID;
				Function f = new Function(name,returnValue,arguments,(SimpleNode)node.jjtGetChild(node.jjtGetNumChildren()-1));
				m.addFunction(f);
			}
		}
	}
	
	public static Variable getReturnValue(SimpleNode n)
	{
		Variable newVar = null;
		
		if(n.getOriginalId() == YAL2JVMTreeConstants.JJTRETURN)
		{
			SimpleNode retVarNode = (SimpleNode)n.jjtGetChild(0);
			int returnId = retVarNode.getOriginalId();
			
			if(returnId == YAL2JVMTreeConstants.JJTARRAY)
			{
				newVar = new Array(retVarNode.ID);
				newVar.setType("Array");
			}
			
			if(returnId == YAL2JVMTreeConstants.JJTSCALAR)
			{
				newVar = new Scalar(retVarNode.ID);
				newVar.setType("Scalar");
			}
		}
		
		return newVar;
	}
	
	public static ArrayList<Variable> getArguments(SimpleNode node, String functionId, Variable returnVar)
	{
		ArrayList<Variable> arguments = new ArrayList<>();
		ArrayList<String> aux = new ArrayList<String>();
		int numChildren = node.jjtGetNumChildren();
		
		for (int i = 0; i < numChildren; i++) 
		{
			SimpleNode n = (SimpleNode)node.jjtGetChild(i);
			if(n.getOriginalId() == YAL2JVMTreeConstants.JJTSCALAR)
			{
				Scalar new_scalar = new Scalar(n.ID);
				new_scalar.setType("Scalar");
				arguments.add(new_scalar);
			}
			else if(n.getOriginalId() == YAL2JVMTreeConstants.JJTARRAY)
			{
				Array new_array = new Array(n.ID);
				new_array.setType("Array");
				arguments.add(new_array);
			}
			if(Utils.checkIfExistArray(aux,n.ID))
			{
				String error_message = "A fun��o " + functionId + " tem o argumento " + n.ID + " mais que uma vez.";
				Utils.error(error_message);
			}
			else if(returnVar != null && n.ID.equals(returnVar.getVariableID()))
			{
				String error_message = "A fun��o " + functionId + " tem o argumento " + n.ID + " com o mesmo nome da vari�vel de retorno.";
				Utils.error(error_message);
			}
			else 
			{
				aux.add(n.ID);
			}
		}
		return arguments;
	}
}