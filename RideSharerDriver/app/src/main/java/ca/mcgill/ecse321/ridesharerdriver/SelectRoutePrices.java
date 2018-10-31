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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route_prices);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
//        tvError.setVisibility(View.GONE);
//
////        if (error == null || error != "i") {
////            tvError.setVisibility(View.GONE);
////        } else {
////            tvError.setVisibility(View.VISIBLE);
////        }
//
//    }

    public void switchViewCreateFinalTrip(View v){
        final TextView stops = (TextView) findViewById(R.id.editText5);
        final TextView prices = (TextView) findViewById(R.id.editText7);
        String stops2 = stops.getText().toString();
        String[] finalStops = stops2.split(",");
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


        Intent intent6 = new Intent(this, CreateFinalTrip.class);

        Bundle extras = getIntent().getExtras();
        intent6.putExtras(extras);
        intent6.putExtra("ROUTES", urlStops);
        intent6.putExtra("PRICES", urlPrices);



        startActivity(intent6);
    }

}
