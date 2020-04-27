package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
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

import java.util.Calendar;

public class GastosActivity extends AppCompatActivity {

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
    private Button guardarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        guardarButton=findViewById(R.id.buttonGuardarGasto);
        txtNombreProducto=findViewById(R.id.editTextNombreProducto);
        txtValor=findViewById(R.id.editTextValor);
        txtFechaGasto=findViewById(R.id.editTextFechaGasto);
        switchMaterialManoDeObra=findViewById(R.id.switchMaterialesVsManoObra);
        tvMateriales=findViewById(R.id.textViewMateriales);
        tvManoDeObra=findViewById(R.id.textViewManoDeObra);
        spinnerTipo=findViewById(R.id.spinnerGastoTipoPoceso);
        spinnerSubtipo=findViewById(R.id.spinnerGastoSubtipoProceso);
        ArrayAdapter<CharSequence> spinnerAdapterTipo = ArrayAdapter.createFromResource(this, R.array.TipoActividad, R.layout.spinner_item);
        spinnerTipo.setAdapter(spinnerAdapterTipo);
        ArrayAdapter<CharSequence> spinnerAdapterSubtipo = ArrayAdapter.createFromResource(this, R.array.TipoActividad, R.layout.spinner_item);
        spinnerTipo.setAdapter(spinnerAdapterSubtipo);

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    Toast.makeText(GastosActivity.this, "Es valido", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                datePGasto= new DatePickerDialog(GastosActivity.this, R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {
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

    private boolean validateForm(){
        boolean valid = true;

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
        if (TextUtils.isEmpty(txtFechaGasto.getText().toString())) {
            txtFechaGasto.setError("Requerido");
            valid = false;
        }else{
            String divide=txtFechaGasto.getText().toString();
            String separated[]=divide.split(" ");
            String aux=separated[3];
            String data[]=aux.split("/");

            Calendar cal=Calendar.getInstance();
            cal.getTime();
            Calendar cal2=Calendar.getInstance();
            cal2.set(Integer.parseInt(data[2]),Integer.parseInt(data[1])-1,Integer.parseInt(data[0]),00,00);
            if(cal.compareTo(cal2)>0){
                txtFechaGasto.setError(null);
            }else{
                txtFechaGasto.setError("La fecha debe ser la actual o anterior a la actual");
                valid=false;
            }
        }
        if (TextUtils.isEmpty(txtNombreProducto.getText().toString())) {
            txtNombreProducto.setError("Requerido");
            valid = false;
        }else{
            txtNombreProducto.setError(null);
        }
        if (TextUtils.isEmpty(txtValor.getText().toString())) {
            txtValor.setError("Requerido");
            valid = false;
        }else{
            txtValor.setError(null);
        }



        return valid;
    }
}
