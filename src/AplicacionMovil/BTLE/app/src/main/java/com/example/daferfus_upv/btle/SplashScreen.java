package com.example.daferfus_upv.btle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.Activities.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    //Variables animaciones
    Animation animArriba, animAbajo;
    ImageView imagen;
    TextView eslogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        //Animaciones
        animArriba = AnimationUtils.loadAnimation(this, R.anim.arriba_splash_anim);
        animAbajo = AnimationUtils.loadAnimation(this, R.anim.abajo_splash_anim);

        //Hooks
        imagen = findViewById(R.id.imagenSplash);
        eslogan = findViewById(R.id.textViewSplash);

        imagen.setAnimation(animArriba);
        eslogan.setAnimation(animAbajo);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
    }

}
