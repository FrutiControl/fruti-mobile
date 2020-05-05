package com.fruticontrol;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;

    private TextView txtUsername;
    private TextView txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        txtUsername = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPass);
        TextView txtRegistro = findViewById(R.id.textViewRegistrarse);
        Button iniciarSesion = findViewById(R.id.buttonIniciarSesion);
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (validateForm()) {
                    Toast.makeText(MainActivity.this, "Espere un momento por favor", Toast.LENGTH_SHORT).show();
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String body = "{\"username\":\"" + txtUsername.getText() + "\",\"password\":\"" + txtPass.getText() + "\"}";
                    JSONObject credentials = null;
                    try {
                        credentials = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,
                            "http://10.0.2.2:8000/users/login/"/*TODO: cambiar a URL real para producción!!!!*/, credentials,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("usersAPI", response.toString());
                                    Token token = (Token) getApplicationContext();
                                    if (response.has("error")) {
                                        try {
                                            Toast.makeText(MainActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            token.setToken(response.getString("token"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(view.getContext(), ListaGranjasActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(MainActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(loginRequest);
                }
            }
        });
        txtRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestPermission(Activity context, String permiso, String justificacion, int idCode) {
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, idCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "sin acceso a localizacion, hardware deshabilitado!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Pattern passPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&_+=])(?=\\S+$).{8,}$");
        Matcher emailMatcher = emailPattern.matcher(txtUsername.getText());
        Matcher passMatcher = passPattern.matcher(txtPass.getText());

        if (TextUtils.isEmpty(txtUsername.getText().toString())) {
            txtUsername.setError("Requerido");
            valid = false;
        } else if (!emailMatcher.matches()) {
            Log.e("usersAPI", "El correo no es válido");
            txtUsername.setError("Correo inválido");
            valid = false;
        } else {
            txtUsername.setError(null);
        }

        if (TextUtils.isEmpty(txtPass.getText().toString())) {
            txtPass.setError("Requerido");
            valid = false;
        } else if (!passMatcher.matches()) {
            Log.e("usersAPI", "La contraseña no es válido");
            txtPass.setError("La contraseña debe tener minimo 8 caracteres, una mayuscula, una minuscula, un caracter especial y un número");
            valid = false;
        } else {
            txtPass.setError(null);
        }
        return valid;
    }
}
