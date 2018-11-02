package ca.mcgill.ecse321.ridesharerpassenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.loopj.android.http.*;
import org.json.*;
import cz.msebera.android.httpclient.*;

public class SignUp extends AppCompatActivity {

    String error = "";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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
        refreshErrorMessage();
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

    public void createPassenger(View v) {
        error = "";
        final TextView ta = (TextView) findViewById(R.id.new_username);
        final TextView tb = (TextView) findViewById(R.id.new_password);
        final TextView tc = (TextView) findViewById(R.id.newFirstName);
        final TextView td = (TextView) findViewById(R.id.newLastName);
        final TextView te = (TextView) findViewById(R.id.newEmail);
        final TextView tf = (TextView) findViewById(R.id.newPhoneNumber);

        if(ta.getText().toString().equals("") || tb.getText().toString().equals("") || tc.getText().toString().equals("") ||
                td.getText().toString().equals("") || te.getText().toString().equals("") || tf.getText().toString().equals("")){
            error = "Please fill in all fields";
            refreshErrorMessage();
            return;
        }

        if(!(te.getText().toString().contains("@"))){
            error = "Please enter a valid email";
            refreshErrorMessage();
            return;
        }

        if(!(tf.getText().toString().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}"))){
            error = "Please enter a valid phone number";
            refreshErrorMessage();
            return;
        }

        username = ta.getText().toString();

        HttpUtils.post("/User/createPassenger?username=" + ta.getText().toString() +
                "&password=" + tb.getText().toString() + "&firstName=" + tc.getText().toString()
                + "&lastName=" + td.getText().toString() + "&email=" + te.getText().toString()
                + "&phoneNumber=" + tf.getText().toString(), new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                username = ta.getText().toString();
                refreshErrorMessage();
                viewMainMenu();
                ta.setText("");
                tb.setText("");
                tc.setText("");
                td.setText("");
                te.setText("");
                tf.setText("");
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

    public void viewMainMenu(){
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }
}
