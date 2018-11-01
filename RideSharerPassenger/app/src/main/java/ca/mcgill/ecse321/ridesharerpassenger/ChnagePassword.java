package ca.mcgill.ecse321.ridesharerpassenger;

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

public class ChnagePassword extends AppCompatActivity {

    String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnage_password);
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

        if (error == null || error.length() == 0) {
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
        final TextView td = (TextView) findViewById(R.id.error);

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
