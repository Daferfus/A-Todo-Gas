// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.BD;

// ------------------------------------------------------------------
// ------------------------------------------------------------------
import android.content.ContentValues;
import android.util.Log;

import java.util.Date;

import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ESTADO_SINCRONIZACION_SERVIDOR;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ID_MAGNITUD;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.MOMENTO;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.UBICACION;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.VALOR;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class Lectura {
        private final String momento;
        private final String ubicacion;
        private final int valor;
        private final String idMagnitud;
        private final int estadoSincronizacionServidor;

        // --------------------------------------------------------------
        //                  constructor() <-
        //                  <- 3 Textos, 2N
        //
        // Invocado desde: MainActivity
        // Función: Crea un objeto Lectura para su subida a la base de datos.
        // --------------------------------------------------------------
        public Lectura(String momento,
                       String ubicacion, int valor,
                       String idMagnitud, int estadoSincronizacionServidor) {
            this.momento = momento;
            this.ubicacion = ubicacion;
            this.valor = valor;
            this.idMagnitud = idMagnitud;
            this.estadoSincronizacionServidor = estadoSincronizacionServidor;
        }

        // --------------------------------------------------------------
        // Getters/Setters
        // --------------------------------------------------------------
        public String getMomento() {
            return momento;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public int getValor() {
            return valor;
        }

        public String getIdMagnitud() {
            return idMagnitud;
        }

        public int getEstadoSincronizacionServidor() {
        return estadoSincronizacionServidor;
    }
        // ------------------------------------------------------------------
        // ------------------------------------------------------------------

    // --------------------------------------------------------------
    //                  -> ContentValues
    //                  toContentValues() <-
    //                  <- Lectura, N
    //
    // Invocado desde: LecturasDbHelper:insertarLectura()
    // Función: Crea un objeto Lectura para su subida a la base de datos.
    // --------------------------------------------------------------
    public ContentValues toContentValues(int estadoSincronizacionServidor) {
        ContentValues values = new ContentValues();
        values.put(MOMENTO, new Date().toString());
        values.put(UBICACION, ubicacion);
        values.put(VALOR, valor);
        values.put(ID_MAGNITUD, idMagnitud);
        values.put(ESTADO_SINCRONIZACION_SERVIDOR, estadoSincronizacionServidor);
        Log.d("Base de Datos: ", values.toString());
        return values;
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------