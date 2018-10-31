package ca.mcgill.ecse321.ridesharerdriver;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UpdateTripFields extends AppCompatActivity implements TimePickerFragment.OnCompleteListener, DatePickerFragment.OnCompleteListener {

    String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip_fields);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_or_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }

    }

    public void onComplete(String time) {
        // System.out.print("This is the time: " + time);
    }


    public void showTimePickerOnDialog(View v) {
//        final TextView time = findViewById(R.id.textView1);
//        System.out.print("This is the time: " + time.getText().toString());
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showDatePickerOnDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public void updateTime(View v) {
        Bundle extras = getIntent().getExtras();
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        String time = tv1.getText().toString();
        String tripID = extras.getString("TRIPID");


        HttpUtils.post("/Trip/updateTime?time=" + time + "&tripID=" + tripID, new RequestParams(), new JsonHttpResponseHandler() {


        });


    }

    public void updateDate(View v) {
        Bundle extras = getIntent().getExtras();
        TextView tv1 = (TextView) findViewById(R.id.textView2);
        String date = tv1.getText().toString();
        String tripID = extras.getString("TRIPID");


        HttpUtils.post("/Trip/updateDate?date=" + date + "&tripID=" + tripID, new RequestParams(), new JsonHttpResponseHandler() {


        });


    }

    public void updateStart(View v){
        Bundle extras = getIntent().getExtras();
        final TextView tv1 = (TextView) findViewById(R.id.oldStart);
        final TextView tv2 = (TextView) findViewById(R.id.newStart);
        String tripID = extras.getString("TRIPID");
        String oldStart = tv1.getText().toString();
        String newStart = tv2.getText().toString();

        HttpUtils.post("/Trip/updateStart?oldStart=" + oldStart + "&newStart=" + newStart + "&tripID=" + tripID, new RequestParams(), new JsonHttpResponseHandler() {


        });


    }
}
