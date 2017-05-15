import java.util.ArrayList;

public class Side 
{
	public String id;
	public String scope;
	public String access;
	public String type;
	public String arrayIndexId;
	public String arrayAccessType;
	public ArrayList<String> args_id;
	public Function function;
	public boolean other_module;
	
	public Side()
	{
		
	}
	
	public void initializeArray()
	{
		args_id = new ArrayList<String>();
	}
}
