package org.omich.tool.bcops;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;
import android.os.Bundle;

public interface IBcConnector
{
	public @Nonnull String startTask (@Nonnull Class<? extends BcService> serviceClass,
									@Nonnull Class<? extends IBcTask> taskClass,
									@Nonnull Intent intent,
									@Nullable IListener<Intent> handler);
	public @Nonnull String startTypicalTask (@Nonnull Class<? extends IBcTask> taskClass,
									@Nonnull Intent intent,
									@Nullable IListener<Bundle> finishHandler);
	public void cancelTask (@Nonnull String opId);
	public void unsubscribeTask (@Nonnull String opId);
}
