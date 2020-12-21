package com.example.daferfus_upv.btle.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.daferfus_upv.btle.BD.VolleySingleton;
import com.example.daferfus_upv.btle.MyApplication;
import com.example.daferfus_upv.btle.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ConsultasActivity extends AppCompatActivity {

    TextView tvDistancia;
    TextView tvPasos;
    TextView tvLogros;
    TextView tvSensores;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas);
        mostrarConsultas("http://192.168.1.16:81/Web1/Backend/consultar_logrosUsuario.php",1);
        mostrarConsultas("http://192.168.1.16:81/Web1/Backend/consultar_pasosRecorridos.php",2);
        mostrarConsultas("http://192.168.1.16:81/Web1/Backend/consultar_distanciaRecorrida.php",3);
        mostrarConsultas("http://192.168.1.16:81/Web1/Backend/consultar_sensoresActivos.php",4);
    }

    public void mostrarConsultas(String URL, int c) {
        tvDistancia = findViewById(R.id.tvDistanciaRecorrida);
        tvLogros= findViewById(R.id.tvLogrosUsuario);
        tvPasos= findViewById(R.id.tvPasosRecorridos);
        tvSensores= findViewById(R.id.tvSensores);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if (!response.isEmpty()) {
                //Usamos el gson de google
                Gson gson = new Gson();
               String resultado = gson.toJson(response);
                Log.d("----ConsultasActivity", resultado);
               if(c==1){
                   tvLogros.setText(resultado);
               }
               if(c==2){
                    tvPasos.setText(resultado);
               }
                if(c==3){
                    tvDistancia.setText(resultado);
                }
                if(c==4){
                    tvSensores.setText(resultado);
                }

            }else{
                Log.d("----ConsultasActivity", "mal");
            }
        }, error -> Toast.makeText(ConsultasActivity.this, error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros= new HashMap<>();
                return parametros;
            }
        };
        VolleySingleton.tomarInstancia(MyApplication.getAppContext()).anyadirAColaPeticiones(stringRequest);
    }
}
