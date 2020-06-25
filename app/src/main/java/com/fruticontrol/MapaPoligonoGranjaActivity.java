package com.fruticontrol;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class MapaPoligonoGranjaActivity extends FragmentActivity implements OnMapReadyCallback {
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
    private ArrayList<String> puntosEscogidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_poligono_granja);
        puntosEscogidos = new ArrayList<String>();
        Token token = (Token) getApplicationContext();
        Button ghostButton = findViewById(R.id.buttonGhostGranja);
        Button eliminarPuntoButton = findViewById(R.id.buttonEliminarUltimoPunto);
        Button marcarPuntoButton = findViewById(R.id.buttonEscogerUbicacion);
        Button finalizarMarcadoButton = findViewById(R.id.buttonFinalizarMarcado);
        ImageView marcadorArbol = findViewById(R.id.imageViewMarcadorArbol);
        new MaterialIntroView.Builder(this)
                .enableDotAnimation(true)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Arrastre el mapa y oprima marcar punto para ubicar un punto en el mapa. Repita este paso tantas veces hasta que se forme un poligono delimitando la granja, debe marcar al menos 3 puntos. Para eliminar un punto haga clic en eliminar último punto, y para finalizar la delimitación de la granja oprimar finalizar marcado")
                .setShape(ShapeType.CIRCLE)
                .setTarget(ghostButton)
                .setUsageId("granja_showcase")
                .show();
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
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                }
                if (gottenLocations > 1) {
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
        startLocationUpdates();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        ghostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                button.setVisibility(View.INVISIBLE);
            }
        });
        finalizarMarcadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (puntosEscogidos.size() >= 6) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("puntoPoligono", puntosEscogidos);
                    intent.putStringArrayListExtra("puntoPoligono", puntosEscogidos);
                    intent.putExtra("bundle", bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(view.getContext(), "Debe escoger por lo menos 3 puntos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        eliminarPuntoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setTitle(Html.fromHtml("<font color='#964F2D'>Eliminar último punto</font>"));
                builder.setMessage(Html.fromHtml("<font color='#910C00'>¿Está seguro de que desea eliminar el último punto?</font>"));
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Si, eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> auxList = new ArrayList<>();
                        for (int a = 0; a < puntosEscogidos.size() - 2; a++) {
                            auxList.add(puntosEscogidos.get(a));
                        }
                        puntosEscogidos.clear();
                        puntosEscogidos = auxList;
                        mMap.clear();
                        //SE PONEN DE NUEVO TODOS LOS MARCADORES
                        if (!puntosEscogidos.isEmpty()) {
                            for (int j = 0; j < puntosEscogidos.size(); j = j + 2) {
                                LatLng auxPoint = new LatLng(Double.parseDouble(puntosEscogidos.get(j)), Double.parseDouble(puntosEscogidos.get(j + 1)));
                                mMap.addMarker(new MarkerOptions().position(auxPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.seo32fin)));
                            }
                        }
                        //SE VERIFICA SI HAY AL MENOS 3 PUNTOS PARA AGREGAR EL POLYGON
                        if (puntosEscogidos.size() >= 6) {
                            //SE CREA LISTA LATLNG PARA DARSELOS AL POLIGONO
                            List<LatLng> auxPolygonArray = new ArrayList<>();
                            for (int b = 0; b < puntosEscogidos.size(); b = b + 2) {
                                Double a1 = Double.parseDouble(puntosEscogidos.get(b));
                                Double a2 = Double.parseDouble(puntosEscogidos.get(b + 1));
                                LatLng auxLL = new LatLng(a1, a2);
                                auxPolygonArray.add(auxLL);
                            }
                            Polygon polygon1 = mMap.addPolygon(new PolygonOptions().clickable(false).add(new LatLng(-27.457, 153.040),
                                    new LatLng(-33.852, 151.211),
                                    new LatLng(-37.813, 144.962),
                                    new LatLng(-34.928, 138.599)).strokeColor(0xFF00AA00).fillColor(0x2200FFFF).strokeWidth(2));
                            polygon1.setPoints(auxPolygonArray);
                        }
                    }
                });
                builder.show();
            }
        });
        marcarPuntoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SE LIMPIA EL MAPA CUANDO SE AGREGA UN NUEVO MARCADOR
                mMap.clear();
                //SE PONEN DE NUEVO TODOS LOS MARCADORES
                if (!puntosEscogidos.isEmpty()) {
                    for (int i = 0; i < puntosEscogidos.size(); i = i + 2) {
                        LatLng auxPoint = new LatLng(Double.parseDouble(puntosEscogidos.get(i)), Double.parseDouble(puntosEscogidos.get(i + 1)));
                        mMap.addMarker(new MarkerOptions().position(auxPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.seo32fin)));
                    }
                }
                //SE AGREGA EL ULTIMO MARCADOR ESCOGIDO AL MAPA Y A LA LISTA DE PUNTOS
                LatLng point = new LatLng(cameraLatitude, cameraLongitude);
                mMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.seo32fin)));
                puntosEscogidos.add(String.valueOf(cameraLatitude));
                puntosEscogidos.add(String.valueOf(cameraLongitude));
                //SE VERIFICA SI HAY AL MENOS 3 PUNTOS PARA AGREGAR EL POLYGON
                if (puntosEscogidos.size() >= 6) {
                    //SE CREA LISTA LATLNG PARA DARSELOS AL POLIGONO
                    List<LatLng> auxPolygonArray = new ArrayList<>();
                    for (int i = 0; i < puntosEscogidos.size(); i = i + 2) {
                        Double a1 = Double.parseDouble(puntosEscogidos.get(i));
                        Double a2 = Double.parseDouble(puntosEscogidos.get(i + 1));
                        LatLng auxLL = new LatLng(a1, a2);
                        auxPolygonArray.add(auxLL);
                    }
                    Polygon polygon1 = mMap.addPolygon(new PolygonOptions().clickable(false).add(new LatLng(-27.457, 153.040),
                            new LatLng(-33.852, 151.211),
                            new LatLng(-37.813, 144.962),
                            new LatLng(-34.928, 138.599)).strokeColor(0xFF00AA00).fillColor(0x2200FFFF).strokeWidth(2));
                    polygon1.setPoints(auxPolygonArray);
                }
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
        arbolMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(4.6272415, -74.0640134)).icon(BitmapDescriptorFactory.fromResource(R.drawable.seo32)).draggable(true));
        arbolMarker.setVisible(false);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = mMap.getCameraPosition().target;
                if (arbolMarker != null) {
                    arbolMarker.remove();
                    mMap.addMarker(new MarkerOptions().position(center)).setVisible(false);
                    cameraLatitude = center.latitude;
                    cameraLongitude = center.longitude;
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
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
}

//Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>
