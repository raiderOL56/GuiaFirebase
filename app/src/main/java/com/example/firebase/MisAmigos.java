package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView misAmigos_RV;

    List<User> elements = new ArrayList<>();
    AdaptadorRV adaptadorRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_amigos);

        misAmigos_RV = findViewById(R.id.misAmigos_RV);
        misAmigos_RV.setLayoutManager(new LinearLayoutManager(this));
        adaptadorRV = new AdaptadorRV(elements, this);
        misAmigos_RV.setAdapter(adaptadorRV);

        // Obtener valores de sus amigos
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Limpiar lista para evitar que se dupliquen los datos
                    elements.clear();
                    // elements.removeAll(elements); /*Esto elimina los datos de la lista para evitar que se dupliquen*/
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        // Crear objeto que obtenga todos los datos de los amigos
                        User usuario = dataSnapshot.getValue(User.class);
                        // Agregar objeto a la lista
                        elements.add(usuario);
                        // TODO: Ya se muestran los nombres en el RecyclerView. MisAmigos COMPLETO.
                    }
                    // Actualizar adaptador para ver los cambios en el RecyclerView
                    adaptadorRV.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}