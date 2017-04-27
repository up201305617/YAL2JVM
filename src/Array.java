public class Array extends Variable
{
	private int size;
	private int[] array;
	
	//Default Array (ex.: c=[])
	public Array(String id)
	{
		super(id);
		this.array = new int[0];
	}
	
	//Array com tamanho específico (ex.: c=[2])
	public Array(String id, int size)
	{
		super(id);
		this.size = size;
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
