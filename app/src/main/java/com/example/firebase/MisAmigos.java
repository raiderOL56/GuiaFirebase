package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.model.AdaptadorRV;
import com.example.firebase.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MisAmigos extends AppCompatActivity {

    private TextView misAmigos_TXTvNingunAmigo;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView misAmigos_RV;

    List<User> elements = new ArrayList<>();
    AdaptadorRV adaptadorRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_amigos);

        misAmigos_TXTvNingunAmigo = findViewById(R.id.misAmigos_TXTvNingunAmigo);
        misAmigos_RV = findViewById(R.id.misAmigos_RV);
        misAmigos_RV.setLayoutManager(new LinearLayoutManager(this));
        adaptadorRV = new AdaptadorRV(elements, this);
        misAmigos_RV.setAdapter(adaptadorRV);

        // Obtener valores de sus amigos
        mDatabase.child(mAuth.getUid()).child("misAmigos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    misAmigos_RV.setVisibility(View.VISIBLE);
                    misAmigos_TXTvNingunAmigo.setVisibility(View.GONE);
                    // Limpiar lista para evitar que se dupliquen los datos
                    elements.clear();
                    // elements.removeAll(elements); /*Esto elimina los datos de la lista para evitar que se dupliquen*/
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        // Crear objeto que obtenga todos los datos de los amigos
                        User usuario = dataSnapshot.getValue(User.class);
                        // Agregar objeto a la lista
                        elements.add(usuario);
                    }
                    // Actualizar adaptador para ver los cambios en el RecyclerView
                    adaptadorRV.notifyDataSetChanged();
                } else {
                    misAmigos_TXTvNingunAmigo.setVisibility(View.VISIBLE);
                    misAmigos_RV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}