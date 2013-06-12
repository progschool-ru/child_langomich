package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerInt;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditWordActivity extends BcActivity
{
        private String mEditWordTaskId;
        private DictSpinner dictSpinner;
        private boolean mIsDestroyed;
        
        private String nativ;
        private String foreign;
        private long id;

        private final int REQUEST_CODE_ADD_DICT = 101;
        
        //==== live cycle =========================================================
        @Override
		protected void onCreate (Bundle b)
        {
                super.onCreate(b);
                id = getIntent().getExtras().getLong("id");
                nativ = getIntent().getExtras().getString("nativ");
                foreign = getIntent().getExtras().getString("foreign");
                setContentView(R.layout.app_screen_edit_word);

                dictSpinner = new DictSpinner((Spinner)findViewById(R.id.editWord_dictSpinner), this, true, false, new IListenerInt()
                {
                        @Override
						public void handle (int key)
                        { 
                                if(key == DictSpinner.ADD_DICT)
                                        startAddDictActivity();
                        }                       
                });
                setEditTextListeners();      
        }
        
        private void setEditTextListeners()
        {
        	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); 
        	
        	EditText nativ_et = (EditText)findViewById(R.id.editWord_edit_nativ);
        	final EditText foreign_et = (EditText) findViewById(R.id.editWord_edit_foreign);
        	nativ_et.setText(nativ);
        	foreign_et.setText(foreign);
        	
        	nativ_et.setSelection(nativ.length());
        	OnEditorActionListener l = new OnEditorActionListener()
        	{        		
        		@Override
        		public boolean onEditorAction(TextView v, int actionId,
        				KeyEvent event) 
        		{
        			boolean handled = false;
        			if(actionId == EditorInfo.IME_ACTION_SEND)
        			{
        				foreign_et.requestFocus();
        				foreign_et.setSelection(foreign.length());
        				handled = true;
        			}
            		return handled;
        		}
        	};
        	nativ_et.setOnEditorActionListener(l);
        	OnEditorActionListener m = new OnEditorActionListener()
        	{
        		@Override
        		public boolean onEditorAction(TextView v, int actionId,
        				KeyEvent event) 
        		{
        			boolean handled = false;
        			if(actionId == EditorInfo.IME_ACTION_SEND)
        			{
        				onEditButton(v);
        				handled = true;
        			}
        			return handled;
        		}
        	};
        	foreign_et.setOnEditorActionListener(m);
        }
        
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) 
        {
                if(requestCode == REQUEST_CODE_ADD_DICT)
                {
                        if(resultCode == RESULT_OK)
                        {
                                if(data!= null)
                                        dictSpinner.reload(data.getLongExtra("dictId", DictSpinner.NULL_DICT)); 
                                else
                                        dictSpinner.reload(DictSpinner.NULL_DICT);
                        }
                        else if(resultCode == RESULT_CANCELED)
                                dictSpinner.reload(DictSpinner.NULL_DICT);
                }
        }               
        @Override
        protected void onDestroy ()
        {
                if(mEditWordTaskId != null)
                {
                        getBcConnector().unsubscribeTask(mEditWordTaskId);
                        mEditWordTaskId = null;
                }       
                mIsDestroyed = true;
                dictSpinner.destroy();
                super.onDestroy();
        }
        private void startAddDictActivity()
        {
            Intent intent = new Intent(this, AddDictActivity.class);
            intent.putExtra("changeDictInPreferences", false);
            startActivityForResult(intent, REQUEST_CODE_ADD_DICT);              
        }       
        //==== events =============================================================     
        public void onEditButton (View v)
        {
                if(mEditWordTaskId != null)
                        return;
                boolean error = false;
                
                String newNativ = ((EditText)findViewById(R.id.editWord_edit_nativ)).getText().toString().trim();
                String newForeign = ((EditText)findViewById(R.id.editWord_edit_foreign)).getText().toString().trim();
                String taskAddText = getResources().getString(R.string.editWord_report_changed);
                
                TextView errorViewNativ = (TextView) findViewById(R.id.editWord_errorReport_nativ_text);
                TextView errorViewForeign = (TextView) findViewById(R.id.editWord_errorReport_foreign_text);
                
                errorViewNativ.setText("");
                errorViewForeign.setText("");
                
                if(newNativ.equals(""))
                {
                        errorViewNativ.setTextColor(getErrorColor());
                        errorViewNativ.setText(R.string.addword_report_empty);
                        error = true;
                }
                
                if(newForeign.equals(""))
                {
                        errorViewForeign.setTextColor(getErrorColor());
                        errorViewForeign.setText(R.string.addword_report_empty);
                        error = true;
                }
                
                if(!error)
                {
                        long dictId = dictSpinner.getIdFromDBForSelectedItem();
                        Intent intent = EditWordTask.createIntent(id, newNativ, newForeign, dictId, taskAddText);
                        mEditWordTaskId = getBcConnector().startTypicalTask(EditWordTask.class, intent, new IListener<Bundle>()
                                {
                                        @Override
										public void handle (Bundle bundle)
                                        {
                                                if(mIsDestroyed)
                                                        return;
                
                                                mEditWordTaskId = null;
                                                
                                    setResult(RESULT_OK);
                                                finish();
                                        }
                                });             
                }
        }
        public void onCancelButton (View v)
        {               
                setResult(RESULT_CANCELED);
                finish();
        }
        
    	private int getErrorColor()
    	{
    		return getResources().getColor(R.color.lang_addWord_error);
    	}
}
