package com.example.daferfus_upv.btle.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.daferfus_upv.btle.POJOS.Usuario;
import com.example.daferfus_upv.btle.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.daferfus_upv.btle.ConstantesAplicacion.ID_USUARIO;
import static com.example.daferfus_upv.btle.ConstantesAplicacion.URL_VALIDAR_USUARIO;

public class LoginActivity extends AppCompatActivity {

    //Valores login
    EditText editTextEmail;
    EditText editTextContrasenya;
    Button botonLogin;
    TextView iniciarSesionInvitado;
    String email;
    String contrasenya;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean sesion = cargarPreferencias();
        if (sesion && !email.equals("admin@admin.com")) {
            Log.d("login: ", "----entrando en main activity");
            List<String> emailContra=LoginActivity.cargarPreferenciasString(getApplicationContext());
           ID_USUARIO = emailContra.get(0);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if (sesion) {
            Log.d("login: ", "----entrando en admin");
            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(intent);
        } else {
            Log.d("login: ", "----entrando en login activity");
            setContentView(R.layout.login_activity);
            botonLogin = findViewById(R.id.buttonIniciarSesion);
            iniciarSesionInvitado = findViewById(R.id.textViewIniciarSesionInvitado);
            iniciarSesionInvitado.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), InvitadoActivity.class);
                intent.putExtra("Usuario", "invitado");
                startActivity(intent);
            });
            botonLogin.setOnClickListener(view -> {
                Toast.makeText(LoginActivity.this, "Bienvendio: usuario", Toast.LENGTH_SHORT).show();
                validarUsuario(URL_VALIDAR_USUARIO);
            });
        }

    }

    /* T ----->
                validarUsuario
                                ---->
     */
    public void validarUsuario(String URL) {
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextContrasenya = findViewById(R.id.editTextContrasenyaLogin);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if (!response.isEmpty()) {
                //Usamos el gson de google
                Gson gson = new Gson();
                //Usuario user = new Usuario();
                Usuario user = gson.fromJson(response, Usuario.class);

                Class<?> claseIntent = (user.getIdUsuario().equals("admin@admin.com") && user.getContrasenya().equals("admin")) ?
                        AdminActivity.class : MainActivity.class;
                ID_USUARIO = user.getIdUsuario();
                guardarPreferencias(user.getNombre());

                Intent intent = new Intent(getApplicationContext(), claseIntent);
                //intent.putExtra("Usuario", user.getNombre());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, R.string.textoContrasenyaOEmailIncorrecto, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idUsuario", editTextEmail.getText().toString());
                parametros.put("contrasenya", editTextContrasenya.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    //Guarda en el archivo credenciales el email y conrasenya
    public void guardarPreferencias( String usuario) {
        SharedPreferences preferences = getSharedPreferences("Credenciales", MODE_PRIVATE);

        String email = editTextEmail.getText().toString();
        String contrasenya = editTextContrasenya.getText().toString();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("contrasenya", contrasenya);
        editor.putString("usuario", usuario);
        editor.putBoolean("sesionIniciada", true);
        editor.apply();
    }

    //Muestra los datos guardados en el archivo
    public boolean cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Credenciales", MODE_PRIVATE);
        email = preferences.getString("email", "No existe informacion");
        contrasenya = preferences.getString("contrasenya", "No existe informacion");
        usuario = preferences.getString("usuario", "No existe informacion");
        return preferences.getBoolean("sesionIniciada", false);

    }

    // funcion para cerrar sesion
    public static void cerrarSesion(Context c, boolean b) {
        SharedPreferences preferences = c.getSharedPreferences("Credenciales", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("sesionIniciada", b);
        editor.apply();
    }

    //muestra en la posicion 0 el email y en la 1 la contrasenya y en la 2 el usuario
    public static List<String> cargarPreferenciasString(Context c) {
        SharedPreferences preferences = c.getSharedPreferences("Credenciales", MODE_PRIVATE);
        String email = preferences.getString("email", "No existe informacion");
        String contrasenya = preferences.getString("contrasenya", "No existe informacion");
        String usuario = preferences.getString("usuario", "No existe informacion");
        List<String> emailContrasenya = new ArrayList<String>();
        emailContrasenya.add(email);
        emailContrasenya.add(contrasenya);
        emailContrasenya.add(usuario);
        return emailContrasenya;
    }
}
