package com.fruticontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
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

public class MapaModificarArbolActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Marker arbolMarker;
    double latitude = 0;
    double longitude = 0;
    double cameraLatitude;
    double cameraLongitude;
    private Button seleccionarUbicacionButton;
    private Button ghostButton;
    private ImageView marcadorArbol;
    private Token token;
    private ArrayList<String> listaArboles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_modificar_arbol);
        Intent intent = getIntent();
        String latAux = intent.getStringExtra("lat");
        System.out.println("lat recibida es  " + latAux);
        String lonAux = intent.getStringExtra("lon");
        System.out.println("lon recibida es  " + lonAux);
        intent.getStringArrayListExtra("todosArboles");
        latitude = Double.parseDouble(latAux);
        longitude = Double.parseDouble(lonAux);
        listaArboles = intent.getStringArrayListExtra("todosArboles");
        System.out.println("XXXXXXXXXXXXXX DESPUES DE CONVERTIDAS LAS COORDENADAS SON: " + latitude + " " + longitude);
        cameraLatitude = latitude;
        cameraLongitude = longitude;
        token = (Token) getApplicationContext();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        seleccionarUbicacionButton = findViewById(R.id.buttonEscogerUbicacion);
        ghostButton=findViewById(R.id.buttonGhostModificar);
        new MaterialIntroView.Builder(this)
                .enableDotAnimation(true)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Para modificar la ubicación del árbol debe arrastrar el mapa, y cuando finalmente esté ubicado en la posición deseada presione escoger ubicación")
                .setShape(ShapeType.CIRCLE)
                .setTarget(ghostButton)
                .setUsageId("modificar_showcase")
                .show();
        ghostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                button.setVisibility(View.INVISIBLE);
            }
        });
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

        //SE DIBUJAN ARBOLES SOBRE EL MAPA
        for (int i = 0; i < listaArboles.size(); i = i + 1) {
            String divide = listaArboles.get(i);
            String[] separated = divide.split("\\(");
            String[] anotherAux = separated[1].split(" ");
            String lat = anotherAux[0];
            System.out.println("XXXXXXX ESTO ES MODIFICAR ARBOL LAT" + anotherAux[0]);
            String[] lonAux = anotherAux[1].split("\\)");
            String lon = lonAux[0];
            System.out.println("XXXXXXX ESTO ES MODIFICAR ARBOL LON" + lonAux[0]);
            Double a1 = Double.parseDouble(lat);
            Double a2 = Double.parseDouble(lon);
            if(a1!=latitude){
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.tree);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                mMap.addMarker(new MarkerOptions().position(new LatLng(a1, a2)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).draggable(false));
            }
        }

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

                    //SE DIBUJAN ARBOLES SOBRE EL MAPA
                    for (int i = 0; i < listaArboles.size(); i = i + 1) {
                        String divide = listaArboles.get(i);
                        String[] separated = divide.split("\\(");
                        String[] anotherAux = separated[1].split(" ");
                        String lat = anotherAux[0];
                        System.out.println("XXXXXXX ESTO ES MODIFICAR ARBOL LAT" + anotherAux[0]);
                        String[] lonAux = anotherAux[1].split("\\)");
                        String lon = lonAux[0];
                        System.out.println("XXXXXXX ESTO ES MODIFICAR ARBOL LON" + lonAux[0]);
                        Double a1 = Double.parseDouble(lat);
                        Double a2 = Double.parseDouble(lon);
                        if(a1!=latitude){
                            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.tree);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(a1, a2)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).draggable(false));
                        }
                    }
                }
            }
        });
    }
}

//Icons made by Freepik Flaticon www.flaticon.com
