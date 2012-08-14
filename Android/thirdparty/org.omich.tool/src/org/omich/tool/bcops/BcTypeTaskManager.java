package org.omich.tool.bcops;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BcTypeTaskManager
{
	private List<Class<? extends IBcTask>> mTypes = new ArrayList<Class<? extends IBcTask>>();
	
	public synchronized int registerType (@Nonnull Class<? extends IBcTask> cl)
	{
		int size = mTypes.size();
		for(int i = 0; i < size; ++i)
		{
			if(mTypes.get(i) == cl)
			{
				return i;
			}
		}
		mTypes.add(cl);
		return size;
	}
	
	public synchronized @Nullable Class<? extends IBcTask> getTypeByIndex (int index)
	{
		if(index < 0 || index >= mTypes.size())
			return null;
		
		return mTypes.get(index);
	}
}
