// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.BD;

// ------------------------------------------------------------------
// ------------------------------------------------------------------
import android.provider.BaseColumns;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class LecturasContract {
    public static abstract class LecturasEntry implements BaseColumns {
        public static final String TABLE_NAME ="Lecturas";

        public static final String MOMENTO = "momento";
        public static final String UBICACION = "ubicacion";
        public static final String VALOR = "valor";
        public static final String ID_MAGNITUD = "idMagnitud";
        public static final String ESTADO_SINCRONIZACION_SERVIDOR = "estadoSincronizacionServidor";
    } // abstract class
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------