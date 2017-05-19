import java.util.ArrayList;

public class AST
{	
	public String type;
	public ArrayList<AST> parents;
	public ArrayList<AST> children;

	public Side left_side;
	public Side right_side_1;
	public Side right_side_2;
	public Call call;
	
	public String compOperator;
	public String condSign;
	public String operation;
	public boolean isOperation;
	
	public boolean visited;
	
	public AST (String t, Function f)
	{
		type = t;
		parents = new ArrayList<AST>();
		children = new ArrayList<AST>();
		left_side = new Side();
		right_side_1 = new Side();
		right_side_2 = new Side(); 
		call = new Call();
		right_side_1.initializeArray();
		right_side_2.initializeArray();
		f.incNodeCount();
		f.getNodes().add(this);
	}
}
