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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.daferfus_upv.btle.Activities.MainActivity;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.MyApplication;
import com.example.daferfus_upv.btle.Utilidades.LecturaCallback;
import com.example.daferfus_upv.btle.Utilidades.TratamientoDeLecturas;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.DIA;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ESTADO_SINCRONIZACION_SERVIDOR;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.HORA;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ID_MAGNITUD;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ID_USUARIO;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.TABLE_NAME;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.UBICACION;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.VALOR;

// ------------------------------------------------------------------
// ------------------------------------------------------------------


public class Logica extends SQLiteOpenHelper implements Callback<String> {


    // ------------------------------------------------------------------
    // Caracteristicas de la Base de Datos
    // ------------------------------------------------------------------
    private static final String NOMBRE_BD = "Lecturas.db";
    private static String RUTA_BD = "";
    private static final int VERSION_BD = 1;
    // ------------------------------------------------------------------
    // Configuración de la Base de Datos
    // ------------------------------------------------------------------
    private final Context mContexto;
    private boolean mNecesitaActualizar = false;
    // --------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context
    //
    // Invocado desde: ComprobadorEstadoRedWorker:constructor()
    //                 MantenimientoDeMedidasWorker:constructor()
    // Función: Inicializa y configura la base de datos.
    // --------------------------------------------------------------
    public Logica(Context contexto) {
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
    public void guardarLecturaEnLocal(Lectura lectura, int estadoSincronizacionBaseDatos) {
        Log.d("Estado", "El dato está: " + estadoSincronizacionBaseDatos);
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.insert(
                TABLE_NAME,
                null,
                lectura.toContentValues(estadoSincronizacionBaseDatos));
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    } // ()


    // ---------------------------------------------------------------------------------------------
    //                  guardarLecturaEnServidor() ->
    //                  <- Lectura, Context, SQLiteDatabase
    //
    // Invocado desde: MainActivity::enviarMedicion()
    // Función: Inserta una lectura del sensor en la tabla Lecturas en la base de datos del servidor.
    // ---------------------------------------------------------------------------------------------
    public void guardarLecturaEnServidor(Lectura lectura, LecturaCallback lecturaCallback) {
        Call<Lectura> call = MyApiAdapter.getApiService().guardarLecturaEnServidor(lectura.getDia(), lectura.getHora(), lectura.getUbicacion(), String.valueOf(lectura.getValor()), lectura.getIdMagnitud(), lectura.getIdUsuario());
        call.enqueue(new Callback<Lectura>() {
            @Override
            public void onResponse(Call<Lectura> call, Response<Lectura> response) {
                Log.d("LogicaResponse guardarLecturaenServidor", "Lectura insertada en servidor " + response.body());
                //lecturaCallback.crearCopiaDeSeguridad();
            }

            @Override
            public void onFailure(Call<Lectura> call, Throwable t) {
                Log.d("LogicaFail guardarLecturaenServidor", t.getLocalizedMessage());
                lecturaCallback.onFailure();
            }
        });
    }

    public void guardarLecturaEstacion(String dia, String hora, String ubicacion, String valor, String idMagnitud) {
        Call<String> call = MyApiAdapter.getApiService().guardarLecturaEstacion(dia, hora, ubicacion, valor, idMagnitud);
        call.enqueue(this);
    }

    public void calibrarSensor(String usuario, String desviacion) {
        Call<String> call = MyApiAdapter.getApiService().calibrarSensor(usuario, desviacion);
        call.enqueue(this);
    }

    public void consultaLecturaEstacion(String dia, String hora, String ubicacion, LecturaCallback lecturaCallback) {
        Call<String> call = MyApiAdapter.getApiService().consultarLecturaEstacion(dia, hora, ubicacion);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.d("LogicaGeo consultaLecturaEstacion", response.body());
                    if(response.body().matches("0")){
                        Log.d("LogicaCron consultaLecturaEstacion", response.message());
                        Log.d("LogicaCron consultaLecturaEstacion", response.headers().toString());
                        lecturaCallback.hacerScrapping();
                    }
                    else{
                        Log.d("LogicaGeo consultaLecturaEstacion", response.message());
                        lecturaCallback.actualizarDesviacion(response.body(), String.valueOf(TratamientoDeLecturas.valor));
                    }
                }
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Logica", t.getLocalizedMessage());
                lecturaCallback.onFailure();
            }
        });

    }

    public static void consultarDesviacion(String idUsuario, LecturaCallback lecturaCallback){
        Call<String> call = MyApiAdapter.getApiService().consultarDesviacion(idUsuario);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.d("LogicaDesv consultarDesviacion", response.body());
                    lecturaCallback.cogerDesviacion(response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("LogicaDesv consultarDesviacion", t.getLocalizedMessage());
                lecturaCallback.onFailure();
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    //                  -> Cursor
    //                  getLecuras() <-
    //
    // Invocado desde: MainActivity::cargarLecturas()
    // Función: Nos da todas las lecturas almacenadas en SQLite.
    // ---------------------------------------------------------------------------------------------
    public boolean getLectura(String dia, String hora, String ubicacion) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String sql = "SELECT * FROM Lecturas WHERE " + DIA + "='" + dia + "' AND " + HORA + "='" + hora + "' AND " + UBICACION + "='" + ubicacion + "';";
        Cursor lecturas = db.rawQuery(sql, null);
        int datoExistente = lecturas.getCount();
        lecturas.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        if (datoExistente > 0){
            return true;
        }
        else{
            return  false;
        }
    }

    // ---------------------------------------------------------------------------------------------
    //                  -> Cursor
    //                  getLecurasNoSincronizadas() <-
    //
    // Invocado desde: ComprobadorEstadoRed::onReceive()
    // Función: Nos da todas las lecturas que no se encuentran en la base de datos del servidor.
    // ---------------------------------------------------------------------------------------------
    public ArrayList<Lectura> getLecurasNoSincronizadas(String idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Lectura> lecturas = new ArrayList<>();
        db.beginTransaction();
        String sql = "SELECT * FROM lecturas  WHERE estadoSincronizacionServidor = 0 AND idUsuario = '" + idUsuario+"';";
        Cursor lecturasNoSincronizadas = db.rawQuery(sql, null);

        if (lecturasNoSincronizadas.moveToFirst()) {
            Log.d("ComprobadorEstadoRedWorker", "Hay datos");
            do {
                // ...se llama a la función para guardar la lectura no sincronizada a MySQL.
                Lectura lectura = new Lectura(lecturasNoSincronizadas.getString(lecturasNoSincronizadas.getColumnIndex(DIA)),
                        lecturasNoSincronizadas.getString(lecturasNoSincronizadas.getColumnIndex(HORA)),
                        lecturasNoSincronizadas.getString(lecturasNoSincronizadas.getColumnIndex(UBICACION)),
                        lecturasNoSincronizadas.getInt(lecturasNoSincronizadas.getColumnIndex(VALOR)),
                        lecturasNoSincronizadas.getString(lecturasNoSincronizadas.getColumnIndex(ID_MAGNITUD)),
                        lecturasNoSincronizadas.getString(lecturasNoSincronizadas.getColumnIndex(ID_USUARIO)),
                        lecturasNoSincronizadas.getInt(lecturasNoSincronizadas.getColumnIndex(ESTADO_SINCRONIZACION_SERVIDOR)));

                lecturas.add(lectura);
            } while (lecturasNoSincronizadas.moveToNext()); // do while()
            Log.d("Logica", "Éxito");
        } // if()
        lecturasNoSincronizadas.close();
        Log.d("Logica", lecturas.toString());
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return lecturas;
    }

    // ---------------------------------------------------------------------------------------------
    //                  borrarLecturasSincronizadas() ->
    //
    // Invocado desde: MainActivity::haLlegadoUnBeacon()
    // Función: Borra todas las lecturas que ya se encuentran en la base de datos del servidor.
    // ---------------------------------------------------------------------------------------------
    public void borrarLecturasSincronizadas() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM Lecturas WHERE estadoSincronizacionServidor = 1");
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    // ---------------------------------------------------------------------------------------------
    //                  -> Buleano
    //                  actualizarEstadoDeSincronizacionLectura() ->
    //                  <-2 Textos, N
    //
    // Invocado desde: ComprobadorEstadoRed::guardarLectura()
    // Función: Actualiza el estado de sincronización de una lectura recogida offline que acaba de
    //          ser guardada en el servidor
    // ---------------------------------------------------------------------------------------------
    public boolean actualizarEstadoDeSincronizacionLectura(String dia, String hora, String ubicacion, String idUsuario, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ESTADO_SINCRONIZACION_SERVIDOR, status);
        Log.d("Base de Datos", "Actualizando estado de sincronización");
        // La siguiente sentencia SQL sería la correcta, pero por algún motivo sale un error.
        //db.execSQL("UPDATE " + TABLE_NAME + " SET " + contentValues + " WHERE " + MOMENTO + "=" + momento + " AND " + UBICACION + "=" + ubicacion);
        // near "Oct": syntax error (code 1 SQLITE_ERROR): , while compiling: UPDATE Lecturas SET estadoSincronizacionServidor=1 WHERE momento=Fri Oct 23 07:14:46 GMT+02:00 2020 AND ubicacion=38.9735361 - -0.1801669

        // Esta sentencia consigue los mismos efectos pero de una forma más chapucera y muy generalista.
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + contentValues + " WHERE " + DIA + "='" + dia + "' AND " + HORA + "='" + hora + "' AND " + UBICACION + "='" + ubicacion + "' AND " + ID_USUARIO + "='" + idUsuario);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return true;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        Log.d("Logica onResponse call", call.request().method());
        Log.d("Logica onResponse response message", response.message());
        if(response.isSuccessful() && call.request().method().matches("GET")){
            Log.d("Logica onResponse headers", response.headers().toString());
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.d("Logica onFailure call", call.request().method());
        Log.d("Logica onFailure throwable", t.getLocalizedMessage());
    }

    public void borrar(){
        mContexto.deleteDatabase(RUTA_BD+NOMBRE_BD);
    }
    // ---------------------------------------------------------------------------------------------
    //                  consultarMedia() -> N
    //                  <- URL, fecha
    //
    // Invocado desde: MainActivity
    // Función: Obtiene la media de un usuario en un dia
    // ---------------------------------------------------------------------------------------------
    public static void consultarMedia(String URL, String fecha, String hora){
        //declara una peticion

        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL+"?dia="+fecha+"&hora="+hora+"&idUsuario="+ConstantesAplicacion.ID_USUARIO, null, response -> {
            int res = 1;
            try {
                res=response.getInt("valor");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("Consultar Media", response.toString());
            if(res==0){
                res = 1;
            }
            ConstantesAplicacion.MEDIA = res;

        }, error ->  ConstantesAplicacion.MEDIA=0)
        {

        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //procesar la peticiones hechas por nuestra app


        requestQueue.add(stringRequest);
        try{MainActivity.valorMedia.setText(ConstantesAplicacion.MEDIA +"%");
            MainActivity.porcentajeCont.setProgress(ConstantesAplicacion.MEDIA);}
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    // ---------------------------------------------------------------------------------------------
    //                  consultarDistancia() -> N
    //                  <- URL, fecha
    //
    // Invocado desde:
    // Función: Obtiene la distancia de un usuario en un dia
    // ---------------------------------------------------------------------------------------------
    public void consultarDistancia(String URL, String fecha){
        //declara una peticion

        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL+"?idUsuario="+ConstantesAplicacion.ID_USUARIO+"&dia="+fecha, null, response -> {

            int res = 1;
            try {
                res=response.getInt("distancia");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("hola", response.toString());

            ConstantesAplicacion.DISTANCIA = res;
            Log.e("hola", String.valueOf(ConstantesAplicacion.DISTANCIA));

        }, error -> ConstantesAplicacion.DISTANCIA=-1)
        { };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //procesar la peticiones hechas por nuestra app


        requestQueue.add(stringRequest);

    }
    // ---------------------------------------------------------------------------------------------
    //                  consultarPasos() -> N
    //                  <- URL, fecha
    //
    // Invocado desde:
    // Función: Obtiene los pasos de un usuario en un dia
    // ---------------------------------------------------------------------------------------------
    public void consultarPasos(String URL, String fecha){
        //declara una peticion

        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL+"?idUsuario="+ConstantesAplicacion.ID_USUARIO+"&dia="+fecha, null, response -> {

            int res = 1;
            try {
                res=response.getInt("pasos");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("hola", response.toString());

            ConstantesAplicacion.PASOS = res;
            Log.e("hola", String.valueOf(ConstantesAplicacion.PASOS));

        }, error -> Log.e("hola", "no hay pasos"))
        { };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //procesar la peticiones hechas por nuestra app


        requestQueue.add(stringRequest);

    }
    // ---------------------------------------------------------------------------------------------
    //                  getFecha() -> Texto
    //                  <-
    //
    // Invocado desde: Cualquier método de consulta que necesite la fecha
    // Función: Obtiene la fecha actual
    // ---------------------------------------------------------------------------------------------
    public static String getFecha(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    // ---------------------------------------------------------------------------------------------
    //                  getFecha() -> Texto
    //                  <-
    //
    // Invocado desde: Cualquier método de consulta que necesite la hora
    // Función: Obtiene la hora actual
    // ---------------------------------------------------------------------------------------------
    public static String getHora(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    // ---------------------------------------------------------------------------------------------
    //                  distancia() -> R
    //                  <- LatLng, LatLng
    //
    // Invocado desde: subirDistanciaYPasos()
    // Función: Calcula la distancia entre dos puntos
    // ---------------------------------------------------------------------------------------------

    public static Double distancia(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }
    // ---------------------------------------------------------------------------------------------
    //                  pasos() -> N
    //                  <- R
    //
    // Invocado desde: subirDistanciaYPasos()
    // Función: Calcula los pasos dados por un usuario para cierta distancia
    // ---------------------------------------------------------------------------------------------

    public static int pasos(double distancia) {
        int res = (int) Math.round(distancia/0.74);
        return res;
    }
    // ---------------------------------------------------------------------------------------------
    //                  iniciarDistancia() ->
    //                  <-
    //
    // Invocado desde: GeolocalizacionWorker
    // Función: Comprueba si ya existe una medida de la distancia recorrida, en caso negativo la crea
    // ---------------------------------------------------------------------------------------------
    public void iniciarDistancia(){
        if(ConstantesAplicacion.DISTANCIA==-1){
            RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantesAplicacion.URL_GUARDADO_DISTANCIA, response -> {

            }, error -> Log.e("hola", error.toString()))
            {
                //indicar los parametros que vamos a enviar

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new LinkedHashMap<>();
                    //enviar los parametros ()
                    parametros.put("idUsuario", ConstantesAplicacion.ID_USUARIO);
                    parametros.put("dia", getFecha());
                    parametros.put("distancia", String.valueOf(0));
                    parametros.put("pasos", String.valueOf(0));
                    return parametros;
                }
            };


            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //procesar la peticiones hechas por nuestra app


            requestQueue.add(stringRequest);
            consultarDistancia(ConstantesAplicacion.URL_CONSULTA_DISTANCIA, getFecha());
        }

    }
    // ---------------------------------------------------------------------------------------------
    //                  subirDistanciaYPasos() ->
    //                  <-URL, Latlng, LatLng
    //
    // Invocado desde: iniciarDistancia() y actualizarDistancia()
    // Función: Sube al servidor la distancia y pasos
    // ---------------------------------------------------------------------------------------------
    public void subirDistanciaYPasos(String URL, LatLng p1, LatLng p2){
        //declara una peticion
        double distancia = distancia(p1,p2);
        int pasos = pasos(distancia);
        int dist = (int) Math.round(distancia);
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {

        }, error -> Log.e("hola", error.toString()))
        {
            //indicar los parametros que vamos a enviar

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new LinkedHashMap<>();
                //enviar los parametros ()
                parametros.put("idUsuario", ConstantesAplicacion.ID_USUARIO);
                parametros.put("dia", getFecha());
                parametros.put("distancia", String.valueOf(dist+ConstantesAplicacion.DISTANCIA));
                parametros.put("pasos", String.valueOf(pasos+ConstantesAplicacion.PASOS));
                return parametros;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //procesar la peticiones hechas por nuestra app


        requestQueue.add(stringRequest);
        consultarDistancia(ConstantesAplicacion.URL_CONSULTA_DISTANCIA, getFecha());
    }
    // ---------------------------------------------------------------------------------------------
    //                  actualizarMedia() ->
    //                  <-URL, int
    //
    // Invocado desde: haLlegadoUnBeacon( )
    // Función: Sube al servidor la media
    // ---------------------------------------------------------------------------------------------
    public static void actualizarMedia(String URL, int valor){
        int mediaAnterior = ConstantesAplicacion.MEDIA;
        int mediaActual = (mediaAnterior + (valor/350*100))/2;
        String val = String.valueOf(mediaActual);
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {

        }, error -> Log.e("hola", error.toString()))
        {
            //indicar los parametros que vamos a enviar

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new LinkedHashMap<>();
                //enviar los parametros ()
                parametros.put("dia", getFecha());
                parametros.put("hora", getHora());
                parametros.put("idUsuario", String.valueOf(ConstantesAplicacion.ID_USUARIO));
                parametros.put("valor", val);
                return parametros;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //procesar la peticiones hechas por nuestra app


        requestQueue.add(stringRequest);
        consultarMedia(ConstantesAplicacion.URL_CONSULTA_MEDIA, getFecha(), getHora());
    }
    // ---------------------------------------------------------------------------------------------
    //                  insertarMedia() ->
    //                  <-URL, int
    //
    // Invocado desde: haLlegadoUnBeacon( )
    // Función: Sube al servidor la media
    // ---------------------------------------------------------------------------------------------
    public static void insertarMedia(String URL, int valor){

        String val = String.valueOf(valor);
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {

        }, error -> Log.e("hola", error.toString()))
        {
            //indicar los parametros que vamos a enviar

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new LinkedHashMap<>();
                //enviar los parametros ()
                parametros.put("dia", getFecha());
                parametros.put("hora", getHora());
                parametros.put("idUsuario", String.valueOf(ConstantesAplicacion.ID_USUARIO));
                parametros.put("valor", val);
                return parametros;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //procesar la peticiones hechas por nuestra app


        requestQueue.add(stringRequest);
        consultarMedia(ConstantesAplicacion.URL_CONSULTA_MEDIA, getFecha(), getHora());
    }
    // ---------------------------------------------------------------------------------------------
    //                  actualizarSensor() ->
    //                  <-
    //
    // Invocado desde:
    // Función: Actualiza el estado de un sensor en la bdd
    // ---------------------------------------------------------------------------------------------
    public static void actualizarSensor(String URL, String estado){
        //declara una peticion
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {

        }, error -> Log.e("hola", error.toString()))
        {
            //indicar los parametros que vamos a enviar

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new LinkedHashMap<>();
                //enviar los parametros ()
                parametros.put("idUsuario", ConstantesAplicacion.ID_USUARIO);
                parametros.put("estado", estado);

                return parametros;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //procesar la peticiones hechas por nuestra app


        requestQueue.add(stringRequest);

    }
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------