package org.omich.tool.log;

public class ByteArrayLoggable implements ILoggable
{
	private byte [] mArr;
	
	public ByteArrayLoggable (byte [] arr)
	{
		mArr = arr;
	}

	public String getShortLogMessage ()
	{
		StringBuilder sb = new StringBuilder("[");
		byte [] arr = mArr;
		for(int i = 0; i < arr.length; ++i)
		{
			sb.append(arr[i] >= 0 ? Byte.toString(arr[i]) : Integer.toString(256 + arr[i]));
			if(i < arr.length -1)
			{
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public String getFullLogMessage ()
	{
		return getShortLogMessage();
	}
	
	
}
