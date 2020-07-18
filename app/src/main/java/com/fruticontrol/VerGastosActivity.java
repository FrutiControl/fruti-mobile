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
    private Config config;
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
        config = (Config) getApplicationContext();
        formatea = new DecimalFormat("###,###.##");
        totalFinal = 0;
        total = findViewById(R.id.textViewTotalHistorialGastos);
        listView = findViewById(R.id.listaGastos);
        dataModels = new ArrayList<>();
        conceptosGastos = new ArrayList<>();
        actividadesGastos = new ArrayList<>();
        tiposGastos = new ArrayList<>();
        valoresGastos = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(VerGastosActivity.this);
        JsonArrayRequest newOutcomeRequest = new JsonArrayRequest(Request.Method.GET, config.getDomain()+
                "/money/outcomes/?recommended=False", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("outcomesList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject moneyObject = response.getJSONObject(i);
                                // TODO: set proper names for this variables
                                String concepto = moneyObject.getString("concept");
                                String valor = moneyObject.getString("value");
                                String actividad = traductorTipoActividadInverso(moneyObject.getString("activity"));
                                String tipo = traductorTipoInverso(moneyObject.getString("type"));
                                //CONVERSION DE FECHA
                                String fecha = moneyObject.getString("date");
                                String divide2 = fecha;
                                String[] separated2 = divide2.split("-");
                                fecha = separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                //CONVERSION SUB TIPO
                                String subtipo = traductorSubTipoActividadInverso(moneyObject.getString("act_type"));
                                conceptosGastos.add(concepto + " - " + fecha);
                                actividadesGastos.add(actividad + ": " + subtipo);
                                tiposGastos.add(tipo);
                                valoresGastos.add("$" + valor);
                                totalFinal = totalFinal + Integer.parseInt(valor);
                                //System.out.println(concepto+" "+valor+" "+actividad+" "+tipo+" "+fecha+" "+subtipo);
                            }
                            total.setText("Total: $" + formatea.format(totalFinal));
                            for (int i = 0; i < conceptosGastos.size(); i++) {
                                dataModels.add(new ResumenGastoDataModel(conceptosGastos.get(i), actividadesGastos.get(i), tiposGastos.get(i), valoresGastos.get(i)));
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
                params.put("Authorization", "Token " + config.getToken());
                return params;
            }
        };
        queue.add(newOutcomeRequest);
    }

    private String traductorTipoInverso(String tipo) {
        if (tipo.equals("M")) {
            return "Materiales";
        } else {
            return "Mano de obra";
        }
    }

    private String traductorTipoActividadInverso(String tipo) {
        switch (tipo) {
            case "P":
                return "Poda";
            case "U":
                return "Fumigación";
            case "F":
                return "Fertilización";
            default:
                return "Riego";
        }
    }

    private String traductorSubTipoActividadInverso(String tipo) {
        switch (tipo) {
            case "R3":
                return "Sistema";
            case "R2":
                return "Manual";
            case "F1":
                return "Crecimiento";
            case "F2":
                return "Produccion";
            case "F3":
            case "P3":
                return "Mantenimiento";
            case "U1":
                return "Insectos";
            case "U2":
                return "Hongos";
            case "U3":
                return "Hierba";
            case "U4":
                return "Ácaro";
            case "U5":
                return "Peste";
            case "P1":
                return "Sanitaria";
            case "P2":
                return "Formación";
            default:
                return "Limpieza";
        }
    }
}
