package com.example.daferfus_upv.btle.BD;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyApiService {

    /*@GET("diseases")
    Call<DiseasesResponse> getDiseases();
*/
    @FormUrlEncoded
    @POST("lecturasREST.php")
    Call<Lectura> guardarLecturaEnServidor(
            @Field("dia") String dia,
            @Field("hora") String hora,
            @Field("ubicacion") String ubicacion,
            @Field("valor") String valor,
            @Field("idMagnitud") String idMagnitud,
            @Field("idUsuario") String idUsuario
    );

    @FormUrlEncoded
    @POST("calibrarSensor.php")
    Call<String> calibrarSensor(
            @Field("idUsuario") String idUsuario,
            @Field("desviacion") String desviacion
    );

    @FormUrlEncoded
    @POST("lecturasEstacionREST.php")
    Call<String> guardarLecturaEstacion(
            @Field("dia") String dia,
            @Field("hora") String hora,
            @Field("ubicacion") String ubicacion,
            @Field("valor") String valor,
            @Field("idMagnitud") String idMagnitud
    );

    @GET("lecturasEstacionREST.php")
    Call<String> consultarLecturaEstacion(
            @Query("dia") String dia,
            @Query("hora") String hora,
            @Query("ubicacion") String ubicacion
    );

    @GET("consultar_desviacion.php")
    Call<String> consultarDesviacion(
            @Query("idUsuario") String idUsuario
    );
}