package com.example.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Utils.Extensions;

public class AgregarColab extends AppCompatActivity {
EditText txtName;
EditText txtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_colab);
        txtName=findViewById(R.id.txtName);
        txtEmail=findViewById(R.id.txtEmail);

    }
    public void eventoAgregar(View view){

        if(txtName.getText().toString().isEmpty() || !Extensions.isEmailValid(txtEmail.getText().toString())){
                Toast.makeText(getApplicationContext(), "Favor de validar los datos",Toast.LENGTH_LONG).show();
        return;
        }
            Toast.makeText(getApplicationContext(),
                    "Colaborador Agregado", Toast.LENGTH_LONG).show();

        finish();
    }
}