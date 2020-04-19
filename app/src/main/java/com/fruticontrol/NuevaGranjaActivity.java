package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NuevaGranjaActivity extends AppCompatActivity {

    private Button crearGranjaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_granja);

        crearGranjaButton =findViewById(R.id.buttonCrearGranja);

        crearGranjaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),AccionesActivity.class);
                startActivity(intent);
            }
        });
        /*TODO: para enviar la petición de creación el método es un POST, tienen que agregar un Header a la petición formado de la siguiente manera
            headers["Authorization"] = `Token ${token}`
            url = http://10.0.2.2:8000/farms/
            body = {name,polygon} para polygon seguir el formato de https://docs.djangoproject.com/en/3.0/ref/contrib/gis/geos/#django.contrib.gis.geos.Polygon
         */
    }
}
