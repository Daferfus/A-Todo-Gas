// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.Workers;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.daferfus_upv.btle.BD.Lectura;
import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.MyApplication;
import com.example.daferfus_upv.btle.Utilidades.TratamientoDeLecturas;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.MOMENTO;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.UBICACION;
import static com.example.daferfus_upv.btle.Workers.GeolocalizacionWorker.ubicacion;

// ------------------------------------------------------------------
// ------------------------------------------------------------------


public class MantenimientoDeMedidasWorker extends Worker {
    public Logica mDBHelper;
    public SQLiteDatabase mDb;
    public Context contexto;

    // ----------------------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context, WorkParameters
    //
    // Invocado desde: TratamientoDeLecturas::haLLegadoUnBeacon()
    // Función: Inicializa y configura la tarea de interacción con la base de datos.
    // -----------------------------------------------------------------------------
    public MantenimientoDeMedidasWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        int valor = TratamientoDeLecturas.valor;

        Log.d("BDWorker", "Enviando Medición");
        String idMagnitud = "SO2";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String momento =  dtf.format(now);
        int estadoSincronizacionServidor = 1;

        boolean datoExistente = mDBHelper.getLectura(momento, ubicacion);

            if(!TextUtils.isEmpty(ubicacion) && !datoExistente){
                mDBHelper.guardarLecturaEnServidor(new Lectura(momento, ubicacion, valor, idMagnitud, estadoSincronizacionServidor), MyApplication.getAppContext());
            }
            else{
                Log.d("BDWorker", "Ubicación nula");
            }

        mDBHelper.borrarLecturasSincronizadas();
        return Result.success();
    } // ()

} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
