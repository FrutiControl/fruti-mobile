package com.fruticontrol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class DashboardActividadesActivity extends AppCompatActivity {
    private Token token;
    private ArrayList<String> listaIds;
    private ArrayList<String> listaTiposActividades;
    private ArrayList<String> listaFechas;
    private ArrayList<String> listaProgresos;
    private ArrayList<String> listaArboles;
    private ArrayList<String> auxTipos;
    private ArrayList<String> auxSubtipos;
    private TextView txtProceso;
    private TextView txtPendiente;
    ArrayList<ResumenActividadDataModel> dataModels;
    ListView listView;
    int contProceso = 0;
    int contPendientes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_actividades);
        Button nuevaActividadButton = findViewById(R.id.buttonNuevaActividad);
        listView = findViewById(R.id.listaActividades);
        txtProceso = findViewById(R.id.textViewActProceso);
        txtPendiente = findViewById(R.id.textViewActPendientes);
        token = (Token) getApplicationContext();
        dataModels = new ArrayList<>();
        auxSubtipos = new ArrayList<>();
        auxTipos = new ArrayList<>();
        listaArboles = new ArrayList<>();
        listaIds = new ArrayList<>();
        listaTiposActividades = new ArrayList<>();
        listaFechas = new ArrayList<>();
        listaProgresos = new ArrayList<>();
        cargarRecolecciones();
        cargarFertilizaciones();
        cargarFumigaciones();
        cargarPodas();
        cargarRiegos();
        llenadoLista();

        new MaterialIntroView.Builder(this)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("En esta pantalla puede visualizar el estado general y la lista de actividades, para crear una nueva actividad haga clic en nueva actividad")
                .setShape(ShapeType.CIRCLE)
                .setTarget(nuevaActividadButton)
                .setUsageId("dash_actividades_showcase")
                .show();
        nuevaActividadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NuevaActividadActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Intent intent = new Intent(view.getContext(), ModificarActividadActivity.class);
                intent.putExtra("idActividad", listaIds.get(i));
                intent.putExtra("tipoActividad", listaTiposActividades.get(i));
                intent.putExtra("fecha", listaFechas.get(i));
                intent.putExtra("listaArboles", listaArboles.get(i));
                intent.putExtra("tipoFoormato", auxTipos.get(i));
                intent.putExtra("subTipoFormato", auxSubtipos.get(i));
                startActivity(intent);
            }
        });
    }

    private void cargarFertilizaciones() {
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,token.getDomain()+
                "/app/fertilizations/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("fertilizationsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                // TODO: set proper names for this variables
                                JSONObject activityObject = response.getJSONObject(i);
                                if (token.getGranjaActual().equals(activityObject.getString("farm"))) {
                                    auxTipos.add("fertilization");
                                    listaIds.add(activityObject.getString("id"));
                                    //SE TRADUCE Y AGREGA TIPO EN LISTA
                                    String auxTipo = traductorFertilizacionesInverso(activityObject.getString("type"));
                                    listaTiposActividades.add("Fertilización: " + auxTipo);
                                    auxSubtipos.add(activityObject.getString("type"));
                                    //SE TRADUCE FECHA INICIO
                                    String auxFecha = activityObject.getString("start_date");
                                    String divide2 = auxFecha;
                                    String[] separated2 = divide2.split("-");
                                    auxFecha = separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                    //SE TRADUCE FECHA FIN
                                    String auxFechaFin = activityObject.getString("end_date");
                                    String divide3 = auxFechaFin;
                                    String[] separated3 = divide3.split("-");
                                    auxFechaFin = separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                    //SE AGREGAN A LA LISTA DE FECHAS
                                    listaFechas.add(auxFecha + "-" + auxFechaFin);
                                    listaArboles.add(activityObject.getString("trees"));

                                    System.out.println("PROGRESO ES " + activityObject.getString("progress"));
                                    String auxPro = activityObject.getString("progress");
                                    float auxProg = Float.parseFloat(auxPro);
                                    auxProg = auxProg * 100;
                                    actualizarValores(auxProg);
                                    auxPro = String.format("%.1f", auxProg);
                                    listaProgresos.add(auxPro + "%");
                                }
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
                //Toast.makeText(DashboardActividadesActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    private void cargarRiegos() {
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,token.getDomain()+
                "/app/waterings/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("wateringsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                // TODO: set proper names for this variables
                                JSONObject activityObject = response.getJSONObject(i);
                                if (token.getGranjaActual().equals(activityObject.getString("farm"))) {
                                    auxTipos.add("watering");
                                    listaIds.add(activityObject.getString("id"));
                                    //SE TRADUCE Y AGREGA TIPO EN LISTA
                                    String auxTipo = traductorRiegosInverso(activityObject.getString("type"));
                                    listaTiposActividades.add("Riego: " + auxTipo);
                                    auxSubtipos.add(activityObject.getString("type"));
                                    //SE TRADUCE FECHA INICIO
                                    String auxFecha = activityObject.getString("start_date");
                                    String divide2 = auxFecha;
                                    String[] separated2 = divide2.split("-");
                                    auxFecha = separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                    //SE TRADUCE FECHA FIN
                                    String auxFechaFin = activityObject.getString("end_date");
                                    String divide3 = auxFechaFin;
                                    String[] separated3 = divide3.split("-");
                                    auxFechaFin = separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                    //SE AGREGAN A LA LISTA DE FECHAS
                                    listaFechas.add(auxFecha + "-" + auxFechaFin);
                                    listaArboles.add(activityObject.getString("trees"));
                                    System.out.println("PROGRESO ES " + activityObject.getString("progress"));
                                    String auxPro = activityObject.getString("progress");
                                    float auxProg = Float.parseFloat(auxPro);
                                    auxProg = auxProg * 100;
                                    actualizarValores(auxProg);
                                    auxPro = String.format("%.1f", auxProg);
                                    listaProgresos.add(auxPro + "%");
                                }
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
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    private void cargarPodas() {
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,token.getDomain()+
                "/app/prunings/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("pruningsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                // TODO: set proper names for this variables
                                JSONObject activityObject = response.getJSONObject(i);
                                if (token.getGranjaActual().equals(activityObject.getString("farm"))) {
                                    auxTipos.add("pruning");
                                    listaIds.add(activityObject.getString("id"));
                                    //SE TRADUCE Y AGREGA TIPO EN LISTA
                                    String auxTipo = traductorPodasInverso(activityObject.getString("type"));
                                    listaTiposActividades.add("Poda: " + auxTipo);
                                    auxSubtipos.add(activityObject.getString("type"));
                                    //SE TRADUCE FECHA INICIO
                                    String auxFecha = activityObject.getString("start_date");
                                    String divide2 = auxFecha;
                                    String[] separated2 = divide2.split("-");
                                    auxFecha = separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                    //SE TRADUCE FECHA FIN
                                    String auxFechaFin = activityObject.getString("end_date");
                                    String divide3 = auxFechaFin;
                                    String[] separated3 = divide3.split("-");
                                    auxFechaFin = separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                    //SE AGREGAN A LA LISTA DE FECHAS
                                    listaFechas.add(auxFecha + "-" + auxFechaFin);
                                    listaArboles.add(activityObject.getString("trees"));
                                    System.out.println("PROGRESO ES " + activityObject.getString("progress"));
                                    String auxPro = activityObject.getString("progress");
                                    float auxProg = Float.parseFloat(auxPro);
                                    auxProg = auxProg * 100;
                                    actualizarValores(auxProg);
                                    auxPro = String.format("%.1f", auxProg);
                                    listaProgresos.add(auxPro + "%");
                                }
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
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    private void cargarFumigaciones() {
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,token.getDomain()+
                "/app/fumigations/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("fumigationsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                // TODO: set proper names for this variables
                                System.out.println("RESPONSE ES " + response.toString());
                                JSONObject activityObject = response.getJSONObject(i);
                                if (token.getGranjaActual().equals(activityObject.getString("farm"))) {
                                    auxTipos.add("fumigation");
                                    listaIds.add(activityObject.getString("id"));
                                    //SE TRADUCE Y AGREGA TIPO EN LISTA
                                    String auxTipo = traductorFumigacionesInverso(activityObject.getString("type"));
                                    listaTiposActividades.add("Fumigación: " + auxTipo);
                                    auxSubtipos.add(activityObject.getString("type"));
                                    //SE TRADUCE FECHA INICIO
                                    String auxFecha = activityObject.getString("start_date");
                                    String divide2 = auxFecha;
                                    String[] separated2 = divide2.split("-");
                                    auxFecha = separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                    //SE TRADUCE FECHA FIN
                                    String auxFechaFin = activityObject.getString("end_date");
                                    String divide3 = auxFechaFin;
                                    String[] separated3 = divide3.split("-");
                                    auxFechaFin = separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                    //SE AGREGAN A LA LISTA DE FECHAS
                                    listaFechas.add(auxFecha + "-" + auxFechaFin);
                                    listaArboles.add(activityObject.getString("trees"));
                                    System.out.println("PROGRESO ES " + activityObject.getString("progress"));
                                    String auxPro = activityObject.getString("progress");
                                    float auxProg = Float.parseFloat(auxPro);
                                    auxProg = auxProg * 100;
                                    actualizarValores(auxProg);
                                    auxPro = String.format("%.1f", auxProg);
                                    listaProgresos.add(auxPro + "%");
                                }
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
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    private void cargarRecolecciones() {
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,token.getDomain()+
                "/app/recollections/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("recollectionsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                // TODO: set proper names for this variables
                                System.out.println("RESPONSE ES " + response.toString());
                                JSONObject activityObject = response.getJSONObject(i);
                                if (token.getGranjaActual().equals(activityObject.getString("farm"))) {
                                    auxTipos.add("recollection");
                                    listaIds.add(activityObject.getString("id"));
                                    //SE TRADUCE Y AGREGA TIPO EN LISTA
                                    String auxTipo = inicialTipoInversa(activityObject.getString("type"));
                                    listaTiposActividades.add("Recolección: " + auxTipo);
                                    auxSubtipos.add(activityObject.getString("type"));
                                    //SE TRADUCE FECHA INICIO
                                    String auxFecha = activityObject.getString("start_date");
                                    String divide2 = auxFecha;
                                    String[] separated2 = divide2.split("-");
                                    auxFecha = separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                    //SE TRADUCE FECHA FIN
                                    String auxFechaFin = activityObject.getString("end_date");
                                    String divide3 = auxFechaFin;
                                    String[] separated3 = divide3.split("-");
                                    auxFechaFin = separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                    //SE AGREGAN A LA LISTA DE FECHAS
                                    listaFechas.add(auxFecha + "-" + auxFechaFin);
                                    listaArboles.add(activityObject.getString("trees"));
                                    System.out.println("PROGRESO ES " + activityObject.getString("progress"));
                                    String auxPro = activityObject.getString("progress");
                                    float auxProg = Float.parseFloat(auxPro);
                                    auxProg = auxProg * 100;
                                    actualizarValores(auxProg);
                                    auxPro = String.format("%.1f", auxProg);
                                    listaProgresos.add(auxPro + "%");
                                }
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
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private String traductorRiegosInverso(String tipo) {
        switch (tipo) {
            case "S":
                return "Sistema de riego ";
            case "M":
                return "Manual";
            case "N":
                return "Natural";
        }
        return "";
    }

    private String traductorFertilizacionesInverso(String tipo) {
        switch (tipo) {
            case "C":
                return "Crecimiento";
            case "P":
                return "Produccion";
            case "M":
                return "Mantenimiento";
        }
        return "";
    }

    private String traductorFumigacionesInverso(String tipo) {
        switch (tipo) {
            case "I":
                return "Insectos";
            case "F":
                return "Hongos";
            case "H":
                return "Hierba";
            case "A":
                return "Ácaro";
            case "P":
                return "Peste";
        }
        return "";
    }

    private String traductorPodasInverso(String tipo) {
        switch (tipo) {
            case "S":
                return "Sanitaria";
            case "F":
                return "Formación";
            case "M":
                return "Mantenimiento";
            case "L":
                return "Limpieza";
        }
        return "";
    }

    private void llenadoLista() {
        if (!listaTiposActividades.isEmpty()) {
            dataModels.clear();
            for (int i = 0; i < listaTiposActividades.size(); i++) {
                dataModels.add(new ResumenActividadDataModel(listaTiposActividades.get(i), listaFechas.get(i), listaProgresos.get(i)));
            }
            ResumenActividadesAdapter adapter = new ResumenActividadesAdapter(dataModels, getApplicationContext());
            listView.setAdapter(adapter);
        }
    }

    private void actualizarValores(float number) {
        if (number > 0 && number < 100) {
            contProceso++;
        } else if (number > -1 && number < 1) {
            contPendientes++;
        }
        txtPendiente.setText(contPendientes + "\n" + "Actividades" + "\n" + "pendientes");
        txtProceso.setText(contProceso + "\n" + "Actividades en" + "\n" + "proceso");
    }

    private String inicialTipoInversa(String opcion) {
        switch (opcion) {
            case "M":
                return "Mango tommy";
            case "F":
                return "Mango farchil";
            case "N":
                return "Naranja";
            case "D":
                return "Mandarina";
            case "L":
                return "Limon";
            case "A":
                return "Aguacate";
            case "B":
                return "Banano";
        }
        return "";
    }
}

