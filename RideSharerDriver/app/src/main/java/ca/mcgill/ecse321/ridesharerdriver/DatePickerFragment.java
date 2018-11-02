package ca.mcgill.ecse321.ridesharerdriver;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.lang.String;

import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Bundle;
import android.text.format.DateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static interface OnCompleteListener {
        public abstract void onComplete(String date);
    }

    private DatePickerFragment.OnCompleteListener mListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (DatePickerFragment.OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView tv1=(TextView) getActivity().findViewById(R.id.textView2);
        int correctMonth = (view.getMonth()) + 1;

        String sCorrectMonth = Integer.toString(correctMonth);
        if(correctMonth < 10){
            sCorrectMonth = "0" + sCorrectMonth;
        }

        tv1.setText(view.getYear()+"-"+sCorrectMonth +"-"+view.getDayOfMonth());
    }
}




