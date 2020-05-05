package com.fruticontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AccionesActivity extends AppCompatActivity {

    private Button arbolButton;
    private Button dashboardActividadesButton;
    private Button dashboardFinanzasButton;
    private Button perfilButton;
    private Button verGranjasButton;
    private Token token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        token = (Token) getApplicationContext();
        arbolButton = findViewById(R.id.buttonArbol);
        dashboardActividadesButton = findViewById(R.id.buttonDashboardActividades);
        dashboardFinanzasButton = findViewById(R.id.buttonDashboardFinanzas);
        perfilButton = findViewById(R.id.buttonPerfil);
        verGranjasButton = findViewById(R.id.buttonVerGranjas);
        System.out.println("XXXXXX LA GRANJA TOUCHADA FUE LA " + token.getGranjaActual());

        verGranjasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListaGranjasActivity.class);
                startActivity(intent);
            }
        });
        arbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListaArbolesActivity.class);
                startActivity(intent);
            }
        });

        dashboardActividadesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DashboardActividadesActivity.class);
                startActivity(intent);
            }
        });

        dashboardFinanzasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DashboardFinanzasActivity.class);
                startActivity(intent);
            }
        });

        perfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PerfilActivity.class);
                startActivity(intent);
            }
        });

    }
}


//https://www.flaticon.com/free-icon/plant_2487517
//https://www.flaticon.com/free-icon/tree_1625460
//https://www.flaticon.com/free-icon/money_2694206
//https://www.flaticon.com/free-icon/farmer_2010087?term=farmer&page=2&position=29
