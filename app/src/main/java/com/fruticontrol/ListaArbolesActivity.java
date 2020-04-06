package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaArbolesActivity extends AppCompatActivity {

    ArrayList<ResumenArbolDataModel> dataModels;
    ListView listView;
    private static ResumenArbolesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_arboles);

        listView=(ListView)findViewById(R.id.listaArbolesList);
        dataModels= new ArrayList<>();

        dataModels.add(new ResumenArbolDataModel("1","Mango","Siembra"));
        dataModels.add(new ResumenArbolDataModel("2", "Aguacate", "Siembra"));
        dataModels.add(new ResumenArbolDataModel("2", "Aguacate", "Siembra"));
        dataModels.add(new ResumenArbolDataModel("2", "Aguacate", "Siembra"));
        dataModels.add(new ResumenArbolDataModel("2", "Aguacate", "Siembra"));
        dataModels.add(new ResumenArbolDataModel("2", "Aguacate", "Siembra"));
        adapter= new ResumenArbolesAdapter(dataModels,getApplicationContext());
        listView.setAdapter(adapter);
    }
}
