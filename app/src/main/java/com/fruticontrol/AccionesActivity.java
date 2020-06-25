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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class AccionesActivity extends AppCompatActivity {
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        token = (Token) getApplicationContext();
        Button arbolButton = findViewById(R.id.buttonArbol);
        Button dashboardActividadesButton = findViewById(R.id.buttonDashboardActividades);
        Button dashboardFinanzasButton = findViewById(R.id.buttonDashboardFinanzas);
        Button perfilButton = findViewById(R.id.buttonPerfil);
        Button verGranjasButton = findViewById(R.id.buttonVerGranjas);
        System.out.println("XXXXXX LA GRANJA TOUCHADA FUE LA " + token.getGranjaActual());

        new MaterialIntroView.Builder(this)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Para comenzar debe agregar árboles a la granja, esto lo puede realizar desde la opción Árboles. Luego, puede crear actividades a partir de los árboles ya agregados desde el botón actividades. Además de esto puede manejar sus finanzas en el botón finanzas y cambiar de granja en el botón granja. Por último en perfil puede modificar los datos de la cuenta y la preferencia del valor del jornal")
                .setShape(ShapeType.CIRCLE)
                .setTarget(arbolButton)
                .setUsageId("first_showcase")
                .show();

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
                        "https://app.fruticontrol.me/users/user/", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("getUsers", response.toString());
                                try {
                                    Intent intent = new Intent(view.getContext(), PerfilActivity.class);
                                    intent.putExtra("nombre", response.getString("first_name"));
                                    intent.putExtra("correo", response.getString("username"));
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
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Token " + token.getToken());
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
