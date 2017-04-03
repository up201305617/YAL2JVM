import java.util.ArrayList;

public class Function 
{
	private String functionId;
	private SimpleNode body;
	private Variable returnValue;
	private ArrayList<Variable> arguments;
	
	public Function(String id, Variable returnValue, ArrayList<Variable> a, SimpleNode body)
	{
		this.functionId = id;
		this.returnValue = returnValue;
		this.arguments = a;
		this.body = body;
	}
}
