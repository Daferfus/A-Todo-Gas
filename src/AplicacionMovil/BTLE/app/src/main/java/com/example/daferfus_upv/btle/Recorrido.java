package com.example.daferfus_upv.btle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daferfus_upv.btle.Activities.MainActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class Recorrido extends AppCompatActivity {

    private static final String TAG = "Recorrido";
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    public static final int[] MATERIAL_COLORS_NACHO = {
            rgb("#f6cd8f"), rgb("#edab47"), rgb("#f6cd8f"), rgb("#edab47")
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido);
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

        findViewById(R.id.linearAtras).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent (getApplicationContext(), MainActivity.class);
                i.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(i);
            }
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


    public String envioDatosEntreActividades(){
        Bundle datos = this.getIntent().getExtras();
        String variable_string = datos.getString("Usuario");
        return variable_string;
    }
}

