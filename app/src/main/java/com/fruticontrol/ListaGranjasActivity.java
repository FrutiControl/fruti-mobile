package com.fruticontrol;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class ListaGranjasActivity extends AppCompatActivity {
    Button nuevaGranjaButton;
    ArrayList<ResumenGranjaDataModel> dataModels;
    ListView listView;
    TextView txtTituloGranja;
    private static ResumenGranjasAdapter adapter;
    private Token token;
    private ArrayList<String> nombresGranjas;
    private ArrayList<String> idGranjas;
    private ArrayList<String> puntosGranjas;
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_granjas);
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        token = (Token) getApplicationContext();
        listView = findViewById(R.id.listaGranjasList);
        txtTituloGranja = findViewById(R.id.textViewTituloGrnjas);
        nuevaGranjaButton = findViewById(R.id.buttonNuevaGranja);
        dataModels = new ArrayList<>();
        puntosGranjas = new ArrayList<>();
        nombresGranjas = new ArrayList<>();
        idGranjas = new ArrayList<>();

        new MaterialIntroView.Builder(this)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("En esta pantalla puede visualizar su lista de granjas, para crear una nueva granja haga clic en el boton nueva granja")
                .setShape(ShapeType.CIRCLE)
                .setTarget(nuevaGranjaButton)
                .setUsageId("lista_granja_showcase")
                .show();

        RequestQueue queue = Volley.newRequestQueue(ListaGranjasActivity.this);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                "https://app.fruticontrol.me/app/farms/", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("farmsList", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject farmObject = response.getJSONObject(i);
                                String id = farmObject.getString("id");
                                String name = farmObject.getString("name");
                                String puntos = farmObject.getString("polygon");
                                nombresGranjas.add(name);
                                idGranjas.add(id);
                                puntosGranjas.add(puntos);
                            }
                            if (!nombresGranjas.isEmpty()) {
                                //SE LLENA LA LISTA
                                for (int i = 0; i < nombresGranjas.size(); i++) {
                                    dataModels.add(new ResumenGranjaDataModel(nombresGranjas.get(i).toString()));
                                }
                                adapter = new ResumenGranjasAdapter(dataModels, getApplicationContext());
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
                Toast.makeText(ListaGranjasActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                token.setGranjaActual(idGranjas.get(i));
                // TODO: set proper names for this variables
                String separated[] = puntosGranjas.get(i).split("\\(");
                String aux[] = separated[2].split("\\)");
                System.out.println("SEPARATED 2 ES " + separated[2]);
                String soloXY[] = aux[0].split(",");
                ArrayList<String> auxPuntosLimpios = new ArrayList<>();
                for (int a = 0; a < soloXY.length; a++) {
                    if (a == 0) {
                        String otroAux[] = soloXY[0].split(" ");
                        auxPuntosLimpios.add(otroAux[0]);
                        auxPuntosLimpios.add(otroAux[1]);
                    } else {
                        String otroAux[] = soloXY[a].split(" ");
                        auxPuntosLimpios.add(otroAux[1]);
                        auxPuntosLimpios.add(otroAux[2]);
                    }
                }
                token.setPuntosPoligonoGranja(auxPuntosLimpios);

                Intent intent = new Intent(view.getContext(), AccionesActivity.class);
                startActivity(intent);
            }
        });
        nuevaGranjaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NuevaGranjaActivity.class);
                startActivity(intent);

            }
        });
    }

    private void requestPermission(Activity context, String permiso, String justificacion, int idCode) {
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, idCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "sin acceso a localizacion, hardware deshabilitado!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
