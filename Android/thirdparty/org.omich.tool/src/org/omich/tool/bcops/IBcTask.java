package org.omich.tool.bcops;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.events.Listeners.IListenerInt;

import android.content.Context;
import android.os.Bundle;

public interface IBcTask
{
	public static class BcTaskEnv
	{
		public @Nullable Bundle extras;
		public @Nonnull Context context;
		public @Nonnull IBcToaster bcToaster;
		public @Nullable IListenerInt ph;
		public @Nullable ICancelledInfo ci;
		
		public BcTaskEnv(@Nullable Bundle extras, @Nonnull Context context,
				@Nonnull  IBcToaster bcToaster,
				@Nullable IListenerInt ph, @Nullable ICancelledInfo ci)
		{
			this.extras = extras;
			this.context = context;
			this.bcToaster = bcToaster;
			this.ph = ph;
			this.ci = ci;
		}
	}

	void init (@Nonnull BcTaskEnv env);
	@Nullable Bundle execute () throws Exception;
}
