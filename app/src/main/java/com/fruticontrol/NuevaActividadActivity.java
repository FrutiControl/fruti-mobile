package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class NuevaActividadActivity extends AppCompatActivity {

    Calendar calInicio;
    Calendar calFin;
    EditText txtFechaInicio;
    EditText txtFechaFin;
    DatePickerDialog datePInicio;
    DatePickerDialog datePFin;
    Spinner spinnerTipo;
    Spinner spinnerSubtipo;
    Button guardarNuevoProcesoButton;
    Button seleccionarArbolesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_actividad);

        txtFechaInicio=findViewById(R.id.fechaInicio);
        txtFechaFin=findViewById(R.id.fechaFin);
        spinnerTipo=findViewById(R.id.spinnerTipoProceso);
        spinnerSubtipo=findViewById(R.id.spinnerSubtipo);
        seleccionarArbolesButton=findViewById(R.id.buttonSeleccionarArboles);
        guardarNuevoProcesoButton=findViewById(R.id.buttonGuardarNuevoProceso);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.TipoActividad, R.layout.spinner_item);
        spinnerTipo.setAdapter(spinnerAdapter);

        seleccionarArbolesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),ListaArbolesActivity.class);
                startActivity(intent);
            }
        });
        guardarNuevoProcesoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    System.out.println("YASSS");
                }
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

        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calInicio= Calendar.getInstance();
                int day = calInicio.get(Calendar.DAY_OF_MONTH);
                int month = calInicio.get(Calendar.MONTH);
                int year = calInicio.get(Calendar.YEAR);
                datePInicio= new DatePickerDialog(NuevaActividadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        txtFechaInicio.setText("Fecha de inicio de tarea: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                datePInicio.show();
            }
        });

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calFin= Calendar.getInstance();
                int day = calFin.get(Calendar.DAY_OF_MONTH);
                int month = calFin.get(Calendar.MONTH);
                int year = calFin.get(Calendar.YEAR);
                datePFin= new DatePickerDialog(NuevaActividadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        txtFechaFin.setText("Fecha de fin de tarea: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                datePFin.show();
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

    private boolean validateForm(){
        boolean valid = true;
        int selectedItemOfMySpinner = spinnerTipo.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipo.getItemAtPosition(selectedItemOfMySpinner);
        if (actualPositionOfMySpinner.equals(" Seleccione el tipo de proceso... ")) {
            setSpinnerError(spinnerTipo);
            valid = false;
        }

        int selectedItemOfMySubSpinner= spinnerSubtipo.getSelectedItemPosition();
        String actualPositionOfMySpinnerSub = (String) spinnerSubtipo.getItemAtPosition(selectedItemOfMySubSpinner);
        if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de poda...")) {
            valid = false;
        }
        if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de fumigación...")) {
            valid = false;
        }
        if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de fertilización...")) {
            valid = false;
        }
        if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de riego...")) {
            valid = false;
        }
        return valid;
    }
}
