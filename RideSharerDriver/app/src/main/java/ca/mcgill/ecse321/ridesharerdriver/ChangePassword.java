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

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class ChangePassword extends AppCompatActivity {

    String username = "";
    String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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
        Intent intent = new Intent(this, ManageProfile.class);
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

        if (error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    public void changePassword(View v){
        error = "";
        final TextView ta = (TextView) findViewById(R.id.cpOldPass);
        final TextView tb = (TextView) findViewById(R.id.cpNewPass);
        final TextView tc = (TextView) findViewById(R.id.cpNewPassConfirm);

        if(ta.getText().toString().equals("") || tb.getText().toString().equals("") || tc.getText().toString().equals("") ){
            error = "Fill in all fields!";
            refreshErrorMessage();
            return;
        }

        if(!(tb.getText().toString().equals(tc.getText().toString()))){
            error = "New passwords do not match";
            refreshErrorMessage();
            return;
        }

        String username = "samcattani";

        String url = "/User/resetPassword?username=" + username + "&currentPassword=" + ta.getText().toString() + "&newPassword=" + tb.getText().toString();

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

                Boolean b = false;

                try{
                    String s = response.get(0).toString();
                    b = Boolean.parseBoolean(s);
                }catch(Exception e){

                }

                if(b){
                    refreshErrorMessage();
                    ta.setText("");
                    tb.setText("");
                    tc.setText("");

                }else{
                    error = "Old password is incorrect";
                    refreshErrorMessage();
                }



            }

        });
    }

}
