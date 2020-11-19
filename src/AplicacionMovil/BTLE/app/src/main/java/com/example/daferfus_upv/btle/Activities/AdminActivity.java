package com.example.daferfus_upv.btle.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.R;

public class AdminActivity extends AppCompatActivity {

    private TextView textViewAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
        textViewAdmin= findViewById(R.id.tvNombreAdmin);
        mostrarUsuario();
    }

    //muestra el usuario registrado
    public void mostrarUsuario() {
        Bundle datos = this.getIntent().getExtras();
        String variable_string = datos.getString("Usuario");
        textViewAdmin.setText("Hola " + variable_string);
    }
}
