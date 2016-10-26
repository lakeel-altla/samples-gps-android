package com.lakeel.altla.sample.gps;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import com.lakeel.altla.sample.android.gms.location.LocationPermissions;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int REQUEST_CODE_REQUEST_PERMISSIONS = 1;

    private static final int REQUEST_CODE_START_RESOLUTION_FOR_RESULT = 1;

    private GoogleApiClient googleApiClient;

    private LocationRequest locationRequest;

    private Location currentLocation;

    private TextView textViewLatitude;

    private TextView textViewLongitude;

    private TextView textViewAltitude;

    private TextView textViewBearing;

    private TextView textViewAccuracy;

    private TextView textViewProvider;

    private TextView textViewSpeed;

    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        textViewLongitude = (TextView) findViewById(R.id.textViewLongitude);
        textViewAltitude = (TextView) findViewById(R.id.textViewAltitude);
        textViewAccuracy = (TextView) findViewById(R.id.textViewAccuracy);
        textViewBearing = (TextView) findViewById(R.id.textViewBearing);
        textViewProvider = (TextView) findViewById(R.id.textViewProvider);
        textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        initializeGoogleApiClient();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (LocationPermissions.isPermissionsGranted(this)) {
            initializeLocationService();
        } else {
            LocationPermissions.requestPermissions(this, REQUEST_CODE_REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (REQUEST_CODE_REQUEST_PERMISSIONS == requestCode) {
            handleRequestPermissionToAccessFineLocation(grantResults);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        updateGui();
    }

    private void initializeGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void handleRequestPermissionToAccessFineLocation(@NonNull int[] grantResults) {
        if (LocationPermissions.isRequestedPermissionsGranted(grantResults)) {
            initializeLocationService();
        } else {
            showRequestPermissionRationale();
        }
    }

    private void initializeLocationService() {
        createLocationRequest();
        checkLocationSettings();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void checkLocationSettings() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
//				final LocationSettingsStates=locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_CODE_START_RESOLUTION_FOR_RESULT);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    default:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void showRequestPermissionRationale() {
        Toast.makeText(this, "Can not get your location.", Toast.LENGTH_LONG).show();
    }

    private void updateGui() {
        final String NA = "N/A";

        textViewLatitude.setText(String.valueOf(currentLocation.getLatitude()));
        textViewLongitude.setText(String.valueOf(currentLocation.getLongitude()));

        String altitude = NA;
        if (currentLocation.hasAltitude()) {
            altitude = String.valueOf(currentLocation.getAltitude());
        }
        textViewAltitude.setText(altitude);

        String accuracy = NA;
        if (currentLocation.hasAccuracy()) {
            accuracy = String.valueOf(currentLocation.getAccuracy());
        }
        textViewAccuracy.setText(accuracy);

        String bearing = NA;
        if (currentLocation.hasBearing()) {
            bearing = String.valueOf(currentLocation.getBearing());
        }
        textViewBearing.setText(bearing);

        textViewProvider.setText(currentLocation.getProvider());

        String speed = NA;
        if (currentLocation.hasSpeed()) {
            speed = String.valueOf(currentLocation.getSpeed());
        }
        textViewSpeed.setText(speed);

        textViewTime.setText(DateFormat.getTimeInstance().format(new Date(currentLocation.getTime())));
    }
}
