package org.omich.lang.app.words;

import org.omich.lang.R;
import org.omich.lang.apptool.activity.BcActivity;
import org.omich.tool.events.Listeners.IListener;
import org.omich.tool.events.Listeners.IListenerInt;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        protected void onCreate (Bundle b)
        {
                super.onCreate(b);
                id = getIntent().getExtras().getLong("id");
                nativ = getIntent().getExtras().getString("nativ");
                foreign = getIntent().getExtras().getString("foreign");
                setContentView(R.layout.app_screen_edit_word);

                dictSpinner = new DictSpinner((Spinner)findViewById(R.id.addword_dictSpinner), this, true, false, new IListenerInt()
                {
                        public void handle (int key)
                        { 
                                if(key == DictSpinner.ADD_DICT)
                                        startAddDictActivity();
                        }                       
                });
                
                ((EditText)findViewById(R.id.editWord_edit_nativ)).setText(nativ);
                ((EditText)findViewById(R.id.editWord_edit_foreign)).setText(foreign);          
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
                
                String newNativ = ((EditText)findViewById(R.id.editWord_edit_nativ)).getText().toString();
                String newForeign = ((EditText)findViewById(R.id.editWord_edit_foreign)).getText().toString();
                String taskAddText = getResources().getString(R.string.editWord_report_changed);
                
                if(newNativ.equals(""))
                {
                        TextView errorView = (TextView) findViewById(R.id.addword_errorReport_nativ);
                        errorView.setTextColor(Color.RED);
                        errorView.setText(R.string.addword_report_empty);
                        error = true;
                }
                
                if(newForeign.equals(""))
                {
                        TextView errorView = (TextView) findViewById(R.id.addword_errorReport_nativ);
                        errorView.setTextColor(Color.RED);
                        errorView.setText(R.string.addword_report_empty);
                        error = true;
                }
                
                if(!error)
                {
                        long dictId = dictSpinner.getIdFromDBForSelectedItem();
                        Intent intent = EditWordTask.createIntent(id, newNativ, newForeign, dictId, taskAddText);
                        mEditWordTaskId = getBcConnector().startTypicalTask(EditWordTask.class, intent, new IListener<Bundle>()
                                {
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
}
