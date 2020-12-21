package com.example.daferfus_upv.btle.Workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.ConstantesAplicacion;

public class InactividadWorker extends Worker {
    public InactividadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        if(ConstantesAplicacion.activo){
            ConstantesAplicacion.activo=false;
            Log.e("soy el worker","el sensor ta gucci tenga un buen dia");
            return Result.success();
        }else{
            Logica.actualizarSensor(ConstantesAplicacion.URL_ACTUALIZAR_SENSOR, "0");
            Log.e("soy el worker","el sensor ta mal");
            return  Result.success();
        }

    }
}