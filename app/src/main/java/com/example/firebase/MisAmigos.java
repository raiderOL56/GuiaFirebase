package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.firebase.model.AdaptadorRV;
import com.example.firebase.model.User;

import java.util.ArrayList;
import java.util.List;

public class MisAmigos extends AppCompatActivity {

    List<User> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_amigos);

        Init();
    }

    public void Init(){
        elements = new ArrayList<>();
        elements.add(new User("Nombre1", "ApellidoP1", "ApellidoM1", "12 años", "Hombre", "prueba1@pruebas.com"));

        AdaptadorRV adaptadorRV = new AdaptadorRV(elements, this);
        RecyclerView recyclerView = findViewById(R.id.misAmigos_RV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptadorRV);


    }
}