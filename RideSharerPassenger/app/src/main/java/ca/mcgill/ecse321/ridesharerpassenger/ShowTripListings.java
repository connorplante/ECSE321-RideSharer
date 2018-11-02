package ca.mcgill.ecse321.ridesharerpassenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class ShowTripListings extends AppCompatActivity {

    String error = "";
    String username = "";

    ArrayList<Integer> tripIds;
    ArrayList<String> prices;
    ArrayList<String> numSeats;
    ArrayList<String> status;
    ArrayList<String> dates;
    ArrayList<ArrayList<String>> stopsLists;
    String start;
    String end;

    public static final String tripIDs = "ca.mcgill.ecse321.ridesharerpassenger.tripIDs";
    public static final String START = "ca.mcgill.ecse321.ridesharerpassenger.start";
    public static final String END = "ca.mcgill.ecse321.ridesharerpassenger.end";
    public static final String PRICES = "ca.mcgill.ecse321.ridesharerpassenger.prices";
    public static final String NUMSEATS = "ca.mcgill.ecse321.ridesharerpassenger.numSeats";
    public static final String STATUS = "ca.mcgill.ecse321.ridesharerpassenger.status";
    public static final String DATES = "ca.mcgill.ecse321.ridesharerpassenger.dates";
    public static final String STOPSLISTS = "ca.mcgill.ecse321.ridesharerpassenger.stopslists";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_trip_listings);

        // Get the username passed to this page
        if (getIntent().hasExtra(MainMenu.USERNAME)) {
            username = getIntent().getStringExtra(MainMenu.USERNAME);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + MainMenu.USERNAME);
        }

        // Get the tripIDs passed to this page from TripListings
        if (getIntent().hasExtra(ShowTripListings.tripIDs)) {
            tripIds = getIntent().getIntegerArrayListExtra(tripIDs);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + tripIDs);
        }

        // Get the tripIDs passed to this page from TripListings
        if (getIntent().hasExtra(START)) {
            start = getIntent().getStringExtra(START);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + START);
        }

        // Get the tripIDs passed to this page from TripListings
        if (getIntent().hasExtra(END)) {
            end = getIntent().getStringExtra(END);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + END);
        }

        if (getIntent().hasExtra(PRICES)) {
            prices = getIntent().getStringArrayListExtra(PRICES);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + PRICES);
        }

        if (getIntent().hasExtra(NUMSEATS)) {
            numSeats = getIntent().getStringArrayListExtra(NUMSEATS);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + NUMSEATS);
        }

        if (getIntent().hasExtra(DATES)) {
            dates = getIntent().getStringArrayListExtra(DATES);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + DATES);
        }

        if (getIntent().hasExtra(STATUS)) {
            status = getIntent().getStringArrayListExtra(STATUS);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + STATUS);
        }

        stopsLists = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < dates.size(); i++) {
            ArrayList<String> stops = new ArrayList<String>();
            if (getIntent().hasExtra(STOPSLISTS + i)) {
                stops = getIntent().getStringArrayListExtra(STOPSLISTS + i);
            } else {
                throw new IllegalArgumentException("Activity cannot find  extras " + STOPSLISTS);
            }
            stopsLists.add(stops);
        }

        // from tutorial
        ListView listView = (ListView) findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tripIds.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_trip_listing, null);

            TextView textView_tripId = (TextView) convertView.findViewById(R.id.textView_tripId);
            TextView textView_date = (TextView) convertView.findViewById(R.id.textView_date);
            TextView textView_numSeats = (TextView) convertView.findViewById(R.id.textView_numSeats);
            TextView textView_price = (TextView) convertView.findViewById(R.id.textView_price);
            Button button_book = (Button) convertView.findViewById(R.id.button_book);

            textView_tripId.setText(tripIds.get(position).toString());
            textView_date.setText(dates.get(position));
            textView_price.setText("$ " + prices.get(position));
            textView_numSeats.setText(numSeats.get(position));
            button_book.setText("book " + tripIds.get(position).toString());

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TripListings.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in_or_up, menu);
        return true;
    }

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, TripListings.class);
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

    public void showMap(View view) {
        TextView textView_tripId = (TextView) view.findViewById(R.id.textView_tripId);
        String id = textView_tripId.getText().toString();

        int i = 0;
        for (i = 0; i < tripIds.size(); i++) {
            if (tripIds.get(i).toString().equals(id)) {
                break;
            }
        }

        ArrayList<String> stops = stopsLists.get(i);

        Bundle b = new Bundle();
        b.putStringArrayList(GoogleMapsActivity.STOPS, stops);
        b.putIntegerArrayList(ShowTripListings.tripIDs, tripIds);
        b.putStringArrayList(ShowTripListings.DATES, dates);
        b.putStringArrayList(ShowTripListings.NUMSEATS, numSeats);
        b.putStringArrayList(ShowTripListings.STATUS, status);
        b.putStringArrayList(ShowTripListings.PRICES, prices);

        for (int j = 0; j < stopsLists.size(); j++) {
            b.putStringArrayList(ShowTripListings.STOPSLISTS + j, stopsLists.get(j));
        }

        b.putString(ShowTripListings.START, start);
        b.putString(ShowTripListings.END, end);

        Intent intent = new Intent(this, GoogleMapsActivity.class);
        intent.putExtras(b);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    public void bookTrip(View view) {

        Button button_book = view.findViewById(R.id.button_book);

        String buttonText = button_book.getText().toString();
        String tripId = buttonText.substring(5);
        String url = "/Request/createRequest?passengerName=" + username + "&tripID=" + tripId + "&start=" + start + "&end=" + end;

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Successfully requested!");
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
