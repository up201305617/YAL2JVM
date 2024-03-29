public class Scalar extends Variable
{
	private int value;
	private String type;
	
	//Sem atribuição do valor (a)
	public Scalar(String id)
	{
		super(id);
		this.type = "Scalar";
	}
	
	//Com atribuição do valor (a=1)
	public Scalar(String id, int value)
	{
		super(id);
		this.value = value;
		this.type = "Scalar";
	}

	public int getValue() 
	{
		return value;
	}

	public void setValue(int value) 
	{
		this.value = value;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
