package com.example.adrian.foldersreslayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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

    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_prueba);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
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


        swipeRefreshLayout.setColorSchemeResources(R.color.windowBackground);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                obtenerListadoNotificaciones();
            }
        });
    }

    public void obtenerListadoNotificaciones() {
        String REQUEST_TAG = "MAINACTIVITY.OBTENERLISTADONOTIFICACIONES";
        if (Utilerias.isOnline(getApplicationContext())) {

            /*progressDialog = new ProgressDialog(ListadoPrueba.this);
            progressDialog.setMessage(getResources().getString(R.string.strProcesando));
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dialog));
            progressDialog.show();*/
            swipeRefreshLayout.setRefreshing(true);

            final JSONObject localJSONObject = new JSONObject();
            try {//creamos el objeto con toda la informacion del dispositivo
                localJSONObject.put(getApplicationContext().getResources().getString(R.string.strIdentificador), Utilerias.getIdentificador(getApplicationContext()));

                StringRequest stringRequest = new StringRequest(Request.Method.POST, getApplicationContext().getResources().getString(R.string.strUrlListadoNotificaciones),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    /*if(progressDialog.isShowing())
                                        progressDialog.dismiss();*/
                                    if (swipeRefreshLayout.isRefreshing())
                                        swipeRefreshLayout.setRefreshing(false);
                                    JSONObject object = new JSONObject(response);

                                    if (!object.getJSONObject("meta").getBoolean("isValid")) {
                                        Snackbar.make(coordinatorLayout,object.getJSONObject("meta").getString("mensaje"),Snackbar.LENGTH_LONG).show();
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
                                Snackbar.make(coordinatorLayout,error.getMessage(),Snackbar.LENGTH_LONG).show();

                                if (swipeRefreshLayout.isRefreshing())
                                    swipeRefreshLayout.setRefreshing(false);
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
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(coordinatorLayout,localJSONException.getMessage(),Snackbar.LENGTH_LONG).show();
            }
        }else{
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(coordinatorLayout,getResources().getString(R.string.strNoCuentaConInternet),Snackbar.LENGTH_LONG).show();
        }
    }
}
