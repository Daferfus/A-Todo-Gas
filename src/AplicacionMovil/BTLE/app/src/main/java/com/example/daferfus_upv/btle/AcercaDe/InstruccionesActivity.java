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

import com.example.daferfus_upv.btle.Activities.InvitadoActivity;
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
        mList.add(new InterfazPantalla("Controla tu Salud","Una vez tengas el sensor y esta aplicación tan solo tienes que encender el sensor cerca del telefono  y abrir esta aplicanción. Inicia Sesión y ya puedes empezar a obtener todos tus datos.",R.drawable.img_1));
        mList.add(new InterfazPantalla("Multiples Opciones","Pinchando en el panel superior accedes a un mapa de gases el cual te da información de tu zona. Pero esto no es todo, pulsa los botonos de abajo y descubre todas sus funcionalidades",R.drawable.img_2));
        mList.add(new InterfazPantalla("Monitoriza tus Logros","Si eres un amante del deporte, en el apartado de 'Recorrido' puedes ver todas tus metricas diarias además de motivarte con los retos del apartado 'Logros'.",R.drawable.img_4));
        mList.add(new InterfazPantalla("Mapa de Contaminación","Una vez en el mapa comprueba que zonas tienen más contaminación y evitalas. Puedes compartir ubicaciones con alto nivel de contaminación para que tus conocidos las eviten.",R.drawable.img_3));

        //ViewPager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new InstruccionesViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //Tablayout con Viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //Botón Siguiente (Listener)
        btnNext.setOnClickListener(v -> {

            position = screenPager.getCurrentItem();
            if (position < mList.size()) {

                position++;
                screenPager.setCurrentItem(position);
            }

            if (position == mList.size()-1) { //Cuando llegamos a la última pantalla

                //Mostramos botón empezar y escondemos el indicador y el botón de siguiente
                loaddLastScreen();
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
        btnGetStarted.setOnClickListener(v -> {

            if(envioDatosEntreActividades().equals("invitado")){
                //Abrimos Invitado
                Intent inivitadoActivity = new Intent(getApplicationContext(), InvitadoActivity.class);
                inivitadoActivity.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(inivitadoActivity);
            }else {
                //Abrimos MainActivity
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                mainActivity.putExtra("Usuario", envioDatosEntreActividades());
                startActivity(mainActivity);
            }

            finish();

        });

        //Botón Saltar
        tvSkip.setOnClickListener(v -> screenPager.setCurrentItem(mList.size()));
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
        return datos.getString("Usuario");
    }
}

