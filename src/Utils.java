import java.util.ArrayList;

public class Utils 
{
	//io.print("")
	public static boolean isArrayOrFunctionAccess(String id) 
	{
		for (int i = 0; i < id.length(); i++)
		{
			if (id.charAt(i) == '.')
			{
				return true;
			}
		}
		return false;
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
			}
			
			if(returnId == YAL2JVMTreeConstants.JJTSCALAR)
			{
				newVar = new Scalar(retVarNode.ID);
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
				new_scalar.setType("scalar");
				arguments.add(new_scalar);
			}
			else if(n.getOriginalId() == YAL2JVMTreeConstants.JJTARRAY)
			{
				Array new_array = new Array(n.ID);
				new_array.setType("array");
				arguments.add(new_array);
			}
			if(checkIfExistArray(aux,n.ID))
			{
				System.out.println("A função " + functionId + " tem o argumento " + n.ID + " mais que uma vez");
			}
			else if(returnVar != null && n.ID.equals(returnVar.getVariableID()))
			{
				System.out.println("A função " + functionId + " tem o argumento " + n.ID + " com o mesmo nome da variável de retorno");
			}
			else 
			{
				aux.add(n.ID);
			}
		}
		return arguments;
	}
	
	public static boolean checkIfExistArray(ArrayList<String> array, String elem)
	{
		for(int i=0; i<array.size();i++)
		{
			if(array.get(i).equals(elem))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCall(String call)
	{
		for (int i = 0; i < call.length(); i++)
		{
			if (call.charAt(i) == '.')
			{
				return true;
			}
		}
		return false;
	}
}
