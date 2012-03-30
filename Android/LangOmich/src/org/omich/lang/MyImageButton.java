package org.omich.lang;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton {
	
	Drawable enable;
	Drawable disable;
	
	boolean enabled = true;
	
	boolean resourseIsSet = false;
	
	public MyImageButton(Context context){
		super(context);
	}
	
	public void setImageResources(int  resEnableId, int resDisableId){
		resourseIsSet = true;
		enable = getResources().getDrawable(resEnableId);
		disable = getResources().getDrawable(resDisableId);
		if(enabled){
			super.setImageDrawable(enable);
		}else{
			super.setImageDrawable(disable);
		}
	}
	
	@Override
	public void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		this.enabled = enabled;
		if(enabled && resourseIsSet){
				super.setImageDrawable(enable);	
		}else{
				super.setImageDrawable(disable);
		}
	}
}

