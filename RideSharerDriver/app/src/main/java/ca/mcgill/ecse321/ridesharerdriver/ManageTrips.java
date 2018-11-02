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
import android.widget.TextView;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ManageTrips extends AppCompatActivity {

    String error = "";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
        getMenuInflater().inflate(R.menu.menu_sign_up_or_in, menu);
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

    public void getRequests(View v){
        error = "";

        String url = "/Request/showRequestsToDriver?username=" + username;

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

            ArrayList<String> tripIDs = new ArrayList<String>();
            ArrayList<String> usernames = new ArrayList<String>();
            ArrayList<String> dates = new ArrayList<String>();
            ArrayList<String> routes = new ArrayList<String>();
            ArrayList<String> requestIDs = new ArrayList<String>();

            String s = "";

            for(int i = 0; i < response.length(); i++) {
                try {
                    s += response.get(i).toString();
                } catch (Exception e) {
                    System.out.println("here");
                }
            }

            s = s.replaceAll("\"", "");
            s = s.replaceAll("\\[", "");
            s = s.replaceAll("\\]", ",");

            System.out.println(s);

            String[] str = s.split(",");

            for(int j = 0; j < str.length; j++){
                if(j % 5 == 0){
                    tripIDs.add(str[j]);
                }
                else if(j % 5 == 1){
                    usernames.add(str[j]);
                }
                else if(j % 5 == 2){
                    dates.add(str[j]);
                }
                else if (j % 5 == 3){
                    routes.add(str[j]);
                }
                else{
                    requestIDs.add(str[j]);
                }
            }
            viewConfirmBooking(tripIDs, usernames, dates, routes, requestIDs);
            }

        });
    }


    public void selectCompleteTrip(View v){
        error = "";
        String url = "/Trip/tripsToBeCompleted?username=" + username;

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

            ArrayList<String> trips = new ArrayList<String>();
            ArrayList<String> places = new ArrayList<String>();
            ArrayList<String> days = new ArrayList<String>();
            ArrayList<String> times = new ArrayList<String>();
            ArrayList<String> numSeats = new ArrayList<String>();

            String s = "";

            for(int i = 0; i < response.length(); i++) {
                try {
                    s += response.get(i).toString();
                } catch (Exception e) {
                    System.out.println("here");
                }
            }

            s = s.replaceAll("\\[", "");
            s = s.replaceAll("\\]", ",");
            String[] str= s.split("\"");

            ArrayList<String> strings = new ArrayList<String>();

            for(int i = 1; i < str.length; i += 2){
                strings.add(str[i]);
            }

            for(int j = 0; j < strings.size(); j++){
                if(j % 5 == 0){
                    trips.add(strings.get(j));
                }
                if(j % 5 == 1){
                    places.add(strings.get(j));
                }
                if(j % 5 == 2){
                    days.add(strings.get(j));
                }
                if(j % 5 == 3){
                    times.add(strings.get(j));
                }
                if(j % 5 == 4){
                    numSeats.add(strings.get(j));
                }
            }
            viewCompleteTrip(trips, places, days, times, numSeats);
            }

        });
    }

    public void viewUpdateTrip(View v){
        Intent intent = new Intent(this, UpdateTrip.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    public void viewCreateTrip(View v){
        Intent intent = new Intent(this, CreateTrip.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    public void viewCancelTrip(View v){
        Intent intent = new Intent(this, CancelTrip.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }
    public void viewConfirmBooking(ArrayList<String> tripIDs, ArrayList<String> usernames, ArrayList<String> dates, ArrayList<String> routes, ArrayList<String> requestIDs){
        Intent intent = new Intent(this, ConfirmBooking.class);
        Bundle b = new Bundle();
        b.putStringArrayList(ConfirmBooking.TRIPIDs, tripIDs);
        b.putStringArrayList(ConfirmBooking.USERNAMES, usernames);
        b.putStringArrayList(ConfirmBooking.DATES, dates);
        b.putStringArrayList(ConfirmBooking.ROUTES, routes);
        b.putStringArrayList(ConfirmBooking.REQUESTIDs, requestIDs);
        intent.putExtras(b);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }
    public void viewCompleteTrip(ArrayList<String> trips, ArrayList<String> places, ArrayList<String> days, ArrayList<String> times, ArrayList<String> numSeats){
        Intent intent = new Intent(this, CompleteTrip.class);
        Bundle b = new Bundle();
        b.putStringArrayList(CompleteTrip.TRIPS, trips);
        b.putStringArrayList(CompleteTrip.PLACES, places);
        b.putStringArrayList(CompleteTrip.DAYS, days);
        b.putStringArrayList(CompleteTrip.TIMES, times);
        b.putStringArrayList(CompleteTrip.NUMSEATS, numSeats);
        intent.putExtras(b);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    public void findDriversTripsCancel(View v) {
        error = "";
        String url = "/Trip/scheduledTripsOfDriver?username=" + username;

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                ArrayList<Integer> tripIds = new ArrayList<>();

                // add trip ids from response to the array list
                for (int i = 0; i < response.length(); i++) {
                    try {
                        tripIds.add(response.getInt(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // call next view to display trips
                getTripsInfo(tripIds);
            }

            @Override
            public void onFinish() {
                //refreshErrorMessage();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
               // refreshErrorMessage();
            }
        });
    }

    public void getTripsInfo(final ArrayList<Integer> tripIds){
        // from me! get tripId info
        String tripIdList = "";

        for (int i = 0; i < tripIds.size(); i++) {
            if (i == tripIds.size()-1) {
                tripIdList = tripIdList + tripIds.get(i).toString();
            } else {
                tripIdList = tripIdList + tripIds.get(i).toString() + ",";
            }
        }
        String start = "Ottawa";
        String end = "Toronto";
        String url = "/Trip/tripInfo?" + "tripIds=" + tripIdList + "&start=" + start + "&end=" + end;

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

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
                viewFoundTripsCancel(tripIds, prices, dates, numSeats, status, stopsLists);
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
                //refreshErrorMessage();
            }
        });
    }

    public void viewFoundTripsCancel(ArrayList<Integer> tripIds, ArrayList<String> prices,
                               ArrayList<String> dates, ArrayList<String> numSeats, ArrayList<String> status,
                               ArrayList<ArrayList<String>> stopsLists){

        Intent intent = new Intent(this, CancelTrip.class);
        Bundle b = new Bundle();
        b.putIntegerArrayList(CancelTrip.tripIDs, tripIds);
        b.putStringArrayList(CancelTrip.DATES, dates);
        b.putStringArrayList(CancelTrip.NUMSEATS, numSeats);
        b.putStringArrayList(CancelTrip.STATUS, status);
        b.putStringArrayList(CancelTrip.PRICES, prices);
        for (int i = 0; i < stopsLists.size(); i++) {
            b.putStringArrayList(CancelTrip.STOPSLISTS + i, stopsLists.get(i));
        }
        intent.putExtra(MainMenu.USERNAME, username);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void findDriversTripsUpdate(View v) {
        error = "";
        String url = "/Trip/scheduledTripsOfDriver?username=donya";

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                ArrayList<Integer> tripIds = new ArrayList<>();

                // add trip ids from response to the array list
                for (int i = 0; i < response.length(); i++) {
                    try {
                        tripIds.add(response.getInt(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // call next view to display trips
                getTripsInfo2(tripIds);
            }

            @Override
            public void onFinish() {
                //refreshErrorMessage();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                // refreshErrorMessage();
            }
        });
    }

    public void getTripsInfo2(final ArrayList<Integer> tripIds){
        String tripIdList = "";

        for (int i = 0; i < tripIds.size(); i++) {
            if (i == tripIds.size()-1) {
                tripIdList = tripIdList + tripIds.get(i).toString();
            } else {
                tripIdList = tripIdList + tripIds.get(i).toString() + ",";
            }
        }

        String start = "Ottawa";
        String end = "Toronto";
        String url = "/Trip/tripInfo?" + "tripIds=" + tripIdList + "&start=" + start + "&end=" + end;

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                // add trip ids from response to the array list
                ArrayList<ArrayList<String>> stopsLists = new ArrayList<ArrayList<String>>();
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

                viewFoundTripsUpdate(tripIds, prices, dates, numSeats, status, stopsLists);
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
                //refreshErrorMessage();
            }
        });
    }

    public void viewFoundTripsUpdate(ArrayList<Integer> tripIds, ArrayList<String> prices,
                                     ArrayList<String> dates, ArrayList<String> numSeats, ArrayList<String> status,
                                     ArrayList<ArrayList<String>> stopsLists){

        Intent intent = new Intent(this, UpdateTrip.class);
        Bundle b = new Bundle();
        b.putIntegerArrayList(UpdateTrip.tripIDs, tripIds);
        b.putStringArrayList(UpdateTrip.DATES, dates);
        b.putStringArrayList(UpdateTrip.NUMSEATS, numSeats);
        b.putStringArrayList(UpdateTrip.STATUS, status);
        b.putStringArrayList(UpdateTrip.PRICES, prices);

        for (int i = 0; i < stopsLists.size(); i++) {
            b.putStringArrayList(UpdateTrip.STOPSLISTS + i, stopsLists.get(i));
        }

        intent.putExtra(MainMenu.USERNAME, username);
        intent.putExtras(b);
        startActivity(intent);
    }
}



















