package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

        etValorCanastilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularTotal();
            }
        });

        etCantidadCanastilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularTotal();
            }
        });

        etConceptoIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularTotal();
            }
        });
    }

    protected void calcularTotal(){
        if(etValorCanastilla.getText().length()>0 &&  etCantidadCanastilla.getText().length()>0){
            int total=(Integer.valueOf(etValorCanastilla.getText().toString()))*Integer.valueOf(etCantidadCanastilla.getText().toString());
            txtTotal.setText(String.valueOf(total));
        }
        /*
        int val=Integer.parseInt(etValorCanastilla.getText().toString());
        int cant=Integer.parseInt(etCantidadCanastilla.getText().toString());
         */

    }
}
