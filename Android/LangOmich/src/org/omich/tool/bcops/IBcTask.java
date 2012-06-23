package org.omich.tool.bcops;

import org.omich.lang.apptool.events.Listeners.IListenerInt;

import android.content.Context;
import android.os.Bundle;

public interface IBcTask
{
	void init (Bundle extras, Context context);
	Bundle execute (IListenerInt ph, ICancelledInfo ci) throws Exception;
}
