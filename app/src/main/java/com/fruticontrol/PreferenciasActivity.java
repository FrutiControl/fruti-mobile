package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PreferenciasActivity extends AppCompatActivity {

    private EditText valorJornalET;
    private Button guardarPreferenciasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
        valorJornalET=findViewById(R.id.editTextValorJornal);
        guardarPreferenciasButton=findViewById(R.id.buttonGuardarPreferencias);

        guardarPreferenciasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    Toast.makeText(PreferenciasActivity.this, "Es valido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        if (TextUtils.isEmpty(valorJornalET.getText().toString())) {
            valorJornalET.setError("Requerido");
            valid = false;
        }else{
            valorJornalET.setError(null);
        }
        return valid;
    }
}
