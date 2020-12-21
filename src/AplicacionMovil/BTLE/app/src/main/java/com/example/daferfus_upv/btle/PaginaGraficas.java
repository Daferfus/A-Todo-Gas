package com.example.daferfus_upv.btle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.Activities.MainActivity;
import com.example.daferfus_upv.btle.POJOS.Lecturas;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class PaginaGraficas extends AppCompatActivity {

    LineChartView grafica;

    String[] axisData = {"10:40", "10:50", "11:00", "11:10", "11:20"};//datos ficticios de momento
    int[] yAxisData = {7, 8, 3, 5, 1};//datos ficticios valor

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grafica_mediciones);


        findViewById(R.id.linearAtras).setOnClickListener(v -> {
            Intent i = new Intent (getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });


        grafica = findViewById(R.id.chart);

        List<PointValue> yAxisValues = new ArrayList<>(); //lista eje y
        List<AxisValue> axisValues = new ArrayList<>();  //lista eje x

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));// la linea de la grafica se forma por la lista del eje y

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis xAxis = new Axis();
        xAxis.setName("Momento");
        xAxis.setValues(axisValues);
        xAxis.setTextSize(12);
        xAxis.setTextColor(Color.parseColor("#D4AF37"));
        data.setAxisXBottom(xAxis);

        Axis yAxis;

        List<AxisValue> axisValuesWeight = new ArrayList<>();
        for(int i = 0; i <= 8;i ++)   //PONEMOS EL VALOR MÁXIMO DE LA LISTA PARA CREAR EL EJE Y HASTA ESE VALOR
            axisValuesWeight.add(new AxisValue(i).setLabel(i + ""));
        yAxis = new Axis(axisValuesWeight);

        yAxis.setName("Valores de SO2");
        yAxis.setHasLines(true);
        yAxis.setTextColor(Color.parseColor("#D4AF37"));
        yAxis.setTextSize(12);

        data.setAxisYLeft(yAxis);

        grafica.setLineChartData(data);
        Viewport viewport = new Viewport(grafica.getMaximumViewport());
        viewport.top = 10;
        viewport.bottom = 0;
        viewport.right = yAxisData.length;
        grafica.setMaximumViewport(viewport);
        grafica.setCurrentViewport(viewport);

        ArrayList<Lecturas> datos = new ArrayList<>();
        ArrayList<String> datosString = new ArrayList<>();


        //Aqui va lo de la lista de datos mostrada

        for(int i = 0; i < yAxisData.length; i++){
            Lecturas lectura = new Lecturas(axisData[i],yAxisData[i]);

            String lecturaString = "Hora: " + lectura.getMomento() + "  Valor SO2: " + lectura.getValor();//creamos el string a mostrar en la lista
            datosString.add(lecturaString);

            datos.add(lectura);
        }

        ListView listView = findViewById(R.id.listview_graficas);

        ArrayAdapter<String> ADP = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,datosString);
        listView.setAdapter(ADP);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, datosString) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };

        listView.setAdapter((adapter));

    }

}
