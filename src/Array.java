//////////////////////////////////////////////
//IMPORTANTE
//the array variables by reference
///////////////////////////////////////////////////

public class Array extends Variable
{
	private int size;
	private int[] array;
	private int ref;
	public static int REFERENCE;
	
	//Default Array (ex.: c=[])
	public Array(String id)
	{
		super(id);
		this.array = new int[0];
		this.ref = REFERENCE;
		REFERENCE++;
	}
	
	//Array com tamanho específico (ex.: c=[2])
	public Array(String id, int size)
	{
		super(id);
		this.size = size;
		this.ref = REFERENCE;
		REFERENCE++;
	}

	public int getSize() 
	{
		return size;
	}

	public int[] getArray() 
	{
		return array;
	}

	public int getRef()
	{
		return ref;
	}
}
