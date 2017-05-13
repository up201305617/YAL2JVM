import java.util.ArrayList;

public class AST
{	
	public String type;
	public ArrayList<AST> parents = new ArrayList<AST>();
	public ArrayList<AST> children = new ArrayList<AST>();
	public static int cfgNodeCount;
	public int nodeNumber;
	
	public boolean isOperation;
	public String operation;
	
	//left_side = right_side_1 <op> right_side_2
	
	//left_side
	public String left_side_id;
	public String left_side_scope;
	public String lhsAccess;
	public String lhsArrayIndexId;
	public String lhsArrayAccessType;
	
	//right_side_1
	public String righ_side_1_id;
	public String right_side_1_scope;
	public String rhs1Access;
	public String rhs1Type;
	public String rhs1ArrayIndexId;
	public String rhs1ArrayAccess;
	public ArrayList<String> right_side_1_args_id;
	public Function right_side_1_function;
	public boolean other_module_1;
	
	//right_side_2
	public String righ_side_2_id;
	public String right_side_2_scope;
	public String rhs2Access;
	public String rhs2Type;
	public String rhs2ArrayIndexId;
	public String rhs2ArrayAccess;
	public ArrayList<String> right_side_2_args_id;
	public Function right_side_2_function;
	public boolean other_module_2;
	
	public String compOperator;
	public String condSign;
	
	public boolean dot;
	public String callFunctionName;
	public String callFunctionDeclaration;
	public String[] callArgs;
	
	public AST (String t, Function f)
	{
		type = t;
		parents = new ArrayList<AST>();
		children = new ArrayList<AST>();
		right_side_1_args_id = new ArrayList<String>();
		right_side_2_args_id = new ArrayList<String>();
		nodeNumber = f.getNodeCount();
		f.incNodeCount();
		f.getNodes().add(this);
	}
}
