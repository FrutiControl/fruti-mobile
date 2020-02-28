package com.fruticontrol;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainCRUD extends AppCompatActivity {

    EditText textFechaSiembra;
    Button buttonFechaSiembra;
    Button buttonUbicacion;
    Calendar cal;
    DatePickerDialog dpd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_arbol);

        textFechaSiembra = findViewById(R.id.textFechaSiembra);
        buttonFechaSiembra = findViewById(R.id.buttonFechaSiembra);
        buttonUbicacion=findViewById(R.id.buttonDefinirUbicacion);

        buttonUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),MapaNuevoArbolActivity.class);
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
                dpd = new DatePickerDialog(MainCRUD.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        textFechaSiembra.setText(mDayOfMonth+"/"+(mMonth+1)+"/"+mYear);
                    }
                }, day,month,year);
                dpd.show();
            }
        });



    }
}
