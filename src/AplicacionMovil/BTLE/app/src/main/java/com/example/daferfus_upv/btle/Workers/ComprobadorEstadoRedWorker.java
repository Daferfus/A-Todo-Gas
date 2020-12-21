package com.example.daferfus_upv.btle.Workers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.daferfus_upv.btle.BD.Lectura;
import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.BD.VolleySingleton;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.Utilidades.LecturaCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.daferfus_upv.btle.ConstantesAplicacion.ID_USUARIO;


public class ComprobadorEstadoRedWorker extends Worker {
    private final Context contexto;
    public Logica mDBHelper;
    public SQLiteDatabase mDb;
    public ComprobadorEstadoRedWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        Log.d("ComprobadorEstadoRedWorker", "Comprobando red");

        ConnectivityManager gestionadorConexion = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = gestionadorConexion.getActiveNetworkInfo();

        // Si hay una red...
        if (activeNetwork != null) {
            Log.d("ComprobadorEstadoRedWorker", "Hay red");
            // ...y si se está conectado por WiFi o por datos...
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                String idUsuario = ID_USUARIO;
                // ...se cogen todas las lecturas no sincronizadas...
                ArrayList<Lectura> lecturasNoSincronizadas = mDBHelper.getLecurasNoSincronizadas(idUsuario);
                for (int i = 0; i<=lecturasNoSincronizadas.size()-1; i++){
                    Lectura lectura = new Lectura(lecturasNoSincronizadas.get(i).getDia(),
                            lecturasNoSincronizadas.get(i).getHora(),
                            lecturasNoSincronizadas.get(i).getUbicacion(),
                            lecturasNoSincronizadas.get(i).getValor(),
                            lecturasNoSincronizadas.get(i).getIdMagnitud(),
                            lecturasNoSincronizadas.get(i).getIdUsuario(),
                            lecturasNoSincronizadas.get(i).getEstadoSincronizacionServidor());
                    guardarLectura(lectura);
                }
                Log.d("ComprobadorEstadoRedWorker", "No hay datos");
            } // if()
        } // if()
        Log.d("ComprobadorEstadoRedWorker", "No hay red. Volviendo a intentar.");
        return Result.success();
    } // ()


    // ---------------------------------------------------------------------------------------------
    //                  guardarLectura() ->
    //                  <- 3 Textos, N
    //
    // Invocado desde: MainActivity::insertarLectura()
    // Función: Guarda los datos no sincronizados a MySQL.
    // ---------------------------------------------------------------------------------------------
    private void guardarLectura(Lectura lectura) {
        Log.d("ComprobadorEstadoRedWorker", "Lectura es " + lectura.toString());
        mDBHelper.guardarLecturaEnServidor(lectura, new LecturaCallback() {
            @Override
            public void hacerScrapping() {

            }

            @Override
            public void crearCopiaDeSeguridad() {
                mDBHelper.borrarLecturasSincronizadas();
            }

            @Override
            public void cogerDesviacion(String desviacion) {

            }

            @Override
            public void actualizarDesviacion(String valorLecturaEstacion, String valorLectura) {

            }

            @Override
            public void onFailure() {
            }
        });
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------