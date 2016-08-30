package hu.ait.android.keely.migrainetracker.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hu.ait.android.keely.migrainetracker.Data.HttpGetTask;
import hu.ait.android.keely.migrainetracker.R;

/**
 * Fragment that gets the weather based on user's last known location
 */
public class FragmentWeather extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /*Text views and buttons*/
    private TextView tvWeather;
    private TextView tvDesc;
    private TextView tvPressure;
    private ImageView imageView;
    private RadioGroup rgpUnits;
    private RadioButton radioButton;

    /*Location services*/
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String latitude;
    private String longitude;

    /*Using open api weather map*/
    private final String URL_BASE =
            "http://api.openweathermap.org/data/2.5/weather?";
    private final String API_KEY = "3a762d1a0b2e642dcf94080d1d6f0fbb";
    private String units; //F or C


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        return rootView;

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = String.valueOf(Math.round(mLastLocation.getLatitude()));
            longitude = String.valueOf(Math.round(mLastLocation.getLongitude()));
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        super.onActivityCreated(savedInstanceState);
        tvWeather = (TextView) getView().findViewById(R.id.tvWeather);
        tvDesc = (TextView) getView().findViewById(R.id.tvDesc);
        tvPressure = (TextView) getView().findViewById(R.id.tvPressure);
        Button btnEnter = (Button) getView().findViewById(R.id.btnEnter);
        rgpUnits = (RadioGroup) getView().findViewById(R.id.rgpUnits);


        imageView = (ImageView) getView().findViewById(R.id.imageView);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = URL_BASE;

                /*Get units, F or C*/
                int selectedId = rgpUnits.getCheckedRadioButtonId();

                if (selectedId == -1) //handles the case of F or C not chosen, then default F
                    units = "F";
                else {
                    radioButton = (RadioButton) getView().findViewById(selectedId);
                    units = radioButton.getText().toString();
                }

                String temp_units; //used for http call

                /*Use imperial or metric in the http call*/
                if (units.equals("F")) {
                    temp_units = "imperial";
                } else
                    temp_units = "metric";

                /*Use our last known location + openweathermap to get current weather*/
                new HttpGetTask(getActivity().getApplicationContext()).execute(query + "lat=" + latitude + "&lon=" + longitude +
                        "&units=" + temp_units + "&APPID=" + API_KEY);

                System.out.println(query + "lat=" + latitude + "&lon=" + longitude +
                        "&units=" + temp_units + "&APPID=" + API_KEY);


            }
        });


    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                brWeatherReceiver,
                new IntentFilter(getString(R.string.FILTER_RESULT)));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(
                getActivity()).unregisterReceiver(brWeatherReceiver);
    }

    private BroadcastReceiver brWeatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String rawResult = intent.getStringExtra(getString(R.string.KEY_RESULT));

            try {
                JSONObject rawJson = new JSONObject(rawResult);

                /*Parse the JSON file to get weather*/
                JSONArray values = rawJson.getJSONArray("weather");
                String tempMain = rawJson.getJSONObject("main").getString("temp");
                String tempMin = rawJson.getJSONObject("main").getString("temp_min");
                String tempMax = rawJson.getJSONObject("main").getString("temp_max");
                String pressure = rawJson.getJSONObject("main").getString("pressure");


                String desc = values.getJSONObject(0).getString("description");

                /*Print out weather to the user*/
                tvWeather.setText("The current weather is " + tempMain + " " + units +
                        ". The min temp is: " + tempMin + " " + units +
                        ". The max temp is: " + tempMax + " " + units + ".");

                tvDesc.setText("Description: " + desc + ".");
                tvPressure.setText("Pressure: " + pressure);

                /*Use icon from openweathermap*/
                String icon = values.getJSONObject(0).getString("icon");
                Glide.with(getActivity().getApplicationContext()).load(
                        "http://openweathermap.org/img/w/" + icon + ".png").into(imageView);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
}
