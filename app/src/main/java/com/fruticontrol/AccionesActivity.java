package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccionesActivity extends AppCompatActivity {

    Button crearArbolButton;
    Button verArbolesButton;
    Button seleccionarArbolesButton;
    Button anadirGastoButton;
    Button nuevoIngresoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        crearArbolButton=findViewById(R.id.buttonCrearArbol);
        verArbolesButton=findViewById(R.id.buttonVerArboles);
        seleccionarArbolesButton=findViewById(R.id.buttonNuevaActividad);
        anadirGastoButton=findViewById(R.id.buttonAñadirGasto);
        nuevoIngresoButton=findViewById(R.id.buttonNuevoIngreso);


        anadirGastoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),GastosActivity.class);
                startActivity(intent);
            }
        });

        crearArbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CrearArbolActivity.class);
                startActivity(intent);
            }
        });
        verArbolesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),ListaArbolesActivity.class);
                startActivity(intent);
            }
        });

        nuevoIngresoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),NuevoIngresoActivity.class);
                startActivity(intent);
            }
        });

        seleccionarArbolesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), NuevaActividadActivity.class);
                startActivity(intent);
            }
        });
    }
}
