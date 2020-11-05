package com.example.daferfus_upv.btle;

import android.Manifest;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.daferfus_upv.btle.BD.ComprobadorEstadoRed;
import com.example.daferfus_upv.btle.Workers.EscaneadoWorker;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class Perfil extends AppCompatActivity {

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_activity);


        findViewById(R.id.linearAtras).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent (getApplicationContext(),MainActivity.class);
                i.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(i);
            }
        });

    } // onCreate()

    public String envioDatosEntreActividades(){
        Bundle datos = this.getIntent().getExtras();
        String variable_string = datos.getString("Usuario");
        return variable_string;
    }
}
