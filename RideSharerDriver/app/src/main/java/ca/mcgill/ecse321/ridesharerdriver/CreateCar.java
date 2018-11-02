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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CreateCar extends AppCompatActivity {

    String error = "";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_or_in, menu);
        return true;
    }

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, ManageCar.class);
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

        if (error == null || error == "i") {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    public void createCar(View v) {
        error = "";
        final TextView ta = (TextView) findViewById(R.id.make);
        final TextView tb = (TextView) findViewById(R.id.model);
        final TextView tc = (TextView) findViewById(R.id.year);
        final TextView td = (TextView) findViewById(R.id.numSeats);
        final TextView te = (TextView) findViewById(R.id.licensePlate);
        final String user = username;

        String make = ta.getText().toString();
        make = make.replaceAll("[^0-9A-Za-z]", "");
        String model = tb.getText().toString();
        model = model.replaceAll("[^0-9A-Za-z]", "");
        String year = tc.getText().toString();
        year = year.replaceAll("[^0-9]", "");
        String numSeats = td.getText().toString();
        numSeats = numSeats.replaceAll("[^0-9]", "");
        String licencePlate = te.getText().toString();
        licencePlate = licencePlate.replaceAll("[^0-9A-Za-z]", "");

        HttpUtils.post("Car/createCar?make=" + make + "&model=" + model +
                "&year=" + year + "&numSeats=" + numSeats + "&licencePlate=" +
                licencePlate + "&username=" + user, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                refreshErrorMessage();
                viewManageCars();
                ta.setText("");
                tb.setText("");
                tc.setText("");
                td.setText("");
                te.setText("");
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

    public void viewManageCars() {
        Intent intent = new Intent(this, ManageCar.class);
        startActivity(intent);
    }
}

