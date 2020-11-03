// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.BD;

// ------------------------------------------------------------------
// ------------------------------------------------------------------
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ID_MAGNITUD;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.MOMENTO;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.UBICACION;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.VALOR;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class ComprobadorEstadoRed extends BroadcastReceiver {

        private Context contexto;
        private LecturasDbHelper bd;

        @Override
        public void onReceive(Context contexto, Intent intent) {

            this.contexto = contexto;

            bd = new LecturasDbHelper(contexto);

            ConnectivityManager gestionadorConexion = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = gestionadorConexion.getActiveNetworkInfo();

            // Si hay una red...
            if (activeNetwork != null) {
                // ...y si se está conectado por WiFi o por datos...
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                    // ...se cogen todas las lecturas no sincronizadas...
                    Cursor cursor = bd.getLecurasNoSincronizadas();
                    if (cursor.moveToFirst()) {
                        do {
                            // ...se llama a la función para guardar la lectura no sincronizada a MySQL.
                            guardarLectura(
                                    cursor.getString(cursor.getColumnIndex(MOMENTO)),
                                    cursor.getString(cursor.getColumnIndex(UBICACION)),
                                    cursor.getInt(cursor.getColumnIndex(VALOR)),
                                    cursor.getString(cursor.getColumnIndex(ID_MAGNITUD))
                            );
                        } while (cursor.moveToNext()); // do while()
                    } // if()
                } // if()
            } // if()
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
                                boolean actualizado = bd.actualizarEstadoDeSincronizacionLectura(momento, ubicacion, ConstantesAplicacion.LECTURA_SINCRONIZADA_CON_SERVIDOR);
                                if (actualizado){
                                    Log.d("Base de Datos", "Dato sincronizado con servidor");
                                }
                                else{
                                    Log.d("Base de Datos", "Dato no sincronizado con servidor");
                                }
                                // ...y se envía un Broadcast para refrescar la lista
                                contexto.sendBroadcast(new Intent(MainActivity.BROADCAST_DATOS_GUARDADOS));
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