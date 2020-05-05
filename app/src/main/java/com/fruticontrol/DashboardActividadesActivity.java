package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActividadesActivity extends AppCompatActivity {

    private Button nuevaActividadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_actividades);

        nuevaActividadButton = findViewById(R.id.buttonNuevaActividad);

        nuevaActividadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NuevaActividadActivity.class);
                startActivity(intent);
            }
        });
    }
}
