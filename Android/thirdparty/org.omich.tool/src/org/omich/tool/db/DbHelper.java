package org.omich.tool.db;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.events.Listeners.INistener;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Полезные функции для работы с базой данных
 */
public class DbHelper
{
	/**
	 * Итератор курсора. Используется в методе iterateCursorAndClose
	 */
	abstract public static class CursorIterator implements INistener<Cursor>
	{
		private boolean mIsTerminated;
		
		protected void terminate () {mIsTerminated = true;}
		public boolean isTerminated () {return mIsTerminated;}
	}

	public static Cursor queryBySingleColumn (@Nonnull SQLiteDatabase db,
			@Nonnull String table, @Nonnull String [] columns,
			@Nonnull String searchColumn, @Nullable String searchValue)
	{
		return db.query(table, columns, 
				searchColumn + "= ? ", new String[]{searchValue}, //$NON-NLS-1$
				null, null, null);
	}

	public static Cursor queryBySingleColumn (@Nonnull SQLiteDatabase db,
			@Nonnull String table, @Nonnull String [] columns,
			@Nonnull String searchColumn, long searchValue)
	{
		return db.query(table, columns, 
				searchColumn + "= " + searchValue,  //$NON-NLS-1$
				null, null, null, null);
	}

	/**
	 * Метод, который для каждой позиции курсора запускает итератор.
	 * По окончании работы вызывает cursor.close();
	 * 
	 * Если в процессе обработки был вызван iterator.terminate(), то выполнение
	 * прерывается, вызывается cursor.close();
	 * 
	 * 
	 * Пример использования:
	 * 
	 * 	Cursor cursor = getDb().query(TNAME_TICKETS, 
	 *		TICKETS_COLUMNS, 
	 *		null, null, null, null, TicketsCols.NAME);
	 *
	 *	final List<Ticket> tickets = new ArrayList<Ticket>();
	 *
	 *	if(cursor != null)
	 *	{
	 *		DbHelper.iterateCursorAndClose(cursor, new CursorIterator()
	 *		{
	 *			@Override
	 *			public void handle (@Nonnull Cursor cursor)
	 *			{
	 *				if (!isCancelled())
	 *				{
	 *					tickets.add(createTicketByCursor(cursor));
	 *				}
	 *				else
	 *				{
	 *					tickets.clear();
	 *					this.terminate();
	 *				}
	 *			}
	 *		});
	 *	}
	 *	return tickets;
	 * 
	 * @param cursor
	 * @param iterator
	 */
	public static void iterateCursorAndClose (@Nonnull Cursor cursor,
			@Nonnull CursorIterator iterator)
	{
		cursor.moveToFirst();
		while(!cursor.isAfterLast() && !iterator.isTerminated())
		{
			iterator.handle(cursor);
			cursor.moveToNext();
		}
		cursor.close();
	}
}
