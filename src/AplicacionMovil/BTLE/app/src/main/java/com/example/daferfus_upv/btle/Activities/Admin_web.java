package com.example.daferfus_upv.btle.Activities;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.R;

public class Admin_web extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_web);

        Bundle bundle=getIntent().getExtras();
        String seccion = bundle.getString("seccion");
        String url = null;
        if(seccion.equals("Usuarios")){
            url="http://192.168.1.16:81/pruebaWeb3/Frontend/usuarios.php";
        }else if(seccion.equals("Sensores")){
            url="http://192.168.1.16:81/pruebaWeb3/Frontend/sensores.php";
        }
        webView=findViewById(R.id.webViewUsuarios);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}