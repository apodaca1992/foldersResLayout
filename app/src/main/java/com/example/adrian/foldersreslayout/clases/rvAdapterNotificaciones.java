package com.example.adrian.foldersreslayout.clases;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrian.foldersreslayout.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class rvAdapterNotificaciones extends RecyclerView.Adapter<rvAdapterNotificaciones.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(JSONObject item,int position);
    }

    private final OnItemClickListener listener;
    private ArrayList<JSONObject> oNotificaciones;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo,fechaEnviado,mensaje;
        public ImageView imageViewVisto;

        public ViewHolder(View view) {
            super(view);
            titulo = view.findViewById(R.id.txtTitulo);
            fechaEnviado = view.findViewById(R.id.txtFechaEnviado);
            mensaje = view.findViewById(R.id.txtMensaje);
            imageViewVisto = view.findViewById(R.id.imageViewVisto);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public rvAdapterNotificaciones(ArrayList<JSONObject> prNotificaciones, OnItemClickListener listener) {
        this.oNotificaciones = prNotificaciones;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public rvAdapterNotificaciones.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final JSONObject oItem = oNotificaciones.get(position);
        try {
            if (oItem.getInt("visto")==1){
                holder.imageViewVisto.setVisibility(View.GONE);
            }else{
                holder.imageViewVisto.setVisibility(View.VISIBLE);
            }
            holder.titulo.setText(oItem.getString("titulo"));
            holder.fechaEnviado.setText(oItem.getString("fechaEnviado"));
            holder.mensaje.setText(oItem.getString("mensaje"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(oItem,position);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return oNotificaciones.size();
    }

    public void actualizarVistoNotificacion(int position,JSONObject item){
        oNotificaciones.set(position,item);
    }
}
