package com.example.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import Models.ObjectEmployee;
import Utils.Extensions;

public class miscolab extends AppCompatActivity {
    private ObjectEmployee objectEmployees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscolab);
        objectEmployees = ObtenerColaboradores();
    }

    public ObjectEmployee ObtenerColaboradores(){

        // Leemos el archivo: /data/data/com.example.examen/Examen/employees_data.json
        String path = "/data/data/com.example.examen/Examen/employees_data.json";
        File validateEmployee = new File(path);

        if(!validateEmployee.exists()){
            Extensions.ShowMessage(miscolab.this, "Ocurrio un error");

        }

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        return gson.fromJson(bufferedReader, ObjectEmployee.class);
    }
}