package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.firebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    // ELEMENTOS UI
    private EditText signUp_eTXTnombre, signUp_eTXTapellidoP, signUp_eTXTapellidoM, signUp_eTXTedad, signUp_eTXTemail, signUp_eTXTpassword;
    private Spinner signUp_SpinnerGenero;
    private Button signUp_BTNregistrarme, signUp_BTNlogIn;

    // FIREBASE AUTH
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // FIREBASE DATABASE
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // TODO: BORRAR loadingBar
    private ProgressDialog loadingBar;

//****************************** ON CREATE ************gi******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // TODO: BORRAR loadingBar
        loadingBar = new ProgressDialog(this);

        signUp_eTXTnombre = findViewById(R.id.signUp_eTXTnombre);
        signUp_eTXTapellidoP = findViewById(R.id.signUp_eTXTapellidoP);
        signUp_eTXTapellidoM = findViewById(R.id.signUp_eTXTapellidoM);
        signUp_eTXTedad = findViewById(R.id.signUp_eTXTedad);
        signUp_SpinnerGenero = findViewById(R.id.signUp_SpinnerGenero); adapterSpinner();
        signUp_eTXTemail = findViewById(R.id.signUp_eTXTemail);
        signUp_eTXTpassword = findViewById(R.id.signUp_eTXTpassword);
        signUp_BTNregistrarme = findViewById(R.id.signUp_BTNregistrarme);
        signUp_BTNlogIn = findViewById(R.id.signUp_BTNlogIn);

        // EVENTO del BTNregistrarme
        signUp_BTNregistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recolectar datos de los eTXT
                String nombre = signUp_eTXTnombre.getText().toString().trim()
                        , apellidoP = signUp_eTXTapellidoP.getText().toString().trim()
                        , apellidoM = signUp_eTXTapellidoM.getText().toString().trim()
                        , edad = signUp_eTXTedad.getText().toString().trim()
                        , genero = signUp_SpinnerGenero.getSelectedItem().toString()
                        , email = signUp_eTXTemail.getText().toString().trim()
                        , password = signUp_eTXTpassword.getText().toString().trim();
                // FIN Recolectar datos de los eTXT

                // Validar que todos los campos estén completos
                if (!nombre.isEmpty() && !apellidoP.isEmpty() && !apellidoM.isEmpty() && !edad.isEmpty() && !email.isEmpty() && !password.isEmpty()) { // Todos los campos están completos
                    // Crear cuenta
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Validar que se haya realizado correctamente el sign up
                            if (task.isSuccessful()) { // Sign up correcto
                                // Registrar datos en la BD
                                User datosUsuario = new User(nombre, apellidoP, apellidoM, edad, genero, email);
                                mDatabase.child("Usuarios").child(mAuth.getUid()).setValue(datosUsuario);
                                // FIN Registrar datos en la BD

                                Toast.makeText(SignUp.this, "Has sido registrado exisosamente", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(SignUp.this, Principal.class));
                                finish();
                            } else { // Sign up incorrecto
                                Toast.makeText(SignUp.this, "Ha ocurrido un error1. Por favor vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
                            }
                            // FIN Validar que se haya realizado correctamente el sign up
                        }
                    });
                    // FIN Crear cuenta
                } else{ // Algún campo está incompleto
                    // Nombre
                    if (nombre.isEmpty()) {
                        signUp_eTXTnombre.setError("Completa este campo");
                        signUp_eTXTnombre.setText("");
                    }
                    // ApellidoP
                    if (apellidoP.isEmpty()) {
                        signUp_eTXTapellidoP.setError("Completa este campo");
                        signUp_eTXTapellidoP.setText("");
                    }
                    // ApellidoM
                    if(apellidoM.isEmpty()) {
                        signUp_eTXTapellidoM.setError("Completa este campo");
                        signUp_eTXTapellidoM.setText("");
                    }
                    // Edad
                    if (edad.isEmpty()) {
                        signUp_eTXTedad.setError("Completa este campo");
                        signUp_eTXTedad.setText("");
                    }
                    // Email
                    if (email.isEmpty()) {
                        signUp_eTXTemail.setError("Completa este campo");
                        signUp_eTXTemail.setText("");
                    }
                    // Password
                    if (password.isEmpty()) {
                        signUp_eTXTpassword.setError("Completa este campo");
                        signUp_eTXTpassword.setText("");
                    }
                }
                // FIN Validar que todos los campos estén completos
            }
        });
        // FIN EVENTO del BTNregistrarme

        // EVENTO del BTNlogIn
        signUp_BTNlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });
        // FINs EVENTO del BTNlogIn
    }
//****************************** FIN ON CREATE ******************************

//****************************** ON START ******************************
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currenUser = mAuth.getCurrentUser();
        if (currenUser != null) {
            startActivity(new Intent(SignUp.this, Principal.class));
            finish();
        }
    }
//****************************** FIN ON START ******************************

//****************************** MÉTODOS ******************************
    // MÉTODO para llenar Spinner
    public void adapterSpinner(){
        String[] spinner_opc = {"Hombre", "Mujer"};
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, R.layout.spinner_custom, spinner_opc);
        signUp_SpinnerGenero.setAdapter(spinner_adapter);
    }
    // FIN MÉTODO para llenar Spinner
//****************************** FIN MÉTODOS ******************************
}