package com.fruticontrol;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CrearArbolActivity extends AppCompatActivity {

    private EditText textFechaSiembra;
    private EditText textUltimaPoda;
    private EditText textUltimaFumigacion;
    private EditText textUltimaFertilizacion;
    private EditText textUltimoRiego;
    private Button buttonNuevoArbol;
    private Button buttonUbicacion;
    Calendar cal, cal1, cal2, cal3, cal4;
    DatePickerDialog dpd, dpd2, dpd3, dpd4, dpd5;
    private Spinner spinnerTipoArbol;
    private Spinner spinnerTipoPoda;
    private Spinner spinnerTipoFumigacion;
    private Spinner spinnerTipoFertilizacion;
    private Spinner spinnerTipoRiego;
    private Token token;
    private String lat;
    private String lon;
    private Boolean consumoPoda;
    private Boolean consumoFumigacion;
    private Boolean consumoFertilzacion;
    private Boolean consumoRiego;
    private Integer auxIdArbol;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_arbol);
        statusCheck();
        token = (Token) getApplicationContext();
        token.setArbolEscogido(false);
        auxIdArbol=0;
        consumoPoda=true;
        consumoFumigacion=true;
        consumoFertilzacion=true;
        consumoRiego=true;
        textFechaSiembra = findViewById(R.id.textFechaSiembra);
        textUltimaPoda = findViewById(R.id.textUltimaPoda);
        spinnerTipoArbol = findViewById(R.id.spinnerTipoArbol);
        spinnerTipoPoda = findViewById(R.id.spinnerTipoPoda);
        spinnerTipoFumigacion = findViewById(R.id.spinnerTipoFumigacion);
        spinnerTipoFertilizacion = findViewById(R.id.spinnerTipoFertilizacion);
        spinnerTipoRiego = findViewById(R.id.spinnerTipoRiego);
        textUltimaFumigacion = findViewById(R.id.textUltimaFumigacion);
        textUltimaFertilizacion = findViewById(R.id.textUltimaFertilizacion);
        textUltimoRiego = findViewById(R.id.textUltimoRiego);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.TipoArbolFrutal, R.layout.spinner_item);
        spinnerTipoArbol.setAdapter(spinnerAdapter);
        ArrayAdapter<CharSequence> spinnerAdapterPoda = ArrayAdapter.createFromResource(this, R.array.TipoPoda, R.layout.spinner_item);
        spinnerTipoPoda.setAdapter(spinnerAdapterPoda);
        ArrayAdapter<CharSequence> spinnerAdapterFumigacion = ArrayAdapter.createFromResource(this, R.array.TipoFumigacion, R.layout.spinner_item);
        spinnerTipoFumigacion.setAdapter(spinnerAdapterFumigacion);
        ArrayAdapter<CharSequence> spinnerAdapterFertilizacion = ArrayAdapter.createFromResource(this, R.array.TipoFertilizacion, R.layout.spinner_item);
        spinnerTipoFertilizacion.setAdapter(spinnerAdapterFertilizacion);
        ArrayAdapter<CharSequence> spinnerAdapterRiego = ArrayAdapter.createFromResource(this, R.array.TipoRiego, R.layout.spinner_item);
        spinnerTipoRiego.setAdapter(spinnerAdapterRiego);
        buttonUbicacion = findViewById(R.id.buttonDefinirUbicacion);
        buttonNuevoArbol = findViewById(R.id.buttonNuevoArbol);
        spinnerTipoArbol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipo = spinnerTipoArbol.getSelectedItem().toString();
                Log.i("c_arbol", "opcion seleccionada " + tipo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTipoPoda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoPoda = spinnerTipoPoda.getSelectedItem().toString();
                Log.i("c_arbol", "opcion seleccionada " + tipoPoda);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTipoFumigacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoFumigacion = spinnerTipoFumigacion.getSelectedItem().toString();
                Log.i("c_arbol", "opcion seleccionada " + tipoFumigacion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTipoFertilizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoFertilizacion = spinnerTipoFertilizacion.getSelectedItem().toString();
                Log.i("c_arbol", "opcion seleccionada " + tipoFertilizacion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTipoRiego.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoRiego = spinnerTipoRiego.getSelectedItem().toString();
                Log.i("c_arbol", "opcion seleccionada " + tipoRiego);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(getApplicationContext(), MapaNuevoArbolActivity.class), 100);

            }
        });
        buttonNuevoArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                consumoPoda=true;
                consumoFumigacion=true;
                consumoFertilzacion=true;
                consumoRiego=true;
                if (validateForm()) {
                    RequestQueue queue = Volley.newRequestQueue(CrearArbolActivity.this);
                    //SE TOMA EL TIPO DE ARBOL Y SE AVERIGUA LA INICIAL
                    int selectedItemOfMySpinner = spinnerTipoArbol.getSelectedItemPosition();
                    String actualPositionOfMySpinner = (String) spinnerTipoArbol.getItemAtPosition(selectedItemOfMySpinner);
                    String inicial = inicialTipo(actualPositionOfMySpinner);
                    //SE TOMA LA FECHA DE SIEMBRA Y SE CAMBIA EL FORMATO
                    String divide = textFechaSiembra.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[3];
                    String data[] = aux.split("/");
                    String auxFecha = data[2] + "-" + data[1] + "-" + data[0];
                    //SE TOMAN LAS COORDENADAS X Y PARA LA POSICION
                    Intent intent = getIntent();
                    String auxUbicacion = "POINT (" + lat + " " + lon + ")";
                    //SE CREA EL BODY CON LOS DATOS ANTERIORES
                    String body = "{\"specie\":\"" + inicial + "\",\"seed_date\":\"" + auxFecha + "\",\"location\":\"" + auxUbicacion + "\",\"farm\":\"" + token.getGranjaActual() + "\"}";
                    Log.i("newTreeAPI", "Nuevo arbol: " + body);
                    JSONObject newTree = null;
                    try {
                        newTree = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest newTreeRequest = new JsonObjectRequest(Request.Method.POST,
                            "http://10.0.2.2:8000/app/trees/"/*TODO: cambiar a URL real para producción!!!!*/, newTree,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("newTreeAPI", response.toString());

                                    if (response.has("error")) {
                                        try {
                                            Toast.makeText(CrearArbolActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            auxIdArbol=Integer.getInteger(response.getString("id"));
                                            System.out.println("XXXXXXXXXX EL ID ES "+response.getString("id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(CrearArbolActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
                    queue.add(newTreeRequest);
                    //CONSUMO DE LAS ACTIVIDADES
                    //ARREGLO ARBOL
                    JSONArray care_type = new JSONArray();
                    care_type.put(auxIdArbol);
                    System.out.println("XXXXXXXXXXXXXXXXXXXX AUX ID ARBOL ES "+auxIdArbol.toString());
                    if(consumoPoda){
                        String url = "http://10.0.2.2:8000/app/";
                        String auxTipo=traductorPodas(spinnerTipoPoda.getSelectedItem().toString());
                        url = url + "prunings/";
                        //EXTRACCION FECHA
                        String divide2 = textUltimaPoda.getText().toString();
                        String separated2[] = divide2.split(" ");
                        String aux2 = separated2[4];
                        String data2[] = aux2.split("/");
                        String auxFecha2 = data2[2] + "-" + data2[1] + "-" + data2[0];
                        //ACABA EXTRACCION DE DATOS ACTIVIDAD Y SE ARMA EL BODY Y LA PETICION CON LOS MISMOS
                        String body2 = "{\"start_date\":\"" + auxFecha2 + "\",\"end_date\":\"" + auxFecha2 + "\",\"farm\":\"" + token.getGranjaActual() + "\",\"trees\":" +care_type.toString()+ ",\"type\":\"" + auxTipo + "\",\"work_cost\":\"" + "0" + "\",\"tools_cost\":\"" + "0" + "\"}";
                        System.out.println("XXXXXXXXXXXXXXXXXX BODY PODA ES "+body2);
                        JSONObject newLastActivity = null;
                        try {
                            newLastActivity = new JSONObject(body2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest lastActivityRequest = new JsonObjectRequest(Request.Method.POST,
                                url/*TODO: cambiar a URL real para producción!!!!*/, newLastActivity,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("newActivityAPI", response.toString());

                                        if (response.has("error")) {
                                            try {
                                                Toast.makeText(CrearArbolActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(CrearArbolActivity.this, "Actividad creada", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                                Toast.makeText(CrearArbolActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
                        queue.add(lastActivityRequest);
                    }
                    if(consumoFertilzacion){
                        String url = "http://10.0.2.2:8000/app/";
                        String auxTipo=traductorFertilizaciones(spinnerTipoFertilizacion.getSelectedItem().toString());
                        url = url + "fertilizations/";
                        //EXTRACCION FECHA
                        String divide2 = textUltimaFertilizacion.getText().toString();
                        String separated2[] = divide2.split(" ");
                        String aux2 = separated2[4];
                        String data2[] = aux2.split("/");
                        String auxFecha2 = data2[2] + "-" + data2[1] + "-" + data2[0];
                        //ACABA EXTRACCION DE DATOS ACTIVIDAD Y SE ARMA EL BODY Y LA PETICION CON LOS MISMOS
                        String body2 = "{\"start_date\":\"" + auxFecha2 + "\",\"end_date\":\"" + auxFecha2 + "\",\"farm\":\"" + token.getGranjaActual() + "\",\"trees\":" +care_type.toString()+ ",\"type\":\"" + auxTipo + "\",\"work_cost\":\"" + "0" + "\",\"tools_cost\":\"" + "0" + "\"}";
                        System.out.println("XXXXXXXXXXXXXXXXXX BODY FERTILIZACION ES "+body2);
                        JSONObject newLastActivity = null;
                        try {
                            newLastActivity = new JSONObject(body2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest lastActivityRequest = new JsonObjectRequest(Request.Method.POST,
                                url/*TODO: cambiar a URL real para producción!!!!*/, newLastActivity,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("newActivityAPI", response.toString());

                                        if (response.has("error")) {
                                            try {
                                                Toast.makeText(CrearArbolActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(CrearArbolActivity.this, "Actividad creada", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                                Toast.makeText(CrearArbolActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
                        queue.add(lastActivityRequest);
                    }
                    if(consumoFumigacion){
                        String url = "http://10.0.2.2:8000/app/";
                        String auxTipo=traductorFumigaciones(spinnerTipoFumigacion.getSelectedItem().toString());
                        url = url + "fumigations/";
                        //EXTRACCION FECHA
                        String divide2 = textUltimaFumigacion.getText().toString();
                        String separated2[] = divide2.split(" ");
                        String aux2 = separated2[4];
                        String data2[] = aux2.split("/");
                        String auxFecha2 = data2[2] + "-" + data2[1] + "-" + data2[0];
                        //ACABA EXTRACCION DE DATOS ACTIVIDAD Y SE ARMA EL BODY Y LA PETICION CON LOS MISMOS
                        String body2 = "{\"start_date\":\"" + auxFecha2 + "\",\"end_date\":\"" + auxFecha2 + "\",\"farm\":\"" + token.getGranjaActual() + "\",\"trees\":" +care_type.toString()+ ",\"type\":\"" + auxTipo + "\",\"work_cost\":\"" + "0" + "\",\"tools_cost\":\"" + "0" + "\"}";
                        System.out.println("XXXXXXXXXXXXXXXXXX BODY FUMIGACION ES "+body2);
                        JSONObject newLastActivity = null;
                        try {
                            newLastActivity = new JSONObject(body2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest lastActivityRequest = new JsonObjectRequest(Request.Method.POST,
                                url/*TODO: cambiar a URL real para producción!!!!*/, newLastActivity,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("newActivityAPI", response.toString());

                                        if (response.has("error")) {
                                            try {
                                                Toast.makeText(CrearArbolActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(CrearArbolActivity.this, "Actividad creada", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                                Toast.makeText(CrearArbolActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
                        queue.add(lastActivityRequest);
                    }
                    if(consumoRiego){
                        String url = "http://10.0.2.2:8000/app/";
                        String auxTipo=traductorRiegos(spinnerTipoRiego.getSelectedItem().toString());
                        url = url + "waterings/";
                        //EXTRACCION FECHA
                        String divide2 = textUltimoRiego.getText().toString();
                        String separated2[] = divide2.split(" ");
                        String aux2 = separated2[4];
                        String data2[] = aux2.split("/");
                        String auxFecha2 = data2[2] + "-" + data2[1] + "-" + data2[0];
                        //ACABA EXTRACCION DE DATOS ACTIVIDAD Y SE ARMA EL BODY Y LA PETICION CON LOS MISMOS
                        String body2 = "{\"start_date\":\"" + auxFecha2 + "\",\"end_date\":\"" + auxFecha2 + "\",\"farm\":\"" + token.getGranjaActual() + "\",\"trees\":" +care_type.toString()+ ",\"type\":\"" + auxTipo + "\",\"work_cost\":\"" + "0" + "\",\"tools_cost\":\"" + "0" + "\"}";
                        System.out.println("XXXXXXXXXXXXXXXXXX BODY RIEGO ES "+body2);
                        JSONObject newLastActivity = null;
                        try {
                            newLastActivity = new JSONObject(body2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest lastActivityRequest = new JsonObjectRequest(Request.Method.POST,
                                url/*TODO: cambiar a URL real para producción!!!!*/, newLastActivity,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("newActivityAPI", response.toString());

                                        if (response.has("error")) {
                                            try {
                                                Toast.makeText(CrearArbolActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(CrearArbolActivity.this, "Actividad creada", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                                Toast.makeText(CrearArbolActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
                        queue.add(lastActivityRequest);
                    }
                }
            }
        });
        textFechaSiembra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                dpd = new DatePickerDialog(CrearArbolActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textFechaSiembra.setText("Fecha de siembra: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd.show();
            }
        });
        textUltimaPoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal1 = Calendar.getInstance();
                int day = cal1.get(Calendar.DAY_OF_MONTH);
                int month = cal1.get(Calendar.MONTH);
                int year = cal1.get(Calendar.YEAR);
                dpd2 = new DatePickerDialog(CrearArbolActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textUltimaPoda.setText("Fecha de última poda: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd2.show();
            }
        });
        textUltimaFertilizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal2 = Calendar.getInstance();
                int day = cal2.get(Calendar.DAY_OF_MONTH);
                int month = cal2.get(Calendar.MONTH);
                int year = cal2.get(Calendar.YEAR);
                dpd3 = new DatePickerDialog(CrearArbolActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textUltimaFertilizacion.setText("Fecha de última fertilización: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd3.show();
            }
        });
        textUltimaFumigacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal3 = Calendar.getInstance();
                int day = cal3.get(Calendar.DAY_OF_MONTH);
                int month = cal3.get(Calendar.MONTH);
                int year = cal3.get(Calendar.YEAR);
                dpd4 = new DatePickerDialog(CrearArbolActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textUltimaFumigacion.setText("Fecha de última fumigación: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd4.show();
            }
        });
        textUltimoRiego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal4 = Calendar.getInstance();
                int day = cal4.get(Calendar.DAY_OF_MONTH);
                int month = cal4.get(Calendar.MONTH);
                int year = cal4.get(Calendar.YEAR);
                dpd5 = new DatePickerDialog(CrearArbolActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textUltimoRiego.setText("Fecha de último riego: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd5.show();
            }
        });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Su GPS está desactivado, para continuar debe encenderlo.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean validateForm() {
        boolean valid = true;
        if (!token.getArbolEscogido()) {
            valid = false;
        }
        int selectedItemOfMySpinner = spinnerTipoArbol.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipoArbol.getItemAtPosition(selectedItemOfMySpinner);
        if (actualPositionOfMySpinner.equals("Seleccione el tipo de árbol...")) {
            setSpinnerError(spinnerTipoArbol);
            valid = false;
        }
//VALIDACION FECHA Y TIPO PODA
        int selectedItemOfMySpinnerPoda = spinnerTipoPoda.getSelectedItemPosition();
        String actualPositionOfMySpinnerPoda = (String) spinnerTipoPoda.getItemAtPosition(selectedItemOfMySpinnerPoda);
        if(actualPositionOfMySpinnerPoda.equals("Seleccione el tipo de poda...") || TextUtils.isEmpty(textUltimaPoda.getText().toString())){
            consumoPoda=false;
        }
        if(!actualPositionOfMySpinnerPoda.equals("Seleccione el tipo de poda...") || !TextUtils.isEmpty(textUltimaPoda.getText().toString())){
            if (!actualPositionOfMySpinnerPoda.equals("Seleccione el tipo de poda...")) {
                if (!TextUtils.isEmpty(textUltimaPoda.getText().toString())) {
                    String divide = textUltimaPoda.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[4];
                    String data[] = aux.split("/");
                    Calendar cal = Calendar.getInstance();
                    cal.getTime();
                    Calendar cal2 = Calendar.getInstance();
                    System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
                    cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
                    if (cal.compareTo(cal2) > 0) {
                        textUltimaPoda.setError(null);
                    } else {
                        textUltimaPoda.setError("La fecha debe ser la actual o anterior a la actual");
                        valid = false;
                        consumoPoda=false;
                    }
                } else {
                    textUltimaPoda.setError("Debe seleccionar una fecha");
                    consumoPoda=false;
                }
            }else{
                String divide = textUltimaPoda.getText().toString();
                String separated[] = divide.split(" ");
                String aux = separated[4];
                String data[] = aux.split("/");
                Calendar cal = Calendar.getInstance();
                cal.getTime();
                Calendar cal2 = Calendar.getInstance();
                System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
                cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
                if (cal.compareTo(cal2) > 0) {
                    textUltimaPoda.setError(null);
                } else {
                    textUltimaPoda.setError("La fecha debe ser la actual o anterior a la actual");
                    consumoPoda=false;
                    valid = false;
                }
                if (actualPositionOfMySpinnerPoda.equals("Seleccione el tipo de poda...")) {
                    consumoPoda=false;
                    setSpinnerError(spinnerTipoPoda);
                    valid = false;
                }
            }
        }
//VALIDACION FECHA Y TIPO FUMIGACION
        int selectedItemOfMySpinnerFumigacion = spinnerTipoFumigacion.getSelectedItemPosition();
        String actualPositionOfMySpinnerFumigacion = (String) spinnerTipoFumigacion.getItemAtPosition(selectedItemOfMySpinnerFumigacion);
        if(actualPositionOfMySpinnerFumigacion.equals("Seleccione el tipo de fumigación...") || TextUtils.isEmpty(textUltimaFumigacion.getText().toString())){
            consumoFumigacion=false;
        }
        if(!actualPositionOfMySpinnerFumigacion.equals("Seleccione el tipo de fumigación...") || !TextUtils.isEmpty(textUltimaFumigacion.getText().toString())){
            if (!actualPositionOfMySpinnerFumigacion.equals("Seleccione el tipo de fumigación...")) {
                if (!TextUtils.isEmpty(textUltimaFumigacion.getText().toString())) {
                    String divide = textUltimaFumigacion.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[4];
                    String data[] = aux.split("/");
                    Calendar cal = Calendar.getInstance();
                    cal.getTime();
                    Calendar cal2 = Calendar.getInstance();
                    System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
                    cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
                    if (cal.compareTo(cal2) > 0) {
                        textUltimaFumigacion.setError(null);
                    } else {
                        textUltimaFumigacion.setError("La fecha debe ser la actual o anterior a la actual");
                        valid = false;
                        consumoFumigacion=false;
                    }
                } else {
                    textUltimaFumigacion.setError("Debe seleccionar una fecha");
                    consumoFumigacion=false;
                }
            }else{
                String divide = textUltimaFumigacion.getText().toString();
                String separated[] = divide.split(" ");
                String aux = separated[4];
                String data[] = aux.split("/");
                Calendar cal = Calendar.getInstance();
                cal.getTime();
                Calendar cal2 = Calendar.getInstance();
                System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
                cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
                if (cal.compareTo(cal2) > 0) {
                    textUltimaFumigacion.setError(null);
                } else {
                    textUltimaFumigacion.setError("La fecha debe ser la actual o anterior a la actual");
                    consumoFumigacion=false;
                    valid = false;
                }
                if (actualPositionOfMySpinnerFumigacion.equals("Seleccione el tipo de fumigación...")) {
                    consumoFumigacion=false;
                    setSpinnerError(spinnerTipoFumigacion);
                    valid = false;
                }
            }
        }
//VALIDACION FECHA Y TIPO FERTILIZACION
        int selectedItemOfMySpinnerFertilizacion = spinnerTipoFertilizacion.getSelectedItemPosition();
        String actualPositionOfMySpinnerFertilizacion = (String) spinnerTipoFertilizacion.getItemAtPosition(selectedItemOfMySpinnerFertilizacion);
        if(actualPositionOfMySpinnerFertilizacion.equals("Seleccione el tipo de fertilización...") || TextUtils.isEmpty(textUltimaFertilizacion.getText().toString())){
            consumoFertilzacion=false;
        }
        if(!actualPositionOfMySpinnerFertilizacion.equals("Seleccione el tipo de fertilización...") || !TextUtils.isEmpty(textUltimaFertilizacion.getText().toString())){
            if (!actualPositionOfMySpinnerFertilizacion.equals("Seleccione el tipo de fertilización...")) {
                if (!TextUtils.isEmpty(textUltimaFertilizacion.getText().toString())) {
                    String divide = textUltimaFertilizacion.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[4];
                    String data[] = aux.split("/");
                    Calendar cal = Calendar.getInstance();
                    cal.getTime();
                    Calendar cal2 = Calendar.getInstance();
                    System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
                    cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
                    if (cal.compareTo(cal2) > 0) {
                        textUltimaFertilizacion.setError(null);
                    } else {
                        textUltimaFertilizacion.setError("La fecha debe ser la actual o anterior a la actual");
                        valid = false;
                        consumoFertilzacion=false;
                    }
                } else {
                    textUltimaFertilizacion.setError("Debe seleccionar una fecha");
                    consumoFertilzacion=false;
                }
            }else{
                String divide = textUltimaFertilizacion.getText().toString();
                String separated[] = divide.split(" ");
                String aux = separated[4];
                String data[] = aux.split("/");
                Calendar cal = Calendar.getInstance();
                cal.getTime();
                Calendar cal2 = Calendar.getInstance();
                System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
                cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
                if (cal.compareTo(cal2) > 0) {
                    textUltimaFertilizacion.setError(null);
                } else {
                    textUltimaFertilizacion.setError("La fecha debe ser la actual o anterior a la actual");
                    consumoFertilzacion=false;
                    valid = false;
                }
                if (actualPositionOfMySpinnerFertilizacion.equals("Seleccione el tipo de fertilización...")) {
                    consumoFertilzacion=false;
                    setSpinnerError(spinnerTipoFertilizacion);
                    valid = false;
                }
            }
        }
//VALIDACION FECHA Y TIPO RIEGO
        int selectedItemOfMySpinnerRiego = spinnerTipoRiego.getSelectedItemPosition();
        String actualPositionOfMySpinnerRiego = (String) spinnerTipoRiego.getItemAtPosition(selectedItemOfMySpinnerRiego);
        if(actualPositionOfMySpinnerRiego.equals("Seleccione el tipo de riego...") || TextUtils.isEmpty(textUltimoRiego.getText().toString())){
            consumoRiego=false;
        }
        if(!actualPositionOfMySpinnerRiego.equals("Seleccione el tipo de riego...") || !TextUtils.isEmpty(textUltimoRiego.getText().toString())){
            if (!actualPositionOfMySpinnerRiego.equals("Seleccione el tipo de riego...")) {
                if (!TextUtils.isEmpty(textUltimoRiego.getText().toString())) {
                    String divide = textUltimoRiego.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[4];
                    String data[] = aux.split("/");
                    Calendar cal = Calendar.getInstance();
                    cal.getTime();
                    Calendar cal2 = Calendar.getInstance();
                    System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
                    cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
                    if (cal.compareTo(cal2) > 0) {
                        textUltimoRiego.setError(null);
                    } else {
                        textUltimoRiego.setError("La fecha debe ser la actual o anterior a la actual");
                        valid = false;
                        consumoRiego=false;
                    }
                } else {
                    textUltimoRiego.setError("Debe seleccionar una fecha");
                    consumoRiego=false;
                }
            }else{
                String divide = textUltimoRiego.getText().toString();
                String separated[] = divide.split(" ");
                String aux = separated[4];
                String data[] = aux.split("/");
                Calendar cal = Calendar.getInstance();
                cal.getTime();
                Calendar cal2 = Calendar.getInstance();
                System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
                cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
                if (cal.compareTo(cal2) > 0) {
                    textUltimoRiego.setError(null);
                } else {
                    textUltimoRiego.setError("La fecha debe ser la actual o anterior a la actual");
                    consumoRiego=false;
                    valid = false;
                }
                if (actualPositionOfMySpinnerRiego.equals("Seleccione el tipo de riego...")) {
                    consumoRiego=false;
                    setSpinnerError(spinnerTipoRiego);
                    valid = false;
                }
            }
        }
//VALIDACION FECHA DE SIEMBRA
        if (TextUtils.isEmpty(textFechaSiembra.getText().toString())) {
            textFechaSiembra.setError("Requerido");
            valid = false;
        } else {
            String divide = textFechaSiembra.getText().toString();
            String separated[] = divide.split(" ");
            String aux = separated[3];
            String data[] = aux.split("/");
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
            if (cal.compareTo(cal2) > 0) {
                textFechaSiembra.setError(null);
            } else {
                textFechaSiembra.setError("La fecha debe ser la actual o anterior a la actual");
                valid = false;
            }
        }
//VALIDACIONES DE FECHA DE ACTIVIDAD
        /*if (!TextUtils.isEmpty(textUltimaFertilizacion.getText().toString())) {
            String divide = textUltimaFertilizacion.getText().toString();
            String separated[] = divide.split(" ");
            String aux = separated[4];
            String data[] = aux.split("/");
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
            if (cal.compareTo(cal2) > 0) {
                textUltimaFertilizacion.setError(null);
            } else {
                textUltimaFertilizacion.setError("La fecha debe ser la actual o anterior a la actual");
                valid = false;
            }
        } else {
            textUltimaFertilizacion.setError(null);
        }
        if (!TextUtils.isEmpty(textUltimoRiego.getText().toString())) {
            String divide = textUltimoRiego.getText().toString();
            String separated[] = divide.split(" ");
            String aux = separated[4];
            String data[] = aux.split("/");
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            System.out.println("La seleccionada es " + data[2] + data[1] + data[0]);
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
            if (cal.compareTo(cal2) > 0) {
                textUltimoRiego.setError(null);
            } else {
                textUltimoRiego.setError("La fecha debe ser la actual o anterior a la actual");
                valid = false;
            }
        } else {
            textUltimoRiego.setError(null);
        }
*/

        return valid;
    }

    private void setSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("Requerido"); // any name of the error will do


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            lat = data.getStringExtra("latFinal");
            lon = data.getStringExtra("longFinal");
        }
    }


    private String inicialTipo(String opcion) {
        if (opcion.equals("Mango tommy")) {
            return "M";
        } else if (opcion.equals("Mango farchil")) {
            return "F";
        } else if (opcion.equals("Naranja")) {
            return "N";
        } else if (opcion.equals("Mandarina")) {
            return "D";
        } else if (opcion.equals("Limon")) {
            return "L";
        } else if (opcion.equals("Aguacate")) {
            return "A";
        } else {
            return "B";
        }
    }


/*    private JsonObjectRequest[] actividadesPrevias(String tipoActividad){
        EditText[] arrayTextViews=new EditText[]{textUltimaPoda,textUltimaFumigacion,textUltimaFertilizacion,textUltimoRiego};
        Spinner[] arraySpinners=new Spinner[]{textUltimaPoda,textUltimaFumigacion,textUltimaFertilizacion,textUltimoRiego};
        for(int i =0;i<4;i++){
            Spinner actualSpinner=arraySpinners[i];
            EditText actualTextView=arrayTextViews[i];

            int selectedItemOfMySpinnerFumigacion = actualSpinner.getSelectedItemPosition();
            String actualPositionOfMySpinnerFumigacion = (String) actualSpinner.getItemAtPosition(selectedItemOfMySpinnerFumigacion);

            if (actualPositionOfMySpinnerFumigacion.equals("Seleccione el tipo de fumigación...")) {
                setSpinnerError(actualSpinner);
                valid = false;
            }
            if(!TextUtils.isEmpty(actualTextView.getText().toString()) || )
        }
        String body = "{\"specie\":\"" + inicial + "\",\"seed_date\":\"" +auxFecha+ "\",\"location\":\""+auxUbicacion+"\",\"farm\":\""+token.getGranjaActual()+"\"}";
        JSONObject newLastActivity = null;
        try {
            newLastActivity = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest lastActivityRequest = new JsonObjectRequest(Request.Method.POST,
                "http://10.0.2.2:8000/app/trees/"*//*TODO: cambiar a URL real para producción!!!!*//*, newLastActivity,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("newTreeAPI", response.toString());

                        if (response.has("error")) {
                            try {
                                Toast.makeText(CrearArbolActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Intent intent = new Intent(v.getContext(), AccionesActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(CrearArbolActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }){    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token "+token.getToken());
                System.out.println("XXXXXXXXX EL TOKEN ES "+token.getToken());
                return params;
            }
        };
        return lastActivityRequest
    }*/

    private String traductorRiegos(String tipo) {
        if (tipo.equals("Sistema")) {
            return "S";
        }
        if (tipo.equals("Manual")) {
            return "M";
        }
        else {
            return "N";
        }
    }


    private String traductorFertilizaciones(String tipo) {
        if (tipo.equals("Crecimiento")) {
            return "C";
        }
        if (tipo.equals("Produccion")) {
            return "P";
        } else {
            return "M";
        }
    }


    private String traductorFumigaciones(String tipo) {
        if (tipo.equals("Insectos")) {
            return "I";
        }
        if (tipo.equals("Hongos")) {
            return "F";
        }
        if (tipo.equals("Hierba")) {
            return "H";
        }
        if (tipo.equals("Ácaro")) {
            return "A";
        } else {
            return "P";
        }
    }


    private String traductorPodas(String tipo) {

        if (tipo.equals("Sanitaria")) {
            return "S";
        }
        if (tipo.equals("Formación")) {
            return "F";
        }
        if (tipo.equals("Mantenimiento")) {
            return "M";
        } else {
            return "L";
        }
    }
}
