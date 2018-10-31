package ca.mcgill.ecse321.ridesharerdriver;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;



import com.loopj.android.http.*;


public class CreateFinalTrip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_final_trip);
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


    public void createFinalTrip(View v){
       Bundle extras = getIntent().getExtras();



        String date = extras.getString("DATE");
        String time = extras.getString("TIME");
        String start = extras.getString("START");
        String end = extras.getString("END");
        String username = extras.getString("USERNAME");
        String carID = extras.getString("CARID");
        String numSeats = extras.getString("NUMSEATS");
        String stops = extras.getString("ROUTES");
        String prices = extras.getString("PRICES");


        HttpUtils.post("/Trip/createTrip?start=" + start +
                "&end=" + end + "&date=" + date
                + "&time=" + time + "&username=" + username
                + "&carID=" + carID + "&numSeats=" + numSeats + stops + prices, new RequestParams(), new JsonHttpResponseHandler(){




        });


    }
}