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
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaNuevoArbolActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Marker arbolMarker;
    double latitude = 0;
    double longitude = 0;
    double cameraLatitude;
    double cameraLongitude;
    private Button seleccionarUbicacionButton;
    private ImageView marcadorArbol;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_nuevo_arbol);
        token = (Token) getApplicationContext();
        Intent intent = getIntent();
        String latAux = intent.getStringExtra("lat");
        System.out.println("lat recibida es  " + latAux);
        String lonAux = intent.getStringExtra("lon");
        System.out.println("lon recibida es  " + lonAux);
        latitude = Double.parseDouble(latAux);
        longitude = Double.parseDouble(lonAux);
        System.out.println("DESPUES DE CONVERTIDOS LOS PUNTOS INICIALES SON "+latitude+" "+longitude);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Log.e("MAP", "Entro a onMapReady");
        //SE DIBUJA LIMITES DE POLIGONO GRANJA
        ArrayList<String> puntosEscogidos = token.getPuntosPoligonoGranja();
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
        //SE DIBUJA LIMITES DE POLIGONO GRANJA ACABA
        arbolMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.tree)).draggable(true));
        System.out.println("XXXXXXXXXXXXXX POS_MARKER " + arbolMarker.getPosition().toString());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(arbolMarker.getPosition()));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
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
                    ArrayList<String> puntosEscogidos = token.getPuntosPoligonoGranja();
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
                    //SE DIBUJA LIMITES DE POLIGONO GRANJA ACABA
                }
            }
        });
    }
}

//Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>