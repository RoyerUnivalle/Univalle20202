package com.example.univalle20202;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnDelegate, btnInterface, btnIniciar;
    EditText etUserName, etPasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // visualización en pantalla.
        Toast.makeText(this,"onCreate",Toast.LENGTH_LONG).show();
        // Enlazamiento
        btnDelegate  = findViewById(R.id.btnDelegate);
        btnInterface = findViewById(R.id.btnInterface);
        btnIniciar = findViewById(R.id.btnIniciar);

        etUserName = findViewById(R.id.etUserName);
        etPasswd = findViewById(R.id.etPasswd);

        btnDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hola Boton delegado",Toast.LENGTH_LONG).show();
            }
        });
        btnInterface.setOnClickListener(this);
        btnIniciar.setOnClickListener(this);
    }
    public void saludarEnLinea(View l){ // obligatoriamente debe recibir un obj de la clase VIew,porque lo llamo desde la UI
        Toast.makeText(this,"Hola Botonen linea",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btnInterface:
                Toast.makeText(getApplicationContext(),"Hola Boton Interface",Toast.LENGTH_LONG).show();
                break;
            case  R.id.btnIniciar:
                Intent ir = new Intent(this,Home.class);
                Bundle data = new Bundle();
                data.putString("userName",etUserName.getText().toString());
                data.putString("passwd",etPasswd.getText().toString());
                ir.putExtras(data);
                ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ir);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"OnStart",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this,"onStop",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"onDestroy",Toast.LENGTH_LONG).show();
    }
}