import java.util.ArrayList;

public class IntermediateRepresentation
{	
	public String type;
	public ArrayList<IntermediateRepresentation> parents = new ArrayList<IntermediateRepresentation>();
	public ArrayList<IntermediateRepresentation> children = new ArrayList<IntermediateRepresentation>();
	public static int cfgNodeCount;
	public int nodeNumber;
	
	public boolean isOperation;
	public String operation;
	
	public String lhsId;
	public String lhsScope;
	public String lhsAccess;
	public String lhsType;
	public String lhsArrayIndexId;
	public String lhsArrayAccess;
	
	public String rhs1Id;
	public String rhs1Scope;
	public String rhs1Access;
	public String rhs1Type;
	public String rhs1ArrayIndexId;
	public String rhs1ArrayAccess;
	public ArrayList<String> rhs1Args;
	public Function rhs1Call;
	public boolean rhs1OtherModule;
	
	public String rhs2Id;
	public String rhs2Scope;
	public String rhs2Access;
	public String rhs2Type;
	public String rhs2ArrayIndexId;
	public String rhs2ArrayAccess;
	public ArrayList<String> rhs2Args;
	public Function rhs2Call;
	public boolean rhs2OtherModule;

}
