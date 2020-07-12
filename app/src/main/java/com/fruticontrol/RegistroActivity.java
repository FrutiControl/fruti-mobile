package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

public class RegistroActivity extends AppCompatActivity {
    public static final int MY_DEFAULT_TIMEOUT = 15000;
    private Token token;
    private TextView txtNombre;
    private TextView txtEmail;
    private TextView txtPass;
    private TextView txtPassVerf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        token=(Token)getApplicationContext();
        txtNombre = findViewById(R.id.editTextNombreCompleto);
        txtEmail = findViewById(R.id.editTextMail);
        txtPass = findViewById(R.id.editTextPass);
        txtPassVerf = findViewById(R.id.editTextPassVerf);
        Button registroButton = findViewById(R.id.buttonRegistro);
        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (validateForm()) {
                    Toast.makeText(RegistroActivity.this, "Espere un momento por favor", Toast.LENGTH_SHORT).show();
                    RequestQueue queue = Volley.newRequestQueue(RegistroActivity.this);
                    String body = "{\"username\":\"" + txtEmail.getText() +
                            "\",\"password\":\"" + txtPassVerf.getText() +
                            "\",\"first_name\":\"" + txtNombre.getText() + "\"}";
                    Log.i("usersAPI", "Información: " + body);
                    JSONObject informacion = null;
                    try {
                        informacion = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST,token.getDomain()+
                            "/users/register/", informacion,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Token token = (Token) getApplicationContext();
                                    try {
                                        token.setToken(response.getString("token"));
                                        Log.i("usersAPI", "respuesta: " + response.toString());
                                        //TODO: extraer el token del response y verificarlo antes de la siguiente actividad
                                        Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(view.getContext(), NuevaGranjaActivity.class);
                                        startActivity(intent);
                                        MainActivity.getInstance().finish();
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.e("usersAPI", "Error en la invocación a la API " + error);
                            Toast.makeText(RegistroActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    });
                    registerRequest.setRetryPolicy(new DefaultRetryPolicy(
                            MY_DEFAULT_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(registerRequest);
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher emailMatcher = emailPattern.matcher(txtEmail.getText());
        Pattern passPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[\\w~@#$%^&*+_=`|{}:;!.?\\\"()\\[\\]-]{8,25}$");
        Matcher passMatcher = passPattern.matcher(txtPass.getText());
        if (TextUtils.isEmpty(txtNombre.getText().toString())) {
            txtNombre.setError("Requerido");
            valid = false;
        } else {
            txtNombre.setError(null);
        }
        if (TextUtils.isEmpty(txtEmail.getText().toString())) {
            txtEmail.setError("Requerido");
            valid = false;
        } else if (!emailMatcher.matches()) {
            Log.e("usersAPI", "El correo no es válido");
            txtEmail.setError("Correo inválido");
            valid = false;
        } else {
            txtEmail.setError(null);
        }
        if (TextUtils.isEmpty(txtPass.getText().toString()) && TextUtils.isEmpty(txtPassVerf.getText().toString())) {
            txtPass.setError("Requerido");
            txtPassVerf.setError("Requerido");
            valid = false;
        } else if (!passMatcher.matches()) {
            Log.e("usersAPI", "La contraseña no es válida");
            txtPass.setError("La contraseña debe tener minimo 8 caracteres, una mayuscula, una minuscula y un número");
            valid = false;
        } else if (!txtPassVerf.getText().toString().equals(txtPass.getText().toString())) {
            txtPass.setError("Las contraseñas deben coincidir");
            txtPassVerf.setError("Las contraseñas deben coincidir");
            valid = false;
        } else {
            txtPass.setError(null);
            txtPassVerf.setError(null);
        }
        return valid;
    }
}
