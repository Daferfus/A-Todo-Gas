package com.example.daferfus_upv.btle;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daferfus_upv.btle.Activities.MainActivity;

import java.util.ArrayList;

public class Logros extends AppCompatActivity {

    private static final String TAG = "Logros";

    //variables
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDescriptions = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getImages();


        findViewById(R.id.linearAtras).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent (getApplicationContext(), MainActivity.class);
                i.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(i);
            }
        });
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add(R.drawable._2kpasos);
        mNames.add("2.000 pasos/día");
        mDescriptions.add("Camina 2.000 pasos en un día");

        mImageUrls.add(R.drawable._20km);
        mNames.add("20 Km/día");
        mDescriptions.add("Camina 2 kilometros en un día");

        mImageUrls.add(R.drawable._3gases);
        mNames.add("Captura 3 gases");
        mDescriptions.add("Captura 3 gases en un día");

        mImageUrls.add(R.drawable._50km);
        mNames.add("50 Km/Semana");
        mDescriptions.add("Camina 50 kilometros en un semana");


        mImageUrls.add(R.drawable._2km_h);
        mNames.add("2Km/h");
        mDescriptions.add("Camina a una velocidad de 2Km/h");

        mImageUrls.add(R.drawable.co2);
        mNames.add("No capturar CO2");
        mDescriptions.add("No captures nada de CO2 en un día");


        mImageUrls.add(R.drawable.invitar_amigos);
        mNames.add("Invita a 5 amigos");
        mDescriptions.add("Invita a 5 amigos a esta app");

        mImageUrls.add(R.drawable._10pasos);
        mNames.add("10 pasos/día");
        mDescriptions.add("Camina 10 pasos en un día");

        mImageUrls.add(R.drawable.invitar_amigos);
        mNames.add("Seguir @camlock_co");
        mDescriptions.add("Seguir a @camlock_co en instagram");

        initRecyclerViewEnProceso();
        initRecyclerViewPorHacer();
        initRecyclerViewConseguidos();

    }

    private void initRecyclerViewEnProceso(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewHorizontal = findViewById(R.id.recyclerViewEnProceso);
        recyclerViewHorizontal.setLayoutManager(layoutManagerHorizontal);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls, mDescriptions);
        recyclerViewHorizontal.setAdapter(adapter);
    }

    private void initRecyclerViewPorHacer(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewVertical = findViewById(R.id.recyclerViewPorHacer);
        recyclerViewVertical.setLayoutManager(layoutManagerVertical);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls, mDescriptions);
        recyclerViewVertical.setAdapter(adapter);
    }


    private void initRecyclerViewConseguidos(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewVertical = findViewById(R.id.recyclerViewConseguidos);
        recyclerViewVertical.setLayoutManager(layoutManagerVertical);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls, mDescriptions);
        recyclerViewVertical.setAdapter(adapter);
    }



    public String envioDatosEntreActividades(){
        Bundle datos = this.getIntent().getExtras();
        String variable_string = datos.getString("Usuario");
        return variable_string;
    }
}
