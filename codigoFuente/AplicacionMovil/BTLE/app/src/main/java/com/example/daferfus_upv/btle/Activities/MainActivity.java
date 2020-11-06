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
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.daferfus_upv.btle.AcercaDe.InstruccionesActivity;
import com.example.daferfus_upv.btle.BD.ComprobadorEstadoRed;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.PaginaGraficas;
import com.example.daferfus_upv.btle.Perfil;
import com.example.daferfus_upv.btle.R;
import com.example.daferfus_upv.btle.Workers.EscaneadoWorker;
import com.example.daferfus_upv.btle.Workers.GeolocalizacionWorker;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {
    public static final String BROADCAST_DATOS_GUARDADOS = "com.example.daferfus_upv.btle";

    // ------------------------------------------------------------------
    // Concesión de Permisos
    // ------------------------------------------------------------------
    TextView textViewUsuario;
    public static TextView textViewvalorSO2;
    LoginActivity login;
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

        //Mostramos el valor del SO2 en el panel principal
        textViewvalorSO2 = (TextView) findViewById(R.id.valorSO2);

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
        textViewUsuario= findViewById(R.id.textViewMostrarUsuario);
        mostrarUsuario();



//      MENU FLOTANTE
        final FloatingActionsMenu menuBotones = (FloatingActionsMenu) findViewById(R.id.grupofab);
        menuBotones.setScaleX(0);
        menuBotones.setScaleY(0);

        //aplicamos un efecto de entrada
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.fast_out_slow_in);

            menuBotones.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(interpolador)
                    .setDuration(600)
                    .setStartDelay(500)
            ;
        }
        //Llamamos a los botones
        final FloatingActionButton fab1 = findViewById(R.id.fab1);
        final FloatingActionButton fab2 = findViewById(R.id.fab2);
        final FloatingActionButton fab3 = findViewById(R.id.fab3);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                menuBotones.collapse();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), InstruccionesActivity.class);
                i.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(i);
                menuBotones.collapse();
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Perfil.class);
                i.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(i);
                menuBotones.collapse();
            }
        });

//      CARDVIEW RECORRIDO
        CardView cardViewRecorrido = findViewById(R.id.cardViewRecorrido); //Creamos cardview y le asignamos un valor

        cardViewRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ponemos lo que queremos que se lance al pulsar el Cardview
                mostrarToast("Recorrido");
            }
        });
//      CARDVIEW MEDICIONES
        CardView cardViewMediciones = findViewById(R.id.cardViewMediciones);

        cardViewMediciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PaginaGraficas.class);
                i.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(i);
            }
        });
//      CARDVIEW LOGROS
        CardView cardViewLogros = findViewById(R.id.cardViewLogros);

        cardViewLogros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarToast("Logros");
            }
        });
//      CARDVIEW CONSEJOS
        CardView cardViewConsejos = findViewById(R.id.cardViewConsejos);

        cardViewConsejos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarToast("Consejos");
            }
        });

    } // onCreate()

    //muestra el usuario registrado
public void mostrarUsuario(){
    Bundle datos = this.getIntent().getExtras();
    String variable_string = datos.getString("Usuario");
    textViewUsuario.setText("Hola "+ variable_string);
}
    //muestra Toast
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    // --------------------------------------------------------------
    //              envioDatosEntreActividades() ->
    //              -> String
    //
    // Invocado desde: CardViews
    // Función: Envia datos del usuario entre actividades.
    // --------------------------------------------------------------
    public String envioDatosEntreActividades(){
        Bundle datos = this.getIntent().getExtras();
        String variable_string = datos.getString("Usuario");
        return variable_string;
    }
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------