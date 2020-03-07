package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccionesActivity extends AppCompatActivity {

    Button crearArbolButton;
    Button verArbolesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        crearArbolButton=findViewById(R.id.buttonCrearArbol);
        verArbolesButton=findViewById(R.id.buttonVerArboles);


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
    }
}
