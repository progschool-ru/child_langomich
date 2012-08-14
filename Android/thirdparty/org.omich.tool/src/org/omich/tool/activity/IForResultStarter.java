package org.omich.tool.activity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.events.Listeners.IListener;

import android.content.Intent;

public interface IForResultStarter
{
	/**
	 * Делает context.startActivityForResult, а полученный впоследствии ответ
	 * передаёт в обработчик handler.
	 * 
	 * @param intent
	 * @param handler
	 */
	void startForResult(@Nonnull Intent intent, @Nullable IListener<Intent> handler);
}
