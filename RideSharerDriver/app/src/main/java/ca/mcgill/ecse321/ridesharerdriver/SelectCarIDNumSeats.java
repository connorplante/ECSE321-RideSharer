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
import android.app.Activity;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.view.View;
import android.widget.ArrayAdapter;
import java.lang.Object;
import android.R.layout;
import android.widget.TextView;


public class SelectCarIDNumSeats extends AppCompatActivity {

    String error = "i";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car_idnum_seats);
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


    public void switchViewSelectRoutePrices(View v){
        final TextView carID = (TextView) findViewById(R.id.editText4);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        String numSeats = spinner.getSelectedItem().toString();



        Intent intent5 = new Intent(this, SelectRoutePrices.class);
        Bundle extras = getIntent().getExtras();
       // System.out.println("CARID: " +carID.getText().toString() + "NUMSEATS: " + Integer.parseInt(spinner.getSelectedItem().toString()));
        intent5.putExtras(extras);
        intent5.putExtra("CARID", carID.getText().toString());
        intent5.putExtra("NUMSEATS", numSeats);


        startActivity(intent5);
    }

}
