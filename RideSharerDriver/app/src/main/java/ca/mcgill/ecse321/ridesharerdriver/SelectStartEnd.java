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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SelectStartEnd extends AppCompatActivity {

    String error = "";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_start_end);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_or_in, menu);
        return true;
    }

    public void onUpButtonPressed() {
        Intent intent = new Intent(this, SelectUsername.class);
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

    public void switchViewSelectCarIDNumSeats(View v){

        error = "";
        String url = "/Car/getDriversCars?username=" + username;
        System.out.println("Url: " + url);
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<String> carIds = new ArrayList<String>();
                ArrayList<String> makes = new ArrayList<String>();
                ArrayList<String> models = new ArrayList<String>();
                String s = "";

                // add trip ids from response to the array list
                for (int i = 0; i < response.length(); i++) {
                    try {
                       s = s + response.get(i).toString();
                    } catch (Exception e) {
                        System.out.println("here");
                    }
                }
                s = s.replaceAll("\\[", "");
                s = s.replaceAll("\\]", ",");
                s = s.replaceAll("\"", "");
                String[] str = s.split(",");

               for (int j = 0; j < str.length; j++){

                   if (j % 3 == 0 ){
                       carIds.add(str[j]);
                   }
                   if (j % 3  == 1){
                       makes.add(str[j]);

                   }
                   if (j % 3 == 2){
                       models.add(str[j]);
                   }
               }
                viewCarIDs(carIds,makes,models);
                // call next view to display trips
            }

            @Override
            public void onFinish() {
                //refreshErrorMessage();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                // refreshErrorMessage();
            }
        });
    }

    public void viewCarIDs(ArrayList<String> carIDs, ArrayList<String> makes, ArrayList<String> models){

        final TextView start = (TextView) findViewById(R.id.editText2);
        final TextView end = (TextView) findViewById(R.id.editText3);

        Intent intent = new Intent(this, SelectCarIDNumSeats.class);

        Bundle extras = getIntent().getExtras();
        extras.putStringArrayList(SelectCarIDNumSeats.CARIDS,carIDs);
        extras.putStringArrayList(SelectCarIDNumSeats.MAKES,makes);
        extras.putStringArrayList(SelectCarIDNumSeats.MODELS,models);
        intent.putExtras(extras);
        intent.putExtra("START", start.getText().toString());
        intent.putExtra("END", end.getText().toString());
        intent.putExtra(MainMenu.USERNAME, username);

        startActivity(intent);
    }

    public void callMethod(View v){
        String url = "/Car/getDriversCars?username=" + username;
        System.out.println("Url: " + url);
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler(){});
    }
}
