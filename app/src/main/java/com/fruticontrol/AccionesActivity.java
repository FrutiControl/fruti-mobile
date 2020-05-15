package com.fruticontrol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccionesActivity extends AppCompatActivity {

    private Button arbolButton;
    private Button dashboardActividadesButton;
    private Button dashboardFinanzasButton;
    private Button perfilButton;
    private Button verGranjasButton;
    private Token token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        token = (Token) getApplicationContext();
        arbolButton = findViewById(R.id.buttonArbol);
        dashboardActividadesButton = findViewById(R.id.buttonDashboardActividades);
        dashboardFinanzasButton = findViewById(R.id.buttonDashboardFinanzas);
        perfilButton = findViewById(R.id.buttonPerfil);
        verGranjasButton = findViewById(R.id.buttonVerGranjas);
        System.out.println("XXXXXX LA GRANJA TOUCHADA FUE LA " + token.getGranjaActual());

        verGranjasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListaGranjasActivity.class);
                startActivity(intent);
            }
        });
        arbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListaArbolesActivity.class);
                startActivity(intent);
            }
        });

        dashboardActividadesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DashboardActividadesActivity.class);
                startActivity(intent);
            }
        });

        dashboardFinanzasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DashboardFinanzasActivity.class);
                startActivity(intent);
            }
        });

        perfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                RequestQueue queue = Volley.newRequestQueue(AccionesActivity.this);
                JsonObjectRequest allTreesRequest = new JsonObjectRequest(Request.Method.GET,
                        "https://app.fruticontrol.me/users/user/"/*TODO: cambiar a URL real para producción!!!!*/, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("getUsers", response.toString());
                                try {
                                    Intent intent = new Intent(view.getContext(), PerfilActivity.class);
                                    intent.putExtra("nombre",response.getString("first_name"));
                                    intent.putExtra("correo",response.getString("username"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                        Toast.makeText(AccionesActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
        });

    }
}


//https://www.flaticon.com/free-icon/plant_2487517
//https://www.flaticon.com/free-icon/tree_1625460
//https://www.flaticon.com/free-icon/money_2694206
//https://www.flaticon.com/free-icon/farmer_2010087?term=farmer&page=2&position=29
