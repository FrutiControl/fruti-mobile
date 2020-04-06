package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class NuevoIngresoActivity extends AppCompatActivity {

    Spinner spinnerTipoArbol;
    EditText txtFechaIngreso;
    Calendar calIngreso;
    DatePickerDialog dateIngreso;
    EditText etCantidadCanastilla;
    EditText etValorCanastilla;
    EditText etConceptoIngreso;
    TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_ingreso);

        spinnerTipoArbol=findViewById(R.id.spinnerTipoArbolNuevoIngreso);
        etCantidadCanastilla=findViewById(R.id.editTextCantidadCanastillas);
        etValorCanastilla=findViewById(R.id.editTextValorCanastilla);
        etConceptoIngreso=findViewById(R.id.editTextConcepto);
        txtTotal=findViewById(R.id.textViewTotalIngreso);
        txtFechaIngreso=findViewById(R.id.editTextFechaIngreso);

        ArrayAdapter<CharSequence> spinnerAdapterTipo = ArrayAdapter.createFromResource(this, R.array.TipoArbolFrutal, R.layout.spinner_item);
        spinnerTipoArbol.setAdapter(spinnerAdapterTipo);

        txtFechaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calIngreso= Calendar.getInstance();
                int day = calIngreso.get(Calendar.DAY_OF_MONTH);
                int month = calIngreso.get(Calendar.MONTH);
                int year = calIngreso.get(Calendar.YEAR);
                dateIngreso= new DatePickerDialog(NuevoIngresoActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                    calcularTotal();
            }
        });

        etCantidadCanastilla.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                    calcularTotal();
            }
        });
    }

    protected void calcularTotal(){
        if(!TextUtils.isEmpty(etValorCanastilla.getText().toString()) && !TextUtils.isEmpty( etCantidadCanastilla.getText().toString())){
            int total=(Integer.valueOf(etValorCanastilla.getText().toString()))*Integer.valueOf(etCantidadCanastilla.getText().toString());
            txtTotal.setText(String.valueOf(total));
        }
    }
}
