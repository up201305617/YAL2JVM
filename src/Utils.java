import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
	
	public static String splitByDot(String string, boolean isFunction)
	{
		String temp = null;

		for (int i = string.length()-1; i >= 0; i--) 
		{
			if(isFunction)
			{
				if(string.charAt(i) == '.')
				{
					temp = string.substring(i+1, string.length());
				}
			}
			else
			{
				if(string.charAt(i) == '.')
				{
					temp = string.substring(0, i);
				}
			}
			
		}
		return temp;
	}
	
	public static void createLogFile(String moduleName) throws FileNotFoundException, UnsupportedEncodingException
	{
		File folder = new File("./log");
		folder.mkdirs();
		String fileName = moduleName+"_log.log";
		File file = new File(folder,fileName);
		PrintWriter write = new PrintWriter(file,"UTF-8");
		
		if(errorsArray.size()!=0)
		{
			if(errorsArray.size()==1)
			{
				write.println("Foi encontrado 1 erro no módulo "+moduleName+".");
			}
			else
			{
				write.println("Foram encontrados "+errorsArray.size()+" erros no módulo "+moduleName+".");
			}
			
			for(int i = 0;i<errorsArray.size();i++)
			{
				write.println(errorsArray.get(i));
			}
		}
		else
		{
			write.println("Não foram encontrados erros no módulo "+moduleName+".");
		}
	
		write.close();
	}
}
