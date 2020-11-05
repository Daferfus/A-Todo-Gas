// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.Utilidades;

// ------------------------------------------------------------------
// ------------------------------------------------------------------
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import static android.content.ContentValues.TAG;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class GpsUtils {

    private final Context contexto;
    private final SettingsClient mConfiguracionCliente;
    private final LocationSettingsRequest mPeticionConfiguracionLocalizacion;
    private final LocationManager gestionadorLocalizacion;

    // --------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context
    //
    // Invocado desde: MainActivity
    // Función: Inicializa y configura GPS.
    // --------------------------------------------------------------
    public GpsUtils(Context contexto) {
        this.contexto = contexto;
        // Se recoge la configuración.
        gestionadorLocalizacion = (LocationManager) contexto.getSystemService(Context.LOCATION_SERVICE);
        mConfiguracionCliente = LocationServices.getSettingsClient(contexto);

        // Se configura el servicio de geolocalización.
        LocationRequest peticionLocalizacion = LocationRequest.create();
        peticionLocalizacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        peticionLocalizacion.setInterval(10 * 1000);
        peticionLocalizacion.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(peticionLocalizacion);
        mPeticionConfiguracionLocalizacion = builder.build();

        // Se deja en constante ejecución.
        //**************************
        builder.setAlwaysShow(true);
        //**************************
    } // ()

    // --------------------------------------------------------------
    //                  activarGPS() <-
    //                  <- onGpsListener
    //
    // Invocado desde: MainActivity
    // Función: Activa GPS.
    // --------------------------------------------------------------
    public void activarGPS(onGpsListener onGpsListener) {

        // Si el proveedor está activado...
        if (gestionadorLocalizacion.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // ...y si el GPS está a la escucha...
            if (onGpsListener != null) {
                // ...el GPS está activado.
                onGpsListener.gpsStatus(true);
            }
            // En caso contrario...
        } else {
            // ...se procede a meterle la configuración...
            mConfiguracionCliente
                    .checkLocationSettings(mPeticionConfiguracionLocalizacion)
                    // ...y en caso de tener éxito...
                    .addOnSuccessListener((Activity) contexto, locationSettingsResponse -> {

                        // ...y en caso de estar a la escucha...
                        if (onGpsListener != null) {
                            // ...se pone como activado.
                            onGpsListener.gpsStatus(true);
                        }
                    })
                    // En caso de fracasar...
                    .addOnFailureListener((Activity) contexto, e -> {
                        // ...se coge el código de excepción de la API...
                        int codigoEstado = ((ApiException) e).getStatusCode();
                        // ...y en función de ese código...
                        switch (codigoEstado) {
                            // ...y en caso de que se requieran permisos...
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    // ...muestra el dialogo llamando a startResolutionForResult(), y comprueba el
                                    // resultado en onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult((Activity) contexto, ConstantesAplicacion.PETICION_GPS);
                                } catch (IntentSender.SendIntentException sie) {
                                    // Y en caso de error lo muestra por terminal.
                                    Log.i(TAG, "PendingIntent incapaz de ejecutar petición.");
                                }
                                break;
                                // En caso de que que la configuración sea inadecuada...
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // ... muestra error.
                                String errorMessage = "La configuración de localización es inadecuada, y no puede ser " +
                                        "arreglada aquí. Arreglala en Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(contexto, errorMessage, Toast.LENGTH_LONG).show();
                        } // switch()
                    }); // addOnFailureListener
        } // if()
    } // ()

    // --------------------------------------------------------------
    //                  onGPSListener()
    //
    // Funciones de la interfaz: gpsStatus () <-
    //                           <- Buleano
    //
    //                          Invocado: activarGPS()
    //                          Función: Indica si el GPS está activado.
    // --------------------------------------------------------------
    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------