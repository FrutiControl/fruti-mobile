package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

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
                    Toast.makeText(NuevaActividadActivity.this, "Es valido", Toast.LENGTH_SHORT).show();
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
                        txtFechaInicio.setText("Fecha de inicio: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
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
                        txtFechaFin.setText("Fecha de fin: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
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
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        int selectedItemOfMySpinner = spinnerTipo.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipo.getItemAtPosition(selectedItemOfMySpinner);
        if (actualPositionOfMySpinner.equals("Seleccione el tipo de actividad...")) {
            setSpinnerError(spinnerTipo);
            valid = false;
        }
        else{
            int selectedItemOfMySubSpinner= spinnerSubtipo.getSelectedItemPosition();
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
        }else{
            String divide=txtFechaInicio.getText().toString();
            String separated[]=divide.split(" ");
            String aux=separated[3];
            String data[]=aux.split("/");
            if(today.year>Integer.valueOf(data[2])){
                valid=false;
                txtFechaInicio.setError("Debe seleccionar la fecha actual o una futura");
            }else if(today.month+1>Integer.valueOf(data[1])){
                valid=false;
                txtFechaInicio.setError("Debe seleccionar la fecha actual o una futura");
            }
            else if(today.monthDay>Integer.valueOf(data[0])){
                valid=false;
                txtFechaInicio.setError("Debe seleccionar la fecha actual o una futura");
            }
            else{
                txtFechaInicio.setError(null);
            }
        }
        if (TextUtils.isEmpty(txtFechaFin.getText().toString())) {
            txtFechaFin.setError("Requerido");
            valid = false;
        }else{
            String divide=txtFechaInicio.getText().toString();
            String separated[]=divide.split(" ");
            String aux=separated[3];
            String data[]=aux.split("/");

            String divide2=txtFechaFin.getText().toString();
            String separated2[]=divide2.split(" ");
            String aux2=separated2[3];
            String data2[]=aux2.split("/");

            if(Integer.valueOf(data2[2])<Integer.valueOf(data[2])){
                valid=false;
                txtFechaFin.setError("Debe ser igual o mayor a la fecha de inicio");
            }else if(Integer.valueOf(data2[1])<Integer.valueOf(data[1])){
                valid=false;
                txtFechaFin.setError("Debe ser igual o mayor a la fecha de inicio");
            }
            else if(Integer.valueOf(data2[0])<Integer.valueOf(data[0])){
                valid=false;
                txtFechaFin.setError("Debe ser igual o mayor a la fecha de inicio");
            }
            else{
                txtFechaFin.setError(null);
            }
        }

        return valid;
    }
}
