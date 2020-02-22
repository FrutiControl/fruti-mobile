package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListaGranjasActivity extends AppCompatActivity {

    Button nuevaGranjaButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_granjas);
        nuevaGranjaButton = findViewById(R.id.buttonNuevaGranja);
        nuevaGranjaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NuevaGranjaActivity.class);
                startActivity(intent);

            }
        });

    }
}
