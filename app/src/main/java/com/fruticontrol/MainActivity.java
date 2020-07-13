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

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;
    static final int GPS_REQUEST = 300;
    private TextView txtUsername;
    private TextView txtPass;
    private Token token;
    private boolean isGPS = false;
    static MainActivity mainActivity;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        token=(Token)getApplicationContext();
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        txtUsername = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPass);
        TextView txtRegistro = findViewById(R.id.textViewRegistrarse);
        Button iniciarSesion = findViewById(R.id.buttonIniciarSesion);
        activarUbicacion();
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (validateForm()) {
                    Toast.makeText(MainActivity.this, "Espere un momento por favor", Toast.LENGTH_SHORT).show();
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String body = "{\"username\":\"" + txtUsername.getText() +
                            "\",\"password\":\"" + txtPass.getText() + "\"}";
                    Log.i("usersAPI", "Credenciales: " + body);
                    JSONObject credentials = null;
                    try {
                        credentials = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,token.getDomain()+
                            "/users/login/", credentials,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("usersAPI", response.toString());
                                    token = (Token) getApplicationContext();
                                    try {
                                        token.setToken(response.getString("token"));
                                        Toast.makeText(MainActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent = new Intent(view.getContext(), ListaGranjasActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String response = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.e("usersAPI", "Error en la invocación a la API " + response);
                            JSONObject errors;
                            try {
                                errors = new JSONObject(response);
                                if (errors.has("non_field_errors")) {
                                    String response_error = (String) errors.getJSONArray("non_field_errors").get(0);
                                    Toast.makeText(MainActivity.this, response_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
            case GPS_REQUEST: {
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "Debe encender la localización para usar la aplicación", Toast.LENGTH_LONG).show();
                    activarUbicacion();
                }
            }
        }
    }

    public void activarUbicacion(){
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Pattern passPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[\\w~@#$%^&*+_=`|{}:;!.?\\\"()\\[\\]-]{8,25}$");
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
            Log.e("usersAPI", "La contraseña no es válida");
            txtPass.setError("La contraseña debe tener minimo 8 caracteres, una mayuscula, una minuscula y un número");
            valid = false;
        } else {
            txtPass.setError(null);
        }
        return valid;
    }
}