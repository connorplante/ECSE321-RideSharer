package ca.mcgill.ecse321.ridesharerdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;

public class SelectTime extends AppCompatActivity  implements TimePickerFragment.OnCompleteListener, DatePickerFragment.OnCompleteListener {

    String username = "";
    String error = "i";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
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
        Intent intent = new Intent(this, CreateTrip.class);
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

//    private void refreshErrorMessage() {
//        // set the error message
//        TextView tvError = (TextView) findViewById(R.id.error);
//        tvError.setText(error);
//        tvError.setVisibility(View.GONE);
//
////        if (error == null || error != "i") {
////            tvError.setVisibility(View.GONE);
////        } else {
////            tvError.setVisibility(View.VISIBLE);
////        }
//
//    }

    public void onComplete(String time) {
       // System.out.print("This is the time: " + time);
    }


    public void showTimePickerOnDialog(View v){
//        final TextView time = findViewById(R.id.textView1);
//        System.out.print("This is the time: " + time.getText().toString());
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showDatePickerOnDialog(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public void switchViewSelectUsername(View v){

        Intent intent2 = new Intent(this, SelectUsername.class);

        TextView tv1 = (TextView) findViewById(R.id.textView1);

        TextView tv2=(TextView) findViewById(R.id.textView2);

        String value = tv2.getText().toString();
        String value2 = tv1.getText().toString();

        intent2.putExtra("DATE", value);
        intent2.putExtra("TIME", value2);
        intent2.putExtra(MainMenu.USERNAME, username);

        startActivity(intent2);
    }

}
