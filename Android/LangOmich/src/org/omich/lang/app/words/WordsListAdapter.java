package org.omich.lang.app.words;

import java.util.ArrayList;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.ListItem;
import org.omich.lang.app.db.Word;
import org.omich.lang.apptool.lists.TaskListAdapter;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

public class WordsListAdapter extends TaskListAdapter<ListItem>
{
	private long dictId = -1;
	private String text = "";
	
	public WordsListAdapter (Context context, IBcConnector conn, Long dictId)
	{
		super(context, conn);		
		this.dictId = dictId;
	}
	public void setNewDictId(Long dictId)
	{
		this.dictId = dictId;
	}
	public void setNewText(String text)
	{
		this.text = text;
	}		
	
	//==== TaskListAdapter ===================================================
	@Override
	protected Class<? extends IBcTask> getLoadItemsTaskClass (){return LoadWordsTask.class;}
	@Override
	public Intent createLoadItemsIntent (){return LoadWordsTask.createIntent(dictId, text);}
	@Override
	protected String getListBundleField (){return BundleFields.WORDS_LIST;}
	
	//==== ListAdapter ========================================================
	@Override
	protected void destroyItem (int position, ListItem item) {}
	@Override
	protected int getItemViewResId (int position) {return R.layout.app_item_wordslist;}

	@Override
	protected void fillViewByData (View view, int position, ListItem item)
	{
		if(item.getWord() == null)
		{
			TextView tvt = (TextView)view.findViewById(R.id.item_wordslist_ratingText);
			tvt.setText("RATING " + Integer.toString(item.sep.rating));
			view.findViewById(R.id.item_wordslist_rating).setVisibility(View.VISIBLE);
			view.findViewById(R.id.item_wordslist_word).setVisibility(View.GONE);
		}
		else
		{
			TextView tvt = (TextView)view.findViewById(R.id.item_wordslist_wordText);
			Word t = item.getWord();
			SpannableStringBuilder text = new SpannableStringBuilder(t.foreign+" - "+ t.nativ); 
			text.setSpan(new StyleSpan(Typeface.BOLD), 0, t.foreign.length(), 
					Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			tvt.setText(text);

			view.findViewById(R.id.item_wordslist_rating).setVisibility(View.GONE);
			view.findViewById(R.id.item_wordslist_word).setVisibility(View.VISIBLE);
		}

		View sideScreen = view.findViewById(R.id.item_wordlist_screen_side);
		if(position != getSelectedPosition())
				sideScreen.setVisibility(View.INVISIBLE);
		else if(position == getSelectedPosition())
				sideScreen.setVisibility(View.VISIBLE);			
	}
	
	@Override
	public boolean isEnabled(int position)
	{
		ListItem li = (ListItem)getItem(position);
		if(li.getWord() == null) return false;
		return true;
	};
	
	@Override
	public boolean areAllItemsEnabled() 
	{
		return false;
	}
	
	//=========================================================================
	public static class LoadWordsTask implements IBcTask
	{
		public static Intent createIntent (long dictId, String text) 
		{
			Intent intent = new Intent();
			intent.putExtra(BundleFields.WORD_DICT_ID, dictId);
			intent.putExtra(BundleFields.TEXT, text);
			return intent;
		}

		private IRStorage mDb;
		private long mDictId;
		private String mText;

		@Override
		public void init(BcTaskEnv env)
		{
			mDictId = env.extras.getLong(BundleFields.WORD_DICT_ID);
			mText = env.extras.getString(BundleFields.TEXT);
			mDb = DbCreator.createReadable(env.context);
		}

		@Override
		public Bundle execute()
		{
			ArrayList<ListItem> words = new ArrayList<ListItem>(mDb.getWordsByDictIdAndText(mDictId, mText));
			Bundle result = new Bundle();
			result.putParcelableArrayList(BundleFields.WORDS_LIST, words);
			mDb.destroy();
			return result;
		}
	}
}
