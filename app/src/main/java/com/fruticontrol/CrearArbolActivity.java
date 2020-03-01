package com.fruticontrol;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CrearArbolActivity extends AppCompatActivity {

    EditText textFechaSiembra;
    Button buttonFechaSiembra;
    Button buttonUbicacion;
    Calendar cal;
    DatePickerDialog dpd;
    Spinner spinnerTipoArbol;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_arbol);
        statusCheck();
        textFechaSiembra = findViewById(R.id.textFechaSiembra);
        spinnerTipoArbol = findViewById(R.id.spinnerTipoArbol);
        buttonFechaSiembra = findViewById(R.id.buttonFechaSiembra);
        buttonUbicacion = findViewById(R.id.buttonDefinirUbicacion);
        spinnerTipoArbol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipo = spinnerTipoArbol.getSelectedItem().toString();
                Log.i("c_arbol", "opcion seleccionada " + tipo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        buttonUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapaNuevoArbolActivity.class);
                startActivity(intent);
            }
        });

        buttonFechaSiembra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month;
                month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                dpd = new DatePickerDialog(CrearArbolActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textFechaSiembra.setText(mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, day, month, year);
                dpd.show();
            }
        });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Su GPS está desactivado, para continuar debe encenderlo.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
