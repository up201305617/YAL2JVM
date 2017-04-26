import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
	}
	
	public void generateNewLine()
	{
		write.println("");
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
					value = newArray.getSize()+"";
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
}
