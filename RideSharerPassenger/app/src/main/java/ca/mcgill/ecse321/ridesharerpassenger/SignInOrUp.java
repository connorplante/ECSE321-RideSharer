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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class SignInOrUp extends AppCompatActivity {

    String error = "";
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        TextView tvError = (TextView) findViewById(R.id.error1);

        if (error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setText(error);
            tvError.setVisibility(View.VISIBLE);
        }
    }

    public void signIn(View v) {
        error = "";
        final TextView ta = (TextView) findViewById(R.id.editText);
        final TextView tb = (TextView) findViewById(R.id.editText2);

        if(ta.equals("") || tb.equals("")){
            error = "Please enter all fields!";
            refreshErrorMessage();
            return;
        }
        HttpUtils.post("/User/logIn?username=" + ta.getText().toString() +
                "&password=" + tb.getText().toString(), new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if(responseString.equals("true")){
                    viewMainMenu();
                }else{
                    error += "Incorrect username or password";

                    refreshErrorMessage();
                }
            }
        });
    }

    public void viewMainMenu(){
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra(MainMenu.USERNAME, username);
        startActivity(intent);
    }

    public void viewSignUp(View v){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
