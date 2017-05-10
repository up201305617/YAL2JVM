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
	
	public String left_side_id;
	public String lhsScope;
	public String lhsAccess;
	public String lhsType;
	public String lhsArrayIndexId;
	public String lhsArrayAccessType;
	
	public String rhs1Id;
	public String rhs1Scope;
	public String rhs1Access;
	public String rhs1Type;
	public String rhs1ArrayIndexId;
	public String rhs1ArrayAccess;
	public ArrayList<String> right_side_1_args_id;
	public Function rhs1Call;
	public boolean rhs1OtherModule;
	
	public String rhs2Id;
	public String rhs2Scope;
	public String rhs2Access;
	public String rhs2Type;
	public String rhs2ArrayIndexId;
	public String rhs2ArrayAccess;
	public ArrayList<String> right_side_2_args_id;
	public Function rhs2Call;
	public boolean rhs2OtherModule;
	
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
