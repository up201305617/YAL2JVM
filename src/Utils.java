import java.util.ArrayList;

public class Utils 
{
	private static ArrayList<String> errorsArray = new ArrayList<String>();
	
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
	
	public static void error(String message)
	{
		YAL2JVM.errorFound();
		YAL2JVM.incErrors();
		errorsArray.add(message);
		System.out.println(message);
	}
	
	public static String buildFunctionDeclaration(Function function, String name, ArrayList<String> args, String varId)
	{
		String declaration = name;
		declaration += "(";
		
		for (int i = 0; i < args.size(); i++)
		{
			String arg = args.get(i);
			
			try
			{
				Integer.parseInt(arg);
				declaration += " scalar";
			} 
			catch (NumberFormatException e) 
			{
				Variable var = function.returnVarById(arg);
				
				if(var == null)
				{
					String error_message = "Na função "+function+" o argumento "+arg+
							" do lado direito da atribuição da variavel "+varId+" não existe!";

					Utils.error(error_message);
				}
				else
				{
					if(var.getType().equals(Constants.SCALAR))
					{
						declaration += " scalar";
					}
					else if(var.getType().equals(Constants.ARRAY))
					{
						declaration += " array";
					}
				}
			}
		}
		
		declaration += ")";
		
		return declaration;
	}
	
	public static String splitByArgumets(String string)
	{
		String name = "";
		
		for (int i = 0; i < string.length(); i++) 
		{
			if(string.charAt(i) == '(')
				break;
			else
				name += string.charAt(i);
		}
		
		return name;
	}
	
	public static String splitByDotModule(String string)
	{
		String module = null;
		
		for (int i = string.length()-1; i >= 0; i--) 
		{
			if(string.charAt(i) == '.')
			{
				module = string.substring(0, i);
			}
		}
		return module;
	}
	
	public static String splitByDotFunction(String string)
	{
		String function = null;
		
		for (int i = string.length()-1; i >= 0; i--) 
		{
			if(string.charAt(i) == '.')
			{
				function = string.substring(i+1, string.length());
			}
		}
		return function;
	}
}
