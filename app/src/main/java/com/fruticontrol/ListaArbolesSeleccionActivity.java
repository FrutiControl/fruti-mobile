package com.fruticontrol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ListaArbolesSeleccionActivity extends AppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener {

    ArrayList<ResumenArbolSeleccionDataModel> dataModels;
    ArrayList<Arbol> arbolList;
    ArbolAdapter arAdapter;
    ListView listView;
    private static ResumenArbolesSeleccionAdapter adapter;
    private ArrayList<String> idsArboles;
    private ArrayList<String> tiposArboles;
    private ArrayList<String> etapasArboles;
    private ArrayList<String> fechasSiembra;
    private ArrayList<String> localizacionesArboles;
    private Token token;
    private Boolean[] places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_arboles_seleccion);
        token = (Token) getApplicationContext();
        listView = findViewById(R.id.listaArbolesList);
        Button nuevoArbolButton = findViewById(R.id.buttonNuevoArbol);
        dataModels = new ArrayList<>();
        idsArboles = new ArrayList<>();
        tiposArboles = new ArrayList<>();
        etapasArboles = new ArrayList<>();
        fechasSiembra = new ArrayList<>();
        localizacionesArboles = new ArrayList<>();
        nuevoArbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CrearArbolActivity.class);
                startActivity(intent);
            }
        });
        nuevoArbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("PLACES ES ");
                for (Boolean place : places) {
                    System.out.println(place);
                }
                ArrayList<Integer> idsSeleccionados = new ArrayList<>();
                for (int i = 0; i < places.length; i++) {
                    if (places[i]) {
                        System.out.println("EL ID DEL SELECCIONADO FUE " + idsArboles.get(i));
                        System.out.println("EL ID DEL SELECCIONADO FUE EN INTEGER " + Integer.parseInt(idsArboles.get(i)));
                        idsSeleccionados.add(Integer.parseInt(idsArboles.get(i)));
                    }
                }
                System.out.println("LOS IDS AGREGADOS FUERON");
                for (int i = 0; i < idsSeleccionados.size(); i++) {
                    System.out.println("A " + idsSeleccionados.get(i).toString());
                }
                Intent intent = new Intent();
                intent.putExtra("arbolesActividad", idsSeleccionados);
                setResult(RESULT_OK, intent);
                finish();
                token.setArbolEscogido(true);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(ListaArbolesSeleccionActivity.this);
        JsonArrayRequest allTreesRequest = new JsonArrayRequest(Request.Method.GET,token.getDomain()+
                "/app/trees/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("treesList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject farmObject = response.getJSONObject(i);
                                if (farmObject.getString("farm").equals(token.getGranjaActual())) {
                                    String id = farmObject.getString("id");
                                    String tipo = farmObject.getString("specie");
                                    String fecha = farmObject.getString("seed_date");
                                    String etapa = farmObject.getString("life_phase");
                                    String localizacion = farmObject.getString("location");
                                    idsArboles.add(id);
                                    tiposArboles.add(inicialTipoInversa(tipo));
                                    etapasArboles.add(devolverNombreFase(etapa));
                                    fechasSiembra.add(fecha);
                                    localizacionesArboles.add(localizacion);
                                }
                            }
                            places = new Boolean[idsArboles.size()];
                            Arrays.fill(places, false);
                            showTrees();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(ListaArbolesSeleccionActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(allTreesRequest);
    }

    private void showTrees() {
        arbolList = new ArrayList<Arbol>();
        for (int i = 0; i < idsArboles.size(); i++) {
            String auxId = "Número de árbol: " + idsArboles.get(i);
            String auxTipo = tiposArboles.get(i);
            arbolList.add(new Arbol(auxId, auxTipo));
        }
        arAdapter = new ArbolAdapter(arbolList, this);
        listView.setAdapter(arAdapter);
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

    private String devolverNombreFase(String num) {
        switch (num) {
            case "1":
                return "Crecimiento";
            case "2":
                return "Floracion";
            case "3":
                return "Produccion";
            case "4":
                return "Post-Produccion";
        }
        return "";
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = listView.getPositionForView(buttonView);
        if (pos != ListView.INVALID_POSITION) {
            Arbol a = arbolList.get(pos);
            places[pos] = isChecked;
            a.setSelected(isChecked);
        }
    }
}
