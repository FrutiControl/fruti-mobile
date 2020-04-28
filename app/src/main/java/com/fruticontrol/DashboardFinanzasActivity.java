package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardFinanzasActivity extends AppCompatActivity {

    private Button nuevoIngresoButton;
    private Button nuevoGastoButton;
    private Button verBalanceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_finanzas);

        nuevoIngresoButton=findViewById(R.id.buttonNuevoIngresoDFinanzas);
        nuevoGastoButton=findViewById(R.id.buttonNuevoGastoDFinanzas);
        verBalanceButton=findViewById(R.id.buttonVerIngresos);

        verBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),BalanceActivity.class);
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
