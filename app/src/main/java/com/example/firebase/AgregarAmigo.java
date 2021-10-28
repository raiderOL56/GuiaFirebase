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

    private TextView addFriend_TXTnombre, addFriend_TXTapellidoP, addFriend_TXTapellidoM, addFriend_TXTedad, addFriend_TXTgenero;
    private LinearLayout addFriend_LLapellidos, addFriend_LLedadgenero;
    private EditText addFriend_eTXTemail;
    private Button addFriend_BTNsearch, addFriend_BTNadd, addFriend_BTNmyFriends;

    // FIREBASE
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_amigo);

        addFriend_TXTnombre = findViewById(R.id.addFriend_TXTnombre);
        addFriend_LLapellidos = findViewById(R.id.addFriend_LLapellidos);
        addFriend_TXTapellidoP = findViewById(R.id.addFriend_TXTapellidoP);
        addFriend_TXTapellidoM = findViewById(R.id.addFriend_TXTapellidoM);
        addFriend_LLedadgenero = findViewById(R.id.addFriend_LLedadgenero);
        addFriend_TXTedad = findViewById(R.id.addFriend_TXTedad);
        addFriend_TXTgenero = findViewById(R.id.addFriend_TXTgenero);
        addFriend_eTXTemail = findViewById(R.id.addFriend_eTXTemail);
        addFriend_BTNsearch = findViewById(R.id.addFriend_BTNsearch);
        addFriend_BTNadd = findViewById(R.id.addFriend_BTNadd);
        addFriend_BTNmyFriends = findViewById(R.id.addFriend_BTNmyFriends);

        addFriend_eTXTemail.setText("1");

        // EVENTO para BTNbuscar
        addFriend_BTNsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = addFriend_eTXTemail.getText().toString().trim();

                // Validar que el campo esté completo
                if (email.isEmpty()) { // Campo vacío
                    addFriend_eTXTemail.setError("Completa este campo");
                } else { // Campo completo
                    // Buscar email en Usuarios
                    Query query = mDatabase.orderByChild("email").equalTo("2@pruebas.com").limitToFirst(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Verificar si existeo no
                            if (snapshot.exists()) { // Si existe
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                                    System.out.println("snapshot: " + snapshot);
//                                    System.out.println("dataSnapshot: " + dataSnapshot);
//                                    System.out.println("dataSnapshotKey: " + dataSnapshot.getKey());
//                                    System.out.println("snapshotKey: " + snapshot.getKey());

                                    // Buscar la información del usuario encontrado
                                    mDatabase.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            // TODO: 1.- Buscar una solución para que el usuario pueda volver a buscar a otro usuario después de haber encontrado a uno.
                                            // Llenar TXT
                                            addFriend_TXTnombre.setVisibility(View.VISIBLE);
                                            addFriend_TXTnombre.setText(snapshot.child("nombre").getValue().toString());
                                            addFriend_TXTapellidoP.setText(snapshot.child("apellidoP").getValue().toString());
                                            addFriend_TXTapellidoM.setText(snapshot.child("apellidoM").getValue().toString());
                                            addFriend_TXTedad.setText(snapshot.child("edad").getValue().toString());
                                            addFriend_TXTgenero.setText(snapshot.child("genero").getValue().toString());
                                            addFriend_eTXTemail.setText(snapshot.child("email").getValue().toString());

                                            // Hacer visibles los LL
                                            addFriend_LLapellidos.setVisibility(View.VISIBLE);
                                            addFriend_LLedadgenero.setVisibility(View.VISIBLE);

                                            // Ocultar BTNsearch y mostrar BTNadd
                                            addFriend_BTNsearch.setVisibility(View.GONE);
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
                            // FIN Verificar si existeo no
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    // FIN Buscar email en Usuarios
                }
                // FIN Validar que el campo esté completo
            }
        });
        // FIN EVENTO para BTNbuscar

        // EVENTO para BTNadd
        addFriend_BTNadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // FIN EVENTO para BTNadd

        // EVENTO para BTNmyFriends
        addFriend_BTNmyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AgregarAmigo.this, MisAmigos.class));
            }
        });
        // FIN EVENTO para BTNmyFriends
    }
}