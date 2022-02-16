package edu.neu.madcourse.numad22sp_eshwaribhide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocatorActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_CODE_LOCATION = 1;
    TextView latitudeValue;
    TextView longitudeValue;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
        init(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        latitudeValue = findViewById(R.id.latitudeValue);
        longitudeValue = findViewById(R.id.longitudeValue);
        showLocationValues();
    }

    public void showLocationValues() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                setLatLongText(location);
            } else {
                Toast.makeText(this, "Cannot find location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("LocValueKey")) {
            latitudeValue.setText(savedInstanceState.getString("Latitude"));
            longitudeValue.setText(savedInstanceState.getString("Longitude"));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("Latitude", String.valueOf(latitudeValue));
        outState.putString("Longitude", String.valueOf(longitudeValue));
        super.onSaveInstanceState(outState);
    }

    private void init(Bundle savedInstanceState) {
        initData(savedInstanceState);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        setLatLongText(location);
    }

    public void setLatLongText(Location location) {
        latitudeValue.setText("Latitude: " + location.getLatitude());
        latitudeValue.setTextSize(20);
        latitudeValue.setTextColor(Color.parseColor("#9C27B0"));
        longitudeValue.setText("Longitude: " + location.getLongitude());
        longitudeValue.setTextSize(20);
        longitudeValue.setTextColor(Color.parseColor("#9C27B0"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                showLocationValues();
            }
        }
    }
}
