package ca.mcgill.ecse321.ridesharerdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;



import com.loopj.android.http.*;


public class CreateFinalTrip extends AppCompatActivity {

    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_final_trip);
        Bundle extras = getIntent().getExtras();

        if (getIntent().hasExtra(MainMenu.USERNAME)) {
            username = getIntent().getStringExtra(MainMenu.USERNAME);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + MainMenu.USERNAME);
        }

        String date = extras.getString("DATE");
        String time = extras.getString("TIME");
        String start = extras.getString("START");
        String end = extras.getString("END");
        String routesC = extras.getString("ROUTES");
        String pricesC = extras.getString("PRICES");

        TextView dateT = (TextView) findViewById(R.id.textView_tripDate);
        dateT.setText(date);

        TextView timeT = (TextView) findViewById(R.id.textView_tripTime);
        timeT.setText(time);

        TextView startT = (TextView) findViewById(R.id.textView_tripStart);
        startT.setText(start);

        TextView endT = (TextView) findViewById(R.id.textView_tripEnd);
        endT.setText(end);

        TextView routesT = (TextView) findViewById(R.id.textView_tripRoute);
        routesT.setText(routesC);

        TextView pricesT = (TextView) findViewById(R.id.textView_tripPrices);
        pricesT.setText(pricesC);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, CreateTrip.class);
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

    public void createFinalTrip(View v){
        Bundle extras = getIntent().getExtras();

        String date = extras.getString("DATE");
        String time = extras.getString("TIME");
        String start = extras.getString("START");
        String end = extras.getString("END");
        String carID = extras.getString("CARID");
        String numSeats = extras.getString("NUMSEATS");
        String stops = extras.getString("URLROUTES");
        String prices = extras.getString("URLPRICES");
        String routesC = extras.getString("ROUTES");
        String pricesC = extras.getString("PRICES");

        TextView dateT = (TextView) findViewById(R.id.textView_tripDate);
        dateT.setText(date);

        TextView timeT = (TextView) findViewById(R.id.textView_tripTime);
        timeT.setText(time);

        TextView startT = (TextView) findViewById(R.id.textView_tripStart);
        startT.setText(start);

        TextView endT = (TextView) findViewById(R.id.textView_tripEnd);
        endT.setText(end);

        TextView routesT = (TextView) findViewById(R.id.textView_tripRoute);
        routesT.setText(routesC);

        TextView pricesT = (TextView) findViewById(R.id.textView_tripPrices);
        pricesT.setText(pricesC);

        System.out.println("/Trip/createTrip?start=" + start +
                "&end=" + end + "&date=" + date
                + "&time=" + time + "&username=" + username
                + "&carID=" + carID + "&numSeats=" + numSeats + stops + prices);

        HttpUtils.post("/Trip/createTrip?start=" + start +
                "&end=" + end + "&date=" + date
                + "&time=" + time + "&username=" + username
                + "&carID=" + carID + "&numSeats=" + numSeats + stops + prices, new RequestParams(), new JsonHttpResponseHandler(){
        });

        Intent intent = new Intent(this, FinishCreateTrip.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }
}