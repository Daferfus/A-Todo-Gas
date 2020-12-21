package com.example.daferfus_upv.btle.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.R;

import static com.example.daferfus_upv.btle.ConstantesAplicacion.URL_MAPA_COMPARTIR;

public class MapaActivity extends AppCompatActivity {

    Button compartirUbicacion;
    ImageView compartirUbicacionImagen;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Hola", "Entering onCreate");
        setContentView(R.layout.activity_mapa);
        WebView mWebView;
        mWebView = (WebView)findViewById(R.id.webViewMapa);
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(ConstantesAplicacion.URL_MAPA);
        Log.e("Hola","Exiting onCreate");

        compartirUbicacion= findViewById(R.id.btCompartirUbicacion);
        compartirUbicacionImagen = findViewById(R.id.imagenCompartir);
        compartirUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartirUbicacion();
            }
        });
        compartirUbicacionImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartirUbicacion();
            }
        });
    }


    public void compartirUbicacion(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, "¡Comparte este mapa para que los usuarios sepan donde hay zonas peligrosas!");
        Uri contentUri =  Uri.parse( "https://www.google.com/search?q=mapa+de+calor&safe=active&sxsrf=ALeKk03Q3q6uTbGCVDRpwRlg0cIjlFdbkg:1608224568251&tbm=isch&source=iu&ictx=1&fir=0nuuA4QrJuzfvM%252CgYULJpH-b5h1xM%252C_&vet=1&usg=AI4_-kQHbVkjuOlLJ9LONCpBZnFZ1Jbj4w&sa=X&ved=2ahUKEwjt6u-wv9XtAhVQyoUKHfIdCZAQ9QF6BAgWEAE#imgrc=0nuuA4QrJuzfvM");
        sendIntent.setData(contentUri);
        sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.putExtra(Intent.EXTRA_TEXT, URL_MAPA_COMPARTIR);
        sendIntent.setType("text/html");


        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }
}