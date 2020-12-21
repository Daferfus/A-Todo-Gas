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
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.daferfus_upv.btle.BD.Lectura;
import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.Utilidades.LecturaCallback;
import com.example.daferfus_upv.btle.Utilidades.TratamientoDeLecturas;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.daferfus_upv.btle.ConstantesAplicacion.DESVIACION;
import static com.example.daferfus_upv.btle.ConstantesAplicacion.ID_USUARIO;
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

        DateTimeFormatter dtfDia = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime nowDia = LocalDateTime.now();

        String dia =  dtfDia.format(nowDia);

        DateTimeFormatter dtfHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime nowHora = LocalDateTime.now();

        String hora =  dtfHora.format(nowHora);
        Log.d("Hora es", hora);
        int estadoSincronizacionServidor = 1;

        Log.d("BDWorker", "Ubicacion es " + ubicacion);
        boolean datoExistente = mDBHelper.getLectura(dia, hora, ubicacion);

            if(!TextUtils.isEmpty(ubicacion) && !datoExistente){
                Log.d("MantenimientoDeMedidasWorker", "La desviacion es" + DESVIACION);
                Lectura lectura = new Lectura(dia, hora, ubicacion, valor+DESVIACION, idMagnitud, ID_USUARIO, estadoSincronizacionServidor);
                mDBHelper.guardarLecturaEnServidor(lectura, new LecturaCallback() {
                    @Override
                    public void hacerScrapping() {

                    }

                    @Override
                    public void crearCopiaDeSeguridad() {
                        Log.d("MantenimientoDeMedidasWorker", "Creando copia de seguridad");
                        mDBHelper.guardarLecturaEnLocal(lectura,1);
                    }

                    @Override
                    public void cogerDesviacion(String desviacion) {

                    }

                    @Override
                    public void actualizarDesviacion(String valorLecturaEstacion, String valorLectura) {

                    }

                    @Override
                    public void onFailure() {
                        mDBHelper.guardarLecturaEnLocal(lectura,0);
                    }
                });
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
