package org.omich.tool.lists;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Наследник от BaseAdapter.
 * Призван:
 *  - переиспользовать существующие элементы списка в методе getView.
 *  - все View элементов однотипные и layout этих элементов запрашивается у наследника.
 *  - предусматривает необходимость очищать ресурсы элементов при уничтожении
 *  и ответственность за это возлагает на наследника.
 *  - предусматривает возможность долгого обновления элементов.
 *  Процесс обновления возлагает на наследника, а сам ждёт callBack, чтобы перерисовать экран. 
 *
 *  
 *  
 *  Методы для переопределения:
 *  
 *	- void destroyItem (int position, @Nonnull Item item);
 *	- int getItemViewResId (int position);
 *	- void fillViewByData (@Nonnull View view, int position, @Nullable Item item);
 *
 * @param <Item> - тип элемента списка.
 */
abstract public class OmListAdapter<Item> extends BaseAdapter
{
	private @Nonnull Context mContext;
	private @Nonnull List<Item> mItems = new ArrayList<Item>();
	private boolean mIsDestroyed;
	
	protected OmListAdapter (@Nonnull Context context)
	{
		mContext = context;
	}

	//==== public interface ===================================================
	/**
	 * Запрос на обновление данных списка. Перечитать их заново из базы данных или что-то подобное.
	 */
	abstract public void reloadItems ();
	
	public void destroy ()
	{
		cleanItems();
		mIsDestroyed = true;
	}

	//==== protected interface ================================================
	/**
	 * Очистить ресурсы уничтожаемого элемента.
	 * 
	 * @param position
	 * @param item
	 */
	abstract protected void destroyItem (int position, @Nonnull Item item);

	/**
	 * Выдаёт идентификатор layout-ресурса, по которому должен генерироваться View элемента списка
	 * @param position
	 * @return
	 */
	abstract protected int getItemViewResId (int position);

	/**
	 * Заполняет View элемента данными.
	 * 
	 * @param view
	 * @param position
	 * @param item
	 */
	abstract protected void fillViewByData (@Nonnull View view, int position, @Nullable Item item);

	/**
	 * Заменяет имеющийся список элементов на новый. Элементы старого списка очищаются и список удаляется.
	 * @param items
	 */
	protected void setItems (@Nonnull List<Item> items)
	{
		if(mIsDestroyed)
			return;

		cleanItems();
		mItems = items;
		notifyDataSetChanged();
	}

	//==== BaseAdapter ========================================================
	/**
	 * How many items are in the data set represented by this Adapter.
	 */
	public int getCount ()
	{
		return mItems.size();
	}
	
	/**
	 * Get the data item associated with the specified position in the data set.
	 */
	public Object getItem (int position)
	{
		return mItems.get(position);
	}
	
	/**
	 * Get the row id associated with the specified position in the list.
	 */
	public long getItemId (int position)
	{
		return position;
	}

	/**
	 * Get a View that displays the data at the specified position in the data set.
	 */
	public View getView (int position, @Nullable View convertView, ViewGroup parent)
	{
		View v = convertView;
		if(v == null)
		{
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(getItemViewResId(position), null);
		}

		if(v != null)
		{
			fillViewByData(v, position, mItems.get(position));
		}

		return v;
	}
	
	//=========================================================================
	/**
	 * Удаляет все элементы из списка, вызвав destroyItem() для каждого из них.
	 */
	private void cleanItems ()
	{
		int size = mItems.size();
		for(int i = 0; i < size; ++i)
		{
			Item item = mItems.get(i);
			if(item != null)
			{
				destroyItem(i, item);
			};
		}
		mItems.clear();
	}
}
