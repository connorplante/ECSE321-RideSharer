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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_listings);
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
                viewFoundTrips(tripIds);
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

    public void viewFoundTrips(ArrayList<Integer> tripIds){
        Intent intent = new Intent(this, ShowTripListings.class);
        Bundle b = new Bundle();
        b.putIntegerArrayList("tripIds", tripIds);
        intent.putExtras(b);
        startActivity(intent);
    }
}
