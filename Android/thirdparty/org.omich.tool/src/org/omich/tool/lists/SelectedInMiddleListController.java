package org.omich.tool.lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.events.Listeners.IListenerInt;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * Прокручивает список так, чтобы какой-нибудь элемент всегда стоял в центре.
 * Во время скроллинга уведомляет слушателя о смене текущего элемента.
 * Текущий элемент - это тот, который в центре. 
 */
public class SelectedInMiddleListController
{	
	private @Nonnull ListView mListView;	
	private @Nonnull IListenerInt mSelectionListener;

	private @Nonnull ScrollRunner mpScrollRunner = new ScrollRunner();
	private int mLastOffsetY;
	private int mLastScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	private int mCurrentPosition;
	private boolean mIsUpdatableByScrolling = true;
	
	public SelectedInMiddleListController(@Nonnull ListView listView,
			@Nonnull IListenerInt selectionListener, int selectedPosition)
	{
		mListView = listView;
		mSelectionListener = selectionListener;
		
//		mListView.setSelection(selectedPosition);
//		mpScrollRunner.checkPlaceOfItemWithPosition(mListView, false);
		mpScrollRunner.setSelectedPosition(selectedPosition, false);

		mListView.setOnScrollListener(new ScrollHandler());
	}
	
	public void setSelectedPosition (final int selectedPosition, final boolean withSmoothing)
	{
		mpScrollRunner.setSelectedPosition(selectedPosition, withSmoothing);
	}
	
	//=========================================================================
	private void checkCurrentPosition(AbsListView listView,
			boolean withSmoothing)
	{
		//Приводим значения к long, потому что сумма двух int не всегда помещается.
		long firstVisiblePosition = listView.getFirstVisiblePosition();
		long lastVisiblePosition = listView.getLastVisiblePosition();
		int middlePosition = (int)((firstVisiblePosition + lastVisiblePosition) / 2);
		mCurrentPosition = middlePosition;

		mpScrollRunner.checkPlaceOfItemWithPosition(mListView, true);
	}

	private static @Nullable View findItemViewByPosition (@Nonnull AbsListView listView, int position)
	{
		// Внутренняя структура списка и его элементов может быть какой угодно,
		// поэтому мы не можем явно знать, какой по счёту дочерний элемент нам нужен.
		// Поэтому просто перебираем, пока не встретим нужный.
		int childrenCount = listView.getChildCount();
		View childView = null;
		int i = 0;
		while(i < childrenCount)
		{
			childView = listView.getChildAt(i);
			if(position == listView.getPositionForView(childView))
			{
				i = childrenCount + 1;
				break;
			}
			++i;
		}
		
		if(childView != null && i == childrenCount + 1)
		{
			return childView;
		}
		else
		{
			return null;
		}
	}

	/*
	 * Призван возвращать сентральный элемент на середину:
	 * - запускается с последними параметрами.
	 * - сам следит за тем, чтобы не вставать в очередь несколько раз.
	 */
	private class ScrollRunner implements Runnable
	{
		private int mOffsetY;
		private int mMilliseconds;
		private boolean mIsInQueue;
		private boolean mNeedCheckAfterSmoothing;
		
		public void setSelectedPosition (int targetPosition, boolean withSmoothing)
		{
			if(targetPosition == mCurrentPosition)
				return;
			
			if(mLastScrollState != OnScrollListener.SCROLL_STATE_IDLE)
				return;
			
			mCurrentPosition = targetPosition;
			mIsUpdatableByScrolling = false;
			if(withSmoothing)
			{
				smoothScrollToCurrentPosition(mLastOffsetY, 200, true);
			}
			else if(!checkPlaceOfItemWithPosition(mListView, false))
			{
				mListView.setSelection(targetPosition);
				smoothScrollToCurrentPosition(mLastOffsetY, 0, true);
			}
		}
		
		public void smoothScrollToCurrentPosition (int offsetY, int milliseconds, boolean needCheckingAfter)
		{
			// Тут делаем заказ на кручение со смуфингом. Устанавливаем параметры
			// И если нужно, то регистрируем Runnable методом View.post();
			//
			mOffsetY = offsetY;
			mMilliseconds = milliseconds;
			mNeedCheckAfterSmoothing = needCheckingAfter;
			
			if(!mIsInQueue)
			{
				mListView.post(this);
				mIsInQueue = true;
			}
		}

		//==== Runnable implementation ==========
		public void run ()
		{
			// Выполняем смуфинг.
			// Потом проверяем, был ли заказ на setSelection, несовпадающий с текущим положением;
			//  Если да, то повторяем попытку, вызовом соответствующего метода
			//  Если нет, то проверяем, был ли заказ на уточнение докрутки.
			mIsInQueue = false;
			mListView.smoothScrollToPositionFromTop(mCurrentPosition, mOffsetY, mMilliseconds);
			
			if(mNeedCheckAfterSmoothing)
			{
				//Уточняем местоположение элемента. Если он изначально был поставлен правильно
				//то повторно скроллинг не будет запускаться.
				checkPlaceOfItemWithPosition(mListView, mMilliseconds > 0);
			}
		}
		
		//=======================================
		private boolean checkPlaceOfItemWithPosition (@Nonnull AbsListView listView,
				boolean withSmoothing)
		{
			//Проводим телодвижения по поиску центрального графического элемента,
			//чтобы дать команду докрутить до нужного положения.
			View view = findItemViewByPosition(listView, mCurrentPosition);
			
			if(view != null)
			{
				int listHeight = listView.getHeight() - listView.getPaddingTop() - listView.getPaddingBottom();
				int middleHeight = view.getHeight();
				final int futureMiddleY = (listHeight - middleHeight) / 2;
				mLastOffsetY = futureMiddleY;

				int deltaMiddleY = futureMiddleY - view.getTop();
				int absDeltaMiddleY = deltaMiddleY >= 0 ? deltaMiddleY : -deltaMiddleY;
				int milliseconds = withSmoothing ? 150 : 0;
				
				if(absDeltaMiddleY > 0)
				{
					mpScrollRunner.smoothScrollToCurrentPosition(futureMiddleY, milliseconds, false);
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	private class ScrollHandler implements OnScrollListener
	{
		public void onScrollStateChanged(final AbsListView listView, int scrollState)
		{
			if(listView == null)
				return;
			
			mLastScrollState = scrollState;

			if(scrollState != OnScrollListener.SCROLL_STATE_IDLE)
				return;

			checkCurrentPosition(listView, true);
		}
		
		public void onScroll(AbsListView view, int firstVisiblePosition,
				int visibleItemCount, int totalItemCount)
		{
			int middleVisiblePosition = visibleItemCount / 2 + firstVisiblePosition;
			if(middleVisiblePosition == mCurrentPosition)
			{
				mIsUpdatableByScrolling = true;
			}
			else if(mIsUpdatableByScrolling)
			{
				mSelectionListener.handle(middleVisiblePosition);
			}
		}
	}
}
