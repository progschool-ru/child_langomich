package org.omich.tool.log;

import org.omich.tool.constants.Strings;

public class ByteArrayLoggable implements ILoggable
{
	private byte [] mArr;
	
	public ByteArrayLoggable (byte [] arr)
	{
		mArr = arr;
	}

	public String getShortLogMessage ()
	{
		StringBuilder sb = new StringBuilder(Strings.CH_BRACE_L);
		byte [] arr = mArr;
		for(int i = 0; i < arr.length; ++i)
		{
			sb.append(arr[i] >= 0 ? Byte.toString(arr[i]) : Integer.toString(256 + arr[i]));
			if(i < arr.length -1)
			{
				sb.append(", "); //$NON-NLS-1$
			}
		}
		sb.append(Strings.CH_BRACE_R);
		return sb.toString();
	}

	public String getFullLogMessage ()
	{
		return getShortLogMessage();
	}
	
	
}
