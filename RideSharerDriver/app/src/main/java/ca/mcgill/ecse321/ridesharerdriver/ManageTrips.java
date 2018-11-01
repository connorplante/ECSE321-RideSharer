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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trips);
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

    public void getRequests(View v){
        error = "";
        final String driverUsername = "donya";

        String url = "/Request/showRequestsToDriver?username="+driverUsername;

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
        final String driverUsername = "donya";

        String url = "/Trip/tripsToBeCompleted?username="+driverUsername;

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
        startActivity(intent);
    }

    public void viewCreateTrip(View v){
        Intent intent = new Intent(this, CreateTrip.class);
        startActivity(intent);
    }

    public void viewCancelTrip(View v){
        Intent intent = new Intent(this, CancelTrip.class);
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
        startActivity(intent);
    }

}
