package org.omich.tool.bcops;

import org.omich.tool.events.Listeners.IListenerInt;

import android.content.Context;
import android.os.Bundle;

public interface IBcTask
{
	public static class BcTaskEnv
	{
		public Bundle extras;
		public Context context;
		public IBcToaster bcToaster;
		public IListenerInt ph;
		public ICancelledInfo ci;
		
		public BcTaskEnv(Bundle extras, Context context, IBcToaster bcToaster,
				IListenerInt ph, ICancelledInfo ci)
		{
			this.extras = extras;
			this.context = context;
			this.bcToaster = bcToaster;
			this.ph = ph;
			this.ci = ci;
		}
	}

	void init (BcTaskEnv env);
	Bundle execute () throws Exception;
}
