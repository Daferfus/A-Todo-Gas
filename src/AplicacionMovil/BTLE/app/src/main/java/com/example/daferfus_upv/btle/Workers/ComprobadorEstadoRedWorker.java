package com.example.daferfus_upv.btle.Workers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.daferfus_upv.btle.Activities.MainActivity;
import com.example.daferfus_upv.btle.BD.Lectura;
import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.BD.VolleySingleton;
import com.example.daferfus_upv.btle.ConstantesAplicacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ID_MAGNITUD;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.MOMENTO;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.UBICACION;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.VALOR;

public class ComprobadorEstadoRedWorker extends Worker {
    private Context contexto;
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

                // ...se cogen todas las lecturas no sincronizadas...
                ArrayList<Lectura> lecturasNoSincronizadas = mDBHelper.getLecurasNoSincronizadas();
                for (int i = 0; i<=lecturasNoSincronizadas.size()-1; i++){
                    guardarLectura(lecturasNoSincronizadas.get(i).getMomento(),
                            lecturasNoSincronizadas.get(i).getUbicacion(),
                            lecturasNoSincronizadas.get(i).getValor(),
                            lecturasNoSincronizadas.get(i).getIdMagnitud());
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
    private void guardarLectura(final String momento, final String ubicacion, final int valor, final String idMagnitud) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantesAplicacion.URL_GUARDADO_LECTURAS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.getBoolean("error")) {
                            // Se actualiza el estado en SqLite...
                            boolean actualizado = mDBHelper.actualizarEstadoDeSincronizacionLectura(momento, ubicacion, ConstantesAplicacion.LECTURA_SINCRONIZADA_CON_SERVIDOR);
                            if (actualizado){
                                Log.d("Base de Datos", "Dato sincronizado con servidor");
                            }
                            else{
                                Log.d("Base de Datos", "Dato no sincronizado con servidor");
                            }
                        } // if()
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } // try()
                },
                error -> {
                    Log.d("ComprobarEstadoRed", "No se ha podido contactar con el servidor:" + error);
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("momento", momento);
                params.put("ubicacion", ubicacion);
                params.put("valor", String.valueOf(valor));
                params.put("idMagnitud", idMagnitud);
                return params;
            }
        };

        VolleySingleton.tomarInstancia(contexto).anyadirAColaPeticiones(stringRequest);
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------