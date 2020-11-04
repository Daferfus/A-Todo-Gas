// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.Workers;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.daferfus_upv.btle.Utilidades.GpsUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class GeolocalizacionWorker extends Worker {
    // --------------------------------------------------------------
    // Geolocalización
    // --------------------------------------------------------------
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitud = 0.0, longitud = 0.0;
    private double latitudAuxiliar = 0.0, longitudAuxiliar = 0.0;
    public static String ubicacion;

    private LocationRequest peticionLocalizacion;
    private LocationCallback callbackLocalizacion;

    private boolean noCambiaPosicion = false;
    public static boolean estaGPSActivo = false;
    private boolean primeraExtraccionPosicion = true;

    // --------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context, WorkParameters
    //
    // Invocado desde: TratamientoDeLecturas::haLLegadoUnBeacon()
    // Función: Inicializa y configura la tarea de geolocalización.
    // --------------------------------------------------------------
    public GeolocalizacionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        // Se configura el servicio de geolocalización.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        peticionLocalizacion = LocationRequest.create();
        peticionLocalizacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        peticionLocalizacion.setInterval(0); // 10 segundos
        peticionLocalizacion.setFastestInterval(0); // 5 segundos

        // Se activa GPS.
        new GpsUtils(getApplicationContext()).activarGPS(isGPSEnable -> estaGPSActivo = isGPSEnable); // new GpsUtils

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
                        Log.d("Localizacion: ", ubicacion);
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
    }

    @NonNull
    @Override
    public Result doWork() {
        getPosicionGPS();
        return Result.success();
    }

    // --------------------------------------------------------------
    //              getPosicionGPS() <-
    //
    // Invocado desde: haLlegadoUnBeacon()
    // Función: Recoge la ubicación actual del usuario.
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void getPosicionGPS() {
        // Monitoriza la ubicación sin coger su posición.
        new Handler(Looper.getMainLooper()).post(() -> mFusedLocationClient.requestLocationUpdates(peticionLocalizacion, callbackLocalizacion, null));
    } // ()


}
