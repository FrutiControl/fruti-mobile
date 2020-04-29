package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardFinanzasActivity extends AppCompatActivity {

    private Button nuevoIngresoButton;
    private Button nuevoGastoButton;
    private Button verIngresosButton;
    private Button verGastosButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_finanzas);

        nuevoIngresoButton=findViewById(R.id.buttonNuevoIngresoDFinanzas);
        nuevoGastoButton=findViewById(R.id.buttonNuevoGastoDFinanzas);
        verIngresosButton =findViewById(R.id.buttonVerIngresos);
        verGastosButton=findViewById(R.id.buttonVerGastos);

        verGastosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),VerGastosActivity.class);
                startActivity(intent);
            }
        });
        verIngresosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), VerIngresosActivity.class);
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

        nuevoGastoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),GastosActivity.class);
                startActivity(intent);
            }
        });
    }
}
