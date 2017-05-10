import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map.Entry;

public class Generator 
{
	private Module module;
	private PrintWriter write;
	private File folder;
	private File file;
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
			this.folder = new File("./j");
			this.folder.mkdirs();
			this.file = new File(folder, this.newFileName);
			this.write = new PrintWriter(file,"UTF-8");
			generate();
			this.write.close();
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
		this.write.print("\n");
	}
	
	public void generateHeader()
	{
		this.write.println(".class public " + this.moduleName);
		this.write.println(".super java/lang/Object");
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
				this.write.println(".field static "+name+" "+type+" = "+value);
			}
			else
			{
				this.write.println(".field static "+name+" "+type);
			}
		}
	}
	
	public void generateFunctionDeclaration(Function f)
	{
		ArrayList<Variable> arguments = f.getArguments();
		Variable returnValue = f.getReturnValue();
		f.setVarNum();
		int num = f.getVarNum();
		
		if(f.getFunctionId().equals("main"))
		{
			this.write.println(".method public static main([Ljava/lang/String;)V");
			this.write.println(".limit locals 1");
			this.write.println(".limit stack 1");
		}
		else
		{
			this.write.print(".method public static " + f.getFunctionId() + "(");
			
			for (int i = 0; i < arguments.size(); i++)
			{
				if(arguments.get(i).getType().equals(Constants.SCALAR))
				{
					this.write.print("I");
				}
				if(arguments.get(i).getType().equals(Constants.ARRAY)) 
				{
					this.write.print("[I");
				}
			}
			
			this.write.print(")");
			
			if(returnValue != null)
			{
				if(returnValue.getType().equals(Constants.SCALAR))
				{
					this.write.println("I");
				}
				if(returnValue.getType().equals(Constants.ARRAY))
				{
					this.write.println("[I");
				}
			}
			else
			{
				this.write.println("V");
			}
			
			this.write.println(".limit locals " + num);
			this.write.println(".limit stack 2");
			this.write.println();
		}
	}
	
	public void generateReturn(Function f)
	{
		if(f.getReturnValue() != null)
		{
			Variable retVar = f.getReturnValue();
			
			if(retVar.getType() == "Scalar")
			{
				this.write.println("ireturn");
			}
			
			if(retVar.getType() == "Array")
			{
				this.write.println("areturn");
			}
		}
		else
		{
			this.write.println("return");
		}
		
		this.write.println(".end method");
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
