package org.omich.tool.timepick;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.omich.tool.R;
import org.omich.tool.constants.Strings;
import org.omich.tool.events.Listeners.IListenerLong;
import org.omich.tool.events.Listeners.INistener;
import org.omich.tool.timepick.UnitSelectionController.EventData;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TimePickController implements ITimePicker
{
	private int mHours;
	private int mMinutes;
	private int mSeconds;
	
	private @Nullable TextView mHoursView;
	private @Nullable TextView mMinutesView;
	private @Nullable TextView mSecondsView;
	
	private @Nullable UnitSelectionController mHoursUsc;
	private @Nullable UnitSelectionController mMinutesUsc;
	private @Nullable UnitSelectionController mSecondsUsc;

	private @Nullable IListenerLong mHandler;
	private boolean mShowSeconds;
	private long mResetTime;
	private final @Nonnull String mHoursFormat;

	public TimePickController (@Nonnull View v, boolean showSeconds,
			int maxHours,
			long resetTime)
	{
		mShowSeconds = showSeconds;
		mResetTime = resetTime;
		mHoursFormat = maxHours > 24 ? Strings.FORMAT_0 : Strings.FORMAT_00;

		mHoursView = (TextView)v.findViewById(R.id.timepick_picker_hoursView);
		mMinutesView = (TextView)v.findViewById(R.id.timepick_picker_minutesView);
		mSecondsView = (TextView)v.findViewById(R.id.timepick_picker_secondsView);
		
		@Nullable View secondsContainer = v.findViewById(R.id.timepick_picker_secondsContainer);
		if(secondsContainer != null)
		{
			secondsContainer.setVisibility(showSeconds ? View.VISIBLE : View.GONE);
		}
		
		@Nullable View okButton = v.findViewById(R.id.timepick_picker_okButton);
		if(okButton != null)
		{
			okButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v){onOkClicked();}
			});
		}

		@Nullable View resetButton = v.findViewById(R.id.timepick_picker_resetButton);
		if(resetButton != null)
		{
			resetButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v){onResetClicked();}
			});
		}
		
		@Nullable View hoursUp = v.findViewById(R.id.timepick_picker_hoursUpButton);
		if(hoursUp != null)
		{
			hoursUp.setOnClickListener(new OnClickListener()
			{
				public void onClick (View v) {onHoursUp();}
			});
		}
		
		@Nullable View hoursDown = v.findViewById(R.id.timepick_picker_hoursDownButton);
		if(hoursDown != null)
		{
			hoursDown.setOnClickListener(new OnClickListener()
			{
				public void onClick (View v) {onHoursDown();}
			});
			hoursDown.setOnLongClickListener(new OnLongClickListener()
			{
				public boolean onLongClick(View v)
				{
					mHours = 0;
					updateTextViews();
					return false;
				}
			});
		}

		@Nullable ListView hoursList = (ListView)v.findViewById(R.id.timepick_picker_hoursList);
		if(hoursList != null)
		{
			mHoursUsc = new UnitSelectionController(hoursList, 0, maxHours, true, 0, mHoursFormat);
			mHoursUsc.setSelectionChangeListener(new INistener<UnitSelectionController.EventData>()
			{
				public void handle(@Nonnull EventData data)
				{
					mHours = data.value;
					updateTextViews();
				}
			});
		}
		
		@Nullable View minutesUp = v.findViewById(R.id.timepick_picker_minutesUpButton);
		if(minutesUp != null)
		{
			minutesUp.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) {onMinutesUp();}
			});
		}

		@Nullable View minutesDown = v.findViewById(R.id.timepick_picker_minutesDownButton);
		if(minutesDown != null)
		{
			minutesDown.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) {onMinutesDown();}
			});
			minutesDown.setOnLongClickListener(new OnLongClickListener()
			{
				public boolean onLongClick(View v)
				{
					mMinutes = 0;
					updateTextViews();
					return false;
				}
			});
		}
		
		@Nullable ListView minutesList = (ListView)v.findViewById(R.id.timepick_picker_minutesList);
		if(minutesList != null)
		{
			mMinutesUsc = new UnitSelectionController(minutesList, 0, 60, true, 0, Strings.FORMAT_00);
			mMinutesUsc.setSelectionChangeListener(new INistener<UnitSelectionController.EventData>()
			{
				public void handle(@Nonnull EventData data)
				{
					int oldValue = mMinutes;
					mMinutes = data.value;
					
					if(data.isOver)
					{
						if(oldValue < mMinutes)
						{
							downHours();
						}
						else
						{
							upHours();
						}
					}

					updateTextViews();
				}
			});
		}

		@Nullable View secondsUp = v.findViewById(R.id.timepick_picker_secondsUpButton);
		if(secondsUp != null)
		{
			secondsUp.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) {onSecondsUp();}
			});
		}

		@Nullable View secondsDown = v.findViewById(R.id.timepick_picker_secondsDownButton);
		if(secondsDown != null)
		{
			secondsDown.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) {onSecondsDown();}
			});
			secondsDown.setOnLongClickListener(new OnLongClickListener()
			{
				public boolean onLongClick(View v)
				{
					mSeconds = 0;
					updateTextViews();
					return false;
				}
			});
		}
		
		@Nullable ListView secondsList = (ListView)v.findViewById(R.id.timepick_picker_secondsList);
		if(secondsList != null)
		{
			mSecondsUsc = new UnitSelectionController(secondsList, 0, 60, true, 0, Strings.FORMAT_00);
			mSecondsUsc.setSelectionChangeListener(new INistener<UnitSelectionController.EventData>()
			{
				public void handle(@Nonnull EventData data)
				{
					int oldValue = mSeconds;
					mSeconds = data.value;
					
					if(data.isOver)
					{
						if(oldValue < mSeconds)
						{
							downMinutes();
						}
						else
						{
							upMinutes();
						}
					}

					updateTextViews();
				}
			});
		}
		
		updateTextViews();
	}
	
	//==== ITimePicker ========================================================
	public void pickTime(long milliseconds, @Nullable IListenerLong handler)
	{
		int[] parsed = parseTimeToHoursMinutesSeconds(milliseconds);

		mHours = parsed[0];
		mMinutes = parsed[1];
		mSeconds = parsed[2];

		mHandler = handler;
		
		updateTextViews(false);
	}
	
	//==== events =============================================================
	public void onOkClicked ()
	{
		if(!mShowSeconds)
		{
			mSeconds = 0;
		}

		long result = (mHours * 3600L + mMinutes * 60L + mSeconds) * 1000L;
		if(mHandler != null)
		{
			mHandler.handle(result);
		}
	}

	private void onResetClicked ()
	{
		int[] resetTime = parseTimeToHoursMinutesSeconds(mResetTime);
		mHours = resetTime[0];
		mMinutes = resetTime[1];
		mSeconds = resetTime[2];
		updateTextViews();
	}
	
	private void onHoursUp ()
	{
		upHours();
		updateTextViews();
	}

	private void onHoursDown ()
	{
		downHours();
		updateTextViews();
	}

	private void onMinutesUp ()
	{
		upMinutes();
		updateTextViews();
	}

	private void onMinutesDown ()
	{
		downMinutes();
		updateTextViews();
	}

	private void onSecondsUp ()
	{
		upSeconds();
		updateTextViews();
	}

	private void onSecondsDown ()
	{
		downSeconds();
		updateTextViews();
	}
	
	//========================================================================
	private void updateTextViews() {updateTextViews(true);}
	private void updateTextViews (boolean withSmoothing)
	{
		if(mHoursView != null)
		{
			mHoursView.setText(String.format(mHoursFormat, mHours));
		}
		if(mMinutesView != null)
		{
			mMinutesView.setText(String.format(Strings.FORMAT_00, mMinutes));
		}
		if(mSecondsView != null)
		{
			mSecondsView.setText(String.format(Strings.FORMAT_00, mSeconds));
		}
		if(mHoursUsc != null)
		{
			mHoursUsc.setSelectedPosition(mHours, withSmoothing);
		}
		if(mMinutesUsc != null)
		{
			mMinutesUsc.setSelectedPosition(mMinutes, withSmoothing);
		}
		if(mSecondsUsc != null)
		{
			mSecondsUsc.setSelectedPosition(mSeconds, withSmoothing);
		}
	}
	
	private static int [] parseTimeToHoursMinutesSeconds (long milliseconds)
	{
		long inSeconds = milliseconds / 1000;
		long inMinutes = inSeconds / 60;
		long inHours = inMinutes / 60;

		int[] result = new int[3];
		result[2] = (int)inSeconds % 60;
		result[1] = (int)inMinutes % 60;
		result[0] = (int)inHours;

		return result;
	}
	
	private void upHours ()
	{
		++mHours;
	}

	private boolean downHours ()
	{
		if(mHours == 0)
			return false;
		--mHours;
		return true;
	}
	
	private void upMinutes ()
	{
		++mMinutes;
		
		if(mMinutes == 60)
		{
			upHours();
			mMinutes = 0;
		}
	}
	
	private boolean downMinutes ()
	{
		--mMinutes;
		
		if(mMinutes == -1)
		{
			if(downHours())
			{
				mMinutes = 59;
				return true;
			}
			else
			{
				mMinutes = 0;
				return false;
			}
		}
		else
		{
			return true;
		}
	}
	
	private void upSeconds ()
	{
		++mSeconds;
		
		if(mSeconds == 60)
		{
			upMinutes();
			mSeconds = 0;
		}
	}
	
	private boolean downSeconds ()
	{
		--mSeconds;
		
		if(mSeconds == -1)
		{
			if(downMinutes())
			{
				mSeconds = 59;
				return true;
			}
			else
			{
				mSeconds = 0;
				return false;
			}
		}
		else
		{
			return true;
		}
	}
}
