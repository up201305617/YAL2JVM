import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map.Entry;

public class Generator 
{
	private Module module;
	private PrintWriter write;
	private String moduleName;
	private String newFileName;
	private SimpleNode node;
	
	public Generator(Module m, String name, SimpleNode n)
	{
		this.module = m;
		this.moduleName = name;
		this.newFileName = name+".j";
		this.node = n;
	}
	
	public void initiateGeneration()
	{
		try 
		{
			write = new PrintWriter(this.newFileName,"UTF-8");
			generate();
			write.close();
		} 
		catch (FileNotFoundException | UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void generate()
	{
		generateHeader();
		generateNewLine();
		generateGlobalVariables();
		generateNewLine();
		generateFunctions();
	}
	
	public void generateNewLine()
	{
		write.print("\n");
	}
	
	public void generateHeader()
	{
		write.println(".class public " + this.moduleName);
		write.println(".super java/lang/Object");
	}
	
	@SuppressWarnings("unused")
	public void generateGlobalVariables()
	{
		for(Entry<String,Variable> entry : this.module.getGlobalVariables().entrySet())
		{
			String name = null;
			String type = null;
			String value = null;
			
			if(entry.getValue() instanceof Scalar)
			{
				name = entry.getKey();
				type = Constants.JVM_SCALAR;
				Scalar newScalar = (Scalar) entry.getValue();
				if(newScalar.isAssign())
				{
					value = newScalar.getValue()+"";
				}
			}
			
			if(entry.getValue() instanceof Array)
			{
				name = entry.getKey();
				type = Constants.JVM_ARRAY;
				Array newArray = (Array) entry.getValue();
				if(newArray.isAssign())
				{
					//value = newArray.getSize()+"";
				}
			}
			
			if(value != null && !type.equals(Constants.JVM_ARRAY))
			{
				write.println(".field static "+name+" "+type+" = "+value);
			}
			else
			{
				write.println(".field static "+name+" "+type);
			}
		}
	}
	
	public void generateFunctionDeclaration(Function f)
	{
		ArrayList<Variable> arguments = f.getArguments();
		Variable returnValue = f.getReturnValue();
		
		if(f.getFunctionId().equals("main"))
		{
			write.println(".method public static main([Ljava/lang/String;)V");
		}
		else
		{
			write.print(".method public static " + f.getFunctionId() + "(");
			
			for (int i = 0; i < arguments.size(); i++)
			{
				if(arguments.get(i).getType().equals(Constants.SCALAR))
				{
					write.print("I");
				}
				if(arguments.get(i).getType().equals(Constants.ARRAY)) 
				{
					write.print("[I");
				}
			}
			
			write.print(")");
			
			if(returnValue != null)
			{
				if(returnValue.getType().equals(Constants.SCALAR))
				{
					write.println("I");
				}
				if(returnValue.getType().equals(Constants.ARRAY))
				{
					write.println("[I");
				}
			}
			else
			{
				write.println("V");
			}
		}
	}
	
	public void generateReturn(Function f)
	{
		if(f.getReturnValue() != null)
		{
			
		}
		else
		{
			write.println("return");
		}
		
		write.println(".end method");
	}
	
	public void generateFunctions()
	{
		for(Function f : module.getAllFunctions().values())
		{
			generateFunctionDeclaration(f);
			generateReturn(f);
			generateNewLine();
		}
	}
}
