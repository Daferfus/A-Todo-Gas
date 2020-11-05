package com.example.daferfus_upv.btle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.Activities.MainActivity;

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
                Intent i = new Intent (getApplicationContext(), MainActivity.class);
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
