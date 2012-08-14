package org.omich.tool.lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**
 *  Обёртка вокруг конечного ListAdapter, которая зацикливает список.
 */
public class CycledAdapter extends BaseAdapter
{
	private @Nonnull ListAdapter mWrappedAdapter;
	
	public CycledAdapter (@Nonnull ListAdapter wrappedAdapter)
	{
		mWrappedAdapter = wrappedAdapter;
	}

	/**
	 * Функция конвертирования postion в системе отсчёта CycledAdapter в position системы отсчёта mWrappedAdapter
	 * 
	 * @param position
	 * @return
	 */
	public int getWrappedPosition (int position)
	{
		return position % mWrappedAdapter.getCount();
	}

	/**
	 * Возвращает позицию для наименьшего скроллинга
	 * 
	 * @param wrappedPosition - позиция куда надо проскроллить, в системе отсчёта Wrapped.
	 * @param comparingCycledPosition - текущая позиция в системе отсчёта Cycled.
	 * @return
	 */
	public int getNearestCycledPosition (int wrappedPosition, int comparingCycledPosition)
	{
		int comparingWrappedPosition = getWrappedPosition(comparingCycledPosition);
		int wrappedDelta = wrappedPosition - comparingWrappedPosition;
		
		int wrappedCount = mWrappedAdapter.getCount();
		int increasingDirectionDelta = (wrappedDelta >= 0) ? wrappedDelta : wrappedDelta + wrappedCount;
		int decreasingDirectionDelta = (wrappedDelta <= 0) ? wrappedDelta : wrappedDelta - wrappedCount;
		int cycledDelta = (increasingDirectionDelta <= -decreasingDirectionDelta) ? increasingDirectionDelta : decreasingDirectionDelta;
		
		return comparingCycledPosition + cycledDelta;
	}

	//==== BaseAdapter implementation =========================================
	@Override
	public int getCount ()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public @Nullable Object getItem (int position)
	{
		return mWrappedAdapter.getItem(getWrappedPosition(position));
	}

	@Override
	public long getItemId (int position)
	{
		return mWrappedAdapter.getItemId(getWrappedPosition(position));
	}

	@Override
	public @Nullable View getView (int position, @Nullable View convertView, @Nullable ViewGroup parent)
	{
		return mWrappedAdapter.getView(getWrappedPosition(position), convertView, parent);
	}
	
	//=========================================================================
}
