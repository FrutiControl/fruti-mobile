package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerfilActivity extends AppCompatActivity {


    private Button preferenciasButton;
    private Button guardarCambiosButton;
    private EditText nombreET;
    private EditText correoET;
    private EditText passET;
    private EditText passRepET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        preferenciasButton = findViewById(R.id.buttonPreferencias);
        nombreET = findViewById(R.id.editTextNombrePerfil);
        correoET = findViewById(R.id.editTextCorreoPerfil);
        passET = findViewById(R.id.editTextContraPerfil);
        passRepET = findViewById(R.id.editTextContraRepPerfil);
        guardarCambiosButton = findViewById(R.id.buttonGuardarCambiosPerfil);

        preferenciasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PreferenciasActivity.class);
                startActivity(intent);
            }
        });
        guardarCambiosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    Toast.makeText(PerfilActivity.this, "Es valido", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean validateForm() {
        boolean valid = true;
        Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher emailMatcher = emailPattern.matcher(correoET.getText());
        Pattern passPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher passMatcher = passPattern.matcher(passET.getText());
        if (TextUtils.isEmpty(nombreET.getText().toString())) {
            nombreET.setError("Requerido");
            valid = false;
        } else {
            correoET.setError(null);
        }
        if (TextUtils.isEmpty(correoET.getText().toString())) {
            correoET.setError("Requerido");
            valid = false;
        } else if (!emailMatcher.matches()) {
            Log.e("usersAPI", "El correo no es válido");
            correoET.setError("Correo inválido");
        } else {
            correoET.setError(null);
        }
        if (TextUtils.isEmpty(passET.getText().toString()) && TextUtils.isEmpty(passRepET.getText().toString())) {
            passET.setError("Requerido");
            passRepET.setError("Requerido");
            valid = false;
        } else if (!passMatcher.matches()) {
            Log.e("usersAPI", "La contraseña no es válido");
            passET.setError("La contraseña debe tener minimo 8 caracteres, una mayuscula, una minuscula, un caracter especial y un número");
            valid = false;
        } else if (!passRepET.getText().toString().equals(passET.getText().toString())) {
            passET.setError("Las contraseñas deben coincidir");
            passRepET.setError("Las contraseñas deben coincidir");
            valid = false;
        } else {
            passET.setError(null);
            passRepET.setError(null);
        }
        return valid;
    }
}
