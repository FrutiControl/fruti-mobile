package com.fruticontrol;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class ListaFincasActivity extends AppCompatActivity {
    Button nuevaFincaButton;
    ArrayList<ResumenFincaDataModel> dataModels;
    ListView listView;
    TextView txtTituloFinca;
    private static ResumenFincasAdapter adapter;
    private Config config;
    private ArrayList<String> nombresFincas;
    private ArrayList<String> idFincas;
    private ArrayList<String> puntosFincas;
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;
    static final int GPS_REQUEST = 300;
    private boolean isGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_fincas);
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        config = (Config) getApplicationContext();
        listView = findViewById(R.id.listaFincasList);
        txtTituloFinca = findViewById(R.id.textViewTituloFincas);
        nuevaFincaButton = findViewById(R.id.buttonNuevaFinca);
        dataModels = new ArrayList<>();
        puntosFincas = new ArrayList<>();
        nombresFincas = new ArrayList<>();
        idFincas = new ArrayList<>();
        activarUbicacion();
        sessionAlive();

        new MaterialIntroView.Builder(this)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(false)
                .setInfoText("En esta pantalla puede visualizar su lista de fincas, para crear una nueva finca haga clic en el boton nueva finca")
                .setShape(ShapeType.RECTANGLE)
                .setTarget(nuevaFincaButton)
                .setUsageId("lista_finca_showcase")
                .show();

        RequestQueue queue = Volley.newRequestQueue(ListaFincasActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET, config.getDomain()+
                "/app/farms/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("farmsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject farmObject = response.getJSONObject(i);
                                String id = farmObject.getString("id");
                                String name = farmObject.getString("name");
                                String puntos = farmObject.getString("polygon");
                                nombresFincas.add(name);
                                idFincas.add(id);
                                puntosFincas.add(puntos);
                            }
                            if (!nombresFincas.isEmpty()) {
                                //SE LLENA LA LISTA
                                for (int i = 0; i < nombresFincas.size(); i++) {
                                    dataModels.add(new ResumenFincaDataModel(nombresFincas.get(i).toString()));
                                }
                                adapter = new ResumenFincasAdapter(dataModels, getApplicationContext());
                                listView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(ListaFincasActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + config.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                config.setFincaActual(idFincas.get(i));
                // TODO: set proper names for this variables
                String separated[] = puntosFincas.get(i).split("\\(");
                String aux[] = separated[2].split("\\)");
                System.out.println("SEPARATED 2 ES " + separated[2]);
                String soloXY[] = aux[0].split(",");
                ArrayList<String> auxPuntosLimpios = new ArrayList<>();
                for (int a = 0; a < soloXY.length; a++) {
                    if (a == 0) {
                        String otroAux[] = soloXY[0].split(" ");
                        auxPuntosLimpios.add(otroAux[0]);
                        auxPuntosLimpios.add(otroAux[1]);
                    } else {
                        String otroAux[] = soloXY[a].split(" ");
                        auxPuntosLimpios.add(otroAux[1]);
                        auxPuntosLimpios.add(otroAux[2]);
                    }
                }
                config.setPuntosPoligonoFinca(auxPuntosLimpios);

                Intent intent = new Intent(view.getContext(), AccionesActivity.class);
                startActivity(intent);
            }
        });
        nuevaFincaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NuevaFincaActivity.class);
                startActivity(intent);

            }
        });
    }

    public void activarUbicacion(){
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
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
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "sin acceso a localizacion, hardware deshabilitado!", Toast.LENGTH_SHORT).show();
                }
            }
            case GPS_REQUEST: {
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "Debe encender la localización para usar la aplicación", Toast.LENGTH_LONG).show();
                    activarUbicacion();
                }
            }
        }
    }


    private void sessionAlive(){
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Token",config.getToken());
        editor.putBoolean("Valid",true);
        editor.apply();
    }
}
