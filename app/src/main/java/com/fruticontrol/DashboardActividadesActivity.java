package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
    private static ResumenActividadesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_actividades);
        nuevaActividadButton = findViewById(R.id.buttonNuevaActividad);
        listView=findViewById(R.id.listaActividades);
        txtProceso=findViewById(R.id.textViewActProceso);
        txtPendiente=findViewById(R.id.textViewActPendientes);
        token=(Token)getApplicationContext();
        dataModels = new ArrayList<>();
        auxSubtipos=new ArrayList<>();
        auxTipos=new ArrayList<>();
        listaArboles=new ArrayList<>();
        listaIds=new ArrayList<>();
        listaTiposActividades=new ArrayList<>();
        listaFechas=new ArrayList<>();
        listaProgresos=new ArrayList<>();
        cargarFertilizaciones();
        cargarFumigaciones();
        cargarPodas();
        cargarRiegos();
        llenadoLista();

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
                intent.putExtra("listaArboles",listaArboles.get(i));
                intent.putExtra("tipoFoormato",auxTipos.get(i));
                intent.putExtra("subTipoFormato",auxSubtipos.get(i));


                startActivity(intent);
            }
        });
    }

    private void cargarFertilizaciones(){
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "https://app.fruticontrol.me/app/fertilizations/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("fertilizationsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activityObject = response.getJSONObject(i);
                                auxTipos.add("fertilization");
                                listaIds.add(activityObject.getString("id"));
                                //SE TRADUCE Y AGREGA TIPO EN LISTA
                                String auxTipo=traductorFertilizacionesInverso(activityObject.getString("type"));
                                listaTiposActividades.add("Fertilización: "+auxTipo);
                                auxSubtipos.add(activityObject.getString("type"));
                                //SE TRADUCE FECHA INICIO
                                String auxFecha = activityObject.getString("start_date");
                                String divide2 = auxFecha;
                                String[] separated2 = divide2.split("-");
                                auxFecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //SE TRADUCE FECHA FIN
                                String auxFechaFin = activityObject.getString("end_date");
                                String divide3 = auxFechaFin;
                                String[] separated3 = divide3.split("-");
                                auxFechaFin=separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                //SE AGREGAN A LA LISTA DE FECHAS
                                listaFechas.add(auxFecha+"-"+auxFechaFin);
                                listaArboles.add(activityObject.getString("trees"));

                                System.out.println("PROGRESO ES "+activityObject.getString("progress"));
                                String auxPro=activityObject.getString("progress");
                                float auxProg=Float.parseFloat(auxPro);
                                auxProg=auxProg*100;
                                auxPro=String.format("%.1f", auxProg);
                                listaProgresos.add(auxPro+"%");
                            }
                            llenadoLista();
                            actualizarValores();
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
                System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    private void cargarRiegos(){
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "https://app.fruticontrol.me/app/waterings/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("wateringsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activityObject = response.getJSONObject(i);
                                auxTipos.add("watering");
                                listaIds.add(activityObject.getString("id"));
                                //SE TRADUCE Y AGREGA TIPO EN LISTA
                                String auxTipo=traductorRiegosInverso(activityObject.getString("type"));
                                listaTiposActividades.add("Riego: "+auxTipo);
                                auxSubtipos.add(activityObject.getString("type"));
                                //SE TRADUCE FECHA INICIO
                                String auxFecha = activityObject.getString("start_date");
                                String divide2 = auxFecha;
                                String[] separated2 = divide2.split("-");
                                auxFecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //SE TRADUCE FECHA FIN
                                String auxFechaFin = activityObject.getString("end_date");
                                String divide3 = auxFechaFin;
                                String[] separated3 = divide3.split("-");
                                auxFechaFin=separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                //SE AGREGAN A LA LISTA DE FECHAS
                                listaFechas.add(auxFecha+"-"+auxFechaFin);
                                listaArboles.add(activityObject.getString("trees"));

                                System.out.println("PROGRESO ES "+activityObject.getString("progress"));
                                String auxPro=activityObject.getString("progress");
                                float auxProg=Float.parseFloat(auxPro);
                                auxProg=auxProg*100;
                                auxPro=String.format("%.1f", auxProg);
                                listaProgresos.add(auxPro+"%");
                            }

                            /*for(int j=0;j<listaTiposActividades.size();j++){
                                consultaAvance(auxTipos.get(j),listaIds.get(j),j);
                            }*/

                            llenadoLista();
                            actualizarValores();
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
                System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    private void cargarPodas(){
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "https://app.fruticontrol.me/app/prunings/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("pruningsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activityObject = response.getJSONObject(i);
                                auxTipos.add("pruning");
                                listaIds.add(activityObject.getString("id"));
                                //SE TRADUCE Y AGREGA TIPO EN LISTA
                                String auxTipo=traductorPodasInverso(activityObject.getString("type"));
                                listaTiposActividades.add("Poda: "+auxTipo);
                                auxSubtipos.add(activityObject.getString("type"));
                                //SE TRADUCE FECHA INICIO
                                String auxFecha = activityObject.getString("start_date");
                                String divide2 = auxFecha;
                                String[] separated2 = divide2.split("-");
                                auxFecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //SE TRADUCE FECHA FIN
                                String auxFechaFin = activityObject.getString("end_date");
                                String divide3 = auxFechaFin;
                                String[] separated3 = divide3.split("-");
                                auxFechaFin=separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                //SE AGREGAN A LA LISTA DE FECHAS
                                listaFechas.add(auxFecha+"-"+auxFechaFin);
                                listaArboles.add(activityObject.getString("trees"));

                                System.out.println("PROGRESO ES "+activityObject.getString("progress"));
                                String auxPro=activityObject.getString("progress");
                                float auxProg=Float.parseFloat(auxPro);
                                auxProg=auxProg*100;
                                auxPro=String.format("%.1f", auxProg);
                                listaProgresos.add(auxPro+"%");
                            }
                            llenadoLista();
                            actualizarValores();
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
                System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }

    private void cargarFumigaciones(){
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "https://app.fruticontrol.me/app/fumigations/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("fumigationsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                System.out.println("RESPONSE ES "+response.toString());
                                JSONObject activityObject = response.getJSONObject(i);
                                auxTipos.add("fumigation");
                                listaIds.add(activityObject.getString("id"));
                                //SE TRADUCE Y AGREGA TIPO EN LISTA
                                String auxTipo=traductorFumigacionesInverso(activityObject.getString("type"));
                                listaTiposActividades.add("Fumigación: "+auxTipo);
                                auxSubtipos.add(activityObject.getString("type"));
                                //SE TRADUCE FECHA INICIO
                                String auxFecha = activityObject.getString("start_date");
                                String divide2 = auxFecha;
                                String[] separated2 = divide2.split("-");
                                auxFecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //SE TRADUCE FECHA FIN
                                String auxFechaFin = activityObject.getString("end_date");
                                String divide3 = auxFechaFin;
                                String[] separated3 = divide3.split("-");
                                auxFechaFin=separated3[2] + "/" + separated3[1] + "/" + separated3[0];
                                //SE AGREGAN A LA LISTA DE FECHAS
                                listaFechas.add(auxFecha+"-"+auxFechaFin);
                                listaArboles.add(activityObject.getString("trees"));

                                System.out.println("PROGRESO ES "+activityObject.getString("progress"));
                                String auxPro=activityObject.getString("progress");
                                float auxProg=Float.parseFloat(auxPro);
                                auxProg=auxProg*100;
                                auxPro=String.format("%.1f", auxProg);
                                listaProgresos.add(auxPro+"%");
                            }
                            llenadoLista();
                            actualizarValores();
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
            dataModels.clear();
            for (int i = 0; i < listaTiposActividades.size(); i++) {
                dataModels.add(new ResumenActividadDataModel(listaTiposActividades.get(i).toString(), listaFechas.get(i),listaProgresos.get(i)));
            }
            adapter = new ResumenActividadesAdapter(dataModels, getApplicationContext());
            listView.setAdapter(adapter);
        }else{
            System.out.println("XXXXXXXXXXXXXXXXXXXX ESTA VACIA");
        }
    }

    private void actualizarValores(){
        int contProceso=0;
        int contPendientes=0;
        for(int i=0;i<listaTiposActividades.size();i++){
            if(listaProgresos.get(i).equals("0,0%")){
                contPendientes++;
            }else if(!listaProgresos.get(i).equals("0,0%")){
                if(!listaProgresos.get(i).equals("100,0%")){
                    contProceso++;
                }
            }
        }
        txtPendiente.setText(contPendientes+"\n"+"Actividades"+"\n" +"pendientes");
        txtProceso.setText(contProceso+"\n"+"Actividades en"+"\n"+"proceso");
    }

    private void consultaAvance(String tipo, String idActividad, int posACambiar){
        final int[] auxProgreso = {0};
        final int pos=posACambiar;
        RequestQueue queue = Volley.newRequestQueue(DashboardActividadesActivity.this);
        String url="http://10.0.2.2:8000/app/";
        url=url+tipo+"_trees/?"+tipo+"="+idActividad;
        System.out.println("XXXXXXXXXXXXXXXXX URL ES "+url);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                url/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("progressList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activityObject = response.getJSONObject(i);
                                if(activityObject.getString("applied").equals("true")){
                                    auxProgreso[0]++;
                                }
                            }
                            if(response.length()!=0){
                                dataModels.get(pos).setProgreso(Integer.toString(auxProgreso[0]/response.length()*100)+"%");
                            }else{
                                dataModels.get(pos).setProgreso("0%");
                            }
                            System.out.println("XXXXXXXXXXXXXXXXXXXX TAMAÑANO DE LISTA PROGESOS EN PORCENTAJES ES "+listaProgresos.size());



                            adapter = new ResumenActividadesAdapter(dataModels, getApplicationContext());
                            listView.setAdapter(adapter);




                            System.out.println(" "+dataModels.get(0).getProgreso());

                            //System.out.println("XXXXXXXXXXX VALOR FINALD E AUX PROGRESO FUE "+auxProgreso[0]);
                            //System.out.println("XXXXXXXXXXX EN PORCENTAJE FUE "+auxProgreso[0]/response.length()*100);
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
                System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
    }
}

