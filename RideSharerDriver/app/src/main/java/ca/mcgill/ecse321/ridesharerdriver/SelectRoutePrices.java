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

public class SelectRoutePrices extends AppCompatActivity {

    String error = "i";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route_prices);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_or_in, menu);
        return true;
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

    public void switchViewCreateFinalTrip(View v){
        final TextView stops = (TextView) findViewById(R.id.editText5);
        final TextView prices = (TextView) findViewById(R.id.editText7);
        String stops2 = stops.getText().toString();
        String[] finalStops = stops2.split("-");
        String urlStops = "";

        for(int i = 0; i < finalStops.length; i++ ){
            String str1 = "&stops=";
            urlStops += str1 + finalStops[i];
        }

        String prices2 = prices.getText().toString();
        String[] finalPrices = prices2.split(",");
        String urlPrices = "";

        for(int j = 0; j < finalPrices.length; j++ ){
            String str2 = "&prices=";
            urlStops += str2 + finalPrices[j];
        }

        Intent intent = new Intent(this, CreateFinalTrip.class);

        Bundle extras = getIntent().getExtras();
        intent.putExtras(extras);
        intent.putExtra("ROUTES",stops2);
        intent.putExtra("PRICES",prices2);
        intent.putExtra("URLROUTES", urlStops);
        intent.putExtra("URLPRICES", urlPrices);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }
}
