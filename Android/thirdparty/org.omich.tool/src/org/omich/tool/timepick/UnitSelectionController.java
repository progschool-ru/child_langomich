package org.omich.tool.timepick;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.R;
import org.omich.tool.events.Listeners.IListenerInt;
import org.omich.tool.events.Listeners.INistener;
import org.omich.tool.lists.CycledAdapter;
import org.omich.tool.lists.SelectedInMiddleListController;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Снабжает список зацикленным адаптером с центрированием выбранного элемента,
 *  заполняя его числами от start до (end-1) включительно.
 */
public class UnitSelectionController
{
	public static final class EventData
	{
		public int value;
		public boolean isOver;
	}
	
	private @Nonnull ListView mListView;
	private @Nonnull ArrayAdapter<Integer> mWrappedAdapter;
	private @Nonnull CycledAdapter mCycledAdapter;
	
	private int mItemsNumber;
	private int mStart;
	private boolean mIsInverted;
	
	private @Nonnull SelectedInMiddleListController mSimlController;
	private @Nullable INistener<EventData> mSelectionChangeListener; 

	private int mCurrentCycledPosition;

	/**
	 * 
	 * @param listView - список к которому применяется создаваемый внутри адаптер
	 * @param start - начальное число для заполнения
	 * @param end - конец заполнения. Адаптер заполняется числами от start до (end-1) включительно.
	 * @param inverted - должны ли числа идти от конца к началу.
	 * @param startOffset - костыльный параметр.
	 */
	public UnitSelectionController (@Nonnull ListView listView, int start, int end,
			boolean isInverted, int startOffset, final @Nonnull String format)
	{
		mStart = start;
		mItemsNumber = end - start;
		mIsInverted = isInverted;

		//Настраиваем адаптеры
		mListView = listView;
		mWrappedAdapter = new ArrayAdapter<Integer>(listView.getContext(), R.layout.timepick_picker_item)
				{
					@Override
					public View getView(int position, View convertView, ViewGroup parent)
					{
						convertView = super.getView(position, convertView, parent);
						TextView tv = (TextView)convertView;
						tv.setText(String.format(format, getItem(position).intValue()));
						return convertView;
					}
				};
		fillWrappedAdapter(mWrappedAdapter, start, end, isInverted);
		mCycledAdapter = new CycledAdapter(mWrappedAdapter);
		mListView.setAdapter(mCycledAdapter);
		
		int halfCount = mCycledAdapter.getCount() / 2;
		int zeroSelection = halfCount - halfCount % mItemsNumber;
		if(isInverted)
		{
			--zeroSelection;
		}
		mCurrentCycledPosition = zeroSelection;

		//Настраиваем срединный контроллер
		mSimlController = new SelectedInMiddleListController(mListView, new IListenerInt()
		{
			public void handle(int position)
			{
				handleSelection(position);
			}
		}, zeroSelection);
	}
	
	/**
	 * Обработчик изменений.
	 * @param listener
	 */
	public void setSelectionChangeListener (@Nullable INistener<EventData> listener)
	{
		mSelectionChangeListener = listener;
	}
	
	public void setSelectedPosition (final int selectedItem, final boolean withSmoothing)
	{
		int wrappedPosition = selectedItem - mStart;
		if(mIsInverted)
		{
			wrappedPosition = mItemsNumber - wrappedPosition - 1;
		}
		
		int cycledPosition = mCycledAdapter.getNearestCycledPosition(wrappedPosition, mCurrentCycledPosition);
		
		mSimlController.setSelectedPosition(cycledPosition, withSmoothing);
		mCurrentCycledPosition = cycledPosition;
	}
	
	//=========================================================================
	private static void fillWrappedAdapter (ArrayAdapter<Integer> adapter, int start, int end,
			boolean inverted)
	{
		if(!inverted)
		{
			for(int i = start; i < end; ++i)
			{
				adapter.add(Integer.valueOf(i));
			}
		}
		else
		{
			for(int i = end - 1; i >= start; --i)
			{
				adapter.add(Integer.valueOf(i));
			}
		}
	}

	private void handleSelection (int position)
	{
		// Если позиция изменилась, то производим присваивания.
		// Если есть обработчик, то формируем для него ответ data.
		//   если значение изменилось на слишком большое число, то считаем,
		//   что скроллинг прошёл через верх и устанавливаем true в data.isOver
		//
		if(position == mCurrentCycledPosition)
			return;
		
		int oldPosition = mCurrentCycledPosition;
		mCurrentCycledPosition = position;
		
		@Nullable INistener<EventData> listener = mSelectionChangeListener;
		if(listener != null)
		{
			int wrappedOldPosition = mCycledAdapter.getWrappedPosition(oldPosition);
			int wrappedCurPosition = mCycledAdapter.getWrappedPosition(mCurrentCycledPosition);
			int absDelta = Math.abs((wrappedCurPosition - wrappedOldPosition));

			EventData data = new EventData();
			Integer item = (Integer)mCycledAdapter.getItem(position);
			data.value = item != null ? item.intValue() : 0;
			data.isOver = absDelta > (mItemsNumber / 2);
			
			listener.handle(data);
		}
	}
}
