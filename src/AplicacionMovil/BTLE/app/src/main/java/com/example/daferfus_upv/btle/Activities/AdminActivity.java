package com.example.daferfus_upv.btle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.daferfus_upv.btle.R;

public class AdminActivity extends AppCompatActivity {

    private TextView textViewAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
        textViewAdmin= findViewById(R.id.tvNombreAdmin);
        mostrarUsuario();
        CardView botonUsuarios=findViewById(R.id.cardViewAñadirUsuario);
        botonUsuarios.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), Admin_web.class);
            i.putExtra("seccion", "Usuarios");
            startActivity(i);
        });
        CardView botonSensores=findViewById(R.id.cardViewMediciones);
        botonSensores.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), Admin_web.class);
            i.putExtra("seccion", "Sensores");
            startActivity(i);
        });
    }

    //muestra el usuario registrado
    public void mostrarUsuario() {
        Bundle datos = this.getIntent().getExtras();
        String variable_string = datos.getString("Usuario");
        textViewAdmin.setText("Hola " + variable_string);
    }
}
