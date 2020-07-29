package com.example.examen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import Models.ObjectEmployee;
import Models.ObjectServices;
import Utils.Extensions;

public class LoginActivity extends AppCompatActivity {

    private final int REQUEST_CODE_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);

        File fileTest = new File("/data/data/com.example.examen/Examen/employees_data.json");
        boolean exist = fileTest.exists();

    }






    public void DownloadFiles(String urlDownload){
        try {
            URL u = new URL(urlDownload);
            InputStream is = u.openStream();
            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length=0;

            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + "data/employes.zip"));
            while ((length = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }


    public void Acceder(View view) {
        Intent evento = new Intent(this, Colaboradores.class);
        startActivity(evento);
    }

    public void ValidateFile(){

        //Validar que el archivo no este en el telefono, si ya esta, vamos a la siguiente pantalla
        //if(fileexist)

        RequestQueue queue = Volley.newRequestQueue (this);
        String url = "https://dl.dropboxusercontent.com/s/5u21281sca8gj94/getFile.json?dl=0";

        StringRequest stringRequest = new StringRequest (Request.Method.GET, url,
                new  Response.Listener <String> () {
            @Override
            public void onResponse (String response) {
                final ObjectServices objectservices = new Gson().fromJson(response,ObjectServices.class);

                if(objectservices != null && objectservices.getSuccess()){
                    File gameDir = new File("/data/data/" + LoginActivity.this.getPackageName() + "/Examen");
                    if(!gameDir.exists()){
                        boolean success = gameDir.mkdirs();
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                downloadZipFile(objectservices.getData().getFile(), "/data/data/" + LoginActivity.this.getPackageName() + "/Examen/employes.zip");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
                else{
                    Toast.makeText(LoginActivity.this, getString(R.string.ErrorConsumo), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this, "Error al consumir el servicio: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    //region Permisos

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (REQUEST_CODE_STORAGE == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ValidateFile();
            } else {
                finish();
            }
        }  else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkPermissionStorage() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
            } else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                ValidateFile();
            }
        }
        return;
    }

    //endregion

    //region ZIP
    public void downloadZipFile(String urlStr, String destinationFilePath) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d("downloadZipFile", "Server ResponseCode=" + connection.getResponseCode() + " ResponseMessage=" + connection.getResponseMessage());
            }

            // download the file
            input = connection.getInputStream();

            Log.d("downloadZipFile", "destinationFilePath=" + destinationFilePath);
            new File(destinationFilePath).createNewFile();
            output = new FileOutputStream(destinationFilePath);

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                if (output != null) output.close();
                if (input != null) input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (connection != null) connection.disconnect();
        }

        File f = new File(destinationFilePath);

        Log.d("downloadZipFile", "f.getParentFile().getPath()=" + f.getParentFile().getPath());
        Log.d("downloadZipFile", "f.getName()=" + f.getName().replace(".zip", ""));
        unpackZip(destinationFilePath);
    }
    public boolean unpackZip(String filePath) {
        InputStream is;
        ZipInputStream zis;
        try {

            File zipfile = new File(filePath);
            String parentFolder = zipfile.getParentFile().getPath();
            String filename;

            is = new FileInputStream(filePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File(parentFolder + "/" + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(parentFolder + "/" + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //endregion


}