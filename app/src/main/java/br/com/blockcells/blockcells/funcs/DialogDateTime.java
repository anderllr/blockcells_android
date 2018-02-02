package br.com.blockcells.blockcells.funcs;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.blockcells.blockcells.R;


/**
 * Created by anderson on 08/12/16.
 * This function set a TimePicker or a DatePicker on EditText
 */

public class DialogDateTime {

    public static void setTimePicker(final Context context, final EditText Edit_Time) {

        Edit_Time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar c = Calendar.getInstance();

                //Start conversion of EditText
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm"); // Make sure user insert date into edittext in this format.

                String date = "2016-01-01";
                String time = "08:00";

                if (! Edit_Time.getText().toString().isEmpty()) {
                    time = Edit_Time.getText().toString();
                }

                Date dateObject = null;
                try {
                    dateObject = formatter.parse(date + " " + time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.setTime(dateObject);

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                //Create and return a new instance of TimePickerDialog
                /*
                   public constructor.....
                      TimePickerDialog(Context context, int theme,
                      TimePickerDialog.OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView)

                   The 'theme' parameter allow us to specify the theme of TimePickerDialog

            .......List of Themes.......
            THEME_DEVICE_DEFAULT_DARK
            THEME_DEVICE_DEFAULT_LIGHT
            THEME_HOLO_DARK
               THEME_HOLO_LIGHT
                THEME_TRADITIONAL

         */

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, 2, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = String.valueOf(selectedHour);
                        String minute = String.valueOf(selectedMinute);

                        if (selectedHour < 10) {
                            hour = "0" + hour;
                        }
                        if (selectedMinute < 10) {
                            minute = "0" + minute;
                        }
                        Edit_Time.setText(hour + ":" + minute);
                    }
                }, hour, minute, true);
                TextView tvTitle = new TextView(context);
                tvTitle.setText(R.string.descTitleTimePicker);
                tvTitle.setBackgroundColor(Color.parseColor("#EEE8AA"));
                tvTitle.setPadding(5, 3, 5, 3);
                tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
                mTimePicker.setCustomTitle(tvTitle);

//                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }
}
