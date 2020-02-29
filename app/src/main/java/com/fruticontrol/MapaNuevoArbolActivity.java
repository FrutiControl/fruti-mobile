package com.fruticontrol;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapaNuevoArbolActivity extends FragmentActivity implements OnMapReadyCallback {

    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Marker arbolMarker;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    double latitude;
    double longitude;
    double altitude;

    Button seleccionarUbicacionButton;
    ImageView marcadorArbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_nuevo_arbol);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        seleccionarUbicacionButton=findViewById(R.id.buttonEscogerUbicacion);
        marcadorArbol=findViewById(R.id.imageViewMarcadorArbol);

        marcadorArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(latitude,longitude);
            }
        });
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.i("FLOCATION", "First Location update in the callback: " + location);
                            arbolMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                            arbolMarker.setVisible(false);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(arbolMarker.getPosition()));
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                        }
                    }
                }
        );
        mLocationRequest = createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location update in the callback: " + location);
                if (location != null) {
                    if (location.getLatitude() != latitude || location.getLongitude() != longitude) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        arbolMarker.setPosition(new LatLng(latitude, longitude));
                        arbolMarker.setVisible(false);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(arbolMarker.getPosition()));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                    }
                }
            }
        };
    }


    public void openDialog(double lat,double lng) {
        Dialogo exampleDialog = new Dialogo();
        exampleDialog.setText("Latitud: "+lat+"\n"+"Longitud: "+lng);
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        arbolMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(4, -72)).icon(BitmapDescriptorFactory.fromResource(R.drawable.tree)).draggable(true));
        arbolMarker.setVisible(false);
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center=mMap.getCameraPosition().target;
                if(arbolMarker!=null){
                    mMap.clear();
                    arbolMarker.remove();
                    mMap.addMarker(new MarkerOptions().position(center)).setVisible(false);
                    latitude=center.latitude;
                    longitude=center.longitude;
                }
            }
        });
    }




    private void requestPermission(Activity context, String permiso, String justificacion, int idCode) {
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, idCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "sin acceso a localizacion, hardware deshabilitado!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(8000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }


}


    //Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>