package ca.mcgill.ecse321.ridesharerpassenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RateDriver extends AppCompatActivity {

    String error = "";
    String driveruser;
    public RatingBar ratingBar;
    public TextView driverName;
    public Button submitButton;

    public static final String DRIVER = "ca.mcgill.ecse321.ridesharerpassenger.driver";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);
        if (getIntent().hasExtra(DRIVER)) {
            driveruser = getIntent().getStringExtra(DRIVER);
        } else {
            throw new IllegalArgumentException("cannot find extras " + DRIVER);
        }

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        driverName = (TextView) findViewById(R.id.driverName);
        submitButton = (Button) findViewById(R.id.buttonRating);
        driveruser = driveruser.replaceAll("[^A-Za-z]+", "");
        driverName.setText(driveruser);
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

    public void changeRating(View v) {
        final int numStars = ratingBar.getNumStars();
        final TextView driver = (TextView) findViewById(R.id.driverName);
        final String username = "user1";

        driver.setText(username);

        HttpUtils.post("/User/updateRating?username=" + username + "&rating=" + numStars,
                new RequestParams(), new JsonHttpResponseHandler() {
                    @Override
                    public void onFinish() {
                        refreshErrorMessage();
                        viewPreviousTrips();
                        driver.setText("");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                            errorResponse){
                        try {
                            error += errorResponse.get("message").toString();
                        } catch (JSONException e) {
                            error += e.getMessage();
                        }
                        refreshErrorMessage();
                    }
                });
    }

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.error);
        tvError.setText(error);

        if (error == null) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }

    }

    public void viewPreviousTrips(){
        Intent intent = new Intent(this, PreviousTrips.class);
        startActivity(intent);
    }
}

