package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
    private List<String> listaIds;
    private List<String> listaTiposActividades;
    private List<String> listaFechas;
    ArrayList<ResumenActividadDataModel> dataModels;
    ListView listView;
    private static ResumenActividadesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_actividades);
        nuevaActividadButton = findViewById(R.id.buttonNuevaActividad);
        listView=findViewById(R.id.listaActividades);
        token=(Token)getApplicationContext();
        dataModels = new ArrayList<>();
        listaIds=new ArrayList<>();
        listaTiposActividades=new ArrayList<>();
        listaFechas=new ArrayList<>();
        cargarFertilizaciones();
        cargarFumigaciones();
        cargarPodas();
        cargarRiegos();

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
                                JSONObject activityObject = response.getJSONObject(i);
                                listaIds.add(activityObject.getString("id"));
                                //SE TRADUCE Y AGREGA TIPO EN LISTA
                                String auxTipo=traductorFertilizacionesInverso(activityObject.getString("type"));
                                listaTiposActividades.add("Fertilizacion: "+auxTipo);
                                //SE TRADUCE FECHA INICIO
                                String auxFecha = activityObject.getString("start_date");
                                String divide2 = auxFecha;
                                String[] separated2 = divide2.split("-");
                                auxFecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //SE TRADUCE FECHA FIN
                                String auxFechaFin = activityObject.getString("start_date");
                                String divide3 = auxFechaFin;
                                String[] separated3 = divide3.split("-");
                                auxFechaFin=separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                //SE AGREGAN A LA LISTA DE FECHAS
                                listaFechas.add(auxFecha+"-"+auxFechaFin);
                            }
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

    private void cargarRiegos(){
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "http://10.0.2.2:8000/app/waterings/"/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("wateringsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activityObject = response.getJSONObject(i);
                                listaIds.add(activityObject.getString("id"));
                                //SE TRADUCE Y AGREGA TIPO EN LISTA
                                String auxTipo=traductorRiegosInverso(activityObject.getString("type"));
                                listaTiposActividades.add("Riego: "+auxTipo);
                                //SE TRADUCE FECHA INICIO
                                String auxFecha = activityObject.getString("start_date");
                                String divide2 = auxFecha;
                                String[] separated2 = divide2.split("-");
                                auxFecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //SE TRADUCE FECHA FIN
                                String auxFechaFin = activityObject.getString("start_date");
                                String divide3 = auxFechaFin;
                                String[] separated3 = divide3.split("-");
                                auxFechaFin=separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                //SE AGREGAN A LA LISTA DE FECHAS
                                listaFechas.add(auxFecha+"-"+auxFechaFin);
                            }
                            llenadoLista();
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

    private void cargarPodas(){
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "http://10.0.2.2:8000/app/prunings/"/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("pruningsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activityObject = response.getJSONObject(i);
                                listaIds.add(activityObject.getString("id"));
                                //SE TRADUCE Y AGREGA TIPO EN LISTA
                                String auxTipo=traductorPodasInverso(activityObject.getString("type"));
                                listaTiposActividades.add("Poda: "+auxTipo);
                                //SE TRADUCE FECHA INICIO
                                String auxFecha = activityObject.getString("start_date");
                                String divide2 = auxFecha;
                                String[] separated2 = divide2.split("-");
                                auxFecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //SE TRADUCE FECHA FIN
                                String auxFechaFin = activityObject.getString("start_date");
                                String divide3 = auxFechaFin;
                                String[] separated3 = divide3.split("-");
                                auxFechaFin=separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                //SE AGREGAN A LA LISTA DE FECHAS
                                listaFechas.add(auxFecha+"-"+auxFechaFin);
                            }
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

    private void cargarFumigaciones(){
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "http://10.0.2.2:8000/app/fumigations/"/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("fumigationsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activityObject = response.getJSONObject(i);
                                listaIds.add(activityObject.getString("id"));
                                //SE TRADUCE Y AGREGA TIPO EN LISTA
                                String auxTipo=traductorFumigacionesInverso(activityObject.getString("type"));
                                listaTiposActividades.add("Fumigacion: "+auxTipo);
                                //SE TRADUCE FECHA INICIO
                                String auxFecha = activityObject.getString("start_date");
                                String divide2 = auxFecha;
                                String[] separated2 = divide2.split("-");
                                auxFecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //SE TRADUCE FECHA FIN
                                String auxFechaFin = activityObject.getString("start_date");
                                String divide3 = auxFechaFin;
                                String[] separated3 = divide3.split("-");
                                auxFechaFin=separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                //SE AGREGAN A LA LISTA DE FECHAS
                                listaFechas.add(auxFecha+"-"+auxFechaFin);
                            }
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


    private void llenadoLista(){
        if (!listaTiposActividades.isEmpty()) {
            System.out.println("XXXXXXXXXXXXXXXXXXXX TAMAÑANO DE LISTA ACTIVIDAES ES "+listaTiposActividades.size());
            for (int i = 0; i < listaTiposActividades.size(); i++) {
                dataModels.add(new ResumenActividadDataModel(listaTiposActividades.get(i).toString(), listaFechas.get(i),"0"));
            }
            adapter = new ResumenActividadesAdapter(dataModels, getApplicationContext());
            listView.setAdapter(adapter);
        }else{
            System.out.println("XXXXXXXXXXXXXXXXXXXX ESTA VACIA");
        }
    }
}
