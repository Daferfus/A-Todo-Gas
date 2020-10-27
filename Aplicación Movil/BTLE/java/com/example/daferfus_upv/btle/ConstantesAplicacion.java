// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class ConstantesAplicacion {

    // --------------------------------------------------------------
    // Peticiones de Permisos
    // --------------------------------------------------------------
    public static final int PETICION_LOCALIZACION = 1000;
    public static final int PETICION_GPS = 1001;

    // --------------------------------------------------------------
    // Servidor
    // --------------------------------------------------------------
    // URL empleado para realizar una petición POST a nuestro servidor web.
    public static final String URL_GUARDADO_LECTURAS = "http://10.236.11.47/Web/lecturasREST.php";

    // Constantes para indicar la sincronización de nuestros datos locales con respecto al servidor
    // (1 significa que está sincronizado, 0 que no lo está)
    public static final int LECTURA_SINCRONIZADA_CON_SERVIDOR = 1;
    public static final int LECTURA_NO_SINCRONIZADA_CON_SERVIDOR = 0;
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------