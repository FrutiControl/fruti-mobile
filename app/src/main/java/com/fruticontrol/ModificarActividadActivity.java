package com.fruticontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ModificarActividadActivity extends AppCompatActivity {

    private Calendar cal;
    private EditText txtFechaFin;
    private DatePickerDialog dpd;
    private Token token;
    private ArrayList<String> listaEstados;
    private ArrayList<String> listaIds;
    private String tipo;
    private String idActividad;

    ArrayList<ResumenArbolSeleccionDataModel> dataModels;
    ListView listView;
    private static ResumenArbolesSeleccionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_actividad);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        token=(Token)getApplicationContext();
        listView=findViewById(R.id.listModificarProgreso);
        txtFechaFin=findViewById(R.id.editTextModificarFin);
        dataModels= new ArrayList<>();
        listaIds=new ArrayList<>();
        listaEstados=new ArrayList<>();

        String auxFechas=getIntent().getStringExtra("fecha");
        String[] separated = auxFechas.split("-");
        txtFechaFin.setText("Fecha de fin: "+separated[1]);

        tipo=getIntent().getStringExtra("tipoFoormato");
        idActividad=getIntent().getStringExtra("idActividad");
        RequestQueue queue = Volley.newRequestQueue(ModificarActividadActivity.this);
        String url="http://10.0.2.2:8000/app/";
        url=url+tipo+"_trees/?"+tipo+"="+idActividad;
        System.out.println("XXXXXXXXXXXXXXXXX URL ES "+url);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                url/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("progressList", response.toString());
                        try {
                            for (int j = 0; j < response.length(); j++) {
                                JSONObject activityObject = response.getJSONObject(j);
                                listaIds.add(activityObject.getString("tree"));
                                if(activityObject.getString("applied").equals("true")){
                                    listaEstados.add("true");
                                }else{
                                    listaEstados.add("false");
                                }
                            }

                            if(!listaEstados.isEmpty()){
                                //SE LLENA LA LISTA
                                for(int i=0;i<listaIds.size();i++){
                                    dataModels.add(new ResumenArbolSeleccionDataModel("Número de árbol: "+listaIds.get(i).toString(),"Estado: "+estadoArbol(listaEstados.get(i).toString()),""));
                                }
                                adapter= new ResumenArbolesSeleccionAdapter(dataModels,getApplicationContext());
                                listView.setAdapter(adapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(ModificarActividadActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                dpd = new DatePickerDialog(ModificarActividadActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        txtFechaFin.setText("Fecha de fin: " + String.format("%s/%s/%s", mDayOfMonth, mMonth + 1, mYear));
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                RequestQueue queue = Volley.newRequestQueue(ModificarActividadActivity.this);
                String url="http://10.0.2.2:8000/app/";
                url=url+tipo+"_trees/?"+tipo+"="+idActividad+"&tree="+listaIds.get(i);
                System.out.println("XXXXXXXXXXXXXXXXX URL ES "+url);
                JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                        url/*TODO: cambiar a URL real para producción!!!!*/, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.i("progressList", response.toString());
                                try {
                                    System.out.println(response.toString());
                                    JSONObject activityObject = response.getJSONObject(0);
                                    System.out.println("EL ESTADO AHORA ES "+activityObject.getString("applied"));
                                    update();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                        Toast.makeText(ModificarActividadActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                    }
                }) {    //this is the part, that adds the header to the request
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Token " + token.getToken());
                        System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                        return params;
                    }
                };
                queue.add(newFarmRequest);
            }
        });
    }

    private String estadoArbol(String act){
        if(act.equals("true")){
            return "Hecho";
        }else{
            return "Pendiente";
        }
    }


    private void update(){
        listaEstados.clear();
        listaIds.clear();
        RequestQueue queue = Volley.newRequestQueue(ModificarActividadActivity.this);
        String url="http://10.0.2.2:8000/app/";
        url=url+tipo+"_trees/?"+tipo+"="+idActividad;
        System.out.println("XXXXXXXXXXXXXXXXX URL ES "+url);
        JsonArrayRequest newFarmRequest = new JsonArrayRequest(Request.Method.GET,
                url/*TODO: cambiar a URL real para producción!!!!*/, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("progressList", response.toString());
                        try {
                            for (int j = 0; j < response.length(); j++) {
                                JSONObject activityObject = response.getJSONObject(j);
                                listaIds.add(activityObject.getString("tree"));
                                if(activityObject.getString("applied").equals("true")){
                                    listaEstados.add("true");
                                }else{
                                    listaEstados.add("false");
                                }
                            }
                            if(!listaEstados.isEmpty()){
                                //SE LLENA LA LISTA
                                dataModels.clear();
                                for(int i=0;i<listaIds.size();i++){
                                    dataModels.add(new ResumenArbolSeleccionDataModel("Número de árbol: "+listaIds.get(i).toString(),"Estado: "+estadoArbol(listaEstados.get(i).toString()),""));
                                }
                                adapter= new ResumenArbolesSeleccionAdapter(dataModels,getApplicationContext());
                                listView.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("usersAPI", "Error en la invocación a la API " + error.getCause());
                Toast.makeText(ModificarActividadActivity.this, "Se presentó un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
            }
        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                System.out.println("XXXXXXXXX EL TOKEN ES " + token.getToken());
                return params;
            }
        };
        queue.add(newFarmRequest);

    }


    /*private boolean validateForm() {
        boolean valid = true;
        if(listaArbolesSeleccionados==null){
            valid=false;
        }
        int selectedItemOfMySpinner = spinnerTipo.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinnerTipo.getItemAtPosition(selectedItemOfMySpinner);
        if (actualPositionOfMySpinner.equals("Seleccione el tipo de actividad...")) {
            setSpinnerError(spinnerTipo);
            valid = false;
        } else {
            int selectedItemOfMySubSpinner = spinnerSubtipo.getSelectedItemPosition();
            String actualPositionOfMySpinnerSub = (String) spinnerSubtipo.getItemAtPosition(selectedItemOfMySubSpinner);
            if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de poda...")) {
                setSpinnerError(spinnerSubtipo);
                valid = false;
            }
            if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de fumigación...")) {
                setSpinnerError(spinnerSubtipo);
                valid = false;
            }
            if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de fertilización...")) {
                setSpinnerError(spinnerSubtipo);
                valid = false;
            }
            if (actualPositionOfMySpinnerSub.equals("Seleccione el tipo de riego...")) {
                setSpinnerError(spinnerSubtipo);
                valid = false;
            }
        }
        if (TextUtils.isEmpty(txtFechaInicio.getText().toString())) {
            txtFechaInicio.setError("Requerido");
            valid = false;
        } else {
            String divide = txtFechaInicio.getText().toString();
            String[] separated = divide.split(" ");
            String aux = separated[3];
            String[] data = aux.split("/");
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 23, 59);
            if (cal.compareTo(cal2) > 0) {
                txtFechaInicio.setError("La fecha debe ser la actual o posterior a la actual");
                valid = false;
            } else {
                txtFechaInicio.setError(null);
            }
        }
        if (TextUtils.isEmpty(txtFechaFin.getText().toString())) {
            txtFechaFin.setError("Requerido");
            valid = false;
        } else {
            String divide = txtFechaInicio.getText().toString();
            String separated[] = divide.split(" ");
            String aux = separated[3];
            String data[] = aux.split("/");

            String divide2 = txtFechaFin.getText().toString();
            String separated2[] = divide2.split(" ");
            String aux2 = separated2[3];
            String data2[] = aux2.split("/");

            Calendar cal = Calendar.getInstance();
            cal.getTime();
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 23, 59);

            Calendar cal3 = Calendar.getInstance();
            cal3.set(Integer.parseInt(data2[2]), Integer.parseInt(data2[1]) - 1, Integer.parseInt(data2[0]), 23, 59);
            cal2.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]), 00, 00);
            if (cal3.compareTo(cal2) < 0) {
                txtFechaFin.setError("La fecha debe ser igual o superior a la actual");
                valid = false;
            } else {
                txtFechaFin.setError(null);
            }
        }

        return valid;
    }*/
}
