package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AgregarAmigo extends AppCompatActivity {

    private TextView addFriend_TXTnombre, addFriend_TXTedad, addFriend_TXTgenero, addFriend_TXTemail;
    private LinearLayout addFriend_LLedadgenero;
    private EditText addFriend_eTXTemail;
    private Button addFriend_BTNsearch, addFriend_BTNadd;

    // FIREBASE
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");

//****************************** ON CREATE ******************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_amigo);

        addFriend_eTXTemail = findViewById(R.id.addFriend_eTXTemail);
        addFriend_BTNsearch = findViewById(R.id.addFriend_BTNsearch);
        addFriend_TXTnombre = findViewById(R.id.addFriend_TXTnombre);
        addFriend_LLedadgenero = findViewById(R.id.addFriend_LLedadgenero);
        addFriend_TXTedad = findViewById(R.id.addFriend_TXTedad);
        addFriend_TXTgenero = findViewById(R.id.addFriend_TXTgenero);
        addFriend_TXTemail = findViewById(R.id.addFriend_TXTemail);
        addFriend_BTNadd = findViewById(R.id.addFriend_BTNadd);

        // EVENTO para BTNbuscar
        addFriend_BTNsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = addFriend_eTXTemail.getText().toString().trim();

                // Validar que el campo esté completo
                if (email.isEmpty()) { // Campo vacío
                    addFriend_eTXTemail.setError("Completa este campo");
                } else { // Campo completo
                    // Validar que el usuario no se busque a sí mismo
                    if (email.equals(mAuth.getCurrentUser().getEmail())) {
                        Toast.makeText(AgregarAmigo.this, "Busca otra dirección de correo electrónico.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Buscar email en Usuarios
                        Query query = mDatabase.orderByChild("email").equalTo(email).limitToFirst(1);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Verificar si existe o no
                                if (snapshot.exists()) { // Si existe
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        // Buscar la información del usuario encontrado
                                        mDatabase.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                // Llenar TXT
                                                addFriend_TXTnombre.setText(snapshot.child("nombre").getValue().toString() + " " + snapshot.child("apellidoP").getValue().toString() + " " + snapshot.child("apellidoM").getValue().toString());
                                                addFriend_TXTedad.setText(snapshot.child("edad").getValue().toString() + " años");
                                                addFriend_TXTgenero.setText(snapshot.child("genero").getValue().toString());
                                                addFriend_TXTemail.setText(snapshot.child("email").getValue().toString());

                                                // Vaciar eTXT
                                                addFriend_eTXTemail.setText("");

                                                // Hacer visibles los elementos
                                                addFriend_TXTnombre.setVisibility(View.VISIBLE);
                                                addFriend_TXTemail.setVisibility(View.VISIBLE);
                                                addFriend_LLedadgenero.setVisibility(View.VISIBLE);

                                                // Ocultar BTNsearch y mostrar BTNadd
                                                addFriend_BTNadd.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        // FIN Buscar la información del usuario encontrado
                                    }
                                } else { // No existe
                                    Toast.makeText(AgregarAmigo.this, "Ese usuario no existe. Verifica la dirección.", Toast.LENGTH_SHORT).show();
                                }
                                // FIN Verificar si existe o no
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        // FIN Buscar email en Usuarios
                    }
                    // FIN Validar que el usuario no se busque a sí mismo
                }
                // FIN Validar que el campo esté completo
            }
        });
        // FIN EVENTO para BTNbuscar

        // EVENTO para BTNadd
        addFriend_BTNadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener email del usuario encontrado
                Query query = mDatabase.orderByChild("email").equalTo(addFriend_TXTemail.getText().toString()).limitToFirst(1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            // Obtener valores de mAuth
                            mDatabase.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot mySnapshot) {
                                    // Validar si ya tiene agregado a ese usuario o no
                                    if (mySnapshot.child("misAmigos").child(dataSnapshot.getKey()).exists()) { // Agregado
                                        Toast.makeText(AgregarAmigo.this, "Ese usuario ya es tu amigo.", Toast.LENGTH_SHORT).show();
                                    } else { // No agregado
                                        // Agregar misAmigos a BD de Uid
                                        mDatabase.child(mAuth.getUid()).child("misAmigos").child(dataSnapshot.getKey()).child("nombre").setValue(dataSnapshot.child("nombre").getValue());
                                        mDatabase.child(mAuth.getUid()).child("misAmigos").child(dataSnapshot.getKey()).child("apellidoP").setValue(dataSnapshot.child("apellidoP").getValue());
                                        mDatabase.child(mAuth.getUid()).child("misAmigos").child(dataSnapshot.getKey()).child("apellidoM").setValue(dataSnapshot.child("apellidoM").getValue());
                                        mDatabase.child(mAuth.getUid()).child("misAmigos").child(dataSnapshot.getKey()).child("edad").setValue(dataSnapshot.child("edad").getValue());
                                        mDatabase.child(mAuth.getUid()).child("misAmigos").child(dataSnapshot.getKey()).child("genero").setValue(dataSnapshot.child("genero").getValue());
                                        mDatabase.child(mAuth.getUid()).child("misAmigos").child(dataSnapshot.getKey()).child("email").setValue(dataSnapshot.child("email").getValue());

                                        // Agregar misAmigos a BD de Uid encontrado
                                        mDatabase.child(dataSnapshot.getKey()).child("misAmigos").child(mAuth.getUid()).child("nombre").setValue(mySnapshot.child("nombre").getValue());
                                        mDatabase.child(dataSnapshot.getKey()).child("misAmigos").child(mAuth.getUid()).child("apellidoP").setValue(mySnapshot.child("apellidoP").getValue());
                                        mDatabase.child(dataSnapshot.getKey()).child("misAmigos").child(mAuth.getUid()).child("apellidoM").setValue(mySnapshot.child("apellidoM").getValue());
                                        mDatabase.child(dataSnapshot.getKey()).child("misAmigos").child(mAuth.getUid()).child("edad").setValue(mySnapshot.child("edad").getValue());
                                        mDatabase.child(dataSnapshot.getKey()).child("misAmigos").child(mAuth.getUid()).child("genero").setValue(mySnapshot.child("genero").getValue());
                                        mDatabase.child(dataSnapshot.getKey()).child("misAmigos").child(mAuth.getUid()).child("email").setValue(mySnapshot.child("email").getValue());

                                        Toast.makeText(AgregarAmigo.this, "Usuario agregado a tu lista de amigos.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AgregarAmigo.this, Principal.class));
                                        finish();
                                    }
                                    // FIN Validar si ya tiene agregado a ese usuario o no
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            // FIN Obtener valores de mAuth
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                // FIN Obtener email del usuario encontrado
            }
        });
        // FIN EVENTO para BTNadd
    }
//****************************** FIN ON CREATE ******************************

//****************************** MÉTODOS ******************************

//****************************** FIN MÉTODOS ******************************
}