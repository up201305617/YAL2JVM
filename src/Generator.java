import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
	}
	
	public void generateHeader()
	{
		write.println(".class public " + this.moduleName);
		write.println(".super java/lang/Object");
	}
	
	public void generateGlobalVariables()
	{
		for(int i=0; i<node.jjtGetNumChildren(); i++)
		{
			
		}
	}
}
