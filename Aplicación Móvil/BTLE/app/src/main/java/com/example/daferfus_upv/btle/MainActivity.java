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
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.daferfus_upv.btle.BD.ComprobadorEstadoRed;
import com.example.daferfus_upv.btle.Workers.EscaneadoWorker;
import com.example.daferfus_upv.btle.Workers.GeolocalizacionWorker;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {
    public static final String BROADCAST_DATOS_GUARDADOS = "com.example.daferfus_upv.btle";

    // ------------------------------------------------------------------
    // Concesión de Permisos
    // ------------------------------------------------------------------

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
                // ...se crea una tarea exclusivamente dedicada a la Geolocalización.
                WorkRequest geolocalizacionWorkRequest =
                        new OneTimeWorkRequest.Builder(GeolocalizacionWorker.class)
                                .build();
                WorkManager
                        .getInstance()
                        .enqueue(geolocalizacionWorkRequest);
                // En caso contario...
            } else {
                // ...recuerda al usuario de que no ha dado permiso a la aplicación para conceder
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
                GeolocalizacionWorker.estaGPSActivo = true; // Se activa GPS antes de coger localización.
            } // if()
        } // if()
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Se activa el adaptador Bluetooth
        BluetoothAdapter adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!adaptadorBluetooth.isEnabled()) {
            Intent intentActivarBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            int PETICION_ACTIVAR_BT = 1;
            startActivityForResult(intentActivarBt, PETICION_ACTIVAR_BT);
        }

        // Se comprueba el estado de la red para meter en el servidor los datos no sincronizados.
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
        WorkRequest medicionesWorkRequest =
                new OneTimeWorkRequest.Builder(EscaneadoWorker.class)
                        .build();
        WorkManager
                .getInstance()
                .enqueue(medicionesWorkRequest);
    } // onCreate()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------