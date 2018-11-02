package ca.mcgill.ecse321.ridesharerpassenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListPreviousRides extends AppCompatActivity {

    String error = "";
    String username = "";

    ArrayList<String> trips;
    ArrayList<String> places;
    ArrayList<String> days;
    ArrayList<String> times;
    ArrayList<String> numSeats;

    public static final String TRIPS = "ca.mcgill.ecse321.ridesharerpassenger.trips";
    public static final String PLACES = "ca.mcgill.ecse321.ridesharerpassenger.places";
    public static final String DAYS = "ca.mcgill.ecse321.ridesharerpassenger.days";
    public static final String TIMES = "ca.mcgill.ecse321.ridesharerpassenger.times";
    public static final String NUMSEATS = "ca.mcgill.ecse321.ridesharerpassenger.numSeats";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list_previous_rides);

        // Get the username passed to this page
        if (getIntent().hasExtra(MainMenu.USERNAME)) {
            username = getIntent().getStringExtra(MainMenu.USERNAME);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + MainMenu.USERNAME);
        }

        //Get TripIDs passed from PreviousTrips
        if (getIntent().hasExtra(TRIPS)) {
            trips = getIntent().getStringArrayListExtra(TRIPS);
        }

        //Get Places passed from PreviousTrips
        if (getIntent().hasExtra(PLACES)) {
            places = getIntent().getStringArrayListExtra(PLACES);
        }

        //Get Days passed from PreviousTrips
        if (getIntent().hasExtra(DAYS)) {
            days = getIntent().getStringArrayListExtra(DAYS);
        }

        //Get Times passed from PreviousTrips
        if (getIntent().hasExtra(TIMES)) {
            times = getIntent().getStringArrayListExtra(TIMES);
        }

        //Get NumSeats passed from PreviousTrips
        if (getIntent().hasExtra(NUMSEATS)) {
            numSeats = getIntent().getStringArrayListExtra(NUMSEATS);
        }


        ListView listView = (ListView) findViewById(R.id.listView);
        ListPreviousRides.CustomAdapter customAdapter = new ListPreviousRides.CustomAdapter();
        listView.setAdapter(customAdapter);
    }

        class CustomAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                return trips.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                view = getLayoutInflater().inflate(R.layout.custom_previousrides, null);
                TextView tripIdView = (TextView) view.findViewById(R.id.textTripID);
                TextView routeView = (TextView) view.findViewById(R.id.textView7);
                TextView timeView = (TextView) view.findViewById(R.id.textView8);
                TextView dateView = (TextView) view.findViewById(R.id.textView9);
                TextView numSeatsView = (TextView) view.findViewById(R.id.textView10);

                if (trips.size() == 0 || places.size() == 0) {
                    tripIdView.setText("");
                    routeView.setText("No trips ended");
                    dateView.setText("");
                    timeView.setText("");
                    numSeatsView.setText("");
                    return view;
                }

                tripIdView.setText("Trip " + trips.get(position));
                routeView.setText(places.get(position));
                dateView.setText("Date: " + days.get(position));
                timeView.setText("Time: " + times.get(position));
                numSeatsView.setText("Num Seats: " + numSeats.get(position));

                return view;

            }

        }
    public void viewRateDriver (View v) {
        error = "";

        TextView tripIdView = (TextView) ListPreviousRides.this.findViewById(R.id.textTripID);
        String tripID = tripIdView.getText().toString();
        tripID = tripID.substring(5);

        System.out.print("\n tripID is :" + tripID);
        String url = "/User/showDriverForTrip?TripID="+tripID;
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                String s = response.toString();

                s = s.replaceAll("\\[", "");
                s = s.replaceAll("\\]", ",");

                viewRateDriver(s);
                }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in_or_up, menu);
        return true;
    }

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, PreviousTrips.class);
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

   public void viewRateDriver(String driver){
        Intent intent = new Intent(this, RateDriver.class);
        Bundle b = new Bundle();
        b.putStringArrayList(ListPreviousRides.TRIPS, trips);
        b.putString(RateDriver.DRIVER, driver);

        intent.putExtras(b);
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
}
