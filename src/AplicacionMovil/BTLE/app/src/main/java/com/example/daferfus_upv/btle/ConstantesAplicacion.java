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
    public static final String IP = "http://10.236.13.250/";
    public static final String URL_GUARDADO_LECTURAS = IP + "Web/Backend/lecturasREST.php";
    public static final String URL_CONSULTA_MEDIA = IP + "Web/Backend/consultar_media.php";
    public static final String URL_CONSULTA_DISTANCIA = IP + "Web/Backend/consultar_distanciaRecorrida.php";
    public static final String URL_GUARDADO_DISTANCIA = IP + "Web/Backend/insertar_pasosYDistancia.php";
    public static final String URL_ACTUALIZAR_DISTANCIA = IP + "Web/Backend/actualizar_pasos_y_distancia.php";
    public static final String URL_CONSULTA_PASOS = IP + "Web/Backend/consultar_pasosRecorridos.php";
    public static final String URL_VALIDAR_USUARIO = IP + "Web/Backend/validar_usuario.php";
    // --------------------------------------------------------------
    // Variables para consultas
    // --------------------------------------------------------------
    public static String ID_USUARIO = "invitado";
    // --------------------------------------------------------------
    // Resultados de consultas
    // --------------------------------------------------------------
    public static int MEDIA = 1;
    public static int DISTANCIA = -1;
    public static int PASOS = 0;
    // Constantes para indicar la sincronización de nuestros datos locales con respecto al servidor
    // (1 significa que está sincronizado, 0 que no lo está)
    public static final int LECTURA_SINCRONIZADA_CON_SERVIDOR = 1;
    public static final int LECTURA_NO_SINCRONIZADA_CON_SERVIDOR = 0;
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------