package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Principal extends AppCompatActivity {

    // Elementos UI
    private Button principal_BTNshow, principal_BTNupdate, principal_BTNaddFriend, principal_BTNsignOut;

    // FIREBASE
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        principal_BTNshow = findViewById(R.id.principal_BTNshow);
        principal_BTNupdate = findViewById(R.id.principal_BTNupdate);
        principal_BTNaddFriend = findViewById(R.id.principal_BTNaddFriend);
        principal_BTNsignOut = findViewById(R.id.principal_BTNsignOut);

        principal_BTNshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, Read.class));
            }
        });

        principal_BTNupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, Update.class));
            }
        });

        principal_BTNaddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, AgregarAmigo.class));
            }
        });

        principal_BTNsignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Principal.this, SignUp.class));
                finish();
            }
        });
    }
}