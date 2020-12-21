// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------


package com.example.daferfus_upv.btle.Activities;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.daferfus_upv.btle.AcercaDe.InstruccionesActivity;
import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.POJOS.Lecturas;
import com.example.daferfus_upv.btle.PaginaGraficas;
import com.example.daferfus_upv.btle.Perfil;
import com.example.daferfus_upv.btle.R;
import com.example.daferfus_upv.btle.Utilidades.LecturaCallback;
import com.example.daferfus_upv.btle.Workers.CronWorker;
import com.example.daferfus_upv.btle.Workers.EscaneadoWorker;
import com.example.daferfus_upv.btle.Workers.GeolocalizacionWorker;
import com.example.daferfus_upv.btle.Workers.InactividadWorker;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.daferfus_upv.btle.ConstantesAplicacion.DESVIACION;
import static com.example.daferfus_upv.btle.ConstantesAplicacion.ID_USUARIO;


// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // Concesión de Permisos
    // ------------------------------------------------------------------
    TextView textViewUsuario;
    @SuppressLint("StaticFieldLeak")
    public static TextView textViewvalorSO2;
    @SuppressLint("StaticFieldLeak")
    public static TextView valorMedia;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar porcentajeCont;
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

        //Guardamos paen preferencias para ver sia es la primera vez que entra el usuario y mostrarle el manual
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {

            startActivity(new Intent(MainActivity.this, InstruccionesActivity.class));
            //Muestra primer arranque
            //Toast.makeText(InvitadoActivity.this, "First Run", Toast.LENGTH_LONG).show();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
        WorkManager mManager = new WorkManager() {
            @NonNull
            @Override
            public Operation enqueue(@NonNull List<? extends WorkRequest> requests) {
                return null;
            }

            @NonNull
            @Override
            public WorkContinuation beginWith(@NonNull List<OneTimeWorkRequest> work) {
                return null;
            }

            @NonNull
            @Override
            public WorkContinuation beginUniqueWork(@NonNull String uniqueWorkName, @NonNull ExistingWorkPolicy existingWorkPolicy, @NonNull List<OneTimeWorkRequest> work) {
                return null;
            }

            @NonNull
            @Override
            public Operation enqueueUniqueWork(@NonNull String uniqueWorkName, @NonNull ExistingWorkPolicy existingWorkPolicy, @NonNull List<OneTimeWorkRequest> work) {
                return null;
            }

            @NonNull
            @Override
            public Operation enqueueUniquePeriodicWork(@NonNull String uniqueWorkName, @NonNull ExistingPeriodicWorkPolicy existingPeriodicWorkPolicy, @NonNull PeriodicWorkRequest periodicWork) {
                return null;
            }

            @NonNull
            @Override
            public Operation cancelWorkById(@NonNull UUID id) {
                return null;
            }

            @NonNull
            @Override
            public Operation cancelAllWorkByTag(@NonNull String tag) {
                return null;
            }

            @NonNull
            @Override
            public Operation cancelUniqueWork(@NonNull String uniqueWorkName) {
                return null;
            }

            @NonNull
            @Override
            public Operation cancelAllWork() {
                return null;
            }

            @NonNull
            @Override
            public Operation pruneWork() {
                return null;
            }

            @NonNull
            @Override
            public LiveData<Long> getLastCancelAllTimeMillisLiveData() {
                return null;
            }

            @NonNull
            @Override
            public ListenableFuture<Long> getLastCancelAllTimeMillis() {
                return null;
            }

            @NonNull
            @Override
            public LiveData<WorkInfo> getWorkInfoByIdLiveData(@NonNull UUID id) {
                return null;
            }

            @NonNull
            @Override
            public ListenableFuture<WorkInfo> getWorkInfoById(@NonNull UUID id) {
                return null;
            }

            @NonNull
            @Override
            public LiveData<List<WorkInfo>> getWorkInfosByTagLiveData(@NonNull String tag) {
                return null;
            }

            @NonNull
            @Override
            public ListenableFuture<List<WorkInfo>> getWorkInfosByTag(@NonNull String tag) {
                return null;
            }

            @NonNull
            @Override
            public LiveData<List<WorkInfo>> getWorkInfosForUniqueWorkLiveData(@NonNull String uniqueWorkName) {
                return null;
            }

            @NonNull
            @Override
            public ListenableFuture<List<WorkInfo>> getWorkInfosForUniqueWork(@NonNull String uniqueWorkName) {
                return null;
            }
        };
        //Mostramos el valor del SO2 en el panel principal
        textViewvalorSO2 = (TextView) findViewById(R.id.valorSO2);
        valorMedia = (TextView) findViewById(R.id.mediaContaminacion);
        porcentajeCont = (ProgressBar) findViewById(R.id.progress_increase);
        Logica logica = new Logica(this);
        logica.iniciarDistancia();

        logica.consultarDistancia(ConstantesAplicacion.URL_CONSULTA_DISTANCIA, logica.getFecha());
        logica.consultarPasos(ConstantesAplicacion.URL_CONSULTA_PASOS, logica.getFecha());
        Logica.consultarMedia(ConstantesAplicacion.URL_CONSULTA_MEDIA, Logica.getFecha(), Logica.getHora());

        // Se activa el adaptador Bluetooth
        BluetoothAdapter adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!adaptadorBluetooth.isEnabled()) {
            Intent intentActivarBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            int PETICION_ACTIVAR_BT = 1;
            startActivityForResult(intentActivarBt, PETICION_ACTIVAR_BT);
        }

        // Se comprueba el estado de la red para meter en el servidor los datos no sincronizados.
        //registerReceiver(new ComprobadorEstadoRed(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Se comprueban los permisos de geolocalización.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permisos: ", "Pidiendo permisos");
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso localización no puedo ubicar tus" +
                            " lecturas.", ConstantesAplicacion.PETICION_LOCALIZACION, this);
        }

        Log.d("MainActivity", ID_USUARIO);
        // Se empieza con la ejecución automática de la búsqueda del dispositivo del usuario.
        if(!ID_USUARIO.matches("invitado")){

            logica.borrar();
            Log.d("MainActivity", "usuario");

            WorkRequest medicionesWorkRequest =
                    new OneTimeWorkRequest.Builder(EscaneadoWorker.class)
                            .build();
            WorkManager
                    .getInstance()
                    .enqueue(medicionesWorkRequest);

           WorkRequest cronWorkRequest =
                    new PeriodicWorkRequest.Builder(CronWorker.class, 1, TimeUnit.HOURS)
                            // Constraints
                            .build();
            WorkManager
                    .getInstance()
                    .enqueue(cronWorkRequest);


        Logica.consultarDesviacion(ID_USUARIO, new LecturaCallback() {
            @Override
            public void hacerScrapping() {

            }

            @Override
            public void crearCopiaDeSeguridad() {

            }

            @Override
            public void cogerDesviacion(String desviacion) {
                Log.d("MainActivity", desviacion);
                DESVIACION = Integer.parseInt(desviacion);
            }

            @Override
            public void actualizarDesviacion(String valorLecturaEstacion, String valorLectura) {

            }

            @Override
            public void onFailure() {

            }
        });
        }
        textViewUsuario= findViewById(R.id.textViewMostrarUsuario);
        mostrarUsuario();
        //Se inicializa la detección de inactividad del sensor
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest inactividad =
                new PeriodicWorkRequest.Builder(InactividadWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        mManager.enqueueUniquePeriodicWork(
                "inactivity",
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                inactividad //work request
        );

//      MENU FLOTANTE
        final FloatingActionsMenu menuBotones = findViewById(R.id.grupofab);
        menuBotones.setScaleX(0);
        menuBotones.setScaleY(0);

        //aplicamos un efecto de entrada
        final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                android.R.interpolator.fast_out_slow_in);

        menuBotones.animate()
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(interpolador)
                .setDuration(600)
                .setStartDelay(500)
        ;
        //Llamamos a los botones
        final FloatingActionButton fab1 = findViewById(R.id.fab1);
        final FloatingActionButton fab2 = findViewById(R.id.fab2);
        final FloatingActionButton fab3 = findViewById(R.id.fab3);

        //cerrar sesion
        fab1.setOnClickListener(view -> {
            LoginActivity.cerrarSesion(getApplicationContext(), false);
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            menuBotones.collapse();
        });
        //acerca de
        fab2.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), InstruccionesActivity.class);

            startActivity(i);
            menuBotones.collapse();
        });
        //perfil
        fab3.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), Perfil.class);

            startActivity(i);
            menuBotones.collapse();
        });

//      CARDVIEW RECORRIDO
        CardView cardViewRecorrido = findViewById(R.id.cardViewRecorrido); //Creamos cardview y le asignamos un valor

        cardViewRecorrido.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), RecorridoActivity.class);

            startActivity(i);
        });
//      CARDVIEW MEDICIONES
        CardView cardViewMediciones = findViewById(R.id.cardViewMediciones);

        cardViewMediciones.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PaginaGraficas.class);

            startActivity(i);
        });
//      CARDVIEW LOGROS
        CardView cardViewLogros = findViewById(R.id.cardViewLogros);

        cardViewLogros.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), LogrosActivity.class);

            startActivity(i);
        });
//      CARDVIEW CONSEJOS
        CardView cardViewConsejos = findViewById(R.id.cardViewConsejos);

        cardViewConsejos.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ConsejosActivity.class);

            startActivity(i);
        });
        //      CARDVIEW MAPA
        CardView cardViewMapa = findViewById(R.id.cardView7); // creating a CardView and assigning a value.

        cardViewMapa.setOnClickListener(v -> {
            // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
            Intent i = new Intent(getApplicationContext(), MapaActivity.class);
            startActivity(i);
        });


    } // onCreate()

    //muestra el usuario registrado
    public void mostrarUsuario() {
        List<String> emailContra=LoginActivity.cargarPreferenciasString(getApplicationContext());
        textViewUsuario.setText("Hola " + emailContra.get(2));
    }
    //muestra Toast
    /*private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }*/


} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------