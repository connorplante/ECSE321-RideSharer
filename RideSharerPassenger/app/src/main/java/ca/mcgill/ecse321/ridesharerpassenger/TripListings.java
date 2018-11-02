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
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;



public class TripListings extends AppCompatActivity {

    String error = "";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Get the username passed to this page
        if (getIntent().hasExtra(MainMenu.USERNAME)) {
            username = getIntent().getStringExtra(MainMenu.USERNAME);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + MainMenu.USERNAME);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    // Search for trips //
    public void findTrip(View v) {
        error = "";
        final TextView tStart = (TextView) findViewById(R.id.start);
        final TextView tEnd = (TextView) findViewById(R.id.end);
        final TextView tPrice = (TextView) findViewById(R.id.price);
        final TextView tMake = (TextView) findViewById(R.id.make);
        final TextView tModel = (TextView) findViewById(R.id.model);
        final TextView tYear = (TextView) findViewById(R.id.year);
        final TextView tLowDate = (TextView) findViewById(R.id.low_date);
        final TextView tHighDate = (TextView) findViewById(R.id.high_date);

        String url = "/Trip/findTrip?";

        if (tStart.getText().toString().equals("")) {
            error += "Enter a start location\n";
        } else {
            url += "start=" + tStart.getText().toString();
        }

        if (tEnd.getText().toString().equals("")) {
            error += "Enter an end location";
        } else {
            url += "&end=" + tEnd.getText().toString();
        }

        if (!tPrice.getText().toString().equals("")) {
            url += "&price=" + tPrice.getText().toString();
        }

        if (!tMake.getText().toString().equals("")) {
            url += "&make=" + tMake.getText().toString();
        }

        if (!tModel.getText().toString().equals("")) {
            url += "&model=" + tModel.getText().toString();
        }

        if (!tYear.getText().toString().equals("")) {
            url += "&year=" + tYear.getText().toString();
        }

        if (!tLowDate.getText().toString().equals("")) {
            url += "&lowDate=" + tLowDate.getText().toString();
        }

        if (!tHighDate.getText().toString().equals("")) {
            url += "&highDate=" + tHighDate.getText().toString();
        }

        final String start = tStart.getText().toString();
        final String end = tEnd.getText().toString();

        System.out.println("Url: " + url);

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Success!!!");
                System.out.println("Response: ");
                System.out.println(response.toString());
                ArrayList<Integer> tripIds = new ArrayList<>();

                // add trip ids from response to the array list
                for (int i = 0; i < response.length(); i++) {
                    try {
                        tripIds.add(response.getInt(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("ArrayList: " + tripIds.toString());
                System.out.println("start: " + start);
                System.out.println("end: " + end);

                // call next view to display trips
                getTripsInfo(tripIds, start, end);
            }

            @Override
            public void onFinish() {
                refreshErrorMessage();
                tStart.setText("");
                tEnd.setText("");
                tPrice.setText("");
                tMake.setText("");
                tModel.setText("");
                tYear.setText("");
                tLowDate.setText("");
                tHighDate.setText("");

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

    public void getTripsInfo(final ArrayList<Integer> tripIds, final String start, final String end){
        // from me! get tripId info
        String tripIdList = "";

        for (int i = 0; i < tripIds.size(); i++) {
            if (i == tripIds.size()-1) {
                tripIdList = tripIdList + tripIds.get(i).toString();
            } else {
                tripIdList = tripIdList + tripIds.get(i).toString() + ",";
            }
        }
        String url = "/Trip/tripInfo?" + "tripIds=" + tripIdList + "&start=" + start + "&end=" + end;
        System.out.println(url);
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Success!!!");
                System.out.println("Response: ");
                System.out.println(response.toString());

                // add trip ids from response to the array list
                ArrayList<ArrayList<String>> stopsLists = new ArrayList<ArrayList<String>>();
                ArrayList<String> stopsStrs = new ArrayList<String>();
                ArrayList<String> dates = new ArrayList<String>();
                ArrayList<String> prices = new ArrayList<String>();
                ArrayList<String> numSeats = new ArrayList<String>();
                ArrayList<String> status = new ArrayList<String>();


                for (int i = 0; i < response.length(); i++) {
                    ArrayList<String> stopList = new ArrayList<String>();
                    try {
                        for (int j = 0; j < response.getJSONArray(i).length(); j++){
                            if ( j == 0) {
                                dates.add(response.getJSONArray(i).getString(0));
                            } else if ( j == 1) {
                                prices.add(response.getJSONArray(i).getString(1));
                            } else if ( j == 2) {
                                numSeats.add(response.getJSONArray(i).getString(2));
                            } else if ( j == 3) {
                                status.add(response.getJSONArray(i).getString(3));
                            } else {
                                stopList.add(response.getJSONArray(i).getString(j));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    stopsLists.add(stopList);
                }

                System.out.println("response in arraylist of arraylists:");
                System.out.println(dates);
                System.out.println(prices);
                System.out.println(status);
                System.out.println(numSeats);
                System.out.println(stopsLists);

                viewFoundTrips(tripIds, start, end, prices, dates, numSeats, status, stopsLists);

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

    public void viewFoundTrips(ArrayList<Integer> tripIds, String start, String end, ArrayList<String> prices,
                               ArrayList<String> dates, ArrayList<String> numSeats, ArrayList<String> status,
                               ArrayList<ArrayList<String>> stopsLists){

        Intent intent = new Intent(this, ShowTripListings.class);
        Bundle b = new Bundle();
        b.putIntegerArrayList(ShowTripListings.tripIDs, tripIds);
        b.putStringArrayList(ShowTripListings.DATES, dates);
        b.putStringArrayList(ShowTripListings.NUMSEATS, numSeats);
        b.putStringArrayList(ShowTripListings.STATUS, status);
        b.putStringArrayList(ShowTripListings.PRICES, prices);
        for (int i = 0; i < stopsLists.size(); i++) {
            b.putStringArrayList(ShowTripListings.STOPSLISTS + i, stopsLists.get(i));
        }

        b.putString(ShowTripListings.START, start);
        b.putString(ShowTripListings.END, end);

        System.out.println("=======================================");

        intent.putExtras(b);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

}
