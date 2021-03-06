package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Principal extends AppCompatActivity {

    // Elementos UI
    private Button principal_BTNshow, principal_BTNupdate, principal_BTNmyFriends, principal_BTNaddFriends, principal_BTNsignOut, principal_BTNdeleteAccount;

    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

//****************************** ON CREATE ******************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        principal_BTNshow = findViewById(R.id.principal_BTNshow);
        principal_BTNupdate = findViewById(R.id.principal_BTNupdate);
        principal_BTNmyFriends = findViewById(R.id.principal_BTNmyFriends);
        principal_BTNaddFriends = findViewById(R.id.principal_BTNaddFriends);
        principal_BTNsignOut = findViewById(R.id.principal_BTNsignOut);
        principal_BTNdeleteAccount = findViewById(R.id.principal_BTNdeleteAccount);

        // EVENTO BTNshow
        principal_BTNshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, Read.class));
            }
        });

        // EVENTO BTNupdate
        principal_BTNupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, Update.class));
            }
        });

        // EVENTO BTNmyFriends
        principal_BTNmyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, MisAmigos.class));
            }
        });

        // EVENTO BTNaddFriends
        principal_BTNaddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, AgregarAmigo.class));
            }
        });

        // EVENTO BTNsignOut
        principal_BTNsignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Principal.this, SignUp.class));
                finish();
            }
        });

        // EVENTO BTNdeleteAccount
        principal_BTNdeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);

                LayoutInflater inflater = getLayoutInflater();

                View view = inflater.inflate(R.layout.dialog_password, null);

                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);

                // Instanciar elementos de dialog_password
                EditText dialogPass_eTXTpassword = view.findViewById(R.id.dialogPass_eTXTpassword);
                TextView dialogPass_TXTverificar = view.findViewById(R.id.dialogPass_TXTverificar), dialogPass_TXTcancelar = view.findViewById(R.id.dialogPass_TXTcancelar);

                // EVENTO al presionar Verificar
                dialogPass_TXTverificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Guardar contrase??a del eTXT
                        String password = dialogPass_eTXTpassword.getText().toString().trim();

                        // Validar que eTXT est?? lleno
                        if (password.isEmpty()) { // No est?? lleno
                            dialogPass_eTXTpassword.setError("Completa este campo");
                        } else { // Est?? lleno
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mAuth.getUid());
                            AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getEmail(), password);

                            // Realizar reautenticaci??n para verificar password
                            mAuth.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // Validar si se hizo bien o no la reautenticaci??n
                                            if (task.isSuccessful()) { // Reautenticaci??n v??lida
                                                // Eliminar cuenta
                                                mAuth.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                // Validar que se haya eliminado la cuenta correctamente
                                                                if (task.isSuccessful()) { // Se elimin?? la cuenta
                                                                    Toast.makeText(Principal.this, "Lamentamos que te vayas.", Toast.LENGTH_SHORT).show();

                                                                    // Borrar datos de la BD
                                                                    mDatabase.removeValue();

                                                                    // Ir a SignUp
                                                                    startActivity(new Intent(Principal.this, SignUp.class));
                                                                    finish();
                                                                } else { // No se elimin?? la cuenta
                                                                    Toast.makeText(Principal.this, "Algo sali?? mal. Int??ntalo de nuevo.", Toast.LENGTH_SHORT).show();
                                                                }
                                                                // FIN Validar que se haya eliminado la cuenta correctamente
                                                            }
                                                        });
                                                // FIN Eliminar cuenta
                                            } else { // Reautenticaci??n inv??lida
                                                Toast.makeText(Principal.this, "Contrase??a incorrecta", Toast.LENGTH_SHORT).show();
                                                dialogPass_eTXTpassword.setText("");
                                            }
                                            // FIN Validar si se hizo bien o no la reautenticaci??n
                                        }
                                    });
                            // FIN Realizar reautenticaci??n para verificar password
                        }
                        // FIN Validar que eTXT est?? lleno
                    }
                });
                // FIN EVENTO al presionar Verificar

                // EVENTO al presionar Cancelar
                dialogPass_TXTcancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                // FIN EVENTO al presionar Cancelar
            }
        });
        // FIN EVENTO BTNdeleteAccount
    }
//FIN ****************************** ON CREATE ******************************

//****************************** M??TODOS ******************************

//****************************** FIN M??TODOS ******************************
}