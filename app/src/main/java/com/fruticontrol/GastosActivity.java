package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

public class GastosActivity extends AppCompatActivity {

    Spinner spinnerTipo;
    Spinner spinnerSubtipo;
    TextView tvMateriales;
    TextView tvManoDeObra;
    Switch switchMaterialManoDeObra;
    EditText txtFechaGasto;
    Calendar calGasto;
    DatePickerDialog datePGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        txtFechaGasto=findViewById(R.id.editTextFechaGasto);
        switchMaterialManoDeObra=findViewById(R.id.switchMaterialesVsManoObra);
        tvMateriales=findViewById(R.id.textViewMateriales);
        tvManoDeObra=findViewById(R.id.textViewManoDeObra);
        spinnerTipo=findViewById(R.id.spinnerGastoTipoPoceso);
        spinnerSubtipo=findViewById(R.id.spinnerGastoSubtipoProceso);
        ArrayAdapter<CharSequence> spinnerAdapterTipo = ArrayAdapter.createFromResource(this, R.array.TipoProceso, R.layout.spinner_item);
        spinnerTipo.setAdapter(spinnerAdapterTipo);
        ArrayAdapter<CharSequence> spinnerAdapterSubtipo = ArrayAdapter.createFromResource(this, R.array.TipoProceso, R.layout.spinner_item);
        spinnerTipo.setAdapter(spinnerAdapterSubtipo);

        switchMaterialManoDeObra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchMaterialManoDeObra.isChecked()){
                    tvManoDeObra.setTypeface(null,Typeface.BOLD);
                    tvMateriales.setTypeface(null, Typeface.NORMAL);
                }
                else{
                    tvMateriales.setTypeface(null, Typeface.BOLD);
                    tvManoDeObra.setTypeface(null,Typeface.NORMAL);
                }
            }
        });

        txtFechaGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calGasto= Calendar.getInstance();
                int day = calGasto.get(Calendar.DAY_OF_MONTH);
                int month = calGasto.get(Calendar.MONTH);
                int year = calGasto.get(Calendar.YEAR);
                datePGasto= new DatePickerDialog(GastosActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                if(tipo.equals("Seleccione el tipo de proceso...")){
                    setSpinnerError(spinnerTipo);
                }
                if(tipo.equals("Poda")){
                    ArrayAdapter<CharSequence> spinnerAdapterPoda = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoPoda, R.layout.spinner_item);
                    spinnerSubtipo.setAdapter(spinnerAdapterPoda);
                }
                if(tipo.equals("Fumigación")){
                    ArrayAdapter<CharSequence> spinnerAdapterFumigacion = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoFumigacion, R.layout.spinner_item);
                    spinnerSubtipo.setAdapter(spinnerAdapterFumigacion);
                }
                if(tipo.equals("Fertilización")){
                    ArrayAdapter<CharSequence> spinnerAdapterFertilizacion = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoFertilizacion, R.layout.spinner_item);
                    spinnerSubtipo.setAdapter(spinnerAdapterFertilizacion);
                }
                if(tipo.equals("Riego")){
                    ArrayAdapter<CharSequence> spinnerAdapterRiego = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TipoRiego, R.layout.spinner_item);
                    spinnerSubtipo.setAdapter(spinnerAdapterRiego);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("Requerido");
        }
    }
}
