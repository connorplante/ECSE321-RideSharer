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

//    private void refreshErrorMessage() {
//        // set the error message
//        TextView tvError = (TextView) findViewById(R.id.error);
//        tvError.setText(error);
//
//        if (error == null || error.length() == 0) {
//            tvError.setVisibility(View.GONE);
//        } else {
//            tvError.setVisibility(View.VISIBLE);
//        }
//
//    }

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
    public void viewConfirmBooking(View v){
        Intent intent = new Intent(this, ConfirmBooking.class);
        startActivity(intent);
    }
    public void viewCompleteTrip(View v){
        Intent intent = new Intent(this, CompleteTrip.class);
        startActivity(intent);
    }

    public void findDriversTripsCancel(View v) {
        error = "";


        String url = "/Trip/scheduledTripsOfDriver?username=donya";


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


        System.out.println("=======================================");

        intent.putExtras(b);
        startActivity(intent);
    }


    //COPIED RIGHT HERE


    public void findDriversTripsUpdate(View v) {
        error = "";


        String url = "/Trip/scheduledTripsOfDriver?username=donya";


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


                // call next view to display trips
                getTripsInfo2(tripIds);
            }

            @Override
            public void onFinish() {
                //refreshErrorMessage();
System.out.println("On FINISH");

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


        System.out.println("=======================================");

        intent.putExtras(b);
        startActivity(intent);
    }

}



















