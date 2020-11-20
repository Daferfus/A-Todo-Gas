// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.BD;

// ------------------------------------------------------------------
// ------------------------------------------------------------------
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.daferfus_upv.btle.ConstantesAplicacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ESTADO_SINCRONIZACION_SERVIDOR;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.MOMENTO;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.TABLE_NAME;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.UBICACION;

// ------------------------------------------------------------------
// ------------------------------------------------------------------


public class LecturasDbHelper extends SQLiteOpenHelper {


    // ------------------------------------------------------------------
    // Caracteristicas de la Base de Datos
    // ------------------------------------------------------------------
    private static final String NOMBRE_BD = "Lecturas.db";
    private static String RUTA_BD = "";
    private static final int VERSION_BD = 1;

    // ------------------------------------------------------------------
    // Configuración de la Base de Datos
    // ------------------------------------------------------------------
    private SQLiteDatabase mDataBase;
    private final Context mContexto;
    private boolean mNecesitaActualizar = false;


    // --------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context
    //
    // Invocado desde: MainActivity
    // Función: Inicializa y configura la base de datos.
    // --------------------------------------------------------------
    public LecturasDbHelper(Context contexto) {
        super(contexto, NOMBRE_BD, null, VERSION_BD);

        // Se coge la ruta dentro del móvil donde se copiara la base datos...
        RUTA_BD = contexto.getApplicationInfo().dataDir + "/databases/";
        Log.d("Base de Datos: ", RUTA_BD);
        this.mContexto = contexto;

        // ...se manda a copiarla desde assets...
        mandarACopiarBaseDatos();

        // ...y se la hace legible mediante consulta.
        this.getReadableDatabase();
    } // ()


    // ----------------------------------------------------------------------------------------
    //                  actualizarBaseDatos() ->
    //
    // Invocado desde: MainActivity
    // Función: Actualiza la base de datos, volviéndola a copiar de la carpeta assets al móvil.
    // ----------------------------------------------------------------------------------------
    public void actualizarBaseDatos() throws IOException {
        // Si necesita ser actualizada...
        if (mNecesitaActualizar) {
            // ...se crea un objeto File con la ruta de la base de datos para así comprobar
            // si ya existe...
            File archivoBD = new File(RUTA_BD + NOMBRE_BD);
            // ...y en caso de que exista...
            if (archivoBD.exists())
                // ...se borra...
                archivoBD.delete(); // if()

            // ...y se manda a copiar la base de datos de la carpeta assets al móvil.
            mandarACopiarBaseDatos();

            // ...y se deja constancia de que ya no hace falta actualizar.
            mNecesitaActualizar = false;
        } // if()
    } // ()


    // ----------------------------------------------------------------------------------------
    //                  -> Buleano
    //                  comprobarBaseDatos() <-
    //
    // Invocado desde: mandarACopiarBaseDatos()
    // Función: Crea un objeto File con la ruta de la base de datos para así comprobar si ya existe.
    // ----------------------------------------------------------------------------------------
    private boolean comprobarBaseDatos() {

        File dbFile = new File(RUTA_BD + NOMBRE_BD);
        return dbFile.exists();
    } // ()


    // ----------------------------------------------------------------------------------------
    //                  mandarACopiarBaseDatos() <-
    //
    // Invocado desde: constructor()
    //                 actualizarBaseDatos()
    // Función: Comprueba que la base de datos exista en el directorio "databases,
    //          y en caso contrario la manda a copiar de assets.
    // ----------------------------------------------------------------------------------------
    private void mandarACopiarBaseDatos() {
        if (!comprobarBaseDatos()) {
            this.getReadableDatabase();
            this.close();
            try {
                copiarArchivoBD();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        } // if()
    } // ()

    // ----------------------------------------------------------------------------------------
    //                  copiarArchivoBD() ->
    //
    // Invocado desde: copiarBaseDatos()
    // Función: Copia la base de datos de la carpeta assets a un directorio del móvil.
    // ----------------------------------------------------------------------------------------
    private void copiarArchivoBD() throws IOException {
        InputStream mFlujoEntrada = mContexto.getAssets().open(NOMBRE_BD);
        //InputStream mFlujoEntrada = mContexto.getResources().openRawResource(R.raw.info);
        OutputStream mFlujoSalida = new FileOutputStream(RUTA_BD + NOMBRE_BD);
        byte[] mBuffer = new byte[1024];
        int mTamanyo;
        while ((mTamanyo = mFlujoEntrada.read(mBuffer)) > 0){
            mFlujoSalida.write(mBuffer, 0, mTamanyo);
        } // while()
        mFlujoSalida.flush();
        mFlujoSalida.close();
        mFlujoEntrada.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    } // onCreate()

    // ----------------------------------------------------------------------------------------
    //                  onUpgrade() <-
    //                  <- SQLiteDatabase, 2N
    //
    // Invocado desde: Ningún sitio. Solo está aquí porque puede resultar útil más adelante.
    // Función: Compara la versión de la base de datos con otra anteriormente almacenada y
    //          en caso de que sea más nueva, la actualiza.
    // ----------------------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase bd, int viejaVersion, int nuevaVersion) {
        if (nuevaVersion > viejaVersion)
            mNecesitaActualizar = true;
    } // ()


    // ----------------------------------------------------------------------------------------
    //                  guardarLecturaEnLocal() ->
    //                  <- SQLiteDatabase, Lecturas
    //
    // Invocado desde: guardarLecturaEnServidor()
    // Función: Inserta una lectura del sensor en la tabla Lecturas en la base de datos local.
    // ----------------------------------------------------------------------------------------
    public long guardarLecturaEnLocal(SQLiteDatabase bd, Lectura lectura, int estadoSincronizacionBaseDatos) {
        Log.d("Estado", "El dato está: " + estadoSincronizacionBaseDatos);
        return bd.insert(
                TABLE_NAME,
                null,
                lectura.toContentValues(estadoSincronizacionBaseDatos));
    } // ()


    // ---------------------------------------------------------------------------------------------
    //                  guardarLecturaEnServidor() ->
    //                  <- Lectura, Context, SQLiteDatabase
    //
    // Invocado desde: MainActivity::enviarMedicion()
    // Función: Inserta una lectura del sensor en la tabla Lecturas en la base de datos del servidor.
    // ---------------------------------------------------------------------------------------------
    public void guardarLecturaEnServidor(Lectura lectura, Context contexto, SQLiteDatabase db) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantesAplicacion.URL_GUARDADO_LECTURAS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        // En caso de tener éxito...
                        if (!obj.getBoolean("error")) {
                            // ...almacena la lectura en SQLite con estado sincronizado.
                            long numeroLectura = guardarLecturaEnLocal(db, lectura, ConstantesAplicacion.LECTURA_SINCRONIZADA_CON_SERVIDOR);
                            Log.d("LecturasDbHelper", "Lectura número " + numeroLectura + " almacenada");
                            // En caso de error...
                        } else {
                            // ...guarda la lectura en SQLite con estado no sincronizado.
                            Log.d("LecturasDbHelper", "Error devuelto por el servidor: " + obj.getBoolean("error"));
                            long numeroLectura = guardarLecturaEnLocal(db, lectura, ConstantesAplicacion.LECTURA_NO_SINCRONIZADA_CON_SERVIDOR);
                            Log.d("LecturasDbHelper", "Lectura número " + numeroLectura + " almacenada");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                // En caso de no recibir respuesta del servidor almacenar la lectura en SQLite
                // con estado no sincronizado.
                error -> {
                    Log.d("LecturasDbHelper", "No se ha podido contactar con el servidor:" + error);
                    long numeroLectura = guardarLecturaEnLocal(db, lectura, ConstantesAplicacion.LECTURA_NO_SINCRONIZADA_CON_SERVIDOR);
                    Log.d("LecturasDbHelper", "Lectura número " + numeroLectura + " almacenada");
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("momento", lectura.getMomento());
                params.put("ubicacion", lectura.getUbicacion());
                params.put("valor", String.valueOf(lectura.getValor()));
                params.put("idMagnitud", lectura.getIdMagnitud());
                return params;
            }
        };
        VolleySingleton.tomarInstancia(contexto).anyadirAColaPeticiones(stringRequest);
    }


    // ---------------------------------------------------------------------------------------------
    //                  -> Cursor
    //                  getLecuras() <-
    //
    // Invocado desde: MainActivity::cargarLecturas()
    // Función: Nos da todas las lecturas almacenadas en SQLite.
    // ---------------------------------------------------------------------------------------------
    public Cursor getLecturas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM Lecturas ORDER BY momento ASC;";
        return db.rawQuery(sql, null);
    }

    // ---------------------------------------------------------------------------------------------
    //                  -> Cursor
    //                  getLecurasNoSincronizadas() <-
    //
    // Invocado desde: ComprobadorEstadoRed::onReceive()
    // Función: Nos da todas las lecturas que no se encuentran en la base de datos del servidor.
    // ---------------------------------------------------------------------------------------------
    public Cursor getLecurasNoSincronizadas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM lecturas  WHERE estadoSincronizacionServidor = 0;";
        return db.rawQuery(sql, null);
    }

    // ---------------------------------------------------------------------------------------------
    //                  borrarLecturasSincronizadas() ->
    //
    // Invocado desde: MainActivity::haLlegadoUnBeacon()
    // Función: Borra todas las lecturas que ya se encuentran en la base de datos del servidor.
    // ---------------------------------------------------------------------------------------------
    public void borrarLecturasSincronizadas() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM Lecturas WHERE estadoSincronizacionServidor = 1");
    }

    // ---------------------------------------------------------------------------------------------
    //                  -> Cursor
    //                  actualizarEstadoDeSincronizacionLectura() ->
    //                  <-2 Textos, N
    //
    // Invocado desde: ComprobadorEstadoRed::guardarLectura()
    // Función: Actualiza el estado de sincronización de una lectura recogida offline que acaba de
    //          ser guardada en el servidor
    // ---------------------------------------------------------------------------------------------
    public boolean actualizarEstadoDeSincronizacionLectura(String momento, String ubicacion, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ESTADO_SINCRONIZACION_SERVIDOR, status);
        Log.d("Base de Datos", "Actualizando estado de sincronización");
        // La siguiente sentencia SQL sería la correcta, pero por algún motivo sale un error.
        //db.execSQL("UPDATE " + TABLE_NAME + " SET " + contentValues + " WHERE " + MOMENTO + "=" + momento + " AND " + UBICACION + "=" + ubicacion);
        // near "Oct": syntax error (code 1 SQLITE_ERROR): , while compiling: UPDATE Lecturas SET estadoSincronizacionServidor=1 WHERE momento=Fri Oct 23 07:14:46 GMT+02:00 2020 AND ubicacion=38.9735361 - -0.1801669

        // Esta sentencia consigue los mismos efectos pero de una forma más chapucera y muy generalista.
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + contentValues + " WHERE " + ESTADO_SINCRONIZACION_SERVIDOR + "=" + 0);
        db.close();
        return true;
    }
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------