package org.omich.lang.app.words;

import java.util.ArrayList;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.lang.apptool.lists.TaskListAdapter;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

public class WordsListAdapter extends TaskListAdapter<Word>
{
	private long dictId = -1;
	
	public WordsListAdapter (Context context, IBcConnector conn, Long dictId)
	{
		super(context, conn);		
		this.dictId = dictId;
	}
	public void setNewDictId(Long dictId)
	{
		this.dictId = dictId;
	}
	//==== TaskListAdapter ===================================================
	@Override
	protected Class<? extends IBcTask> getLoadItemsTaskClass (){return LoadWordsTask.class;}
	@Override
	public Intent createLoadItemsIntent (){return LoadWordsTask.createIntent(dictId);}
	@Override
	protected String getListBundleField (){return BundleFields.WORDS_LIST;}
	
	//==== ListAdapter ========================================================
	@Override
	protected void destroyItem (int position, Word item) {}
	@Override
	protected int getItemViewResId (int position) {return R.layout.app_item_wordslist;}

	@Override
	protected void fillViewByData (View view, int position, Word item)
	{	
		TextView tvt = (TextView)view.findViewById(R.id.item_wordslist_text);
		
		SpannableStringBuilder text = new SpannableStringBuilder(item.foreign+" - "+item.nativ); 
	    text.setSpan(new StyleSpan(Typeface.BOLD), 0, item.foreign.length(), 
	    		  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
	    tvt.setText(text);
	      
		TextView tvr = (TextView)view.findViewById(R.id.item_wordslist_text_rating);
		tvr.setText(Integer.toString(item.rating));
		
		View sideScreen = view.findViewById(R.id.item_wordlist_screen_side);
		if(position != getSelectedPosition())
				sideScreen.setVisibility(View.INVISIBLE);
		else if(position == getSelectedPosition())
				sideScreen.setVisibility(View.VISIBLE);			
	}
	//=========================================================================
	public static class LoadWordsTask implements IBcTask
	{
		public static Intent createIntent (long dictId) 
		{
			Intent intent = new Intent();
			intent.putExtra(BundleFields.WORD_DICT_ID, dictId);
			return intent;
		}

		private IRStorage mDb;
		private long mDictId;

		public void init(BcTaskEnv env)
		{
			mDictId = env.extras.getLong(BundleFields.WORD_DICT_ID);
			mDb = DbCreator.createReadable(env.context);
		}

		public Bundle execute()
		{
			ArrayList<Word> words = new ArrayList<Word>(mDb.getWordsByDictId(mDictId));
			Bundle result = new Bundle();
			result.putParcelableArrayList(BundleFields.WORDS_LIST, words);
			mDb.destroy();
			return result;
		}
	}
}
