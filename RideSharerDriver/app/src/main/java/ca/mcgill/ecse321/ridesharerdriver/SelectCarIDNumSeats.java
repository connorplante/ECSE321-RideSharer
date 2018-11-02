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
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.view.View;
import android.widget.ArrayAdapter;
import java.lang.Object;
import java.util.ArrayList;

import android.R.layout;
import android.widget.TextView;


public class SelectCarIDNumSeats extends AppCompatActivity {

    String error = "i";
    String username = "";

    ArrayList<String> carIDs;
    ArrayList<String> makes;
    ArrayList<String> models;

    public static final String CARIDS = "ca.mcgill.ecse321.ridesharerdriver.carIDs";
    public static final String MAKES = "ca.mcgill.ecse321.ridesharerdriver.makes";
    public static final String MODELS = "ca.mcgill.ecse321.ridesharerdriver.models";


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

        if (getIntent().hasExtra(CARIDS)) {
            carIDs = getIntent().getStringArrayListExtra(CARIDS);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + CARIDS);
        }

        if (getIntent().hasExtra(MAKES)) {
            makes = getIntent().getStringArrayListExtra(MAKES);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + MAKES);
        }

        if (getIntent().hasExtra(MODELS)) {
            models = getIntent().getStringArrayListExtra(MODELS);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + MODELS);
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        SelectCarIDNumSeats.CustomAdapter customAdapter = new SelectCarIDNumSeats.CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return carIDs.size();
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

            convertView = getLayoutInflater().inflate(R.layout.custom_select_cars, null);

            TextView textView_id = (TextView)convertView.findViewById(R.id.textView_carID);
            TextView textView_make = (TextView)convertView.findViewById(R.id.textView_make);
            TextView textView_model = (TextView)convertView.findViewById(R.id.textView_model);

            textView_id.setText(carIDs.get(position));
            textView_make.setText(makes.get(position));
            textView_model.setText(models.get(position));

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
        Intent intent = new Intent(this, SelectStartEnd.class);
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

    public void switchViewSelectRoutePrices(View v){
        final TextView carID = (TextView) v.findViewById(R.id.textView_carID);
        Intent intent5 = new Intent(this, SelectRoutePrices.class);
        Bundle extras = getIntent().getExtras();
        intent5.putExtras(extras);
        intent5.putExtra("CARID", carID.getText().toString());
        intent5.putExtra(MainMenu.USERNAME, username);
        startActivity(intent5);
    }
}