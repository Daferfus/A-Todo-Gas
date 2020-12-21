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

import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.DIA;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ESTADO_SINCRONIZACION_SERVIDOR;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.HORA;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ID_MAGNITUD;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ID_USUARIO;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.UBICACION;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.VALOR;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class Lectura {
        private final String dia;
        private final String hora;
        private final String ubicacion;
        private final int valor;
        private final String idMagnitud;
        private final String idUsuario;
        private final int estadoSincronizacionServidor;
        // --------------------------------------------------------------
        //                  constructor() <-
        //                  <- 3 Textos, 2N
        //
        // Invocado desde: MainActivity
        // Función: Crea un objeto Lectura para su subida a la base de datos.
        // --------------------------------------------------------------
        public Lectura(String dia, String hora,
                       String ubicacion, int valor,
                       String idMagnitud, String idUsuario, int estadoSincronizacionServidor) {
            this.dia = dia;
            this.hora = hora;
            this.ubicacion = ubicacion;
            this.valor = valor;
            this.idMagnitud = idMagnitud;
            this.idUsuario = idUsuario;
            this.estadoSincronizacionServidor = estadoSincronizacionServidor;
        }

        // --------------------------------------------------------------
        // Getters/Setters
        // --------------------------------------------------------------
        public String getDia() {
            return dia;
        }

        public String getHora() {
        return hora;
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

        public String getIdUsuario() { return idUsuario; }

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
        values.put(DIA, dia);
        values.put(HORA, hora);
        values.put(UBICACION, ubicacion);
        values.put(VALOR, valor);
        values.put(ID_MAGNITUD, idMagnitud);
        values.put(ID_USUARIO, idUsuario);
        values.put(ESTADO_SINCRONIZACION_SERVIDOR, estadoSincronizacionServidor);
        Log.d("Base de Datos: ", values.toString());
        return values;
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------