package com.example.univalle20202;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.univalle20202.services.MyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends AppCompatActivity{
    EditText edData, etCount;
    Button colorear;
    int contador = 0;
    int cantidadAgenda = 0;
    Colorear objColorear;
    GetHttp objConsultar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        edData = findViewById(R.id.etData); // enlazamimiento
        etCount = findViewById(R.id.etCount); // enlazamimiento
        colorear = findViewById(R.id.bntColorear); // enlazamimiento
        Bundle dataReceive = getIntent().getExtras();
        edData.setText(dataReceive.getString("userName") + " "+ dataReceive.getString("passwd"));
        // FRAGMENTOS --
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Se instancia fragment creado.
        BlankFragment fragment = new BlankFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    // MENUS - INICIO
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mHelp:
                Toast.makeText(this,"Menu Help", Toast.LENGTH_LONG).show();
                return true;
            case R.id.mLogout:
                Toast.makeText(this,"Menu Logout", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // MENU - FIN

    public void volver(View l){ // obligatoriamente debe recibir un obj de la clase VIew,porque lo llamo desde la UI
        Intent ir = new Intent(this, MainActivity.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ir);
    }
    public void contar(View r){
        contador = contador + 1;
        etCount.setText(""+contador);
    }

    public void iniciarServicio(View h){
        Intent ir = new Intent(this, MyService.class);
        startService(ir); // --> Oncreate -> OnstarComand
    }

    public void getData(View q) { // HttpUrlConecction
        objConsultar = new GetHttp();
        objConsultar.execute("https://invessoft.com/api/eventos/2");

    }

    public class GetHttp extends  AsyncTask<String, Void, Void>{
        // a diferencia de voley no trabaja con un hilo independiente por defecto
        @Override
        protected Void doInBackground(String... voids) {
         URL url = null;
            try {
                System.out.println(voids[0]);
                url = new URL(voids[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.HTTP_OK == urlConnection.getResponseCode()){
                    System.out.println("Conectado");
                    edData.setText(" ");
                    //// PARA LEER LA RESPUESTA
                    InputStream intputS = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(intputS));
                    String line = "";
                    StringBuilder responseStrBuilder = new StringBuilder();
                    while((line = br.readLine()) != null){
                        responseStrBuilder.append(line);
                    }
                    intputS.close();
                    //// PARA LEER LA RESPUESTA
                    JSONObject response = new JSONObject(responseStrBuilder.toString());
                    cantidadAgenda = response.getInt("count");
                    JSONArray  data =  response.getJSONArray("agenda");
                    System.out.println("Cantidad agenda: "+cantidadAgenda + " tamaño array: "+data.length());
                    edData.setText("Cantidad agenda: "+cantidadAgenda + " tamaño array: "+data.length());
                }else {
                    System.out.println("Error");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
            return null;
        }
    }

    public void iniviarVolley(View q){
        //EL CREA SU PROPIO HILO -> GET POST PUT UPDATE INDEX
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://invessoft.com/api/eventos/2";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                             cantidadAgenda = response.getInt("count");
                            JSONArray  data =  response.getJSONArray("agenda");
                            edData.setText("Response is: " +cantidadAgenda + " cantidad "+ data.length() );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                edData.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public int aleatorio(){
        return (int) (Math.random()* 255) + 1;
    }

    public void colorear(View r) throws InterruptedException {
        // Opción 1 :(
        /*for (int i = 0; i < 10; i++) {
            colorear.setBackgroundColor(Color.rgb(aleatorio(),aleatorio(),aleatorio()));
            Thread.sleep(1000);// el duerme pero en el hilo actual que es el que esta asociado con UI
        }*/
        // Opción 2 :) Hilo independiente enlinea
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        colorear.setBackgroundColor(Color.rgb(aleatorio(),aleatorio(),aleatorio()));
                        // colorear.setText("Colorear: " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            public int aleatorio(){
                return (int) (Math.random()* 255) + 1;
            }
        }).start();*/
        //Opción 3 por medio de Asytask
        objColorear = new Colorear();
        objColorear.execute();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("contador",contador);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contador = savedInstanceState.getInt("contador");
    }


    class Colorear extends AsyncTask<Void,Void,Void>{ // Paramtros, Progreso, Resultados
        @Override
        protected Void doInBackground(Void... voids) {
            // colorear.setBackgroundColor(Color.rgb(aleatorio(),aleatorio(),aleatorio()));
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(); // <-- se llama al método onProgressUpdate
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            colorear.setBackgroundColor(Color.rgb(aleatorio(),aleatorio(),aleatorio()));
        }

        public int aleatorio(){
            return (int) (Math.random()* 255) + 1;
        }
    }
}