// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------


package com.example.daferfus_upv.btle;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.daferfus_upv.btle.BD.Lectura;
import com.example.daferfus_upv.btle.BD.LecturaAdaptador;
import com.example.daferfus_upv.btle.BD.LecturasDbHelper;
import com.example.daferfus_upv.btle.BD.ComprobadorEstadoRed;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ESTADO_SINCRONIZACION_SERVIDOR;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.ID_MAGNITUD;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.MOMENTO;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.UBICACION;
import static com.example.daferfus_upv.btle.BD.LecturasContract.LecturasEntry.VALOR;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    // Impresión de Datos
    // --------------------------------------------------------------
    private List<Lectura> lecturas;
    private ListView listViewLecturas;
    private LecturaAdaptador lecturaAdaptador;

    public static final String BROADCAST_DATOS_GUARDADOS = "com.example.daferfus_upv.btle";
    // --------------------------------------------------------------
    // LOG
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";

    // --------------------------------------------------------------
    // Base de Datos
    // --------------------------------------------------------------
    private LecturasDbHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String ubicacion;
    private int valor;

    // --------------------------------------------------------------
    // Geolocalización
    // --------------------------------------------------------------
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitud = 0.0, longitud = 0.0;
    private double latitudAuxiliar = 0.0, longitudAuxiliar = 0.0;
    private LocationRequest peticionLocalizacion;
    private LocationCallback callbackLocalizacion;

    private boolean noCambiaPosicion = false;
    private boolean estaGPSActivo = false;
    private boolean primeraExtraccionPosicion = true;

    TextView textView;

    // --------------------------------------------------------------
    // Bluetooth
    // --------------------------------------------------------------
    private BluetoothAdapter.LeScanCallback callbackLeScan = null;


    // --------------------------------------------------------------
    // Funciones de Búsqueda
    // --------------------------------------------------------------

    // --------------------------------------------------------------
    // Ejecución en bucle de la función buscarEsteDispositivoBTLE()
    // con un retraso de 1 segundo entre busquedas.
    // --------------------------------------------------------------
    /*private final Handler ejecucionBusquedaAutomatica = new Handler();
    private final Runnable ejecutable = new Runnable() {
        @Override
        public void run() {
            // Por cada segundo pasado, la aplicación busca los datos que han sido
            // mandados por el microcontrolador.
            buscarEsteDispositivoBTLE(Utilidades.stringToUUID("EPSG-GTI-PROY-G2"));
            ejecucionBusquedaAutomatica.postDelayed(this, 0);
        }// ()
    };// new Runnable*/

    // --------------------------------------------------------------
    //              buscarTodosLosDispositivosBTLE() ->
    //
    // Invocado desde: botonBuscarDispositivosBTLEPulsado()
    // Función: Busca todos los dispositivos BTLE visibles.
    // --------------------------------------------------------------
    private void buscarTodosLosDispositivosBTLE() {

        this.callbackLeScan = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {

                //
                //  se ha encontrado un dispositivo
                //
                mostrarInformacionDispositivoBTLE( bluetoothDevice, rssi, bytes );

            } // onLeScan()
        }; // new LeScanCallback

        //
        //
        //
        boolean resultado = BluetoothAdapter.getDefaultAdapter().startLeScan( this.callbackLeScan );

        // Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): startLeScan(), resultado= " + resultado );
    } // ()

    // --------------------------------------------------------------
    //              mostrarInformacionDispositivoBTLE() ->
    //              <- BluetoothDevice, N, byte[]
    //
    // Invocado desde: buscarTodosLosDispositivosBTLE()
    //                 buscarEsteDispositivoBTLE()
    // Función: Muestra datos de los dispositivos BTLE detectados.
    // --------------------------------------------------------------
    private void mostrarInformacionDispositivoBTLE(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {

        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi);

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        haLlegadoUnBeacon(tib);
    } // ()

    // --------------------------------------------------------------
    //              buscarEsteDispositivoBTLE() ->
    //              <- UUID
    //
    // Invocado desde: botonBuscarDispositivosBTLEPulsado()
    // Función: Busca el dispositivo BTLE con el UUID pasado.
    // --------------------------------------------------------------
    private void buscarEsteDispositivoBTLE(final UUID dispositivoBuscado) {
        this.callbackLeScan = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {

                //
                // dispostivo encontrado
                //

                TramaIBeacon tib = new TramaIBeacon(bytes);
                haLlegadoUnBeacon(tib);

                String uuidString = Utilidades.bytesToString(tib.getUUID());

                if (uuidString.compareTo(Utilidades.uuidToString(dispositivoBuscado)) == 0) {
                    //detenerBusquedaDispositivosBTLE();
                    mostrarInformacionDispositivoBTLE(bluetoothDevice, rssi, bytes);
                } else {
                    Log.d(MainActivity.ETIQUETA_LOG, " * UUID buscado >" +
                            Utilidades.uuidToString(dispositivoBuscado) + "< no concuerda con este uuid = >" + uuidString + "<");
                }

            } // onLeScan()
        }; // new LeScanCallback

        //
        //
        //
        BluetoothAdapter.getDefaultAdapter().startLeScan(this.callbackLeScan);
    } // ()
    // --------------------------------------------------------------
    //              detenerBusquedaDispositivosBTLE() ->
    //
    // Invocado desde: botonDetenerBusquedaDispositivosBTLEPulsado()
    // Función: Detiene la búsqueda de dispositivos BTLE.
    // --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {
        if (this.callbackLeScan == null) {
            return;
        }

        //
        //
        //
        BluetoothAdapter.getDefaultAdapter().stopLeScan(this.callbackLeScan);
        this.callbackLeScan = null;
    } // ()
    // ------------------------------------------------------------------
    // ------------------------------------------------------------------


    // --------------------------------------------------------------
    // Botones
    // --------------------------------------------------------------

    // --------------------------------------------------------------
    //              botonBuscarDispositivosBTLEPulsado() ->
    //              <- View
    //
    // Invocado desde: Botón "Detener Busqueda Dispositivos BTLE"
    // Función: Invoca al método detenerBusquedaDispositivosBTLE()
    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado");
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // ----------------------------------------------------------------------------------------
    //              botonBuscarNuestroDispositivoBTLEPulsado ->
    //              <- View
    //
    // Invocado desde: Botón "Buscar Nuestro Dispositivo BTLE"
    // Función: Invoca al método buscarEsteDispositivoBTLE(), pasandole como parámetro una UUID
    // ----------------------------------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado");
        this.buscarEsteDispositivoBTLE(Utilidades.stringToUUID("EPSG-GTI-PROY-G2"));
    } // ()

    // --------------------------------------------------------------
    //              botonDetenerBusquedaDispositivosBTLEPulsado() ->
    //              <- View
    //
    // Invocado desde: Botón "Detener Busqueda Dispositivos BTLE"
    // Función: Invoca al método detenerBusquedaDispositivosBTLE()
    // --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton detener búsqueda BTLE Pulsado");
        this.detenerBusquedaDispositivosBTLE();
    } // ()
    // ------------------------------------------------------------------
    // ------------------------------------------------------------------

    // --------------------------------------------------------------
    // Funciones de Tratado de Datos
    // --------------------------------------------------------------

    // --------------------------------------------------------------
    //              haLlegadoUnBeacon() <-
    //              <- TramaIBeacon
    //
    // Invocado desde: buscarEsteDispositivoBTLE()
    // Función: Avisa de que ha encontrado el dispositivo BTLE del usuario y se prepara para
    //          extraer datos esenciales.
    // --------------------------------------------------------------
    public void haLlegadoUnBeacon(TramaIBeacon trama) {
        if (Utilidades.bytesToString(trama.getUUID()).equals("EPSG-GTI-PROY-G2")) {
            Log.d("Tratamiento Datos", "¡¡Ha llegado un beacon!! ");
            getPosicionGPS();
            extraerMediciones(trama);
            enviarMedicion();
            mDBHelper.borrarLecturasSincronizadas();
        } // if()
    } // ()

    // ---------------------------------------------------------------------------
    //                  extraerMediciones() ->
    //                  <- TramaIBeacon
    //
    // Invocado desde: haLlegadoUnBeacon()
    // Función: Extrae las mediciones de la trama de la baliza para su tratamiento.
    // ----------------------------------------------------------------------------
    public void extraerMediciones(TramaIBeacon trama) {
        byte[] contador = trama.getMajor();
        byte[] valorSO2 = trama.getMinor();
        valor = Utilidades.bytesToInt(valorSO2);
        Log.d("Tratamiento Datos", "Contador: " + Utilidades.bytesToInt(contador));
        Log.d("Tratamiento Datos", "SO2: " + Utilidades.bytesToInt(valorSO2));
    } // ()

    // ---------------------------------------------------------------------------
    //                  enviarMedicion() <-
    //
    // Invocado desde: haLlegadoUnBeacon()
    // Función: Manda a la base de datos, datos para insertar en la tabla Lecturas.
    // ----------------------------------------------------------------------------
    public void enviarMedicion() {
        String idMagnitud = "SO2";
        int estadoSincronizacionServidor = 1;
        mDBHelper.guardarLecturaEnServidor(new Lectura(new Date().toString(), ubicacion, valor, idMagnitud, estadoSincronizacionServidor), this, mDb);
    }
    // ------------------------------------------------------------------
    // ------------------------------------------------------------------

    // --------------------------------------------------------------
    // Funciones de Muestra de Datos en Interfaz
    // --------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    //                  cargarLecturas() <-
    //
    // Invocado desde: LocationCallback
    // Función: Carga los lecturas de la base de datos con estado de sincronización actualizado
    // ----------------------------------------------------------------------------------------
    private void cargarLecturas() {
        lecturas.clear();
        Cursor cursor = mDBHelper.getLecturas();
        if (cursor.moveToFirst()) {
            do {
                Lectura lectura = new Lectura(
                        cursor.getString(cursor.getColumnIndex(MOMENTO)),
                        cursor.getString(cursor.getColumnIndex(UBICACION)),
                        cursor.getInt(cursor.getColumnIndex(VALOR)),
                        cursor.getString(cursor.getColumnIndex(ID_MAGNITUD)),
                        cursor.getInt(cursor.getColumnIndex(ESTADO_SINCRONIZACION_SERVIDOR))
                );
                lecturas.add(lectura);
                Log.d("Cursor", lectura.getMomento());
            } while (cursor.moveToNext()); // do while()
        } // if()
        lecturaAdaptador = new LecturaAdaptador(this, R.layout.listview_mediciones, lecturas);
        listViewLecturas.setAdapter(lecturaAdaptador);
    } // ()

    // ----------------------------------------------------------------------------------------
    //                  refrescarLista() <-
    //
    // Invocado desde: enviarMedicion()
    // Función: Refresca el listado de lecturas mostrado en la vista principal.
    // ----------------------------------------------------------------------------------------
    public static void refrescarLista(LecturaAdaptador lecturaAdaptador) {
        Log.d("MainActivity", "Datos de la lista actualizados");
        lecturaAdaptador.notifyDataSetChanged();
    } // ()
    // ------------------------------------------------------------------
    // ------------------------------------------------------------------

    // --------------------------------------------------------------
    // Funciones de Localización
    // --------------------------------------------------------------

    // --------------------------------------------------------------
    //              getPosicionGPS() <-
    //
    // Invocado desde: haLlegadoUnBeacon()
    // Función: Recoge la ubicación actual del usuario.
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void getPosicionGPS() {
        // Monitoriza la ubicación sin coger su posición.
        mFusedLocationClient.requestLocationUpdates(peticionLocalizacion, callbackLocalizacion, null);
    } // ()

    // --------------------------------------------------------------
    //              solicitarPermiso() <-
    //
    // Invocado desde: onCreate()
    // Función: Solicita permiso al usuario para emplear la funcionalidad de localización
    public static void solicitarPermiso(final String permiso, String
            justificacion, final int codigoPeticion, final Activity actividad) {
        Log.d("Permisos: ", "Preparando permisos");
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            new android.app.AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", (dialog, whichButton) -> ActivityCompat.requestPermissions(actividad,
                            new String[]{permiso}, codigoPeticion))
                    .show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, codigoPeticion);
        }
    }

    // --------------------------------------------------------------
    //              onRequestPermissionsResult() ->
    //              <- N, Texto[], N[]
    //
    // Invocado desde: getPosicionGPS()
    // Función: Gestiona la concesión de permisos.
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int codigoPeticion, @NonNull String[] permisos, @NonNull int[] resultadosConcesion) {
        super.onRequestPermissionsResult(codigoPeticion, permisos, resultadosConcesion);
        // Si la petición es de permisos de geolocalización...
        if (codigoPeticion == 1000) {
            // ...y el usuario ha dado permiso...
            if (resultadosConcesion.length > 0
                    && resultadosConcesion[0] == PackageManager.PERMISSION_GRANTED) {
                // ...y el usuario está parado...
                if (noCambiaPosicion) {
                    // ...sigue monitorizando la ubicación.
                    mFusedLocationClient.requestLocationUpdates(peticionLocalizacion, callbackLocalizacion, null);

                    // En caso contrario...
                } else {
                    // ...se recoge la última ubicación...
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
                        // ...y en caso de no dar valores vacios...
                        if (location != null) {
                            // ...la almacena y la muestra.
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                            latitudAuxiliar = latitud;
                            longitudAuxiliar = longitud;
                            primeraExtraccionPosicion = false;
                            textView.setText(String.format(Locale.US, "%s - %s", latitud, longitud));
                            Log.d("Localizacion: ", "Parte de los permisos");
                            // De lo contrario...
                        } else {
                            // ...vuelve a pedir ubicación.
                            mFusedLocationClient.requestLocationUpdates(peticionLocalizacion, callbackLocalizacion, null);
                        }
                    });
                }
                // En caso contario...
            } else {
                // ...recuerda al usuario de que ha dado permiso a la aplicación para conceder
                // sus servicios.
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // --------------------------------------------------------------
    //              onActivityResult() <-
    //              <- N, N, Intent
    //
    // Invocado desde: onCreate()
    // Función: Activa GPS tras conceder los permisos
    // --------------------------------------------------------------
    @Override
    protected void onActivityResult(int codigoPeticion, int codigoResultado, Intent datos) {
        super.onActivityResult(codigoPeticion, codigoResultado, datos);
        if (codigoResultado == Activity.RESULT_OK) {
            if (codigoResultado == ConstantesAplicacion.PETICION_GPS) {
                estaGPSActivo = true; // Se activa GPS antes de coger localización.
            } // if()
        } // if()
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Se busca texto donde mostrar los datos.
        textView = findViewById(R.id.textView);
        listViewLecturas = (ListView) findViewById(R.id.listViewLecturas);

        // Se configura el servicio de geolocalización.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        peticionLocalizacion = LocationRequest.create();
        peticionLocalizacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        peticionLocalizacion.setInterval(0); // 10 segundos
        peticionLocalizacion.setFastestInterval(0); // 5 segundos

        // Se activa GPS.
        new GpsUtils(this).activarGPS(isGPSEnable -> estaGPSActivo = isGPSEnable); // new GpsUtils

        // Se crea el callback para la geolocalizacion...
        callbackLocalizacion = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult resultadoLocalizacion) {
                // ...y en caso de que no tenga resultados...
                if (resultadoLocalizacion == null) {
                    // ...no hace nada.
                    return;
                } // if()
                // Pero en caso de que los tenga, y por cada resultado obtenido...
                for (Location localizacion : resultadoLocalizacion.getLocations()) {
                    // ...y en caso de no estar vacio...
                    if (localizacion != null) {
                        // ...almacena los componentes del resultado...
                        latitud = localizacion.getLatitude();
                        longitud = localizacion.getLongitude();
                        ubicacion = latitud + " - " + longitud;
                        // ...y en caso de que sea la primera posición extraida...
                        if (primeraExtraccionPosicion) {
                            Log.d("Localizacion: ", "Primera Extracción");
                            // ...la mete en una variable auxiliar.
                            latitudAuxiliar = latitud;
                            longitudAuxiliar = longitud;
                            primeraExtraccionPosicion = false;
                            // Y en caso contrario...
                        } else {
                            // ...y si no se ha cambiado posición...
                            if (latitudAuxiliar == latitud && longitudAuxiliar == longitud) {
                                // ...simplemente se comenta eso.
                                Log.d("Localizacion: ", "Misma ubicación");
                                noCambiaPosicion = true;
                                // Y en caso contrario...
                            } else {
                                // ...añade los datos a la variable auxiliar...
                                latitudAuxiliar = latitud;
                                longitudAuxiliar = longitud;

                                // ...y se deja constancia del cambio de posición.
                                noCambiaPosicion = false;
                                Log.d("Localizacion: ", "Distinta ubicación");
                            }
                        }
                        // En caso de que el usuario esté estático y que el cliente de
                        // localización este funcional...
                        if (noCambiaPosicion && mFusedLocationClient != null) {
                            // ...deja de actualizar para evitar consumir recursos.
                            mFusedLocationClient.removeLocationUpdates(callbackLocalizacion);
                        } // if()
                    } // if()
                } // for()
            } // ()
        }; // new LocationCallback

        // Se activa el adaptador Bluetooth
        BluetoothAdapter adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!adaptadorBluetooth.isEnabled()) {
            Intent intentActivarBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            int PETICION_ACTIVAR_BT = 1;
            startActivityForResult(intentActivarBt, PETICION_ACTIVAR_BT);
        }

        // Se recoge el servicio de sistema de Bluetooth para su uso.
        BluetoothManager bluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);

        // Se inicializa la base de datos...
        mDBHelper = new LecturasDbHelper(this);

        // ...la actualiza...
        try {
            mDBHelper.actualizarBaseDatos();
        } catch (IOException mIOException) {
            throw new Error("Incapaz de Actualizar Base de Datos");
        }

        // ...y coge su referencia para escritura.
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            Log.d("Base de Datos: ", mSQLException.toString());
            throw mSQLException;
        }

        lecturas = new ArrayList<>();

        // Se llama al método para cargar todas las lecturas almacenadas
        cargarLecturas();
        refrescarLista(lecturaAdaptador);

        // Receptor Broadcast para actualizar el estado de sincronización.
        BroadcastReceiver receptorBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Se cargan los nombres de nuevo
                cargarLecturas();
                refrescarLista(lecturaAdaptador);
            }
        };

        // Se registra el receptor Broadcast para actualizar el estado de sincronización.
        registerReceiver(receptorBroadcast, new IntentFilter(BROADCAST_DATOS_GUARDADOS));
        registerReceiver(new ComprobadorEstadoRed(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Se comprueban los permisos de geolocalización.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permisos: ", "Pidiendo permisos");
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso localización no puedo ubicar tus" +
                            " lecturas.", ConstantesAplicacion.PETICION_LOCALIZACION, this);
        }

        // Se empieza con la ejecución automática de la búsqueda del dispositivo del usuario.

        buscarEsteDispositivoBTLE(Utilidades.stringToUUID("EPSG-GTI-PROY-G2"));
        //ejecucionBusquedaAutomatica.postDelayed(ejecutable, 1000);
    } // onCreate()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------