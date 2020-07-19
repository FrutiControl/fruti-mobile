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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CrearArbolActivity extends AppCompatActivity {
    private EditText textFechaSiembra;
    Calendar cal, cal1, cal2, cal3, cal4;
    DatePickerDialog dpd, dpd2, dpd3, dpd4, dpd5;
    private Spinner spinnerTipoArbol;
    private Config config;
    private String lat;
    private String lon;
    private ArrayList<String> listaArboles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_arbol);
        statusCheck();
        config = (Config) getApplicationContext();
        config.setArbolEscogido(false);
        textFechaSiembra = findViewById(R.id.textFechaSiembra);
        spinnerTipoArbol = findViewById(R.id.spinnerTipoArbol);
        listaArboles = getIntent().getStringArrayListExtra("todosArboles");
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.TipoArbolFrutal, R.layout.spinner_item);
        spinnerTipoArbol.setAdapter(spinnerAdapter);
        Button buttonUbicacion = findViewById(R.id.buttonDefinirUbicacion);
        Button buttonNuevoArbol = findViewById(R.id.buttonNuevoArbol);
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
        buttonUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapaNuevoArbolActivity.class);
                intent.putExtra("todosArboles", listaArboles);
                intent.putExtra("lat", config.getPuntosPoligonoFinca().get(0));
                intent.putExtra("lon", config.getPuntosPoligonoFinca().get(1));
                startActivityForResult(intent, 100);
            }
        });
        buttonNuevoArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (validateForm()) {
                    RequestQueue queue = Volley.newRequestQueue(CrearArbolActivity.this);
                    //SE TOMA EL TIPO DE ARBOL Y SE AVERIGUA LA INICIAL
                    int selectedItemOfMySpinner = spinnerTipoArbol.getSelectedItemPosition();
                    String actualPositionOfMySpinner = (String) spinnerTipoArbol.getItemAtPosition(selectedItemOfMySpinner);
                    String inicial = inicialTipo(actualPositionOfMySpinner);
                    //SE TOMA LA FECHA DE SIEMBRA Y SE CAMBIA EL FORMATO
                    // TODO: set proper names for this variables
                    String divide = textFechaSiembra.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[3];
                    String data[] = aux.split("/");
                    String auxFecha = data[2] + "-" + data[1] + "-" + data[0];
                    //SE TOMAN LAS COORDENADAS X Y PARA LA POSICION
                    Intent intent = getIntent();
                    String auxUbicacion = "POINT (" + lat + " " + lon + ")";
                    //SE CREA EL BODY CON LOS DATOS ANTERIORES
                    String body = "{\"specie\":\"" + inicial +
                            "\",\"seed_date\":\"" + auxFecha +
                            "\",\"location\":\"" + auxUbicacion +
                            "\",\"farm\":\"" + config.getFincaActual() + "\"}";
                    Log.i("newTreeAPI", "Nuevo arbol: " + body);
                    JSONObject newTree = null;
                    try {
                        newTree = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest newTreeRequest = new JsonObjectRequest(Request.Method.POST, config.getDomain()+
                            "/app/trees/", newTree,
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
                                        Toast.makeText(CrearArbolActivity.this, "Árbol creado con éxito", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(CrearArbolActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Token " + config.getToken());
                            return params;
                        }
                    };
                    queue.add(newTreeRequest);
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
        if (!config.getArbolEscogido()) {
            valid = false;
            Toast.makeText(CrearArbolActivity.this, "Debe seleccionar la ubicación del árbol para poder continuar", Toast.LENGTH_LONG).show();
        }
        int selectedItemOfMySpinner = spinnerTipoArbol.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipoArbol.getItemAtPosition(selectedItemOfMySpinner);
        if (actualPositionOfMySpinner.equals("Seleccione el tipo de árbol...")) {
            setSpinnerError(spinnerTipoArbol);
            valid = false;
        }
        //VALIDACION FECHA DE SIEMBRA
        if (TextUtils.isEmpty(textFechaSiembra.getText().toString())) {
            textFechaSiembra.setError("Requerido");
            valid = false;
        } else {
            // TODO: set proper names for this variables
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
        return valid;
    }

    private void setSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView instanceof TextView) {
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
        switch (opcion) {
            case "Mango tommy":
                return "M";
            case "Mango farchil":
                return "F";
            case "Naranja":
                return "N";
            case "Mandarina":
                return "D";
            case "Limon":
                return "L";
            case "Aguacate":
                return "A";
            default:
                return "B";
        }
    }
}
