package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class DashboardFinanzasActivity extends AppCompatActivity {
    private TextView txTotalIngresos;
    private TextView txTotalGastos;
    private TextView txTotal;
    private int totalGastos;
    private int totalIngresos;
    int auxTotal;
    private Token token;
    private DecimalFormat formatea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_finanzas);
        auxTotal = 0;
        formatea = new DecimalFormat("###,###.##");
        token = (Token) getApplicationContext();
        TextView txFinanzas = findViewById(R.id.textViewTituloFinanzas);
        txTotalIngresos = findViewById(R.id.textViewIngresosDashboard);
        txTotalGastos = findViewById(R.id.textViewGastosDashboard);
        Button nuevoIngresoButton = findViewById(R.id.buttonNuevoIngresoDFinanzas);
        Button nuevoGastoButton = findViewById(R.id.buttonNuevoGastoDFinanzas);
        Button verIngresosButton = findViewById(R.id.buttonVerIngresos);
        Button verGastosButton = findViewById(R.id.buttonVerGastos);
        txTotal = findViewById(R.id.textViewTotalFinanzas);
        new MaterialIntroView.Builder(this)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(false)
                .setInfoText("En esta pantalla puede ver su total de ingresos y gastos. Con los botones de ver gastos e ingresos puede ver el detalle y con los botones de nuevo gasto e ingreso puede agregarlos")
                .setShape(ShapeType.RECTANGLE)
                .setTarget(txFinanzas)
                .setUsageId("dash_finanzas_showcase")
                .show();
        RequestQueue queue = Volley.newRequestQueue(DashboardFinanzasActivity.this);
        JsonArrayRequest newOutcomeRequest = new JsonArrayRequest(Request.Method.GET,token.getDomain()+
                "/money/outcomes/?recommended=False", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("outcomesList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject moneyObject = response.getJSONObject(i);
                                String valor = moneyObject.getString("value");
                                totalGastos = totalGastos + Integer.parseInt(valor);
                            }
                            txTotalGastos.setText("$" + formatea.format(totalGastos));
                            auxTotal = auxTotal - totalGastos;
                            DecimalFormat formatea = new DecimalFormat("###,###.##");
                            txTotal.setText("Total: $" + formatea.format(auxTotal));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(DashboardFinanzasActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(newOutcomeRequest);
        JsonArrayRequest newIncomeRequest = new JsonArrayRequest(Request.Method.GET,token.getDomain()+
                "/money/incomes/?recommended=False", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("incomesList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject moneyObject = response.getJSONObject(i);
                                String valor = moneyObject.getString("value");
                                String cantidad = moneyObject.getString("quantity");
                                int totalIngreso = Integer.parseInt(cantidad) * Integer.parseInt(valor);
                                totalIngresos = totalIngresos + totalIngreso;
                            }
                            txTotalIngresos.setText("$" + formatea.format(totalIngresos));
                            auxTotal = auxTotal + totalIngresos;

                            txTotal.setText("Total: $" + formatea.format(auxTotal));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(DashboardFinanzasActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(newIncomeRequest);
        verGastosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VerGastosActivity.class);
                startActivity(intent);
            }
        });
        verIngresosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VerIngresosActivity.class);
                startActivity(intent);
            }
        });
        nuevoIngresoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NuevoIngresoActivity.class);
                startActivity(intent);
            }
        });

        nuevoGastoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NuevoGastoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
