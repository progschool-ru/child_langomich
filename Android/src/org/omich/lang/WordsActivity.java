package org.omich.lang;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class WordsActivity extends Activity implements OnClickListener {

	private TextView viewLogin;
	private TextView viewPassword;
	private TextView viewResponse;
	private Button exitButton;
	private Button loadButton;
	
	private String login;
	private String password;
	
	private SmdClient smdClient;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.words);
		
		viewLogin = (TextView) findViewById(R.id.textView1);
		viewPassword = (TextView) findViewById(R.id.textView2);
		viewResponse = (TextView) findViewById(R.id.textView3);
		exitButton = (Button) findViewById(R.id.button1);
		loadButton = (Button) findViewById(R.id.button2);
		
		Intent intent = getIntent();
		
		login = intent.getStringExtra(Constants.STR_LOGIN);
		password = intent.getStringExtra(Constants.STR_PASSWORD);
		
		viewLogin.append(login);
		viewPassword.append(password);
		
	
		
		exitButton.setOnClickListener(this);
		loadButton.setOnClickListener(this);
	
	
	}
	
	public void onPause(){
		super.onPause();
	}
	
	public void onClick(View g){
		switch(g.getId()){
			case R.id.button1:
				finish();
				break;
			case R.id.button2:
				smdClient = new SmdClient();
				try{
					viewResponse.append(smdClient.auth(login, password)+"\n");
					viewResponse.append(smdClient.getWords());
					
					
					//String jString = "{\"success\":false}";
					//String jString2 = 
					//smdClient.auth(login, password);
					//JSONObject jObject = new JSONObject(jString);
					//JSONObject user = jObject.getJSONObject("user");
					//String userID = user.getString("userId");
					//String login = user.getString("login");
					//boolean success = jObject.getBoolean("success");
					//if(success){
					//	viewResponse.append("Авторизация прошла успешно\n");
					//}else{
					//	   viewResponse.append("логин или пароль введены не верно");
					//}
					
				}catch(Exception e){
					viewResponse.append("error "+e.getMessage()+"\n");
				}
				break;
		}
	}

}
