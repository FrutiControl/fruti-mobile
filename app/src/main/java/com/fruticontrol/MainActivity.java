package com.fruticontrol;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;

    TextView txtRegistro;
    Button iniciarSesion;
    TextView txtUsername;
    TextView txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        txtUsername = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPass);
        iniciarSesion = findViewById(R.id.buttonIniciarSesion);
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //TODO: Realizar validación de campos como en RegisterActivity
                Toast.makeText(MainActivity.this, "Espere un momento por favor", Toast.LENGTH_SHORT).show();
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String body = "{\"username\":\"" + txtUsername.getText() + "\",\"password\":\"" + txtPass.getText() + "\"}";
                Log.i("usersAPI", "Credenciales: " + body);
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
                        if (response.has("error")) {
                            try {
                                Toast.makeText(MainActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //TODO: extraer el token del response y verificarlo antes de la siguiente actividad
                            Intent intent = new Intent(view.getContext(), AccionesActivity.class);
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
                Intent intent = new Intent(view.getContext(), AccionesActivity.class);
                startActivity(intent);
            }
        });
        txtRegistro = findViewById(R.id.textViewRegistrarse);
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
}
