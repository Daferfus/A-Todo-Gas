package com.example.daferfus_upv.btle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.daferfus_upv.btle.POJOS.StaggeredRecyclerViewAdapter;
import com.example.daferfus_upv.btle.R;

import java.util.ArrayList;

public class ConsejosActivity extends AppCompatActivity {

    private static final String TAG = "Consejos";
    private static final int NUM_COLUMNS = 2;

    private final ArrayList<String> mImageUrls = new ArrayList<>();
    private final ArrayList<String> mNamesConsejos = new ArrayList<>();
    private final ArrayList<String> mRedireccionUrl = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos);

        initImageBitmaps();

        findViewById(R.id.linearAtras).setOnClickListener(v -> {
            Intent i = new Intent (getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://img.freepik.com/vector-gratis/fondo-concepto-contaminacion-estilo-plano_23-2148222771.jpg?size=338&ext=jpg");
        mNamesConsejos.add("Como protegerse de la contaminacion");
        mRedireccionUrl.add("https://sostenibilidad.semana.com/medio-ambiente/articulo/consejos-practicos-para-protegerse-de-la-contaminacion-en-tu-ciudad/43729");

        mImageUrls.add("data:image/jpeg;base64,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");
        mNamesConsejos.add("Peligros de inhalar SO2");
        mRedireccionUrl.add("https://www.saludgeoambiental.org/dioxido-azufre-so2");

        mImageUrls.add("https://www.habbitus.com/wp-content/uploads/2018/06/imagen-post-pasos.jpg");
        mNamesConsejos.add("Ventajas caminar 10k pasos al día");
        mRedireccionUrl.add("https://www.ticbeat.com/salud/10-cosas-pasan-cuerpo-10000-pasos-dia/");

        mImageUrls.add("https://image.freepik.com/vector-gratis/personaje-feliz-ganando-premio-diseno-plano_23-2147873122.jpg");
        mNamesConsejos.add("Consigue más logros");
        mRedireccionUrl.add("https://www.importancia.org/superacion-personal.php");


        mImageUrls.add("https://www.foroambiental.com.mx/wp-content/uploads/2020/05/huella-de-carbono.jpg");
        mNamesConsejos.add("Como reducir tu huella de carbono");
        mRedireccionUrl.add("https://www.ngenespanol.com/naturaleza/10-formas-de-reducir-tu-huella-de-carbono/");


        initRecyclerViewConsejos();

    }

    private void initRecyclerViewConsejos(){
        Log.d(TAG, "initRecyclerView: initializing staggered recyclerview.");

        RecyclerView recyclerViewConsejos = findViewById(R.id.recyclerViewConsejos);
        StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter =
                new StaggeredRecyclerViewAdapter(this, mNamesConsejos, mImageUrls, mRedireccionUrl);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerViewConsejos.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewConsejos.setAdapter(staggeredRecyclerViewAdapter);
    }
}