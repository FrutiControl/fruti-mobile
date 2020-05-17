package com.fruticontrol;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapaNuevoArbolActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker arbolMarker;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    FusedLocationProviderClient mFusedLocationClient;
    double latitude = 4.627;
    double longitude = -74.064;
    double cameraLatitude;
    double cameraLongitude;
    int gottenLocations = 0;
    Task removeCallback;
    private Button seleccionarUbicacionButton;
    private ImageView marcadorArbol;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_nuevo_arbol);
        token = (Token) getApplicationContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.e("LOCATION", "Location update in the callback: " + location);
                if (location != null) {
                    gottenLocations++;
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                if (gottenLocations > 0) {
                    removeCallback = mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    arbolMarker.setPosition(new LatLng(latitude, longitude));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(arbolMarker.getPosition()));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                    if (removeCallback.isSuccessful()) {
                        Log.e("LOCATION", "Callback murió");
                    }
                }
            }
        };
        //startLocationUpdates();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        seleccionarUbicacionButton = findViewById(R.id.buttonEscogerUbicacion);
        seleccionarUbicacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("latFinal", String.valueOf(cameraLatitude));
                intent.putExtra("longFinal", String.valueOf(cameraLongitude));
                setResult(RESULT_OK, intent);
                finish();
                token.setArbolEscogido(true);
            }
        });
        marcadorArbol = findViewById(R.id.imageViewMarcadorArbol);
        marcadorArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(cameraLatitude, cameraLongitude);
            }
        });
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    public void openDialog(double lat, double lng) {
        Dialogo exampleDialog = new Dialogo();
        exampleDialog.setText("Latitud: " + lat + "\n" + "Longitud: " + lng);
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Log.e("MAP", "Entro a onMapReady");

        //PARA UBICAR ARBOL EN PRIMER PUNTO DE GRANJA
        String auxPuntos=token.getPuntosPoligonoGranja();
        String auxLatInit[]= auxPuntos.split(" ");
        String auxLongInit[]= auxLatInit[1].split(",");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(auxLatInit[0]), Double.parseDouble(auxLongInit[0]))));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        //PARA UBICAR ARBOL EN PRIMER PUNTO DE GRANJA ACABA

        arbolMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(auxLatInit[0]), Double.parseDouble(auxLongInit[0]))).icon(BitmapDescriptorFactory.fromResource(R.drawable.tree)).draggable(true));
        arbolMarker.setVisible(false);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = mMap.getCameraPosition().target;
                if (arbolMarker != null) {
                    mMap.clear();
                    arbolMarker.remove();
                    mMap.addMarker(new MarkerOptions().position(center)).setVisible(false);
                    cameraLatitude = center.latitude;
                    cameraLongitude = center.longitude;

                    //SE DIBUJA LIMITES DE POLIGONO GRANJA
                    String xyJuntas[]=token.getPuntosPoligonoGranja().split(",");
                    System.out.println("xxxxxxxxxxxxx LOS PUNTOS DEL POLIGONO SON "+token.getPuntosPoligonoGranja());
                    List<LatLng> auxPolygonArray=new ArrayList<>();
                    for(int i=0;i<xyJuntas.length;i++){
                        if(i==0){
                            String auxCoordenadas[]=xyJuntas[i].split(" ");
                            String auxX=auxCoordenadas[0];
                            String auxY=auxCoordenadas[1];
                            System.out.println("XXXXXXXXXXXX LAS COORDENADAS SON X "+auxX+" Y "+auxY);
                            auxPolygonArray.add(new LatLng(Double.parseDouble(auxX),Double.parseDouble(auxY)));
                        }else if(i!=xyJuntas.length-1){
                            String auxCoordenadas[]=xyJuntas[i].split(" ");
                            String auxX=auxCoordenadas[1];
                            String auxY=auxCoordenadas[2];
                            System.out.println("XXXXXXXXXXXX LAS COORDENADAS SON X "+auxX+" Y "+auxY);
                            auxPolygonArray.add(new LatLng(Double.parseDouble(auxX),Double.parseDouble(auxY)));
                        }
                    }
                    Polygon polygon1=mMap.addPolygon(new PolygonOptions().clickable(false).add(
                            new LatLng(4.731199083413069,-74.03176970779896),
                            new LatLng(4.731256220275831,-74.03191421180964),
                            new LatLng(4.73149612822121,-74.03193466365337),
                            new LatLng(4.731448681252535,-74.0316815301776)).strokeColor(0xFF00AA00).fillColor(0x2200FFFF).strokeWidth(2));
                    System.out.println("EL POLIGONO RECUPERADO ES "+auxPolygonArray);
                    polygon1.setPoints(auxPolygonArray);
                    //SE DIBUJA LIMITES DE POLIGONO GRANJA ACABA

                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
}

//Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>