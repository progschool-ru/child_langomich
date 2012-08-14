package org.omich.tool.bcops;

import javax.annotation.Nonnull;

public interface IBcToaster
{
	public static final @Nonnull IBcToaster EMPTY_TOASTER = new IBcToaster()
	{	
		public void showToast(@Nonnull String msg)
		{
			//Do nothing. It's an empty toaster;
		}
	};

	void showToast (@Nonnull String msg);
}
