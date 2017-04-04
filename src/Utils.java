import java.util.ArrayList;

public class Utils 
{
	public static boolean dot(String id) 
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
		int num = root.jjtGetNumChildren();
		for (int i = 0; i < num; i++) 
		{
			SimpleNode node = (SimpleNode)root.jjtGetChild(i);
			if(node.getOriginalId() == YAL2JVMTreeConstants.JJTFUNCTION)
			{
				ArrayList<Variable> arguments = new ArrayList<>();
				Variable returnValue = null;
				int num2 = node.jjtGetNumChildren();
				for (int j = 0; j < num2; j++)
				{
					SimpleNode n = (SimpleNode)node.jjtGetChild(j);
					int id = n.getOriginalId();
					if(id == YAL2JVMTreeConstants.JJTRETURN)
					{
						returnValue = getReturnValue(n);
					}
					else if (id == YAL2JVMTreeConstants.JJTARGUMENT)
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
		Variable var = null;
		if(n.getOriginalId() == YAL2JVMTreeConstants.JJTRETURN)
		{
			SimpleNode retVarNode = (SimpleNode)n.jjtGetChild(0);
			int returnId = retVarNode.getOriginalId();
			if(returnId == YAL2JVMTreeConstants.JJTARRAY)
			{
				var = new Array(retVarNode.ID);
			}
			else if(returnId == YAL2JVMTreeConstants.JJTSCALAR)
			{
				var = new Scalar(retVarNode.ID);
			}
		}
		return var;
	}
	
	public static ArrayList<Variable> getArguments(SimpleNode node, String functionId, Variable returnVar)
	{
		ArrayList<Variable> arguments = new ArrayList<>();
		ArrayList<String> aux = new ArrayList<String>();
		int num = node.jjtGetNumChildren();
		
		for (int i = 0; i < num; i++) 
		{
			SimpleNode n = (SimpleNode)node.jjtGetChild(i);
			if(n.getOriginalId() == YAL2JVMTreeConstants.JJTSCALAR)
			{
				arguments.add(new Scalar(n.ID));
			}
			else if(n.getOriginalId() == YAL2JVMTreeConstants.JJTARRAY)
			{
				arguments.add(new Array(n.ID));
			}
			if(checkIfExistArray(aux,n.ID))
			{
				System.out.println("Function " + functionId + " has argument " + n.ID + " duplicated");
			}
			else if(returnVar != null && n.ID.equals(returnVar.getVariableID()))
			{
				System.out.println("Function " + functionId + " has argument " + n.ID + " with the same name as the return variable");
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
}
