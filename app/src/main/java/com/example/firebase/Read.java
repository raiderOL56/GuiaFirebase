package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Read extends AppCompatActivity {

    // Elementos UI
    private TextView read_TXTnombre, read_TXTapellidoP, read_TXTapellidoM, read_TXTedad, read_TXTgenero, read_TXTemail;

    // FIREBASE
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        read_TXTnombre = findViewById(R.id.read_TXTnombre);
        read_TXTapellidoP = findViewById(R.id.read_TXTapellidoP);
        read_TXTapellidoM = findViewById(R.id.read_TXTapellidoM);
        read_TXTedad = findViewById(R.id.read_TXTedad);
        read_TXTgenero = findViewById(R.id.read_TXTgenero);
        read_TXTemail = findViewById(R.id.read_TXTemail);

        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    read_TXTnombre.setText(snapshot.child(mAuth.getUid()).child("nombre").getValue().toString());
                    read_TXTapellidoP.setText(snapshot.child(mAuth.getUid()).child("apellidoP").getValue().toString());
                    read_TXTapellidoM.setText(snapshot.child(mAuth.getUid()).child("apellidoM").getValue().toString());
                    read_TXTedad.setText(snapshot.child(mAuth.getUid()).child("edad").getValue().toString());
                    read_TXTgenero.setText(snapshot.child(mAuth.getUid()).child("genero").getValue().toString());
                    read_TXTemail.setText(mAuth.getCurrentUser().getEmail());
                } else {
                    Toast.makeText(Read.this, "No existe ese usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}