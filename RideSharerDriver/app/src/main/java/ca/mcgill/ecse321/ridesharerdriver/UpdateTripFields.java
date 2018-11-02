package ca.mcgill.ecse321.ridesharerdriver;

import android.content.Intent;
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
    String username = "";

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

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, CreateTrip.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
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

    }

    public void showTimePickerOnDialog(View v) {
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

    public void updateEnd(View v){
        Bundle extras = getIntent().getExtras();
        final TextView tv1 = (TextView) findViewById(R.id.oldEnd);
        final TextView tv2 = (TextView) findViewById(R.id.newEnd);
        String tripID = extras.getString("TRIPID");
        String oldEnd = tv1.getText().toString();
        String newEnd = tv2.getText().toString();

        HttpUtils.post("/Trip/updateEnd?oldEnd=" + oldEnd + "&newEnd=" + newEnd + "&tripID=" + tripID, new RequestParams(), new JsonHttpResponseHandler() {

        });
    }

    public void updateRoutePrices(View v){

        Bundle extras = getIntent().getExtras();

        final TextView stops = (TextView) findViewById(R.id.editText9);
        final TextView prices = (TextView) findViewById(R.id.editText10);
        String tripID = extras.getString("TRIPID");
        String stops2 = stops.getText().toString();
        String[] finalStops = stops2.split("-");
        String urlStops = "";

        for(int i = 0; i < finalStops.length; i++ ){
            String str1 = "&stops=";
            urlStops += str1 + finalStops[i];
        }

        String prices2 = prices.getText().toString();
        String[] finalPrices = prices2.split(",");
        String urlPrices = "";

        for(int j = 0; j < finalPrices.length; j++ ){
            String str2 = "&prices=";
            urlStops += str2 + finalPrices[j];
        }

        HttpUtils.post("/Trip/updateRoute?tripID=" + tripID +
                urlStops + urlPrices, new RequestParams(), new JsonHttpResponseHandler(){

        });
    }
}