package com.fruticontrol;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NuevoIngresoActivity extends AppCompatActivity {
    private Spinner spinnerTipoArbol;
    private Spinner spinnerUnidad;
    private EditText txtFechaIngreso;
    private Calendar calIngreso;
    private DatePickerDialog dateIngreso;
    private EditText etCantidadCanastilla;
    private EditText etValorCanastilla;
    private EditText etConceptoIngreso;
    private TextView txtTotal;
    private Token token;
    private DecimalFormat formatea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_ingreso);
        token = (Token) getApplicationContext();
        formatea = new DecimalFormat("###,###.##");
        spinnerTipoArbol = findViewById(R.id.spinnerTipoArbolNuevoIngreso);
        spinnerUnidad = findViewById(R.id.spinnerUnidad);
        etCantidadCanastilla = findViewById(R.id.editTextCantidadCanastillas);
        etValorCanastilla = findViewById(R.id.editTextValorCanastilla);
        etConceptoIngreso = findViewById(R.id.editTextConcepto);
        txtTotal = findViewById(R.id.textViewTotalIngreso);
        txtFechaIngreso = findViewById(R.id.editTextFechaIngreso);
        Button guardarNuevoIngresoButton = findViewById(R.id.buttonGuardarNuevoIngreso);
        ArrayAdapter<CharSequence> spinnerAdapterTipo = ArrayAdapter.createFromResource(this, R.array.TipoArbolFrutal, R.layout.spinner_item);
        spinnerTipoArbol.setAdapter(spinnerAdapterTipo);
        ArrayAdapter<CharSequence> spinnerUnidades = ArrayAdapter.createFromResource(this, R.array.Unidades, R.layout.spinner_item);
        spinnerUnidad.setAdapter(spinnerUnidades);
        spinnerUnidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipo = spinnerUnidad.getSelectedItem().toString();
                switch (tipo) {
                    case "Seleccione las unidades...":
                        etValorCanastilla.setHint("Valor");
                        break;
                    case "Unidades":
                        etValorCanastilla.setHint("Valor unidades");
                        break;
                    case "Kilos":
                        etValorCanastilla.setHint("Valor kilos");
                        break;
                    case "Canastillas":
                        etValorCanastilla.setHint("Valor canastillas");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        guardarNuevoIngresoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    RequestQueue queue = Volley.newRequestQueue(NuevoIngresoActivity.this);
                    //CONCEPTO DEL INGRESO
                    String concepto = etConceptoIngreso.getText().toString();
                    //SE TOMA LA FECHA DE SIEMBRA Y SE CAMBIA EL FORMATO
                    // TODO: set proper names for this variables
                    String divide = txtFechaIngreso.getText().toString();
                    String separated[] = divide.split(" ");
                    String aux = separated[3];
                    String data[] = aux.split("/");
                    String auxFecha = data[2] + "-" + data[1] + "-" + data[0];
                    //VALOR DE CANASTILLA
                    String valorCanastilla = etValorCanastilla.getText().toString();
                    //CANTIDAD DE CANASTILLAS
                    String cantidadCanastillas = etCantidadCanastilla.getText().toString();
                    //TIPO DE UNIDAD
                    int selectedItemOfMySpinnerUnidad = spinnerUnidad.getSelectedItemPosition();
                    String actualPositionOfMySpinnerUnidad = (String) spinnerUnidad.getItemAtPosition(selectedItemOfMySpinnerUnidad);
                    String unidad = traductorUnidades(actualPositionOfMySpinnerUnidad);
                    //SE TOMA EL TIPO DE ARBOL Y SE AVERIGUA LA INICIAL
                    int selectedItemOfMySpinner = spinnerTipoArbol.getSelectedItemPosition();
                    String actualPositionOfMySpinner = (String) spinnerTipoArbol.getItemAtPosition(selectedItemOfMySpinner);
                    String inicial = inicialTipo(actualPositionOfMySpinner);
                    String body = "{\"concept\":\"" + concepto + "\",\"date\":\"" + auxFecha + "\",\"value\":\"" + valorCanastilla + "\",\"quantity\":\"" + cantidadCanastillas + "\",\"fruit_type\":\"" + inicial + "\",\"recommended\":\"" + false + "\",\"unit\":\"" + unidad + "\"}";
                    Log.i("newIncomeAPI", "Nuevo ingreso: " + body);
                    JSONObject newTree = null;
                    try {
                        newTree = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest newTreeRequest = new JsonObjectRequest(Request.Method.POST,token.getDomain()+
                            "/money/incomes/", newTree,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("newIncomeAPI", response.toString());

                                    if (response.has("error")) {
                                        try {
                                            Toast.makeText(NuevoIngresoActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(NuevoIngresoActivity.this, "Ingreso creado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(NuevoIngresoActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }) {    //this is the part, that adds the header to the request
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Token " + token.getToken());
                            return params;
                        }
                    };
                    queue.add(newTreeRequest);
                    Toast.makeText(NuevoIngresoActivity.this, "Es valido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtFechaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calIngreso = Calendar.getInstance();
                int day = calIngreso.get(Calendar.DAY_OF_MONTH);
                int month = calIngreso.get(Calendar.MONTH);
                int year = calIngreso.get(Calendar.YEAR);
                dateIngreso = new DatePickerDialog(NuevoIngresoActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        txtFechaIngreso.setText("Fecha del ingreso: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dateIngreso.show();
            }
        });
        etValorCanastilla.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    calcularTotal();
            }
        });

        etCantidadCanastilla.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    calcularTotal();
            }
        });
    }

    private String traductorUnidades(String tipo) {
        switch (tipo) {
            case "Canastillas":
                return "C";
            case "Kilos":
                return "K";
            case "Unidades":
                return "U";
        }
        return "";
    }

    private boolean validateForm() {
        boolean valid = true;
        int selectedItemOfMySpinner = spinnerTipoArbol.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipoArbol.getItemAtPosition(selectedItemOfMySpinner);
        if (TextUtils.isEmpty(etCantidadCanastilla.getText().toString())) {
            etCantidadCanastilla.setError("Requerido");
            valid = false;
        } else {
            etCantidadCanastilla.setError(null);
        }
        if (TextUtils.isEmpty(etValorCanastilla.getText().toString())) {
            etValorCanastilla.setError("Requerido");
            valid = false;
        } else {
            etValorCanastilla.setError(null);
        }
        if (TextUtils.isEmpty(etConceptoIngreso.getText().toString())) {
            etConceptoIngreso.setError("Requerido");
            valid = false;
        } else {
            etConceptoIngreso.setError(null);
        }
        if (actualPositionOfMySpinner.equals("Seleccione las unidades...")) {
            setSpinnerError(spinnerUnidad);
            valid = false;
        }
        if (actualPositionOfMySpinner.equals("Seleccione el tipo de árbol...")) {
            setSpinnerError(spinnerTipoArbol);
            valid = false;
        }
        if (TextUtils.isEmpty(txtFechaIngreso.getText().toString())) {
            txtFechaIngreso.setError("Requerido");
            valid = false;
        } else {
            String divide = txtFechaIngreso.getText().toString();
            String separated[] = divide.split(" ");
            String aux = separated[3];
            String data[] = aux.split("/");

            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
            if (cal.compareTo(cal2) > 0) {
                txtFechaIngreso.setError(null);
            } else {
                txtFechaIngreso.setError("La fecha debe ser la actual o anterior a la actual");
                valid = false;
            }
        }
        return valid;
    }

    private String inicialTipo(String opcion) {
        switch (opcion) {
            case "Mango Tommy":
                return "M";
            case "Mango Fairchild":
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

    private void setSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("Requerido"); // any name of the error will do
        }
    }

    protected void calcularTotal() {
        if (!TextUtils.isEmpty(etValorCanastilla.getText().toString()) && !TextUtils.isEmpty(etCantidadCanastilla.getText().toString())) {
            int total = (Integer.parseInt(etValorCanastilla.getText().toString())) * Integer.parseInt(etCantidadCanastilla.getText().toString());
            txtTotal.setText("Total: $" + formatea.format(total));
        }
    }
}
