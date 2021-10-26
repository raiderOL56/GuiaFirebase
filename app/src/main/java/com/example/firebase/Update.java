package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity {

    // Elementos UI
    private EditText update_eTXTnombre, update_eTXTapellidoP, update_eTXTapellidoM, update_eTXTedad, update_eTXTpassword, update_eTXTpasswordNew;
    private Spinner update_SpinnerGenero;
    private Switch update_SWpassword, update_SWgenero;
    private Button update_BTNupdate;

    // FIREBASE
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mAuth.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        update_eTXTnombre = findViewById(R.id.update_eTXTnombre);
        update_eTXTapellidoP = findViewById(R.id.update_eTXTapellidoP);
        update_eTXTapellidoM = findViewById(R.id.update_eTXTapellidoM);
        update_eTXTedad = findViewById(R.id.update_eTXTedad);
        update_SpinnerGenero = findViewById(R.id.update_SpinnerGenero); AdapterSpinner();
        update_SWgenero = findViewById(R.id.update_SWgenero);
        update_SWpassword = findViewById(R.id.update_SWpassword);
        update_eTXTpassword = findViewById(R.id.update_eTXTpassword);
        update_eTXTpasswordNew = findViewById(R.id.update_eTXTpasswordNew);
        update_BTNupdate = findViewById(R.id.update_BTNupdate);

        update_BTNupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = update_eTXTnombre.getText().toString().trim()
                        , apellidoP = update_eTXTapellidoP.getText().toString().trim()
                        , apellidoM = update_eTXTapellidoM.getText().toString().trim()
                        , edad = update_eTXTedad.getText().toString().trim()
                        , password = update_eTXTpassword.getText().toString().trim()
                        , passwordNew = update_eTXTpasswordNew.getText().toString().trim();

                // TODO : Aquí me quedé. Terminé de validar todos los campos que estén llenos para hacer los cambios correspondientes. Update COMPLETO
                if(SwitchPassword(v) == false && SwitchGenero(v) == false && nombre.isEmpty() && apellidoP.isEmpty() && apellidoM.isEmpty() && edad.isEmpty()) {
                    System.out.println("Haz una modificación");
                } else { // Algún campo está modificado
                    if ((SwitchPassword(v) == true && SwitchGenero(v) == false) && (nombre.isEmpty() && apellidoP.isEmpty() && apellidoM.isEmpty() && edad.isEmpty())) {
                        // Actualizar contraseña
                        UpdatePassword(password, passwordNew);
                    }

                    if (((SwitchPassword(v) == true && SwitchGenero(v) == false) && (!nombre.isEmpty() || !apellidoP.isEmpty() || !apellidoM.isEmpty() || !edad.isEmpty()))
                            || (SwitchPassword(v) == true && SwitchGenero(v) == true)) {
                        // Actualizar información
                        UpdateChildren(nombre, apellidoP, apellidoM, edad);

                        // Actualizar contraseña
                        UpdatePassword(password, passwordNew);
                    }

                    if (((SwitchPassword(v) == false && SwitchGenero(v) == true) && (!nombre.isEmpty() || !apellidoP.isEmpty() || !apellidoM.isEmpty() || !edad.isEmpty()))
                            || ((SwitchPassword(v) == false && SwitchGenero(v) == true) && (nombre.isEmpty() && apellidoP.isEmpty() && apellidoM.isEmpty() && edad.isEmpty()))
                            ||((SwitchPassword(v) == false && SwitchGenero(v) == false) && (!nombre.isEmpty() || !apellidoP.isEmpty() || !apellidoM.isEmpty() || !edad.isEmpty()))) {
                        Toast.makeText(Update.this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show();

                        // Actualizar información
                        UpdateChildren(nombre, apellidoP, apellidoM, edad);

                        // Activity principal
                        startActivity(new Intent(Update.this, Principal.class));
                        finish();
                    }
                }
            }
        });
    }

//****************************** MÉTODOS ******************************
    // MÉTODO para llenar Spinner
    public void AdapterSpinner(){
        String[] spinner_opc = {"Hombre", "Mujer"};
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, R.layout.spinner_custom, spinner_opc);
        update_SpinnerGenero.setAdapter(spinner_adapter);
    }
    // FIN MÉTODO para llenar Spinner

    // VALIDAR SI SWgenero ESTÁ ACTIVADO O DESACTIVADO
    public boolean SwitchGenero(View v) {
        if (update_SWgenero.isChecked()) { // Activado
            update_SpinnerGenero.setVisibility(View.VISIBLE);
            return true;
        } else { // Desactivado
            update_SpinnerGenero.setVisibility(View.GONE);
            return false;
        }
    }
    // FIN VALIDAR SI SWgenero ESTÁ ACTIVADO O DESACTIVADO

    // VALIDAR SI SWpassword ESTÁ ACTIVADO O DESACTIVADO
    public boolean SwitchPassword(View view){
        if (update_SWpassword.isChecked()) { // Activado
            update_eTXTpassword.setVisibility(View.VISIBLE);
            update_eTXTpassword.setText("");
            update_eTXTpasswordNew.setVisibility(View.VISIBLE);
            update_eTXTpasswordNew.setText("");
            return true;
        } else { // Desactivado
            update_eTXTpassword.setVisibility(View.GONE);
            update_eTXTpasswordNew.setVisibility(View.GONE);
            return false;
        }
    }
    // FIN VALIDAR SI SWpassword ESTÁ ACTIVADO O DESACTIVADO

    // MÉTODO PARA ACTUALIZAR LOS DATOS EN LA BD
    public void UpdateChildren(String nombre, String apellidoP, String apellidoM, String edad){
        Map<String, Object> mapUpdateChildren = new HashMap<>();
        // Nombre
        if (!nombre.isEmpty()) {
            mapUpdateChildren.put("nombre", nombre);
        }
        // ApellidoP
        if (!apellidoP.isEmpty()) {
            mapUpdateChildren.put("apellidoP", apellidoP);
        }
        // ApellidoM
        if (!apellidoM.isEmpty()) {
            mapUpdateChildren.put("apellidoM",apellidoM);
        }
        // Edad
        if (!edad.isEmpty()) {
            mapUpdateChildren.put("edad", edad);
        }
        // Genero
        View view = null;
        if (SwitchGenero(view) == true) {
            mapUpdateChildren.put("genero", update_SpinnerGenero.getSelectedItem().toString());
        }

        mDatabase.updateChildren(mapUpdateChildren).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Update.this, "Algo salió mal. No se pudo actualizar la información", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // FIN MÉTODO PARA ACTUALIZAR LOS DATOS EN LA BD

    // MÉTODO PARA CAMBIAR CONTRASEÑA
    public void UpdatePassword(String password, String passwordNew){
        // Obtener email para validarlo en la reautenticación
        mDatabase.child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Comprobar que exista el email del usuario
                if (snapshot.exists()) { // Se obtuvo el email
                    String email = snapshot.getValue().toString();
                    // Crear credencial con email y password
                    AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);

                    // Realizar la autenticación para validar que el email y password son correctos
                    mAuth.getCurrentUser().reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Comprobar si el email y password son correctos
                            if (task.isSuccessful()) { // Email y password correctos
                                // Actualizar contraseña
                                mAuth.getCurrentUser().updatePassword(passwordNew);

                                Toast.makeText(Update.this, "Contraseña actualizada correctamente.", Toast.LENGTH_SHORT).show();

                                // Cerrar sesión
                                mAuth.signOut();
                                startActivity(new Intent(Update.this, SignUp.class));
                                finish();
                            } else { // Email o password incorrectos
                                update_eTXTpassword.setError("Contraseña incorrecta.");
                                update_eTXTpassword.setText("");
                                update_eTXTpasswordNew.setText("");
                            }
                            // FIN Comprobar si el email y password son correctos
                        }
                    });
                    // FIN Realizar la autenticación para validar que el email y password son correctos
                } else { // No se obtuvo el email
                    Toast.makeText(Update.this, "Lo sentimos, no pudimos verificar tu cuenta. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                }
                // FIN Comprobar que exista el email del usuario
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // FIN Obtener email para validarlo en la reautenticación
    }
    // FIN MÉTODO PARA CAMBIAR CONTRASEÑA
//****************************** FIN MÉTODOS ******************************
}