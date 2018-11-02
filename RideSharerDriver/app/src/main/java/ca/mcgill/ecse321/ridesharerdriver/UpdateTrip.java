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

import java.util.ArrayList;

public class UpdateTrip extends AppCompatActivity {

    String username = "";
    String error = "";
    ArrayList<Integer> tripIds;
    ArrayList<String> prices;
    ArrayList<String> numSeats;
    ArrayList<String> status;
    ArrayList<String> dates;
    ArrayList<ArrayList<String>> stopsLists;


    public static final String tripIDs = "ca.mcgill.ecse321.ridesharerdriver.tripIDs";
    public static final String START = "ca.mcgill.ecse321.ridesharerdriver.start";
    public static final String END = "ca.mcgill.ecse321.ridesharerdriver.end";
    public static final String PRICES = "ca.mcgill.ecse321.ridesharerdriver.prices";
    public static final String NUMSEATS = "ca.mcgill.ecse321.ridesharerdriver.numSeats";
    public static final String STATUS = "ca.mcgill.ecse321.ridesharerdriver.status";
    public static final String DATES = "ca.mcgill.ecse321.ridesharerdriver.dates";
    public static final String STOPSLISTS = "ca.mcgill.ecse321.ridesharerdriver.stopslists";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip);
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

        if (getIntent().hasExtra(tripIDs)) {
            tripIds = getIntent().getIntegerArrayListExtra(tripIDs);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + tripIDs);
        }

        // Get the tripIDs passed to this page from TripListings


        // Get the tripIDs passed to this page from TripListings


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



        ListView listView = (ListView) findViewById(R.id.listView);
        UpdateTrip.CustomAdapter customAdapter = new UpdateTrip.CustomAdapter();
        listView.setAdapter(customAdapter);


    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return tripIds.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i){
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup){


            convertView = getLayoutInflater().inflate(R.layout.custom_update_trip, null);

            TextView textView_id = (TextView)convertView.findViewById(R.id.textView_updateID);
            TextView textView_date = (TextView)convertView.findViewById(R.id.textView_updateDate);
            TextView textView_time = (TextView)convertView.findViewById(R.id.textView_updateTime);
            TextView textView_start = (TextView)convertView.findViewById(R.id.textView_updateStart);
            TextView textView_end = (TextView)convertView.findViewById(R.id.textView_updateEnd);




            textView_id.setText(tripIds.get(position).toString());
            textView_date.setText(dates.get(position));

            textView_time.setText(status.get(position));

            return convertView;
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


    public void switchViewUpdateTripFields(View v){
        Intent intent = new Intent(this, UpdateTripFields.class);
        TextView tv1 = (TextView) v.findViewById(R.id.textView_updateID);
        intent.putExtra("TRIPID", tv1.getText().toString());
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

}
