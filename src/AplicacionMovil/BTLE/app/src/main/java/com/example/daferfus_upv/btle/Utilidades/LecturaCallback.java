package com.example.daferfus_upv.btle.Utilidades;

public interface LecturaCallback {

    void hacerScrapping();
    void crearCopiaDeSeguridad();
    void cogerDesviacion(String desviacion);
    void actualizarDesviacion(String valorLecturaEstacion, String valorLectura);
    void onFailure();
}
