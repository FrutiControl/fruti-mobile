package com.fruticontrol;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NuevaActividadActivity extends AppCompatActivity {

    private Calendar calInicio;
    private Calendar calFin;
    private EditText txtFechaInicio;
    private EditText txtFechaFin;
    private DatePickerDialog datePInicio;
    private DatePickerDialog datePFin;
    private Spinner spinnerTipo;
    private Spinner spinnerSubtipo;
    private Button guardarNuevoProcesoButton;
    private Button seleccionarArbolesButton;
    private Token token;
    private ArrayList<Integer> listaArbolesSeleccionados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_actividad);
        token = (Token) getApplicationContext();
        txtFechaInicio = findViewById(R.id.fechaInicio);
        txtFechaFin = findViewById(R.id.fechaFin);
        spinnerTipo = findViewById(R.id.spinnerTipoProceso);
        spinnerSubtipo = findViewById(R.id.spinnerSubtipo);
        seleccionarArbolesButton = findViewById(R.id.buttonSeleccionarArboles);
        guardarNuevoProcesoButton = findViewById(R.id.buttonGuardarNuevoProceso);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.TipoActividad, R.layout.spinner_item);
        spinnerTipo.setAdapter(spinnerAdapter);

        seleccionarArbolesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListaArbolesSeleccionActivity.class);
                startActivityForResult(intent,100);
            }
        });
        guardarNuevoProcesoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    RequestQueue queue = Volley.newRequestQueue(NuevaActividadActivity.this);
                    String tipoActividad=spinnerTipo.getSelectedItem().toString();
                    String url = "http://10.0.2.2:8000/app/";
                    String sutTipo;
                    if (tipoActividad.equals("Poda")) {
                        url = url + "prunings/";
                        sutTipo = traductorPodas(spinnerSubtipo.getSelectedItem().toString());
                    }
                    else if (tipoActividad.equals("Fumigación")) {
                        url = url + "fumigations/";
                        sutTipo = traductorFumigaciones(spinnerSubtipo.getSelectedItem().toString());
                    }
                    else if (tipoActividad.equals("Fertilización")) {
                        url = url + "fertilizations/";
                        sutTipo = traductorFertilizaciones(spinnerSubtipo.getSelectedItem().toString());
                    } else {
                        url = url + "waterings/";
                        sutTipo = traductorRiegos(spinnerSubtipo.getSelectedItem().toString());
                    }
                    String divide = txtFechaInicio.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[3];
                    String data[] = aux.split("/");
                    String auxFecha = data[2] + "-" + data[1] + "-" + data[0];

                    String divide2 = txtFechaFin.getText().toString();
                    String separated2[] = divide2.split(" ");
                    String aux2 = separated2[3];
                    String data2[] = aux2.split("/");
                    String auxFecha2 = data2[2] + "-" + data2[1] + "-" + data2[0];

                    String arboles="\"[\""+17+"\",\""+18+"\"]\"";

                    ArrayList<Integer> lista = listaArbolesSeleccionados;
                    JSONArray care_type = new JSONArray();
                    for(int i=0; i < lista.size(); i++) {
                        care_type.put(lista.get(i));   // create array and add items into that
                    }


                    String body = "{\"start_date\":\"" + auxFecha + "\",\"end_date\":\"" + auxFecha2 + "\",\"farm\":\"" + token.getGranjaActual() + "\",\"trees\":" +care_type.toString()+ ",\"type\":\"" + sutTipo + "\",\"work_cost\":\"" + "0" + "\",\"tools_cost\":\"" + "0" + "\"}";

                    System.out.println("XXXXXXXXXXXXXXXXXX BODY ES "+body);
                    JSONObject newLastActivity = null;
                    try {
                        newLastActivity = new JSONObject(body);
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
                                            Toast.makeText(NuevaActividadActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(NuevaActividadActivity.this, "Actividad creada", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(NuevaActividadActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
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
        });
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipo = spinnerTipo.getSelectedItem().toString();

                if (tipo.equals("Seleccione el tipo de proceso...")) {
                    setSpinnerError(spinnerTipo);
                }
                if (tipo.equals("Poda")) {
                    ArrayAdapter<CharSequence> spinnerAdapterPoda = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoPoda, R.layout.spinner_item);
                    spinnerSubtipo.setAdapter(spinnerAdapterPoda);
                }
                if (tipo.equals("Fumigación")) {
                    ArrayAdapter<CharSequence> spinnerAdapterFumigacion = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoFumigacion, R.layout.spinner_item);
                    spinnerSubtipo.setAdapter(spinnerAdapterFumigacion);
                }
                if (tipo.equals("Fertilización")) {
                    ArrayAdapter<CharSequence> spinnerAdapterFertilizacion = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoFertilizacion, R.layout.spinner_item);
                    spinnerSubtipo.setAdapter(spinnerAdapterFertilizacion);
                }
                if (tipo.equals("Riego")) {
                    ArrayAdapter<CharSequence> spinnerAdapterRiego = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoRiego, R.layout.spinner_item);
                    spinnerSubtipo.setAdapter(spinnerAdapterRiego);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calInicio = Calendar.getInstance();
                int day = calInicio.get(Calendar.DAY_OF_MONTH);
                int month = calInicio.get(Calendar.MONTH);
                int year = calInicio.get(Calendar.YEAR);
                datePInicio = new DatePickerDialog(NuevaActividadActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        txtFechaInicio.setText("Fecha de inicio: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                datePInicio.show();
            }
        });

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calFin = Calendar.getInstance();
                int day = calFin.get(Calendar.DAY_OF_MONTH);
                int month = calFin.get(Calendar.MONTH);
                int year = calFin.get(Calendar.YEAR);
                datePFin = new DatePickerDialog(NuevaActividadActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        txtFechaFin.setText("Fecha de fin: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                datePFin.show();
            }
        });
    }

    private void setSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("Requerido");
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        if(listaArbolesSeleccionados==null){
            valid=false;
        }
        int selectedItemOfMySpinner = spinnerTipo.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipo.getItemAtPosition(selectedItemOfMySpinner);
        if (actualPositionOfMySpinner.equals("Seleccione el tipo de actividad...")) {
            setSpinnerError(spinnerTipo);
            valid = false;
        } else {
            int selectedItemOfMySubSpinner = spinnerSubtipo.getSelectedItemPosition();
            String actualPositionOfMySpinnerSub = (String) spinnerSubtipo.getItemAtPosition(selectedItemOfMySubSpinner);
            if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de poda...")) {
                setSpinnerError(spinnerSubtipo);
                valid = false;
            }
            if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de fumigación...")) {
                setSpinnerError(spinnerSubtipo);
                valid = false;
            }
            if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de fertilización...")) {
                setSpinnerError(spinnerSubtipo);
                valid = false;
            }
            if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de riego...")) {
                setSpinnerError(spinnerSubtipo);
                valid = false;
            }
        }
        if (TextUtils.isEmpty(txtFechaInicio.getText().toString())) {
            txtFechaInicio.setError("Requerido");
            valid = false;
        } else {
            String divide = txtFechaInicio.getText().toString();
            String[] separated = divide.split(" ");
            String aux = separated[3];
            String[] data = aux.split("/");
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 23, 59);
            if (cal.compareTo(cal2) > 0) {
                txtFechaInicio.setError("La fecha debe ser la actual o posterior a la actual");
                valid = false;
            } else {
                txtFechaInicio.setError(null);
            }
        }
        if (TextUtils.isEmpty(txtFechaFin.getText().toString())) {
            txtFechaFin.setError("Requerido");
            valid = false;
        } else {
            String divide = txtFechaInicio.getText().toString();
            String separated[] = divide.split(" ");
            String aux = separated[3];
            String data[] = aux.split("/");

            String divide2 = txtFechaFin.getText().toString();
            String separated2[] = divide2.split(" ");
            String aux2 = separated2[3];
            String data2[] = aux2.split("/");

            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 23, 59);

            Calendar cal3 = Calendar.getInstance();
            cal3.set(Integer.parseInt(data2[2]), Integer.parseInt(data2[1]) - 1, Integer.parseInt(data2[0]), 23, 59);
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
            if (cal3.compareTo(cal2) < 0) {
                txtFechaFin.setError("La fecha debe ser igual o superior a la actual");
                valid = false;
            } else {
                txtFechaFin.setError(null);
            }
        }

        return valid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("XXXXXXXXXXXX ENTRA A ON ACTIVITY RESULT");
        if (requestCode == 100 && resultCode == RESULT_OK) {
            listaArbolesSeleccionados = data.getIntegerArrayListExtra("arbolesActividad");
            System.out.println("LOS IDS DE LOS ARBOLES SELECCIONADOS FUERON ");
            if(listaArbolesSeleccionados!=null){
                for(int i=0;i<listaArbolesSeleccionados.size();i++){
                    System.out.println("A "+listaArbolesSeleccionados.get(i).toString());
                }
            }
        }
    }


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
