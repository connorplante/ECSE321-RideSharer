package ca.mcgill.ecse321.ridesharerpassenger;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class GoogleMapsUtil {

    static final String url = "https://maps.googleapis.com/maps/api/directions/json?";

    public static void getMapData (/*ArrayList<String> stops*/) {

        String searchUrl = url +
                "origin=" + "Ottawa" +
                "&destination=" + "Toronto" +
                "&key=" + "AIzaSyCGx7U7vLjH64iK3Ex7qP96riFI5Fz5StA";

        HttpUtils.postByUrl(searchUrl, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("Google Map Success!!!");

                // add trip ids from response to the array list
                System.out.println(response.toString());

            }

            @Override
            public void onFinish() {

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });

    }
}
