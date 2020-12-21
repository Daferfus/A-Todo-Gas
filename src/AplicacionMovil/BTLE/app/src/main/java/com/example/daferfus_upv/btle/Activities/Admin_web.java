package com.example.daferfus_upv.btle.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.R;

public class Admin_web extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_web);

        Bundle bundle=getIntent().getExtras();
        String seccion = bundle.getString("seccion");
        String url = null;
        if(seccion.equals("Usuarios")){
            url= ConstantesAplicacion.URL_ADMIN_USUARIOS;
        }else if(seccion.equals("Sensores")){
            url=ConstantesAplicacion.URL_ADMIN_SENSORES;
        }
        WebView webView = findViewById(R.id.webViewUsuarios);
        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);

        String htmlData = "";
        htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + htmlData;
        //Poner el css en /assets/style.css
        webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
    }
}