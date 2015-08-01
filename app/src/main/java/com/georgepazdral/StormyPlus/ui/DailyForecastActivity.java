package com.georgepazdral.StormyPlus.ui;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.georgepazdral.StormyPlus.R;
import com.georgepazdral.StormyPlus.adapters.DayAdapter;
import com.georgepazdral.StormyPlus.weather.Day;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by georgepazdral on 7/31/15.
 */
public class DailyForecastActivity extends Activity {

    private Day[] mDays;

    @Bind(android.R.id.list)
    ListView mListView;
    @Bind(android.R.id.empty)
    TextView mEmptyTextView;
    @Bind(R.id.locationLabel) TextView mlocationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);


        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        //final double latitude = 37.8267;
        //final double longitude = -122.423;


        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            String maxLines = address.get(0).getLocality();
            String State = address.get(0).getAdminArea();
            String cas = maxLines + ", " + State;
            builder.append(cas);
            builder.append(" ");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String fnialAddress = builder.toString(); //This is the complete address.
        mlocationLabel.setText(fnialAddress);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String highTemp = mDays[position].getTemperatureMax() + "";
                String message = String.format("On %s the high will be %s and it will be %s",
                        dayOfTheWeek,
                        highTemp,
                        conditions);
                Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}

