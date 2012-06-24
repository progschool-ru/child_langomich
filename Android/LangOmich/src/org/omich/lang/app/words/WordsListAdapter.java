package org.omich.lang.app.words;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.R;
import org.omich.lang.app.db.Word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WordsListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<Word> mWords = new ArrayList<Word>();
	
	public WordsListAdapter (Context context)
	{
		mContext = context;
		mWords.add(new Word("привет", "hi", 0));
		mWords.add(new Word("пока", "bye", 0));
	}

	public int getCount () {return mWords.size();}
	public Object getItem (int arg0)	{return mWords.get(arg0);}
	public long getItemId (int position) {return position;}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		
		if(v == null)
		{
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.app_item_wordslist, null);
		}
		
		TextView tv = (TextView)v.findViewById(R.id.item_wordslist_text);
		tv.setText(mWords.get(position).nativ);

		return v;
	}
}
