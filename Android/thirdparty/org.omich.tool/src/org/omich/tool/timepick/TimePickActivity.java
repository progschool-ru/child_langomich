package org.omich.tool.timepick;

import javax.annotation.Nonnull;

import org.omich.tool.R;
import org.omich.tool.errors.OmRuntimeException;
import org.omich.tool.events.Listeners.IListenerLong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TimePickActivity extends Activity
{
	public static class LayoutException extends OmRuntimeException
	{
		private static final long serialVersionUID = 1L;

		private LayoutException(Context context)
		{
			super(context.getString(R.string.error_timepick_TimpPickActivity_incorrectLayout));
		}
	}
	
	public static final @Nonnull String BF_MAX_HOURS = "TimePickerActivity.maxHours"; //$NON-NLS-1$
	public static final @Nonnull String BF_SHOW_SECONDS = "TimePickerActivity.showSeconds"; //$NON-NLS-1$
	public static final @Nonnull String BF_TIMESTAMP = "TimePickerActivity.timestamp"; //$NON-NLS-1$

	public static @Nonnull Intent createIntent (@Nonnull Context context,
			long milliseconds, boolean showSeconds, int maxHours)
	{
		Intent intent = new Intent(context, TimePickActivity.class);
		intent.putExtra(BF_TIMESTAMP, milliseconds);
		intent.putExtra(BF_SHOW_SECONDS, showSeconds);
		intent.putExtra(BF_MAX_HOURS, maxHours);
		return intent;
	}

	//=========================================================================
	
	//==== live cycle =========================================================
	@Override
	protected void onCreate (Bundle b)
	{
		super.onCreate(b);
		long milliseconds = getIntent().getExtras().getLong(BF_TIMESTAMP);
		boolean showSeconds = getIntent().getExtras().getBoolean(BF_SHOW_SECONDS, true);
		int maxHours = getIntent().getExtras().getInt(BF_MAX_HOURS, 24);

		setContentView(R.layout.timepick_picker_view);
		View v = findViewById(R.id.timepick_picker);
		if(v != null)
		{
			TimePickController controller = new TimePickController(v, showSeconds, maxHours, 0);
			controller.pickTime(milliseconds, new IListenerLong()
			{
				public void handle(long value) {onOkClicked(value);}
			});
		}
		else
		{
			throw new LayoutException(this);
		}
	}

	//=========================================================================
	private void onOkClicked (long milliseconds)
	{
		Intent intent = new Intent();
		intent.putExtra(BF_TIMESTAMP, milliseconds);
		setResult(RESULT_OK, intent);
		finish();
	}
}
