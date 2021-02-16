package com.example.univalle20202.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

    Saludar obj;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        obj = new Saludar();
        obj.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    /*for (int i=0; i<=15; i++){
        Toast.makeText(this,"Hola servicio i:"+i,Toast.LENGTH_LONG).show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
        return super.onStartCommand(intent, flags, startId);
    }

    class Saludar extends AsyncTask<Void,Integer,Void> { // Paramtros, Progreso, Resultados
        @Override
        protected Void doInBackground(Void... voids) {
            // colorear.setBackgroundColor(Color.rgb(aleatorio(),aleatorio(),aleatorio()));
            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i); // <-- se llama al método onProgressUpdate
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(),"Hola servicio i: "+values[0],Toast.LENGTH_LONG).show();
        }
    }


}
