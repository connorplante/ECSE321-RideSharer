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

public class UpdateInfo extends AppCompatActivity {

    String error = "";

    String firstName;
    String lastName;
    String email;
    String phoneNum;

    public static final String FIRSTNAME = "ca.mcgill.ecse321.ridesharerpassenger.firstName";
    public static final String LASTNAME = "ca.mcgill.ecse321.ridesharerpassenger.lastName";
    public static final String EMAIL  = "ca.mcgill.ecse321.ridesharerpassenger.email";
    public static final String PHONE = "ca.mcgill.ecse321.ridesharerpassenger.phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
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

        if(getIntent().hasExtra(FIRSTNAME)){
            firstName = getIntent().getStringExtra(FIRSTNAME);
        }

        if(getIntent().hasExtra(LASTNAME)){
            lastName = getIntent().getStringExtra(LASTNAME);
        }

        if(getIntent().hasExtra(EMAIL)){
            email = getIntent().getStringExtra(EMAIL);
        }

        if(getIntent().hasExtra(PHONE)){
            phoneNum = getIntent().getStringExtra(PHONE);
        }

        final TextView a = (TextView) findViewById(R.id.firstNameText);
        a.setHint(firstName);
        final TextView b = (TextView) findViewById(R.id.lastNameText);
        b.setHint(lastName);
        final TextView c = (TextView) findViewById(R.id.emailText);
        c.setHint(email);
        final TextView d = (TextView) findViewById(R.id.phoneText);
        d.setHint(phoneNum);
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

    public void updateInfo(View v){
        error = "";
        final TextView ta = (TextView) findViewById(R.id.firstNameText);
        final TextView tb = (TextView) findViewById(R.id.lastNameText);
        final TextView tc = (TextView) findViewById(R.id.emailText);
        final TextView td = (TextView) findViewById(R.id.phoneText);

        String firstName = "";
        String lastName = "";
        String email = "";
        String phone = "";

        if(ta.getText().toString().equals("")){
            firstName = ta.getHint().toString();
        }else{
            firstName = ta.getText().toString();
        }

        if(tb.getText().toString().equals("")){
            lastName = tb.getHint().toString();
        }else{
            lastName = tb.getText().toString();
        }

        if(tc.getText().toString().equals("")){
            email = tc.getHint().toString();
        }else{
            email = tc.getText().toString();
        }

        if(td.getText().toString().equals("")){
            phone = td.getHint().toString();
        }else{
            phone = td.getText().toString();
        }

        final String finalfirstName = firstName;
        final String finalLastName = lastName;
        final String finalEmail = email;
        final String finalPhone = phone;


        String username = "samcattani";

        String url = "/User/updateUserInfo?username=" + username + "&firstName=" + firstName + "&lastName=" + lastName + "&email=" + email + "&phoneNumber=" + phone;

        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

                ta.setHint(finalfirstName);
                tb.setHint(finalLastName);
                tc.setHint(finalEmail);
                td.setHint(finalPhone);

                ta.setText("");
                tb.setText("");
                tc.setText("");
                td.setText("");

            }

        });
    }

}
