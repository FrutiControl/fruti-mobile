package com.fruticontrol;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NuevoIngresoActivity extends AppCompatActivity {

    private Spinner spinnerTipoArbol;
    private EditText txtFechaIngreso;
    private Calendar calIngreso;
    private DatePickerDialog dateIngreso;
    private EditText etCantidadCanastilla;
    private EditText etValorCanastilla;
    private EditText etConceptoIngreso;
    private TextView txtTotal;
    private Button guardarNuevoIngresoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_ingreso);

        spinnerTipoArbol = findViewById(R.id.spinnerTipoArbolNuevoIngreso);
        etCantidadCanastilla = findViewById(R.id.editTextCantidadCanastillas);
        etValorCanastilla = findViewById(R.id.editTextValorCanastilla);
        etConceptoIngreso = findViewById(R.id.editTextConcepto);
        txtTotal = findViewById(R.id.textViewTotalIngreso);
        txtFechaIngreso = findViewById(R.id.editTextFechaIngreso);
        guardarNuevoIngresoButton = findViewById(R.id.buttonGuardarNuevoIngreso);

        ArrayAdapter<CharSequence> spinnerAdapterTipo = ArrayAdapter.createFromResource(this, R.array.TipoArbolFrutal, R.layout.spinner_item);
        spinnerTipoArbol.setAdapter(spinnerAdapterTipo);

        guardarNuevoIngresoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
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

    private void setSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("Requerido"); // any name of the error will do


        }
    }

    protected void calcularTotal() {
        if (!TextUtils.isEmpty(etValorCanastilla.getText().toString()) && !TextUtils.isEmpty(etCantidadCanastilla.getText().toString())) {
            int total = (Integer.valueOf(etValorCanastilla.getText().toString())) * Integer.valueOf(etCantidadCanastilla.getText().toString());
            txtTotal.setText(String.valueOf(total));
        }
    }
}
