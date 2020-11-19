package com.example.daferfus_upv.btle.Utilidades;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

public class NotificationUtils extends ContextWrapper {
    //Maneja las notificaciones
    private NotificationManager mManager;
    //ID canal importancia "baja"
    public static final String ANDROID_CHANNEL_ID = "gti.adguez27.proyecto.ANDROID";
    //ID canal importancia alta
    public static final String IOS_CHANNEL_ID = "gti.adguez27.proyecto.IOS";
    //Nombre canal importancia "baja"
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    //Nombre canal importancia alta
    public static final String IOS_CHANNEL_NAME = "IOS CHANNEL";
    // ------------------------------------------------------------------------------------------------------------------------------
    //Constructor
    /* context ---->
                     NotificationUtils()
                                            ---->
     */
    // ------------------------------------------------------------------------------------------------------------------------------
    public NotificationUtils(Context context) {
        super(context);
        //Creamos los canales de notificaciones
        createChannels();
    }
    // ------------------------------------------------------------------------------------------------------------------------------
    //Método para crear los canales de notificaciones a través de los cuales poder publicarlas
    /*  ---->
                     NotificationUtils()
                                            ---->
     */
    // ------------------------------------------------------------------------------------------------------------------------------
    public void createChannels() {

        // Crear canal de baja prioridad
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Decide si usar luces o no
        androidChannel.enableLights(true);
        // Decide si usar vibración o no
        androidChannel.enableVibration(true);
        // Decide el color de la luz
        androidChannel.setLightColor(Color.GREEN);
        // Decide si aparece la notificación en la pantalla de bloqueo o no
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);

        // Crear canal de alta prioridad
        NotificationChannel iosChannel = new NotificationChannel(IOS_CHANNEL_ID,
                IOS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        iosChannel.enableLights(true);
        iosChannel.enableVibration(true);
        iosChannel.setLightColor(Color.GRAY);
        iosChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(iosChannel);
    }
    // ------------------------------------------------------------------------------------------------------------------------------
    //Get notifiacion manager
    /*  ---->
                     getManager()
                                           ---->NotificationManager
     */
    // ------------------------------------------------------------------------------------------------------------------------------
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    // ------------------------------------------------------------------------------------------------------------------------------
    //Get notificacion de baja prioridad
    /* title, body ---->
                     getAndroidChannelNotification()
                                                      ---->Notification.Builder
     */
    // ------------------------------------------------------------------------------------------------------------------------------
    public Notification.Builder getAndroidChannelNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }
    // ------------------------------------------------------------------------------------------------------------------------------
    //Get notificacion de alta prioridad
    /* title, body ---->
                     getIosChannelNotification()
                                                 ---->Notification.Builder
     */
    // ------------------------------------------------------------------------------------------------------------------------------
    public Notification.Builder getIosChannelNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), IOS_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }
    // ------------------------------------------------------------------------------------------------------------------------------
    //Método para publicar notificaciones de alta importancia
    /* id, title, body ---->
                         notificarAltaImportancia()
                                                    ---->
     */
    // ------------------------------------------------------------------------------------------------------------------------------
    public void notificarAltaImportancia (int id, String title, String body){
        Notification.Builder nb = this.
                getIosChannelNotification(title,body);

        this.getManager().notify(id, nb.build());
    }
    // ------------------------------------------------------------------------------------------------------------------------------
    //Método para publicar notificaciones de baja importancia
    /* id, title, body ----> notificarBajaImportancia()
                                                         ---->
     */
    // ------------------------------------------------------------------------------------------------------------------------------
    public void notificarBajaImportancia (int id, String title, String body){
        Notification.Builder nb = this.
                getAndroidChannelNotification(title,body);

        this.getManager().notify(id, nb.build());
    }
    // ------------------------------------------------------------------------------------------------------------------------------
    //Método para mandar notificaciones de test
    /*             ---->
                          test()
                                 ---->
     */
    // ------------------------------------------------------------------------------------------------------------------------------
    public void test (){
        Notification.Builder nb = this.
                getAndroidChannelNotification("hola","esto es una notificacion");

        this.getManager().notify(101, nb.build());
    }
}
