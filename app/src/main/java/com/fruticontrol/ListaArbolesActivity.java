package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Map;

public class ListaArbolesActivity extends AppCompatActivity {

    ArrayList<ResumenArbolDataModel> dataModels;
    ListView listView;
    private static ResumenArbolesAdapter adapter;
    private ArrayList<String> idsArboles;
    private ArrayList<String> tiposArboles;
    private ArrayList<String> etapasArboles;
    private ArrayList<String> fechasSiembra;
    private ArrayList<String> localizacionesArboles;
    private Button nuevoArbolButton;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_arboles);
        token=(Token)getApplicationContext();
        listView=(ListView)findViewById(R.id.listaArbolesList);
        nuevoArbolButton=findViewById(R.id.buttonNuevoArbol);
        dataModels= new ArrayList<>();
        idsArboles=new ArrayList<>();
        tiposArboles=new ArrayList<>();
        etapasArboles=new ArrayList<>();
        fechasSiembra=new ArrayList<>();
        localizacionesArboles=new ArrayList<>();
        nuevoArbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),CrearArbolActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(view.getContext(),ModificarArbolActivity.class);
                intent.putExtra("idArbolActual",tiposArboles.get(i));
                intent.putExtra("tipo",tiposArboles.get(i));
                intent.putExtra("fecha",fechasSiembra.get(i));
                intent.putExtra("localizacion",localizacionesArboles.get(i));
                startActivity(intent);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(ListaArbolesActivity.this);
        JsonArrayRequest allTreesRequest = new JsonArrayRequest(Request.Method.GET,
                "http://10.0.2.2:8000/app/trees/"/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("treesList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject farmObject = response.getJSONObject(i);
                                if(farmObject.getString("farm")==token.getGranjaActual()){
                                    String id = farmObject.getString("id");
                                    String tipo = farmObject.getString("specie");
                                    String fecha = farmObject.getString("seed_date");
                                    String localizacion = farmObject.getString("location");
                                    idsArboles.add(id);
                                    tiposArboles.add(inicialTipoInversa(tipo));
                                    fechasSiembra.add(fecha);
                                    localizacionesArboles.add(localizacion);
                                }
                            }
                            if(!idsArboles.isEmpty()){
                                //SE LLENA LA LISTA
                                for(int i=0;i<idsArboles.size();i++){
                                    dataModels.add(new ResumenArbolDataModel("Número de árbol: "+idsArboles.get(i).toString(),tiposArboles.get(i).toString(),"Fertilizacion"));
                                }
                                adapter= new ResumenArbolesAdapter(dataModels,getApplicationContext());
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
                Toast.makeText(ListaArbolesActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }){    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token "+token.getToken());
                System.out.println("XXXXXXXXX EL TOKEN ES "+token.getToken());
                return params;
            }
        };
        queue.add(allTreesRequest);
    }


    private String inicialTipoInversa(String opcion){
        if(opcion.equals("M")){
            return "Mango tommy";
        }
        else if(opcion.equals("F")){
            return "Mango farchil";
        }
        else if(opcion.equals("N")){
            return "Naranja";
        }
        else if(opcion.equals("D")){
            return "Mandarina";
        }
        else if(opcion.equals("L")){
            return "Limon";
        }
        else if(opcion.equals("A")){
            return "Aguacate";
        }else{
            return "Banano";
        }
    }
}
