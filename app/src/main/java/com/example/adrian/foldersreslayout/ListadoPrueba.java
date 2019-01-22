package com.example.adrian.foldersreslayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adrian.foldersreslayout.clases.SimpleDividerItemDecoration;
import com.example.adrian.foldersreslayout.clases.rvAdapterNotificaciones;
import com.example.adrian.foldersreslayout.utilerias.Utilerias;
import com.example.adrian.foldersreslayout.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListadoPrueba extends AppCompatActivity {
    private RecyclerView rvNotificaciones;
    private ArrayList<JSONObject> lstNotificaciones = new ArrayList<>();
    private rvAdapterNotificaciones oRvAdapterNotificaciones;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_prueba);

        rvNotificaciones = findViewById(R.id.rvNotificaciones);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvNotificaciones.setLayoutManager(layoutManager);

        rvNotificaciones.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        oRvAdapterNotificaciones = new rvAdapterNotificaciones(lstNotificaciones, new rvAdapterNotificaciones.OnItemClickListener() {
            @Override public void onItemClick(JSONObject item, int position) {
            }
        });

        rvNotificaciones.setAdapter(oRvAdapterNotificaciones);

        obtenerListadoNotificaciones();
    }

    public void obtenerListadoNotificaciones() {
        String REQUEST_TAG = "MAINACTIVITY.OBTENERLISTADONOTIFICACIONES";
        if (Utilerias.isOnline(getApplicationContext())) {

            progressDialog = new ProgressDialog(ListadoPrueba.this);
            progressDialog.setMessage(getResources().getString(R.string.strProcesando));
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dialog));
            progressDialog.show();

            final JSONObject localJSONObject = new JSONObject();
            try {//creamos el objeto con toda la informacion del dispositivo
                localJSONObject.put(getApplicationContext().getResources().getString(R.string.strIdentificador), Utilerias.getIdentificador(getApplicationContext()));

                StringRequest stringRequest = new StringRequest(Request.Method.POST, getApplicationContext().getResources().getString(R.string.strUrlListadoNotificaciones),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    JSONObject object = new JSONObject(response);

                                    if (!object.getJSONObject("meta").getBoolean("isValid")) {
                                        lstNotificaciones.clear();
                                        oRvAdapterNotificaciones.notifyDataSetChanged();
                                    }
                                    else {
                                        lstNotificaciones.clear();
                                        JSONArray notificaciones = object.getJSONObject("data").getJSONArray("notificaciones");
                                        for (int i=0;i<notificaciones.length();i++){
                                            lstNotificaciones.add(notificaciones.getJSONObject(i));
                                        }
                                        oRvAdapterNotificaciones.notifyDataSetChanged();
                                    }
                                } catch (Exception ex) {
                                    Log.i("error", ex.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("error", "errorResponse al listar las notificaciones.");
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        try {
                            params.put(getApplicationContext().getResources().getString(R.string.strIdentificador), localJSONObject.getString(getApplicationContext().getResources().getString(R.string.strIdentificador)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return params;
                    }
                };
                // Adding JsonObject request to request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, REQUEST_TAG);
            } catch (JSONException localJSONException) {
                Log.i("error", localJSONException.getMessage());
                if(progressDialog.isShowing())
                    progressDialog.dismiss();

            }
        }else{
        }
    }
}
