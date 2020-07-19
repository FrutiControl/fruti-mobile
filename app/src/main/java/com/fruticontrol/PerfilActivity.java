package com.fruticontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class PerfilActivity extends AppCompatActivity {
    private EditText nombreET;
    private EditText correoET;
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        config = (Config) getApplicationContext();
        Button preferenciasButton = findViewById(R.id.buttonPreferencias);
        Button cerrarSesionButton = findViewById(R.id.buttonCerrarSersion);
        nombreET = findViewById(R.id.editTextNombrePerfil);
        correoET = findViewById(R.id.editTextCorreoPerfil);
        Button guardarCambiosButton = findViewById(R.id.buttonGuardarCambiosPerfil);
        Intent intent = getIntent();
        nombreET.setText(intent.getStringExtra("nombre"));
        correoET.setText(intent.getStringExtra("correo"));
        new MaterialIntroView.Builder(this)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(false)
                .setInfoText("En esta pantalla puede modificar los datos con los que se registró y modificarlos si lo desea. Con el botón preferencias puede modificar el valor de jornal")
                .setShape(ShapeType.RECTANGLE)
                .setTarget(preferenciasButton)
                .setUsageId("preferencias_showcase")
                .show();
        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PerfilActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                config.setToken("0");
                sessionKill();
                Intent intents = new Intent(PerfilActivity.this, MainActivity.class);
                intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
                finish();
            }
        });
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
                    RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
                    String nombre = nombreET.getText().toString();
                    String correo = correoET.getText().toString();
                    String body = "{\"username\":\"" + correo +
                            "\",\"first_name\":\"" + nombre +
                            "\",\"email\":\"" + correo + "\"}";
                    Log.i("modificateUserAPI", "Usuario modificado: " + body);
                    JSONObject modifyUser = null;
                    try {
                        modifyUser = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String auxUrl = config.getDomain()+"/users/user/";
                    JsonObjectRequest modificateUserRequest = new JsonObjectRequest(Request.Method.PUT,
                            auxUrl, modifyUser,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("modificateUserAPI", response.toString());

                                    if (response.has("error")) {
                                        try {
                                            Toast.makeText(PerfilActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(PerfilActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TreeAPI", "Error en la invocación a la API " + error.getCause());
                            Toast.makeText(PerfilActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }) {    //this is the part, that adds the header to the request
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Token " + config.getToken());
                            return params;
                        }
                    };
                    queue.add(modificateUserRequest);
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher emailMatcher = emailPattern.matcher(correoET.getText());
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
        return valid;
    }


    private void sessionKill(){
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Token","000000000000");
        editor.putBoolean("Valid",false);
        editor.apply();
    }
}
