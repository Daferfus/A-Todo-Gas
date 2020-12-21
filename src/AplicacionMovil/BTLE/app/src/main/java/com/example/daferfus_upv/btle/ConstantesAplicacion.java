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
    public static final String URL_BASE = "http://192.168.1.75/Web/Backend/";
    public static final String URL_GUARDADO_LECTURAS = URL_BASE + "lecturasREST.php";
    public static final String URL_CONSULTA_MEDIA = URL_BASE + "consultar_media.php";
    public static final String URL_CONSULTA_DISTANCIA = URL_BASE + "consultar_distanciaRecorrida.php";
    public static final String URL_GUARDADO_DISTANCIA = URL_BASE + "insertar_pasosYDistancia.php";
    public static final String URL_ACTUALIZAR_DISTANCIA = URL_BASE + "actualizar_pasos_y_distancia.php";
    public static final String URL_CONSULTA_PASOS = URL_BASE + "consultar_pasosRecorridos.php";
    public static final String URL_VALIDAR_USUARIO = URL_BASE + "validar_usuario.php";
    public static final String URL_MAPA = "http://192.168.1.75/Web/Frontend/mapa.html";
    public static final String URL_ADMIN_USUARIOS = "http://192.168.1.75/Web/Frontend/usuarios.php";
    public static final String URL_ADMIN_SENSORES = "http://192.168.1.75/Web/Frontend/sensores.php";
    public static final String URL_MAPA_COMPARTIR = "http://192.168.1.75/Web/Frontend/mapa.html";
    public static final String URL_ACTUALIZAR_SENSOR = URL_BASE + "actualizar_sensor.php";
    public static final String URL_ACTUALIZAR_MEDIA = URL_BASE + "actualizar_media.php";
    public static final String URL_GUARDADO_MEDIA = URL_BASE + "insertarMedia.php";
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
    public static int DESVIACION = 0;
    // Constantes para indicar la sincronización de nuestros datos locales con respecto al servidor
    // (1 significa que está sincronizado, 0 que no lo está)
    public static final int LECTURA_SINCRONIZADA_CON_SERVIDOR = 1;
    public static boolean activo = true;
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------