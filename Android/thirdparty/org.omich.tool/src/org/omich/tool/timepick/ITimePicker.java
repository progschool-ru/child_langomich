package org.omich.tool.timepick;

import javax.annotation.Nullable;

import org.omich.tool.events.Listeners.IListenerLong;

/**
 * Должен спросить у пользователя время и передать его в обработчик.
 */
public interface ITimePicker
{
	/**
	 * Cпрашивает у пользователя время и передаёт его в обработчик handler.
	 *
	 * @param milliseconds - то время, которое надо отобразить пользователю в качестве значения по умолчанию
	 * @param handler
	 */
	void pickTime (long milliseconds, @Nullable IListenerLong handler);
}
