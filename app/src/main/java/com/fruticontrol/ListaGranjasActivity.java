package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class ListaGranjasActivity extends AppCompatActivity {

    Button nuevaGranjaButton;

    ArrayList<ResumenGranjaDataModel> dataModels;
    ListView listView;
    private static ResumenGranjasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_granjas);
        Token token=(Token)getApplicationContext();
        listView=(ListView)findViewById(R.id.listaGranjasList);
        dataModels= new ArrayList<>();

        dataModels.add(new ResumenGranjaDataModel("Lollipop","Android 5.0","21"));
        dataModels.add(new ResumenGranjaDataModel("Marshmallow", "Android 6.0", "23"));
        dataModels.add(new ResumenGranjaDataModel("Marshmallow", "Android 6.0", "23"));
        dataModels.add(new ResumenGranjaDataModel("Marshmallow", "Android 6.0", "23"));
        dataModels.add(new ResumenGranjaDataModel("Marshmallow", "Android 6.0", "23"));
        dataModels.add(new ResumenGranjaDataModel("Marshmallow", "Android 6.0", "23"));
        adapter= new ResumenGranjasAdapter(dataModels,getApplicationContext());
        listView.setAdapter(adapter);

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
