package ca.mcgill.ecse321.ridesharerpassenger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng defaultLoc;
    public static final String STOPS = "ca.mcgill.ecse321.ridesharerpassenger.Stops";
    private static final int LOCATION_REQUEST = 500;
    String username = "";

    ArrayList<String> stops;
    String error = "";
    ArrayList<Integer> tripIds;
    ArrayList<String> prices;
    ArrayList<String> numSeats;
    ArrayList<String> status;
    ArrayList<String> dates;
    ArrayList<ArrayList<String>> stopsLists;
    String start;
    String end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().hasExtra(MainMenu.USERNAME)) {
            username = getIntent().getStringExtra(MainMenu.USERNAME);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + MainMenu.USERNAME);
        }

        // Get the tripIDs passed to this page from TripListings
        if (getIntent().hasExtra(ShowTripListings.tripIDs)) {
            tripIds = getIntent().getIntegerArrayListExtra(ShowTripListings.tripIDs);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + ShowTripListings.tripIDs);
        }

        // Get the tripIDs passed to this page from TripListings
        if (getIntent().hasExtra(ShowTripListings.START)) {
            start = getIntent().getStringExtra(ShowTripListings.START);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + ShowTripListings.START);
        }

        // Get the tripIDs passed to this page from TripListings
        if (getIntent().hasExtra(ShowTripListings.END)) {
            end = getIntent().getStringExtra(ShowTripListings.END);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + ShowTripListings.END);
        }

        if (getIntent().hasExtra(ShowTripListings.PRICES)) {
            prices = getIntent().getStringArrayListExtra(ShowTripListings.PRICES);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + ShowTripListings.PRICES);
        }

        if (getIntent().hasExtra(ShowTripListings.NUMSEATS)) {
            numSeats = getIntent().getStringArrayListExtra(ShowTripListings.NUMSEATS);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + ShowTripListings.NUMSEATS);
        }

        if (getIntent().hasExtra(ShowTripListings.DATES)) {
            dates = getIntent().getStringArrayListExtra(ShowTripListings.DATES);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + ShowTripListings.DATES);
        }

        if (getIntent().hasExtra(ShowTripListings.STATUS)) {
            status = getIntent().getStringArrayListExtra(ShowTripListings.STATUS);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + ShowTripListings.STATUS);
        }

        stopsLists = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < dates.size(); i++) {
            ArrayList<String> stops = new ArrayList<String>();
            if (getIntent().hasExtra(ShowTripListings.STOPSLISTS + i)) {
                stops = getIntent().getStringArrayListExtra(ShowTripListings.STOPSLISTS + i);
            } else {
                throw new IllegalArgumentException("Activity cannot find  extras " + ShowTripListings.STOPSLISTS);
            }
            stopsLists.add(stops);
        }

        if (getIntent().hasExtra(STOPS)) {
            stops = getIntent().getStringArrayListExtra(STOPS);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + STOPS);
        }

        System.out.println(stops);

        String url = getRequestUrl(stops);
        System.out.println("URL ===> " + url);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.5, -73.5), 6.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onBackPressed() {
        System.out.println("==== ON BACK PRESSED ====");

        Intent intent = new Intent(this, ShowTripListings.class);
        intent.putExtra(MainMenu.USERNAME, username);
        Bundle b = new Bundle();
        b.putStringArrayList(GoogleMapsActivity.STOPS, stops);
        b.putIntegerArrayList(ShowTripListings.tripIDs, tripIds);
        b.putStringArrayList(ShowTripListings.DATES, dates);
        b.putStringArrayList(ShowTripListings.NUMSEATS, numSeats);
        b.putStringArrayList(ShowTripListings.STATUS, status);
        b.putStringArrayList(ShowTripListings.PRICES, prices);
        for (int j = 0; j < stopsLists.size(); j++) {
            b.putStringArrayList(ShowTripListings.STOPSLISTS + j, stopsLists.get(j));
        }
        b.putString(ShowTripListings.START, start);
        b.putString(ShowTripListings.END, end);
        intent.putExtras(b);
        startActivity(intent);
    }

    private String getRequestUrl(ArrayList<String> stops) {
        // value of origin
        //String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        // value of origin
        //String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        //String param = str_org + "&" + str_dest;

        String origin = stops.get(0);
        String dest = stops.get(stops.size()-1);

        String waypoints = "";

        for (int i = 1; i < stops.size()-1; i++) {
            if (i == stops.size()-2) {
                waypoints += "via:" + stops.get(i);
            } else {
                waypoints += "via:" + stops.get(i) + "|";
            }
        }

        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin +
                "&destination=" + dest +
                "&waypoints=" + waypoints +
                "&key=AIzaSyCGx7U7vLjH64iK3Ex7qP96riFI5Fz5StA";

        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            // get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }

    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
                // trying it out
                //defaultLoc = directionsParser.getLatLng();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            // get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();

                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Directions not found", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
