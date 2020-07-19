package com.fruticontrol;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NuevaFincaActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;
    private Button crearFincaButton;
    private EditText nombreFincaET;
    private Config config;
    private Button crearPoligono;
    private ArrayList<String> puntosPoligono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_finca);
        config = (Config) getApplicationContext();
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        System.out.println("XXXXXXX EL TOKEN QUE SE RECIBE ES " + config.getToken());
        crearFincaButton = findViewById(R.id.buttonCrearFinca);
        puntosPoligono = new ArrayList<>();
        crearPoligono = findViewById(R.id.buttonCrearPoligono);
        nombreFincaET = findViewById(R.id.editTextNombreFinca);
        System.out.println("El token que se recibe es " + config.getToken());
        crearPoligono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), MapaPoligonoFincaActivity.class), 100);
            }
        });
        crearFincaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (validateForm()) {
                    Toast.makeText(NuevaFincaActivity.this, "Espere un momento por favor", Toast.LENGTH_SHORT).show();
                    RequestQueue queue = Volley.newRequestQueue(NuevaFincaActivity.this);
                    config.setPuntosPoligonoFinca(puntosPoligono);
                    String auxPuntosPoligono = "POLYGON((";
                    for (int i = 0; i < puntosPoligono.size(); i = i + 2) {
                        if (i == 0) {
                            if (i == puntosPoligono.size() - 2) {
                                auxPuntosPoligono = auxPuntosPoligono + puntosPoligono.get(i) + " " + puntosPoligono.get(i + 1) + ",";
                                auxPuntosPoligono = auxPuntosPoligono + puntosPoligono.get(0) + " " + puntosPoligono.get(1);
                            } else {
                                auxPuntosPoligono = auxPuntosPoligono + puntosPoligono.get(i) + " " + puntosPoligono.get(i + 1) + ",";
                            }
                        } else {
                            if (i == puntosPoligono.size() - 2) {
                                auxPuntosPoligono = auxPuntosPoligono + " " + puntosPoligono.get(i) + " " + puntosPoligono.get(i + 1) + ",";
                                auxPuntosPoligono = auxPuntosPoligono + " " + puntosPoligono.get(0) + " " + puntosPoligono.get(1);
                            } else {
                                auxPuntosPoligono = auxPuntosPoligono + " " + puntosPoligono.get(i) + " " + puntosPoligono.get(i + 1) + ",";
                            }
                        }
                    }
                    auxPuntosPoligono = auxPuntosPoligono + "))";
                    String body = "{\"name\":\"" + nombreFincaET.getText().toString() + "\",\"polygon\":\"" + auxPuntosPoligono + "\"}";
                    Log.i("newFarmAPI", "Nueva Finca: " + body);
                    JSONObject newFarm = null;
                    try {
                        newFarm = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String aux;
                    JsonObjectRequest newFarmRequest = new JsonObjectRequest(Request.Method.POST, config.getDomain()+
                            "/app/farms/", newFarm,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("newFarmAPI", response.toString());

                                    if (response.has("error")) {
                                        try {
                                            Toast.makeText(NuevaFincaActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            config.setFincaActual(response.getString("id"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(NuevaFincaActivity.this, "Finca creada", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(view.getContext(), AccionesActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(NuevaFincaActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }) {    //this is the part, that adds the header to the request
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Token " + config.getToken());
                            return params;
                        }
                    };
                    queue.add(newFarmRequest);
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

    private boolean validateForm() {
        boolean valid = true;
        if (puntosPoligono.isEmpty()) {
            valid = false;
            Toast.makeText(NuevaFincaActivity.this, "Debe ubicar la finca en el mapa para poder continuar", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(nombreFincaET.getText().toString())) {
            nombreFincaET.setError("Requerido");
            valid = false;
        } else {
            nombreFincaET.setError(null);
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                if (extras.containsKey("puntoPoligono")) {
                    System.out.println("VLAOR DERICIBO EN EL BUNDLE ");
                } else {
                    System.out.println("EL BUNDLE NO TIENE NADA");
                }
            } else {
                System.out.println("EXTRAS ES NULLL");
            }
            puntosPoligono = extras.getStringArrayList("puntoPoligono");
        }
    }
}
//Icons made by <a href="https://www.flaticon.com/authors/good-ware" title="Good Ware">Good Ware</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>
