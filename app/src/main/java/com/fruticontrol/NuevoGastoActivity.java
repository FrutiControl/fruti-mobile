package com.fruticontrol;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NuevoGastoActivity extends AppCompatActivity {
    private Spinner spinnerTipo;
    private Spinner spinnerSubtipo;
    private TextView tvMateriales;
    private TextView tvManoDeObra;
    private Switch switchMaterialManoDeObra;
    private EditText txtFechaGasto;
    private Calendar calGasto;
    private DatePickerDialog datePGasto;
    private EditText txtNombreProducto;
    private EditText txtValor;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);
        token = (Token) getApplicationContext();

        Button guardarButton = findViewById(R.id.buttonGuardarGasto);
        txtNombreProducto = findViewById(R.id.editTextNombreProducto);
        txtValor = findViewById(R.id.editTextValor);
        txtFechaGasto = findViewById(R.id.editTextFechaGasto);
        switchMaterialManoDeObra = findViewById(R.id.switchMaterialesVsManoObra);
        tvMateriales = findViewById(R.id.textViewMateriales);
        tvManoDeObra = findViewById(R.id.textViewManoDeObra);
        spinnerTipo = findViewById(R.id.spinnerGastoTipoPoceso);
        spinnerSubtipo = findViewById(R.id.spinnerGastoSubtipoProceso);
        ArrayAdapter<CharSequence> spinnerAdapterTipo = ArrayAdapter.createFromResource(this, R.array.TipoActividad, R.layout.spinner_item);
        spinnerTipo.setAdapter(spinnerAdapterTipo);
        ArrayAdapter<CharSequence> spinnerAdapterSubtipo = ArrayAdapter.createFromResource(this, R.array.TipoActividad, R.layout.spinner_item);
        spinnerTipo.setAdapter(spinnerAdapterSubtipo);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    RequestQueue queue = Volley.newRequestQueue(NuevoGastoActivity.this);
                    // TODO: set proper names for this variables
                    //CONCEPTO DEL INGRESO
                    String concepto = txtNombreProducto.getText().toString();
                    //SE TOMA LA FECHA DE SIEMBRA Y SE CAMBIA EL FORMATO
                    String divide = txtFechaGasto.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[3];
                    String data[] = aux.split("/");
                    String auxFecha = data[2] + "-" + data[1] + "-" + data[0];
                    //VALOR
                    String valor = txtValor.getText().toString();
                    //SE AVERIGUA TIPO MATERIALES O MANO DE OBRA
                    String tipoMatOMano = traductorTipo();
                    //INICIAL DE TIPO DE ACTIVIDAD
                    int selectedItemOfMySpinner = spinnerTipo.getSelectedItemPosition();
                    String actualPositionOfMySpinner = (String) spinnerTipo.getItemAtPosition(selectedItemOfMySpinner);
                    String actividad = traductorTipoActividad(actualPositionOfMySpinner);
                    //INICIAL DE SUBTIPO DE ACTIVIDAD
                    int selectedItemOfMySpinner2 = spinnerSubtipo.getSelectedItemPosition();
                    String actualPositionOfMySpinner2 = (String) spinnerSubtipo.getItemAtPosition(selectedItemOfMySpinner2);
                    String tipoActividad = traductorSubTipoActividad(actualPositionOfMySpinner2);
                    String body = "{\"concept\":\"" + concepto + "\",\"date\":\"" + auxFecha + "\",\"value\":\"" + valor + "\",\"recommended\":\"" + false + "\",\"type\":\"" + tipoMatOMano + "\",\"activity\":\"" + actividad + "\",\"act_type\":\"" + tipoActividad + "\"}";
                    Log.i("newOutcomeAPI", "Nuevo gasto: " + body);
                    JSONObject newOutcome = null;
                    try {
                        newOutcome = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest newOutcomeRequest = new JsonObjectRequest(Request.Method.POST,
                            "https://app.fruticontrol.me/money/outcomes/", newOutcome,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("newOutcomeAPI", response.toString());

                                    if (response.has("error")) {
                                        try {
                                            Toast.makeText(NuevoGastoActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(NuevoGastoActivity.this, "Gasto creado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(NuevoGastoActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }) {    //this is the part, that adds the header to the request
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            //params.put("Content-Type", "application/json");
                            params.put("Authorization", "Token " + token.getToken());
                            return params;
                        }
                    };
                    queue.add(newOutcomeRequest);
                    Toast.makeText(NuevoGastoActivity.this, "Es valido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        switchMaterialManoDeObra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchMaterialManoDeObra.isChecked()) {
                    tvManoDeObra.setTypeface(null, Typeface.BOLD);
                    tvMateriales.setTypeface(null, Typeface.NORMAL);
                    RequestQueue queue = Volley.newRequestQueue(NuevoGastoActivity.this);
                    JsonObjectRequest dayCostRequest = new JsonObjectRequest(Request.Method.GET,
                            "https://app.fruticontrol.me/users/owner/", null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("dayCost", response.toString());
                                    try {
                                        txtValor.setText(response.getString("day_cost"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dayCostAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(NuevoGastoActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }) {    //this is the part, that adds the header to the request
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Token " + token.getToken());
                            return params;
                        }
                    };
                    queue.add(dayCostRequest);
                } else {
                    tvMateriales.setTypeface(null, Typeface.BOLD);
                    tvManoDeObra.setTypeface(null, Typeface.NORMAL);
                    txtValor.setText("");
                }
            }
        });
        txtFechaGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calGasto = Calendar.getInstance();
                int day = calGasto.get(Calendar.DAY_OF_MONTH);
                int month = calGasto.get(Calendar.MONTH);
                int year = calGasto.get(Calendar.YEAR);
                datePGasto = new DatePickerDialog(NuevoGastoActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        txtFechaGasto.setText("Fecha del gasto: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                datePGasto.show();
            }
        });
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipo = spinnerTipo.getSelectedItem().toString();
                switch (tipo) {
                    case "Seleccione el tipo de proceso...":
                        setSpinnerError(spinnerTipo);
                        break;
                    case "Poda":
                        ArrayAdapter<CharSequence> spinnerAdapterPoda = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoPoda, R.layout.spinner_item);
                        spinnerSubtipo.setAdapter(spinnerAdapterPoda);
                        break;
                    case "Fumigación":
                        ArrayAdapter<CharSequence> spinnerAdapterFumigacion = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoFumigacion, R.layout.spinner_item);
                        spinnerSubtipo.setAdapter(spinnerAdapterFumigacion);
                        break;
                    case "Fertilización":
                        ArrayAdapter<CharSequence> spinnerAdapterFertilizacion = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoFertilizacion, R.layout.spinner_item);
                        spinnerSubtipo.setAdapter(spinnerAdapterFertilizacion);
                        break;
                    case "Riego":
                        ArrayAdapter<CharSequence> spinnerAdapterRiego = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoRiego, R.layout.spinner_item);
                        spinnerSubtipo.setAdapter(spinnerAdapterRiego);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String traductorTipo() {
        if (switchMaterialManoDeObra.isChecked()) {
            return "O";
        } else {
            return "M";
        }
    }

    private String traductorTipoActividad(String tipo) {
        if (tipo.equals("Poda")) {
            return "P";
        }
        if (tipo.equals("Fumigación")) {
            return "U";
        }
        if (tipo.equals("Fertilización")) {
            return "F";
        } else {
            return "R";
        }
    }

    private String traductorSubTipoActividad(String tipo) {
        int selectedItemOfMySpinner = spinnerTipo.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipo.getItemAtPosition(selectedItemOfMySpinner);
        String inicial = traductorTipoActividad(actualPositionOfMySpinner);
        if (inicial.equals("P")) {
            if (tipo.equals("Mantenimiento")) {
                return "P3";
            }
        } else if (inicial.equals("F")) {
            if (tipo.equals("Mantenimiento")) {
                return "F3";
            }
        }
        switch (tipo) {
            case "Sistema":
                return "R3";
            case "Manual":
                return "R2";
            case "Natural":
                return "R1";
            case "Crecimiento":
                return "F1";
            case "Produccion":
                return "F2";
            case "Insectos":
                return "U1";
            case "Hongos":
                return "U2";
            case "Hierba":
                return "U3";
            case "Ácaro":
                return "U4";
            case "Peste":
                return "U5";
            case "Sanitaria":
                return "P1";
            case "Formación":
                return "P2";
            case "Limpieza":
                return "P4";
        }
        return "";
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

        int selectedItemOfMySpinner = spinnerTipo.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipo.getItemAtPosition(selectedItemOfMySpinner);
        if (actualPositionOfMySpinner.equals("Seleccione el tipo de actividad...")) {
            setSpinnerError(spinnerTipo);
            valid = false;
        } else {
            int selectedItemOfMySubSpinner = spinnerSubtipo.getSelectedItemPosition();
            String actualPositionOfMySpinnerSub = (String) spinnerSubtipo.getItemAtPosition(selectedItemOfMySubSpinner);
            switch (actualPositionOfMySpinnerSub) {
                case "Seleccione el tipo de poda...":
                case "Seleccione el tipo de fumigación...":
                case "Seleccione el tipo de fertilización...":
                case "Seleccione el tipo de riego...":
                    setSpinnerError(spinnerSubtipo);
                    valid = false;
                    break;
            }
        }
        if (TextUtils.isEmpty(txtFechaGasto.getText().toString())) {
            txtFechaGasto.setError("Requerido");
            valid = false;
        } else {
            // TODO: set proper names for this variables
            String divide = txtFechaGasto.getText().toString();
            String separated[] = divide.split(" ");
            String aux = separated[3];
            String data[] = aux.split("/");
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
            if (cal.compareTo(cal2) > 0) {
                txtFechaGasto.setError(null);
            } else {
                txtFechaGasto.setError("La fecha debe ser la actual o anterior a la actual");
                valid = false;
            }
        }
        if (TextUtils.isEmpty(txtNombreProducto.getText().toString())) {
            txtNombreProducto.setError("Requerido");
            valid = false;
        } else {
            txtNombreProducto.setError(null);
        }
        if (TextUtils.isEmpty(txtValor.getText().toString())) {
            txtValor.setError("Requerido");
            valid = false;
        } else {
            txtValor.setError(null);
        }
        return valid;
    }
}
