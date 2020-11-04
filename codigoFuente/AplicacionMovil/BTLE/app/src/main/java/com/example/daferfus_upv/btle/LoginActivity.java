package com.example.daferfus_upv.btle;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //Valores login
    EditText editTextEmail;
    EditText editTextContrasenya;
    Button botonLogin;
    TextView iniciarSesionInvitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        botonLogin = findViewById(R.id.buttonIniciarSesion);
        iniciarSesionInvitado=findViewById(R.id.textViewIniciarSesionInvitado);
        iniciarSesionInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Usuario", "invitado");
                startActivity(intent);
            }
        });
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Bienvendio: usuario"  , Toast.LENGTH_SHORT).show();
                validarUsuario("http://192.168.1.16:81/Web/validar_usuario.php");
            }
        });
    }

    /* T ----->
                validarUsuario
                                ---->
     */
    public void validarUsuario(String URL) {
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextContrasenya = findViewById(R.id.editTextContrasenyaLogin);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    ///JSONObject object = new JSONObject(response); //Creamos un objeto JSON a partir de la cadena
                    //JSONArray json_array = object.optJSONArray("frutas"); //cogemos cada uno de los elementos dentro de la etiqueta "frutas"
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Log.d("Response: ", response);
                    intent.putExtra("Usuario", response);

                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, R.string.textoContrasenyaOEmailIncorrecto, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros= new HashMap<String,String>();
                parametros.put("idUsuario", editTextEmail.getText().toString());
                parametros.put("contrasenya", editTextContrasenya.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
