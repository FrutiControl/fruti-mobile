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

public class VerIngresosActivity extends AppCompatActivity {

    private List<String> conceptosIngresos;
    private List<String> valoresIngresos;
    private List<String> cantidadesIngresos;
    private List<String> tiposFrutaIngresos;
    private TextView txTotalFinal;
    private ArrayList<ResumenGastoDataModel> dataModels;
    private ListView listView;
    private static ResumenGastosAdapter adapter;
    private Token token;
    private int totalFinal;
    private DecimalFormat formatea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ingresos);
        formatea = new DecimalFormat("###,###.##");
        totalFinal=0;
        dataModels = new ArrayList<>();
        listView=findViewById(R.id.listaIngresos);
        txTotalFinal=findViewById(R.id.textViewTotalIngresos);
        conceptosIngresos=new ArrayList<>();
        valoresIngresos=new ArrayList<>();
        cantidadesIngresos=new ArrayList<>();
        tiposFrutaIngresos=new ArrayList<>();
        token=(Token)getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(VerIngresosActivity.this);
        JsonArrayRequest newIncomeRequest = new JsonArrayRequest(Request.Method.GET,
                "http://10.0.2.2:8000/money/incomes/?recommended=False"/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("incomesList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject moneyObject = response.getJSONObject(i);
                                String concepto = moneyObject.getString("concept");
                                String valor = moneyObject.getString("value");
                                String tipo = inicialTipoInversa(moneyObject.getString("fruit_type"));
                                String cantidad=moneyObject.getString("quantity");
                                //CONVERSION DE FECHA
                                String fecha = moneyObject.getString("date");
                                String divide2 = fecha;
                                String[] separated2 = divide2.split("-");
                                fecha=separated2[2] + "/" + separated2[1] + "/" + separated2[0];
                                conceptosIngresos.add(concepto+" - "+fecha);
                                tiposFrutaIngresos.add(tipo);
                                cantidadesIngresos.add(cantidad+" canastillas a $"+formatea.format(valor)+" c/u");
                                int auxTotal=Integer.parseInt(cantidad)*Integer.parseInt(valor);
                                valoresIngresos.add("$"+auxTotal);
                                totalFinal=totalFinal+auxTotal;
                            }
                            txTotalFinal.setText("Total: $"+formatea.format(totalFinal));
                            for (int i = 0; i < conceptosIngresos.size(); i++) {
                                dataModels.add(new ResumenGastoDataModel(conceptosIngresos.get(i),tiposFrutaIngresos.get(i),cantidadesIngresos.get(i),valoresIngresos.get(i)));
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
                Toast.makeText(VerIngresosActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
        queue.add(newIncomeRequest);
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
            default:
                return "Banano";
        }
    }
}
