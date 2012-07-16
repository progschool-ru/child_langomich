package org.omich.lang.app.words;

import java.util.ArrayList;
import java.util.List;

import org.omich.lang.R;
import org.omich.lang.app.BundleFields;
import org.omich.lang.app.db.DbCreator;
import org.omich.lang.app.db.IRStorage;
import org.omich.lang.app.db.Word;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.lang.apptool.events.Listeners.IListener;
import org.omich.tool.bcops.BcEventHelper;
import org.omich.tool.bcops.BcService;
import org.omich.tool.bcops.IBcConnector;
import org.omich.tool.bcops.IBcTask;

import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class GameActivity extends BcActivity
{
	@SuppressWarnings("unused")
	private boolean mIsDestroyed;
	private TextView tvn;
	private TextView tvf;
	private Button bt1;
	private Button bt2;
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.app_screen_game);
		tvn = (TextView)findViewById(R.id.item_wordslist_text_nativ);
		tvf = (TextView)findViewById(R.id.item_wordslist_text_foreign);
		bt1 = (Button)findViewById(R.id.button_1);
		bt2 = (Button)findViewById(R.id.button_2);
		tvn.setText("");
		tvf.setText("");
		bt1.setText("Знаю");
		bt2.setText("Не знаю");
		getWord();
	}
	
	@Override
	protected void onDestroy ()
	{
//		gameAdapter.destroy();
//		gameAdapter = null;
		
		mIsDestroyed = true;
		super.onDestroy();
	}
	Word word;
	private String mGetWordTaskId;
	public void getWord ()
	{
		if(mGetWordTaskId != null)
			return;

		IBcConnector conn = getBcConnector();
		conn.startTask(BcService.class, GetWordTask.class, 
				new Intent(), new IListener<Intent>()
				{
					public void handle(Intent value)
					{
						BcEventHelper.parseEvent(value, null, null, new IListener<Bundle>()
						{
							public void handle (Bundle b)
							{
								mGetWordTaskId = null;
			
								word = b.getParcelable(BundleFields.WORD);	
								tvn.setText(word.nativ);
							}
						}, null, null);
					}
				});	
	}	
	//=========================================================================
}
