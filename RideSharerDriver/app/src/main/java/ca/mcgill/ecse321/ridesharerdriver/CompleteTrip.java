package ca.mcgill.ecse321.ridesharerdriver;

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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CompleteTrip extends AppCompatActivity {

    String username = "";

    String error = "";

    ArrayList<String> trips;
    ArrayList<String> places;
    ArrayList<String> days;
    ArrayList<String> times;
    ArrayList<String> numSeats;

    public static final String TRIPS = "ca.mcgill.ecse321.ridesharerdriver.trips";
    public static final String PLACES = "ca.mcgill.ecse321.ridesharerdriver.places";
    public static final String DAYS  = "ca.mcgill.ecse321.ridesharerdriver.days";
    public static final String TIMES = "ca.mcgill.ecse321.ridesharerdriver.times";
    public static final String NUMSEATS = "ca.mcgill.ecse321.ridesharerdriver.numSeats";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().hasExtra(TRIPS)){
            trips = getIntent().getStringArrayListExtra(TRIPS);
        }

        if(getIntent().hasExtra(PLACES)){
            places = getIntent().getStringArrayListExtra(PLACES);
        }

        if(getIntent().hasExtra(DAYS)){
            days = getIntent().getStringArrayListExtra(DAYS);
        }

        if(getIntent().hasExtra(TIMES)){
            times = getIntent().getStringArrayListExtra(TIMES);
        }

        if(getIntent().hasExtra(NUMSEATS)){
            numSeats = getIntent().getStringArrayListExtra(NUMSEATS);
        }

        ListView listView = (ListView)findViewById(R.id.listView);
        CompleteTrip.CustomAdapter customAdapter = new CompleteTrip.CustomAdapter();
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
              view = getLayoutInflater().inflate(R.layout.custom_complete_trips_layout, null);
              TextView tripIdView = (TextView)view.findViewById(R.id.textView5);
              TextView routeView = (TextView)view.findViewById(R.id.textView7);
              TextView timeView = (TextView)view.findViewById(R.id.textView8);
              TextView dateView = (TextView)view.findViewById(R.id.textView9);
              TextView numSeatsView = (TextView)view.findViewById(R.id.textView10);
              TextView completeTrip = (TextView)view.findViewById(R.id.textView11);

            if(trips.size() == 0 || places.size() == 0){
                tripIdView.setText("");
                routeView.setText("No trips planned");
                dateView.setText("");
                timeView.setText("");
                numSeatsView.setText("");
                completeTrip.setText("");
                return view;
            }

            tripIdView.setText("Trip " + trips.get(position));
            routeView.setText(places.get(position));
            dateView.setText("Date: " + days.get(position));
            timeView.setText("Time: " + times.get(position));
            numSeatsView.setText("Num Seats: " + numSeats.get(position));
            completeTrip.setText("Complete Trip " + trips.get(position));

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_or_in, menu);
        return true;
    }

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, ManageTrips.class);
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

    public void finishTrip(View v){
        final TextView toFinish = (TextView) v.findViewById(R.id.textView11);

        String str = toFinish.getText().toString();
        str = str.replaceAll("[^\\d.]", "");

        String url = "Trip/completeTrip?tripID=" + str + "&username=" + username;
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        TextView tvError = (TextView) findViewById(R.id.error);
                        tvError.setText("Trip completed");
                    }
                }
        );
    }
}
