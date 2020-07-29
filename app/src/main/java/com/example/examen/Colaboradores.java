package com.example.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Colaboradores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colaboradores);
    }
    public void eventomis(View view){
        Intent mis = new Intent(this, miscolab.class);
        startActivity(mis);

    }
    public void eventoAgregarColab(View view){
        Intent agregarColab = new Intent(this, AgregarColab.class);
        startActivity(agregarColab);
    }
}