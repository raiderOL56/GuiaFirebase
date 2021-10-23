package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    // Elementos UI
    private EditText logIn_eTXTemail, logIn_eTXTpassword;
    private Button logIn_BTNlogIn, logIn_BTNsignUp;

    // Firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logIn_eTXTemail = findViewById(R.id.logIn_eTXTemail);
        logIn_eTXTpassword = findViewById(R.id.logIn_eTXTpassword);
        logIn_BTNlogIn = findViewById(R.id.logIn_BTNlogIn);
        logIn_BTNsignUp = findViewById(R.id.logIn_BTNsignUp);

        // EVENTO del BTNlogIn
        logIn_BTNlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = logIn_eTXTemail.getText().toString().trim(), password = logIn_eTXTpassword.getText().toString().trim();

                // Validar que todos los campos estén completos
                if (!email.isEmpty() && !password.isEmpty()) { // Todos los campos están completos
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LogIn.this, Principal.class));
                                Toast.makeText(LogIn.this, "Sesión iniciada con éxito", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(LogIn.this, "Algo salió mal. Intenta de nuevo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else { // Algún campo está incompleto
                    if (email.isEmpty()) {
                    logIn_eTXTemail.setError("Completa este campo");
                    }
                    if (password.isEmpty()) {
                        logIn_eTXTpassword.setError("Completa este campo");
                    }
                }
            }
        });
        // FIN EVENTO del BTNlogIn

        // EVENTO del BTNsignUp
        logIn_BTNsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });
        // FIN EVENTO del BTNsignUp
    }
}