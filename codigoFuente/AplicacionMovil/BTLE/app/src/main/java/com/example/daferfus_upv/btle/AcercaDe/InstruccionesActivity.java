package com.example.daferfus_upv.btle.AcercaDe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.daferfus_upv.btle.Activities.MainActivity;
import com.example.daferfus_upv.btle.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class InstruccionesActivity extends AppCompatActivity {

    private ViewPager screenPager;
    InstruccionesViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Que la actividad ocupe toda la pantalla
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_instrucciones);

        //Escondemos la action bar si es necesario
        //getSupportActionBar().hide();

        //Iniciamos las vistas
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.acercade_anim_boton);
        tvSkip = findViewById(R.id.tv_skip);

        //Llenamos las interfaces
        final List<InterfazPantalla> mList = new ArrayList<>();
        mList.add(new InterfazPantalla("Controla tu Salud","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquidcommodi consequat.",R.drawable.img_1));
        mList.add(new InterfazPantalla("Multiples Opciones","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi.",R.drawable.img_2));
        mList.add(new InterfazPantalla("Mapa de Contaminación","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation.",R.drawable.img_3));

        //ViewPager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new InstruccionesViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //Tablayout con Viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //Botón Siguiente (Listener)
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == mList.size()-1) { //Cuando llegamos a la última pantalla

                    //Mostramos botón empezar y escondemos el indicador y el botón de siguiente
                    loaddLastScreen();
                }
            }
        });

        //Tablayout cambia a Listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loaddLastScreen();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        //Botón empezamos
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Abrimos MainActivity
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                mainActivity.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(mainActivity);

                finish();

            }
        });

        //Botón Saltar
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });
    }


    // Mostramos el botón Empezar y escondemos el indicador y el botón de Siguiente
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        //Lanzamos animación
        btnGetStarted.setAnimation(btnAnim);
    }


    public String envioDatosEntreActividades(){
        Bundle datos = this.getIntent().getExtras();
        String variable_string = datos.getString("Usuario");
        return variable_string;
    }
}

