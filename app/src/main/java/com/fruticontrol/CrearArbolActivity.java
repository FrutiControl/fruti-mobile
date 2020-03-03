package com.fruticontrol;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CrearArbolActivity extends AppCompatActivity {

    EditText textFechaSiembra;
    EditText textUltimaPoda;
    EditText textUltimaFumigacion;
    EditText textUltimaFertilizacion;
    EditText textUltimoRiego;
    Button buttonNuevoArbol;
    Button buttonUbicacion;
    Calendar cal, cal1, cal2, cal3, cal4;
    DatePickerDialog dpd, dpd2, dpd3, dpd4, dpd5;
    Spinner spinnerTipoArbol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_arbol);
        statusCheck();
        textFechaSiembra = findViewById(R.id.textFechaSiembra);
        textUltimaPoda = findViewById(R.id.textUltimaPoda);
        spinnerTipoArbol = findViewById(R.id.spinnerTipoArbol);
        textUltimaFumigacion = findViewById(R.id.textUltimaFumigacion);
        textUltimaFertilizacion = findViewById(R.id.textUltimaFertilizacion);
        textUltimoRiego = findViewById(R.id.textUltimoRiego);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.TipoArbolFrutal, R.layout.spinner_item);
        spinnerTipoArbol.setAdapter(spinnerAdapter);
        buttonUbicacion = findViewById(R.id.buttonDefinirUbicacion);
        buttonNuevoArbol = findViewById(R.id.buttonNuevoArbol);
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
        buttonNuevoArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    Intent intent = new Intent(v.getContext(), AccionesActivity.class);
                    startActivity(intent);
                }
            }
        });
        textFechaSiembra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                dpd = new DatePickerDialog(CrearArbolActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textFechaSiembra.setText("Fecha de siembra: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd.show();
            }
        });
        textUltimaPoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal1 = Calendar.getInstance();
                int day = cal1.get(Calendar.DAY_OF_MONTH);
                int month = cal1.get(Calendar.MONTH);
                int year = cal1.get(Calendar.YEAR);
                dpd2 = new DatePickerDialog(CrearArbolActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textUltimaPoda.setText("Fecha de última poda: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd2.show();
            }
        });
        textUltimaFertilizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal2 = Calendar.getInstance();
                int day = cal2.get(Calendar.DAY_OF_MONTH);
                int month = cal2.get(Calendar.MONTH);
                int year = cal2.get(Calendar.YEAR);
                dpd3 = new DatePickerDialog(CrearArbolActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textUltimaFertilizacion.setText("Fecha de última fertilización: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd3.show();
            }
        });
        textUltimaFumigacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal3 = Calendar.getInstance();
                int day = cal3.get(Calendar.DAY_OF_MONTH);
                int month = cal3.get(Calendar.MONTH);
                int year = cal3.get(Calendar.YEAR);
                dpd4 = new DatePickerDialog(CrearArbolActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textUltimaFumigacion.setText("Fecha de última fumigación: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd4.show();
            }
        });
        textUltimoRiego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal4 = Calendar.getInstance();
                int day = cal4.get(Calendar.DAY_OF_MONTH);
                int month = cal4.get(Calendar.MONTH);
                int year = cal4.get(Calendar.YEAR);
                dpd5 = new DatePickerDialog(CrearArbolActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textUltimoRiego.setText("Fecha de último riego: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd5.show();
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

    private boolean validateForm() {
        boolean valid = true;
        if (TextUtils.isEmpty(textFechaSiembra.getText().toString())) {
            textFechaSiembra.setError("Requerido");
            valid = false;
        } else {
            textFechaSiembra.setError(null);
        }
        if (TextUtils.isEmpty(textUltimaFertilizacion.getText().toString())) {
            textUltimaFertilizacion.setError("Requerido");
            valid = false;
        } else {
            textUltimaFertilizacion.setError(null);
        }
        if (TextUtils.isEmpty(textUltimoRiego.getText().toString())) {
            textUltimoRiego.setError("Requerido");
            valid = false;
        } else {
            textUltimoRiego.setError(null);
        }
        if (TextUtils.isEmpty(textUltimaPoda.getText().toString())) {
            textUltimaPoda.setError("Requerido");
            valid = false;
        } else {
            textUltimaPoda.setError(null);
        }
        if (TextUtils.isEmpty(textUltimaFumigacion.getText().toString())) {
            textUltimaFumigacion.setError("Requerido");
            valid = false;
        } else {
            textUltimaFumigacion.setError(null);
        }
        return valid;
    }
}
