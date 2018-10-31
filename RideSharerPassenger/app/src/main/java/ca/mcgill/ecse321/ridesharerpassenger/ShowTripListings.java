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

        // Get the tripIDs passed to this page from TripListings
        if (getIntent().hasExtra(tripIDs)) {
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



        System.out.println("ShowTripListings:");
        System.out.println(dates);
        System.out.println(prices);
        System.out.println(numSeats);
        System.out.println(status);
        System.out.println(stopsLists);

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

            textView_tripId.setText(tripIds.get(position).toString());
            textView_date.setText(dates.get(position));
            textView_price.setText("$ " + prices.get(position));
            textView_numSeats.setText(numSeats.get(position));

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in_or_up, menu);
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

    public void showMap(View view) {
        TextView textView_tripId = (TextView) view.findViewById(R.id.textView_tripId);
        String id = textView_tripId.getText().toString();

        System.out.println("id = " + id);

        int i = 0;
        for (i = 0; i < tripIds.size(); i++) {
            if (tripIds.get(i).toString().equals(id)) {
                break;
            }
        }

        System.out.println("i = " + i);
        System.out.println("tripIds length = " + tripIds.size());
        System.out.println("stopsLists length = " + stopsLists.size());

        ArrayList<String> stops = stopsLists.get(i);

        System.out.println(stops);

        Bundle b = new Bundle();
        b.putStringArrayList(GoogleMapsActivity.STOPS, stops);
        GoogleMapsUtil.getMapData();

        Intent intent = new Intent(this, GoogleMapsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void bookTrip(View view) {
        //Intent intent = new Intent(this, GoogleMapsActivity.class);
        //startActivity(intent);
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
