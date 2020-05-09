package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerGastosActivity extends AppCompatActivity {

    private Token token;
    private List<String> conceptosGastos;
    private List<String> actividadesGastos;
    private List<String> tiposGastos;
    private List<String> valoresGastos;
    private ArrayList<ResumenGastoDataModel> dataModels;
    private ListView listView;
    private TextView total;
    private static ResumenGastosAdapter adapter;
    private int totalFinal;
    private DecimalFormat formatea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_gastos);
        token=(Token)getApplicationContext();
        formatea = new DecimalFormat("###,###.##");
        totalFinal=0;
        total=findViewById(R.id.textViewTotalHistorialGastos);
        listView = (ListView) findViewById(R.id.listaGastos);
        dataModels = new ArrayList<>();
        conceptosGastos=new ArrayList<>();
        actividadesGastos=new ArrayList<>();
        tiposGastos=new ArrayList<>();
        valoresGastos=new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(VerGastosActivity.this);
        JsonArrayRequest newOutcomeRequest = new JsonArrayRequest(Request.Method.GET,
                "http://10.0.2.2:8000/money/outcomes/?recommended=False"/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("outcomesList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject moneyObject = response.getJSONObject(i);
                                String concepto = moneyObject.getString("concept");
                                String valor = moneyObject.getString("value");
                                String actividad = traductorTipoActividadInverso(moneyObject.getString("activity"));
                                String tipo = traductorTipoInverso(moneyObject.getString("type"));
                                //CONVERSION DE FECHA
                                String fecha = moneyObject.getString("date");
                                String divide2 = fecha;
                                String[] separated2 = divide2.split("-");
                                fecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //CONVERSION SUB TIPO
                                String subtipo = traductorSubTipoActividadInverso(moneyObject.getString("act_type"));
                                conceptosGastos.add(concepto+" - "+fecha);
                                actividadesGastos.add(actividad+": "+subtipo);
                                tiposGastos.add(tipo);
                                valoresGastos.add("$"+valor);
                                totalFinal=totalFinal+Integer.parseInt(valor);
                                //System.out.println(concepto+" "+valor+" "+actividad+" "+tipo+" "+fecha+" "+subtipo);
                            }
                            total.setText("Total: $"+formatea.format(totalFinal));
                            for (int i = 0; i < conceptosGastos.size(); i++) {
                                dataModels.add(new ResumenGastoDataModel(conceptosGastos.get(i),actividadesGastos.get(i),tiposGastos.get(i),valoresGastos.get(i)));
                            }
                            adapter = new ResumenGastosAdapter(dataModels, getApplicationContext());
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(VerGastosActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
        queue.add(newOutcomeRequest);
    }

    private String traductorTipoInverso(String tipo){
        if(tipo.equals("M")){
            return "Materiales";
        }else{
            return "Mano de obra";
        }
    }

    private String traductorTipoActividadInverso(String tipo){
        if (tipo.equals("P")) {
            return "Poda";
        }
        else if (tipo.equals("U")) {
            return "Fumigación";
        }
        else if (tipo.equals("F")) {
            return "Fertilización";
        }else{
            return "Riego";
        }
    }

    private String traductorSubTipoActividadInverso(String tipo) {
        if (tipo.equals("R3")) {
            return "Sistema";
        }
        else if (tipo.equals("R2")) {
            return "Manual";
        }
        else if (tipo.equals("R1")) {
            return "Natural";
        }
        else if (tipo.equals("F1")) {
            return "Crecimiento";
        }
        else if (tipo.equals("F2")) {
            return "Produccion";
        }
        else if (tipo.equals("F3")){
            return "Mantenimiento";
        }
        else if (tipo.equals("U1")) {
            return "Insectos";
        }
        else if (tipo.equals("U2")) {
            return "Hongos";
        }
        else if (tipo.equals("U3")) {
            return "Hierba";
        }
        else if (tipo.equals("U4")) {
            return "Ácaro";
        }
        else if (tipo.equals("U5")) {
            return "Peste";
        }
        else if (tipo.equals("P1")) {
            return "Sanitaria";
        }
        else if (tipo.equals("P2")) {
            return "Formación";
        }
        else if (tipo.equals("P3")){
            return "Mantenimiento";
        }else {
            return "Limpieza";
        }
    }
}
