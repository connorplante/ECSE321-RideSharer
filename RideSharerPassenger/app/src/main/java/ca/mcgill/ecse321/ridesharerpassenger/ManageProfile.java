package ca.mcgill.ecse321.ridesharerpassenger;

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

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class ManageProfile extends AppCompatActivity {

    String error = "";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Get the username passed to this page
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
        getMenuInflater().inflate(R.menu.menu_sign_in_or_up, menu);
        return true;
    }

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, MainMenu.class);
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

    public void viewUserInfo(View v){
        error = "";
        final String username = "samcattani";
        String url = "/User/displayProfileInfo?username=" + username;

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

                String firstName = "";
                String lastName = "";
                String email = "";
                String phone = "";

                try {
                    firstName = response.get(0).toString();
                    lastName = response.get(1).toString();
                    email = response.get(2).toString();
                    phone = response.get(3).toString();
                } catch (Exception e){

                }
                viewUpdateInfo(firstName, lastName, email, phone);
            }
        });
    }

    public void viewUpdateInfo(String firstName, String lastName, String email, String phone){
        Intent intent = new Intent(this, UpdateInfo.class);
        Bundle b = new Bundle();
        b.putString(UpdateInfo.FIRSTNAME, firstName);
        b.putString(UpdateInfo.LASTNAME, lastName);
        b.putString(UpdateInfo.EMAIL, email);
        b.putString(UpdateInfo.PHONE, phone);
        intent.putExtras(b);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    public void viewRemoveUser(View v){
        Intent intent = new Intent(this, RemoveUser.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    public void viewChangePassword(View v){
        Intent intent = new Intent(this, ChnagePassword.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }
}
