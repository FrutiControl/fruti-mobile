package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

public class DashboardActividadesActivity extends AppCompatActivity {

    private Button nuevaActividadButton;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_actividades);
        nuevaActividadButton = findViewById(R.id.buttonNuevaActividad);
        token=(Token)getApplicationContext();
        cargarFertilizaciones();

        nuevaActividadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NuevaActividadActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cargarFertilizaciones(){
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "http://10.0.2.2:8000/app/fertilizations/"/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("fertilizationsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject farmObject = response.getJSONObject(i);
                                String tipo = farmObject.getString("type");
                                System.out.println(farmObject.getString("trees"));
                                JSONArray auxJson=farmObject.getJSONArray("trees");
                                for(int j=0;j<auxJson.length();j++){
                                    System.out.println("XXXXXXXXXXX IMPRESION # "+j);
                                    System.out.println(auxJson.get(j).toString());
                                }
                                System.out.println("VA EL JSON ARRAY ACA ");
                                System.out.println(farmObject.getJSONArray("trees"));
                            }
                            /*if (!nombresGranjas.isEmpty()) {
                                //SE LLENA LA LISTA
                                for (int i = 0; i < nombresGranjas.size(); i++) {
                                    dataModels.add(new ResumenGranjaDataModel(nombresGranjas.get(i).toString(), "Tareas: 3", "Pendientes: 2"));
                                }
                                adapter = new ResumenGranjasAdapter(dataModels, getApplicationContext());
                                listView.setAdapter(adapter);
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(DashboardActividadesActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private String traductorRiegosInverso(String tipo) {
        if (tipo.equals("S")) {
            return "Sistema";
        }
        if (tipo.equals("M")) {
            return "Manual";
        }
        else {
            return "Natural";
        }
    }


    private String traductorFertilizacionesInverso(String tipo) {
        if (tipo.equals("C")) {
            return "Crecimiento";
        }
        if (tipo.equals("P")) {
            return "Produccion";
        } else {
            return "Mantenimiento";
        }
    }


    private String traductorFumigacionesInverso(String tipo) {
        if (tipo.equals("I")) {
            return "Insectos";
        }
        if (tipo.equals("F")) {
            return "Hongos";
        }
        if (tipo.equals("H")) {
            return "Hierba";
        }
        if (tipo.equals("A")) {
            return "Ácaro";
        } else {
            return "Peste";
        }
    }


    private String traductorPodasInverso(String tipo) {

        if (tipo.equals("S")) {
            return "Sanitaria";
        }
        if (tipo.equals("F")) {
            return "Formación";
        }
        if (tipo.equals("M")) {
            return "Mantenimiento";
        } else {
            return "Limpieza";
        }
    }


}
