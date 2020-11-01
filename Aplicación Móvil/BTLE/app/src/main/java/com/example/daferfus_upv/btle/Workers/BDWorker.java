// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.Workers;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.daferfus_upv.btle.BD.Lectura;
import com.example.daferfus_upv.btle.BD.LecturasDbHelper;
import com.example.daferfus_upv.btle.Utilidades.TratamientoDeLecturas;

import java.io.IOException;
import java.util.Date;

// ------------------------------------------------------------------
// ------------------------------------------------------------------


public class BDWorker extends Worker {
    public LecturasDbHelper mDBHelper;
    public SQLiteDatabase mDb;
    public Context contexto;

    // ----------------------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context, WorkParameters
    //
    // Invocado desde: TratamientoDeLecturas::haLLegadoUnBeacon()
    // Función: Inicializa y configura la tarea de interacción con la base de datos.
    // -----------------------------------------------------------------------------
    public BDWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        // Se inicializa la base de datos...
        mDBHelper = new LecturasDbHelper(getApplicationContext());

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
        String ubicacion = GeolocalizacionWorker.ubicacion;
        int valor = TratamientoDeLecturas.valor;
        enviarMedicion(ubicacion, valor);

        return Result.success();
    } // ()

    // ---------------------------------------------------------------------------
    //                  enviarMedicion() <-
    //
    // Invocado desde: haLlegadoUnBeacon()
    // Función: Manda a la base de datos, datos para insertar en la tabla Lecturas.
    // ----------------------------------------------------------------------------
    public void enviarMedicion(String ubicacion, int valor) {
        Log.d("BDWorker", "Enviando Medición");
        String idMagnitud = "SO2";
        int estadoSincronizacionServidor = 1;
        mDBHelper.guardarLecturaEnServidor(new Lectura(new Date().toString(), ubicacion, valor, idMagnitud, estadoSincronizacionServidor), contexto, mDb);
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
