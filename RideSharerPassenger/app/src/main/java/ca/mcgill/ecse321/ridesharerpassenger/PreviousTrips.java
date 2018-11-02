package ca.mcgill.ecse321.ridesharerpassenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PreviousTrips extends AppCompatActivity {

    String error = "";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_trips);

        // Get the username passed to this page
        if (getIntent().hasExtra(MainMenu.USERNAME)) {
            username = getIntent().getStringExtra(MainMenu.USERNAME);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + MainMenu.USERNAME);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in_or_up, menu);
        return true;
    }

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onUpButtonPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void selectCompletedTrips(View v){
        error = "";
        final String driverUsername = username;

        String url = "/Trip/completedTrips?username="+driverUsername;
        System.out.print("\n Url :"+ url);
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

                ArrayList<String> trips = new ArrayList<String>();
                ArrayList<String> places = new ArrayList<String>();
                ArrayList<String> days = new ArrayList<String>();
                ArrayList<String> times = new ArrayList<String>();
                ArrayList<String> numSeats = new ArrayList<String>();

                String s = response.toString();

                s = s.replaceAll("\\[", "");
                s = s.replaceAll("\\]", ",");
                String[] str= s.split("\"");


                ArrayList<String> strings = new ArrayList<String>();

                for(int i = 1; i < str.length; i += 2){
                    strings.add(str[i]);
                }

                for(int j = 0; j < strings.size(); j++){
                    try {
                        if (j % 5 == 0) {
                            trips.add(strings.get(j));
                        }
                        if (j % 5 == 1) {
                            places.add(strings.get(j));
                        }
                        if (j % 5 == 2) {
                            days.add(strings.get(j));
                        }
                        if (j % 5 == 3) {
                            times.add(strings.get(j));
                        }
                        if (j % 5 == 4) {
                            numSeats.add(strings.get(j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                viewListPreviousRides(trips, places, days, times, numSeats);

            }

            @Override
            public void onFinish() {
                refreshErrorMessage();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ArrayList<String> trips = new ArrayList<String>();
                ArrayList<String> places = new ArrayList<String>();
                ArrayList<String> days = new ArrayList<String>();
                ArrayList<String> times = new ArrayList<String>();
                ArrayList<String> numSeats = new ArrayList<String>();
                viewListPreviousRides(trips, places, days, times, numSeats);
            }
        });

    }

    public void viewListPreviousRides(ArrayList<String> trips, ArrayList<String> places, ArrayList<String> days, ArrayList<String> times, ArrayList<String> numSeats){
        Intent intent = new Intent(this, ListPreviousRides.class);
        intent.putExtra(MainMenu.USERNAME, username);
        Bundle b = new Bundle();
        b.putStringArrayList(ListPreviousRides.TRIPS, trips);
        b.putStringArrayList(ListPreviousRides.PLACES, places);
        b.putStringArrayList(ListPreviousRides.DAYS, days);
        b.putStringArrayList(ListPreviousRides.TIMES, times);
        b.putStringArrayList(ShowTripListings.NUMSEATS, numSeats);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void viewRateDriver(View v){
        Intent intent = new Intent(this, RateDriver.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }
}
