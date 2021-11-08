package com.example.firebase.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.R;

import java.util.List;

// 2.- Extender nuestro adaptador ViewHolder
public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolder> {
    private List<User> mData;
    private LayoutInflater mInflater;
    private Context context;

    public AdaptadorRV(List<User> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    // 3.- Implementar métodos

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_myfriends_rv, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User usuario = mData.get(position);
        holder.misAmigos_TXTvNombre.setText(usuario.getNombre() + " " + usuario.getApellidoP() + " " + usuario.getApellidoM());
        holder.misAmigos_TXTvEmail.setText(usuario.getEmail());
        holder.misAmigos_TXTvEdad.setText(usuario.getEdad());
        holder.misAmigos_TXTvGenero.setText(usuario.getGenero());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 1.- Crear clase ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView misAmigos_TXTvNombre, misAmigos_TXTvEmail, misAmigos_TXTvEdad, misAmigos_TXTvGenero;

        public ViewHolder(View itemView){
            super(itemView);
            misAmigos_TXTvNombre = itemView.findViewById(R.id.misAmigos_TXTvNombre);
            misAmigos_TXTvEmail = itemView.findViewById(R.id.misAmigos_TXTvEmail);
            misAmigos_TXTvEdad = itemView.findViewById(R.id.misAmigos_TXTvEdad);
            misAmigos_TXTvGenero = itemView.findViewById(R.id.misAmigos_TXTvGenero);
        }
    }
}
