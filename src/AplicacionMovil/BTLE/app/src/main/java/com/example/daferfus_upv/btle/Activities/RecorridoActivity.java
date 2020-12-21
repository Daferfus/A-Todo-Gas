package com.example.daferfus_upv.btle.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class RecorridoActivity extends AppCompatActivity {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    public static TextView valorDistancia;
    public static TextView valorPasos;
    ArrayList<BarEntry> barEntries = new ArrayList<>();

    public static final int[] MATERIAL_COLORS_NACHO = {
            rgb("#f6cd8f"), rgb("#edab47"), rgb("#f6cd8f"), rgb("#edab47")
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido);
        valorDistancia = (TextView) findViewById(R.id.textViewDistanciaKm);
        valorPasos = (TextView) findViewById(R.id.textViewDistanciaPasos);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Logica logica = new Logica(this);
        logica.consultarDistancia(ConstantesAplicacion.URL_CONSULTA_DISTANCIA, logica.getFecha());
        logica.consultarPasos(ConstantesAplicacion.URL_CONSULTA_PASOS, logica.getFecha());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        barChart = findViewById(R.id.barChart);

        getEntries();

        barDataSet = new BarDataSet(barEntries,"Data Set");
        barData = new BarData(barDataSet);

        barChart.setData(barData);

        barDataSet.setColors(MATERIAL_COLORS_NACHO);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(16f);
        Legend leg = barChart.getLegend();
        leg.setEnabled(false);
        Description des = barChart.getDescription();
        des.setEnabled(false);

        findViewById(R.id.linearAtras).setOnClickListener(v -> {
            Intent i = new Intent (getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });
    }
    private void getEntries(){

        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f,2000));
        barEntries.add(new BarEntry(2f,4000));
        barEntries.add(new BarEntry(3f,1800));
        barEntries.add(new BarEntry(5f,5000));
        barEntries.add(new BarEntry(6f,30000));
        barEntries.add(new BarEntry(7f,2000));


    }
}