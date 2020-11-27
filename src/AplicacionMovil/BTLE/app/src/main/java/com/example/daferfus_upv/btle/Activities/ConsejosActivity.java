package com.example.daferfus_upv.btle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNamesConsejos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos);

        initImageBitmaps();

        findViewById(R.id.linearAtras).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent (getApplicationContext(), MainActivity.class);
                i.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(i);
            }
        });
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://www.promofarma.com/blog/wp-content/uploads//2016/01/160122_LavarselosDientes.png");
        mNamesConsejos.add("Lavate los dientes mucho");

        mImageUrls.add("https://image.freepik.com/vector-gratis/limpio-lavate-manos-agua-jabon_23-2148515332.jpg");
        mNamesConsejos.add("Las manos también");

        mImageUrls.add("https://www.habbitus.com/wp-content/uploads/2018/06/imagen-post-pasos.jpg");
        mNamesConsejos.add("Mínimo 10000 pasos al día");

        mImageUrls.add("https://st4.depositphotos.com/1397034/27369/i/600/depositphotos_273693354-stock-photo-safety-virus-infection-concept-man.jpg");
        mNamesConsejos.add("Evita gasos tóxicos");


        mImageUrls.add("https://img.freepik.com/vector-gratis/empresario-corriendo-mas-logros-recompensas_7534-300.jpg?size=626&ext=jpg");
        mNamesConsejos.add("Consigue más logros");

        mImageUrls.add("https://storage.lacapitalmdp.com/2019/10/Caminar-Calle-1024x683.jpg");
        mNamesConsejos.add("Aspavil! A caminar!!");


        mImageUrls.add("https://laalegriadelamor.com/wp-content/uploads/2019/03/5ayudamutua.jpg");
        mNamesConsejos.add("Siempre que pueda ayuda");

        mImageUrls.add("https://disenowebakus.net/imagenes/articulos/offline-online-conexion-marketing-tradicional-digital-internet.jpg");
        mNamesConsejos.add("Comprueba la conexión");


        initRecyclerViewConsejos();

    }

    private void initRecyclerViewConsejos(){
        Log.d(TAG, "initRecyclerView: initializing staggered recyclerview.");

        RecyclerView recyclerViewConsejos = findViewById(R.id.recyclerViewConsejos);
        StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter =
                new StaggeredRecyclerViewAdapter(this, mNamesConsejos, mImageUrls);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerViewConsejos.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewConsejos.setAdapter(staggeredRecyclerViewAdapter);
    }

    public String envioDatosEntreActividades(){
        Bundle datos = this.getIntent().getExtras();
        String variable_string = datos.getString("Usuario");
        return variable_string;
    }
}