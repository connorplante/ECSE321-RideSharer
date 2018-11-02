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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ConfirmBooking extends AppCompatActivity {

    String error = "";
    String username = "";

    ArrayList<String> tripIDs;
    ArrayList<String> usernames;
    ArrayList<String> dates;
    ArrayList<String> routes;
    ArrayList<String> requestIDs;

    public static final String TRIPIDs = "ca.mcgill.ecse321.ridesharerdriver.tripIDs";
    public static final String USERNAMES = "ca.mcgill.ecse321.ridesharerdriver.usernames";
    public static final String DATES  = "ca.mcgill.ecse321.ridesharerdriver.dates";
    public static final String ROUTES = "ca.mcgill.ecse321.ridesharerdriver.routes";
    public static final String REQUESTIDs = "ca.mcgill.ecse321.ridesharerdriver.requestIDs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra(TRIPIDs)){
            tripIDs = getIntent().getStringArrayListExtra(TRIPIDs);
        }

        if(getIntent().hasExtra(USERNAMES)){
            usernames = getIntent().getStringArrayListExtra(USERNAMES);
        }

        if(getIntent().hasExtra(DATES)){
            dates = getIntent().getStringArrayListExtra(DATES);
        }

        if(getIntent().hasExtra(ROUTES)){
            routes = getIntent().getStringArrayListExtra(ROUTES);
        }

        if(getIntent().hasExtra(REQUESTIDs)){
            requestIDs = getIntent().getStringArrayListExtra(REQUESTIDs);
        }

        ListView listView = (ListView)findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
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

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return tripIDs.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_layout, null);
            TextView tripIdView = (TextView)view.findViewById(R.id.textView);
            TextView usernameView = (TextView)view.findViewById(R.id.textView2);
            TextView legsView = (TextView)view.findViewById(R.id.textView3);
            TextView dateView = (TextView)view.findViewById(R.id.textView4);
            TextView confirmBook = (TextView)view.findViewById(R.id.button3);
            TextView cancelBook = (TextView)view.findViewById(R.id.button17);

            if(tripIDs.size() == 0 || usernames.size() == 0){
                tripIdView.setText("");
                usernameView.setText("No requests");
                dateView.setText("");
                legsView.setText("");
                return view;
            }

            tripIdView.setText("Trip " + tripIDs.get(position));
            usernameView.setText(usernames.get(position));
            dateView.setText(dates.get(position));
            legsView.setText(routes.get(position));
            confirmBook.setText("Confirm Request " + requestIDs.get(position));
            cancelBook.setText("Reject Request " + requestIDs.get(position));

            return view;
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

    public void rejectBooking(View v){
        final TextView cancel = (TextView) v.findViewById(R.id.button17);
        final TextView confirm = (TextView) findViewById(R.id.textView6);

        String str = cancel.getText().toString();
        str = str.replaceAll("[^\\d.]", "");
        confirm.setText("Choice confirmed");


        String url = "Request/rejectRequest?requestID=" + str;
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

            }
        });
    }

    public void confirmBooking(View v){
        final TextView confirmButton = (TextView) v.findViewById(R.id.button3);
        final TextView confirm = (TextView) findViewById(R.id.textView6);

        String str = confirmButton.getText().toString();
        str = str.replaceAll("[^\\d.]", "");
        confirm.setText("Choice confirmed");

        String url = "Request/acceptRequest?requestID=" + str;
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                String a = "";
                String b = "";
                String username = "";
                String id = "";
                try {
                    a = response.get(0).toString();
                    b = response.get(1).toString();
                    username = response.get(2).toString();
                    id = response.get(3).toString();
                } catch (Exception e) {

                }

                HttpUtils.post("Passenger/confirmBook?tripID=" + id + "&username=" + username + "&pointA=" + a + "&pointB=" + b,
                    new RequestParams(), new JsonHttpResponseHandler(){
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println(responseString);
                    }
                });

            }
        });
    }
}



