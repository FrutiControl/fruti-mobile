package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PreferenciasActivity extends AppCompatActivity {

    private EditText valorJornalET;
    private Button guardarPreferenciasButton;
    private Token token;
    private String auxCity;
    private String auxDepartment;
    private String auxCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
        token=(Token)getApplicationContext();
        valorJornalET = findViewById(R.id.editTextValorJornal);
        guardarPreferenciasButton = findViewById(R.id.buttonGuardarPreferencias);
        RequestQueue queue = Volley.newRequestQueue(PreferenciasActivity.this);
        JsonObjectRequest dayCostRequest = new JsonObjectRequest(Request.Method.GET,
                "http://10.0.2.2:8000/users/owner/"/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("dayCost", response.toString());
                        try {
                            auxCity=response.getString("city");
                            auxDepartment=response.getString("department");
                            auxCountry=response.getString("country");
                            valorJornalET.setText(response.getString("day_cost"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("dayCostAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(PreferenciasActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }){    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token " + token.getToken());
                System.out.println("XXXXXXXXX EL TOKEN ES "+token.getToken());
                return params;
            }
        };
        queue.add(dayCostRequest);

        guardarPreferenciasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    RequestQueue queue = Volley.newRequestQueue(PreferenciasActivity.this);
                    String body = "{\"day_cost\":\"" + valorJornalET.getText().toString()+ "\",\"city\":\"" + auxCity+ "\",\"department\":\"" + auxDepartment+ "\",\"country\":\"" + auxCountry + "\"}";
                    Log.i("modificateTreeAPI", "Jornal modificado: " + body);
                    JSONObject newDayCost = null;
                    try {
                        newDayCost = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String auxUrl = "http://10.0.2.2:8000/users/owner/";
                    JsonObjectRequest newDayCostRequest = new JsonObjectRequest(Request.Method.PUT,
                            auxUrl/*TODO: cambiar a URL real para producción!!!!*/, newDayCost,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("modificateDayCostAPI", response.toString());

                                    if (response.has("error")) {
                                        try {
                                            Toast.makeText(PreferenciasActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(PreferenciasActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }) {    //this is the part, that adds the header to the request
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Token " + token.getToken());
                            System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                            return params;
                        }
                    };
                    queue.add(newDayCostRequest);
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        if (TextUtils.isEmpty(valorJornalET.getText().toString())) {
            valorJornalET.setError("Requerido");
            valid = false;
        } else {
            valorJornalET.setError(null);
        }
        return valid;
    }
}

//Icons made by <a href="https://www.flaticon.com/authors/eucalyp" title="Eucalyp">Eucalyp</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>
