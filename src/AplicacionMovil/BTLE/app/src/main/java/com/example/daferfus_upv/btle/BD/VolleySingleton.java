// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.BD;

// ------------------------------------------------------------------
// ------------------------------------------------------------------
import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
// ------------------------------------------------------------------
// ------------------------------------------------------------------
public class VolleySingleton {
    @SuppressLint("StaticFieldLeak")
    private static VolleySingleton mInstancia;
    private RequestQueue mColaPeticion;
    @SuppressLint("StaticFieldLeak")
    private static Context mContexto;

    // --------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context
    //
    // Invocado desde: getInstance()
    // Función: Inicializa y configura el Singleton de petición a servidor.
    // --------------------------------------------------------------
    private VolleySingleton(Context context) {
        mContexto = context;
        mColaPeticion = tomarColaPeticiones();
    } // ()

    // --------------------------------------------------------------
    //                  -> VolleySingleton
    //                  tomarInstancia() <-
    //                  <- Context
    //
    // Invocado desde: LecturasDbHelper
    //                 ComprobadorEstadoRed
    // Función: Devuelve una instancia del Singleton.
    // --------------------------------------------------------------
    public static synchronized VolleySingleton tomarInstancia(Context context) {
        if (mInstancia == null) {
            mInstancia = new VolleySingleton(context);
        } // if()
        return mInstancia;
    } // ()

    // --------------------------------------------------------------
    //                  -> RequestQueue
    //                  tomarColaPeticiones() <-
    //                  <- Context
    //
    // Invocado desde:  constructor()
    //                  addToRequestQueue()
    // Función: Recoge la cola de peticiones al servidor.
    // --------------------------------------------------------------
    public RequestQueue tomarColaPeticiones() {
        if (mColaPeticion == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mColaPeticion = Volley.newRequestQueue(mContexto.getApplicationContext());
        }
        return mColaPeticion;
    } // ()

    // --------------------------------------------------------------
    //                  anyadirAColaPeticiones() <-
    //                  <- Request<T>
    //
    // Invocado desde:  constructor()
    //                  addToRequestQueue()
    // Función: Recoge la cola de peticiones al servidor.
    // --------------------------------------------------------------
    public <T> void anyadirAColaPeticiones(Request<T> req) {
        tomarColaPeticiones().add(req);
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------