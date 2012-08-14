package org.omich.tool.constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;

public class AndroidListeners
{
	public static final @Nonnull IListener<Intent> EMPTY_INTENT_LISTENER = new IListener<Intent>()
	{
		public void handle(@Nullable Intent intent)
		{
			//Do nothing. It's an empty listener
		}
	};
}
