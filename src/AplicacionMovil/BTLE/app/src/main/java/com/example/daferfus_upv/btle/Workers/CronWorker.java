package com.example.daferfus_upv.btle.Workers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.Utilidades.LecturaCallback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CronWorker extends Worker {
    public Logica mDBHelper;
    public SQLiteDatabase mDb;
    public Context contexto;
    public CronWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        // Se inicializa la base de datos...
        mDBHelper = new Logica(getApplicationContext());

        // ...la actualiza...
        try {
            mDBHelper.actualizarBaseDatos();
        } catch (IOException mIOException) {
            throw new Error("Incapaz de Actualizar Base de Datos");
        }

        // ...y coge su referencia para escritura.
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            Log.d("Base de Datos: ", mSQLException.toString());
            throw mSQLException;
        }
        contexto = getApplicationContext();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("CronWorker", "Enviando Medición");
        DateTimeFormatter dtfDia = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime nowDia = LocalDateTime.now();

        String dia =  dtfDia.format(nowDia);

        DateTimeFormatter dtfHora = DateTimeFormatter.ofPattern("HH");
        LocalDateTime nowHora = LocalDateTime.now();

        String hora =  dtfHora.format(nowHora);
        Log.d("CronWorker", hora);
        mDBHelper.consultaLecturaEstacion(dia,hora, "38.9688889 - -0.1902778", new LecturaCallback() {
            @Override
            public void hacerScrapping() {
                Log.d("CronWorker", "Empieza Scrapping");
                if(!hora.equals("00")){
                    WorkRequest scrappingWorkRequest =
                            new OneTimeWorkRequest.Builder(ScrappingWorker.class)
                                    .build();
                    WorkManager
                            .getInstance()
                            .enqueue(scrappingWorkRequest);
                }
            }

            @Override
            public void crearCopiaDeSeguridad() {

            }

            @Override
            public void cogerDesviacion(String desviacion) {

            }

            @Override
            public void actualizarDesviacion(String valorLecturaEstacion, String valorLectura) {

            }

            @Override
            public void onFailure() {
                Log.d("CronWorker", "Problema al llegar al servidor");
            }
        });
        return Result.success();
    }
}
