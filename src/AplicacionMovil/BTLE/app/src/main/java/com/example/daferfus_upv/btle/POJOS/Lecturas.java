package com.example.daferfus_upv.btle.POJOS;

public class Lecturas {

    private String momento;
    private int valor;

    // --------------------------------------------------------------
    //                  Texto, Z ->
    //                  constructor() <-
    //                  <- 3 Textos, 2N
    //
    public Lecturas(String momento, int valor) {
        this.momento = momento;
        this.valor = valor;
    }//constructor

    // --------------------------------------------------------------
    // Getters/Setters
    // --------------------------------------------------------------
    public String getMomento() {
        return momento;
    }

    public int getValor() {
        return valor;
    }

    public void setMomento(String momento){
        this.momento = momento;
    }

    public void setValor(int valor){
        this.valor = valor;
    }

}//class
