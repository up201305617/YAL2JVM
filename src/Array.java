public class Array extends Variable
{
	private int size;
	private int[] array;
	private String size_s;
	
	//Default Array (ex.: c=[])
	public Array(String id)
	{
		super(id);
		this.array = new int[0];
		this.size = 0;
	}
	
	//Array com tamanho específico (ex.: c=[2])
	public Array(String id, int size)
	{
		super(id,size);
		this.array = new int[size];
		this.size = size;
	}
	
	public Array(String id, String size)
	{
		super(id);
		this.size_s=size;
	}

	public int getSize() 
	{
		return size;
	}

	public int[] getArray() 
	{
		return array;
	}
}
