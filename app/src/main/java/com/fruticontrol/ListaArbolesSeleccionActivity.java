package com.fruticontrol;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaArbolesSeleccionActivity extends AppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener{

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
    private Button nuevoArbolButton;
    private Token token;
    private Boolean[] places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_arboles_seleccion);
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
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int color = Color.TRANSPARENT;
                Drawable background = view.getBackground();
                color = ((ColorDrawable) background).getColor();
                if(color==Color.GREEN){
                    view.setBackgroundColor(Color.TRANSPARENT);
                    places[i]=false;
                }else{
                    view.setBackgroundColor(Color.GREEN);
                    places[i]=true;
                }
            }
        });*/
        nuevoArbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("PLACES ES ");
                for(int i=0;i<places.length;i++){
                    System.out.println(places[i]);
                }
                List<Integer> idsSeleccionados=new ArrayList<>();
                for(int i=0;i<places.length;i++){
                    if(places[i]){
                        System.out.println("EL ID DEL SELECCIONADO FUE "+idsArboles.get(i));
                        System.out.println("EL ID DEL SELECCIONADO FUE EN INTEGER "+Integer.parseInt(idsArboles.get(i)));
                        idsSeleccionados.add(Integer.parseInt(idsArboles.get(i)));
                    }
                }
                System.out.println("LOS IDS AGREGADOS FUERON");
                for(int i=0;i<idsSeleccionados.size();i++){
                    System.out.println("A "+idsSeleccionados.get(i).toString());
                }
                Intent intent = new Intent();
                intent.putExtra("arbolesActividad", (ArrayList<Integer>) idsSeleccionados);
                setResult(RESULT_OK, intent);
                finish();
                token.setArbolEscogido(true);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(ListaArbolesSeleccionActivity.this);
        JsonArrayRequest allTreesRequest = new JsonArrayRequest(Request.Method.GET,
                "https://app.fruticontrol.me/app/trees/", null,
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
                                    String etapa = farmObject.getString("life_phase");
                                    String localizacion = farmObject.getString("location");
                                    idsArboles.add(id);
                                    tiposArboles.add(inicialTipoInversa(tipo));
                                    etapasArboles.add(devolverNombreFase(etapa));
                                    fechasSiembra.add(fecha);
                                    localizacionesArboles.add(localizacion);
                                }
                            }
                            places=new Boolean[idsArboles.size()];
                            for(int i=0;i<places.length;i++){
                                places[i]=false;
                            }
                            /*
                            if(!idsArboles.isEmpty()){
                                //SE LLENA LA LISTA
                                for(int i=0;i<idsArboles.size();i++){
                                    dataModels.add(new ResumenArbolSeleccionDataModel("Número de árbol: "+idsArboles.get(i).toString(),tiposArboles.get(i).toString(),etapasArboles.get(i)));
                                }
                                adapter= new ResumenArbolesSeleccionAdapter(dataModels,getApplicationContext());
                                listView.setAdapter(adapter);
                                places=new Boolean[idsArboles.size()];
                                System.out.println("XXXXXXXXXXX child count es "+idsArboles.size());
                                System.out.println("XXXXXXXXXXX SIZE DE PLACES ES "+places.length);
                                for(int i=0;i<places.length;i++){
                                    places[i]=false;
                                }
                            }
                            */
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


    private void showTrees(){
        arbolList= new ArrayList<Arbol>();
        for(int i=0;i<idsArboles.size();i++){
            String auxId="Número de árbol: "+idsArboles.get(i);
            String auxTipo=tiposArboles.get(i);
            arbolList.add(new Arbol(auxId,auxTipo));
        }
        arAdapter=new ArbolAdapter(arbolList,this);
        listView.setAdapter(arAdapter);
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

    private String devolverNombreFase(String num){
        if(num.equals("1"))
            return "Crecimiento";
        if(num.equals("2"))
            return "Floracion";
        if(num.equals("2"))
            return "Produccion";
        else
            return "Post-Produccion";
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos=listView.getPositionForView(buttonView);
        if(pos!=ListView.INVALID_POSITION){
            Arbol a=arbolList.get(pos);
            if(isChecked){
                places[pos]=true;
            }else{
                places[pos]=false;
            }
            a.setSelected(isChecked);

            //Toast.makeText(this,"Chosen one: "+a.getId()+" and type is "+isChecked,Toast.LENGTH_SHORT).show();
        }
    }
}
