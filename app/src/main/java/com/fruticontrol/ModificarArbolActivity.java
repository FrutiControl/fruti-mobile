package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class ModificarArbolActivity extends AppCompatActivity {

    private String idArbol;
    private String tipo;
    private String fecha;
    private String localizacion;
    private String lat;
    private String lon;
    private TextView txtFechaSiembra;
    private Spinner spinnerTipoArbol;
    private Calendar cal;
    private DatePickerDialog dpd;
    private Button modificarUbicacionButton;
    private Button eliminarArbolButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_arbol);
        txtFechaSiembra=findViewById(R.id.textFechaSiembra2);
        spinnerTipoArbol=findViewById(R.id.spinnerModificarTipoArbol);
        modificarUbicacionButton=findViewById(R.id.buttonModificarUbicacion);
        eliminarArbolButton=findViewById(R.id.buttonEliminarArbol);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.TipoArbolFrutal, R.layout.spinner_item);
        spinnerTipoArbol.setAdapter(spinnerAdapter);

        Intent intent=getIntent();
        idArbol=intent.getStringExtra("idArbolActual");
        tipo=intent.getStringExtra("tipo");
        fecha=intent.getStringExtra("fecha");
        localizacion=intent.getStringExtra("localizacion");

        String divide=localizacion;
        String separated[]=divide.split("\\(");
        String anotherAux[]=separated[1].split(" ");
        lat=anotherAux[0];
        System.out.println("Anothreaux es "+anotherAux[0]);
        String lonAux[]=anotherAux[1].split("\\)");
        System.out.println("Anothreaux1 es "+lonAux[0]);
        lon=lonAux[0];

        int setAux=valorPosicion(tipo);
        spinnerTipoArbol.setSelection(setAux);
        String divide2=fecha;
        String separated2[]=divide2.split("-");
        txtFechaSiembra.setText("Fecha de siembra: "+separated2[2]+"/"+separated2[1]+"/"+separated2[0]);

        modificarUbicacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),MapaModificarArbolActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                startActivity(intent);
            }
        });
        txtFechaSiembra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                dpd = new DatePickerDialog(ModificarArbolActivity.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        txtFechaSiembra.setText("Fecha de siembra: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd.show();
            }
        });
    }

    private int valorPosicion(String tipo){
        if(tipo.equals("Mango tommy"))
            return 1;
        if(tipo.equals("Mango farchil"))
            return 2;
        if(tipo.equals("Naranja"))
            return 3;
        if(tipo.equals("Mandarina"))
            return 4;
        if(tipo.equals("Limon"))
            return 5;
        if(tipo.equals("Aguacate"))
            return 6;
        else
            return 7;
    }

    private boolean validateForm() {
        boolean valid = true;

        int selectedItemOfMySpinner = spinnerTipoArbol.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipoArbol.getItemAtPosition(selectedItemOfMySpinner);

        if (actualPositionOfMySpinner.equals("Seleccione el tipo de árbol...")) {
            setSpinnerError(spinnerTipoArbol);
            valid = false;
        }
//VALIDACION FECHA DE SIEMBRA
        if (TextUtils.isEmpty(txtFechaSiembra.getText().toString())) {
            txtFechaSiembra.setError("Requerido");
            valid = false;
        }else{
            String divide=txtFechaSiembra.getText().toString();
            String separated[]=divide.split(" ");
            String aux=separated[3];
            String data[]=aux.split("/");
            Calendar cal=Calendar.getInstance();
            cal.getTime();
            Calendar cal2=Calendar.getInstance();
            System.out.println("La seleccionada es "+data[2]+data[1]+data[0]);
            cal2.set(Integer.parseInt(data[2]),Integer.parseInt(data[1])-1,Integer.parseInt(data[0]),00,00);
            if(cal.compareTo(cal2)>0){
                txtFechaSiembra.setError(null);
            }else{
                txtFechaSiembra.setError("La fecha debe ser la actual o anterior a la actual");
                valid=false;
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
}


