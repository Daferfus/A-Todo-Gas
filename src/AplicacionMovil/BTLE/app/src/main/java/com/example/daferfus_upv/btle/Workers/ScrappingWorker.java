package com.example.daferfus_upv.btle.Workers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.daferfus_upv.btle.BD.Logica;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static android.content.Context.WINDOW_SERVICE;

public class ScrappingWorker extends Worker {
    private static WindowManager windowManager;
    private static WindowManager.LayoutParams params;
    @SuppressLint("StaticFieldLeak")
    private static LinearLayout view;
    @SuppressLint("StaticFieldLeak")
    private static WebView browser;

    @SuppressLint("StaticFieldLeak")
    public static Logica mDBHelper;
    public SQLiteDatabase mDb;
    public Context contexto;
    @SuppressLint("SetJavaScriptEnabled")
    public ScrappingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
            windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            view = new LinearLayout(context);
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            new Handler(Looper.getMainLooper()).post(() -> {
                browser = new WebView(getApplicationContext());
                /* JavaScript must be enabled if you want it to work, obviously */
                browser.getSettings().setJavaScriptEnabled(true);


                /* Register a new JavaScript interface called HTMLOUT */
                browser.clearHistory();
                browser.clearCache(true);
                browser.getSettings().setBuiltInZoomControls(true);
                browser.getSettings().setSupportZoom(true);
                browser.getSettings().setUseWideViewPort(false);
                browser.getSettings().setLoadWithOverviewMode(false);
                browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                /* load a web page */
                browser.loadUrl("https://webcat-web.gva.es/webcat_web/datosOnlineRvvcca/cargarDatosOnlineRvvcca");
                browser.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if(view!=null) {
                    view.addView(browser);
                    windowManager.addView(view, params);
                }
            });

            // Se inicializa la base de datos...
            mDBHelper = new Logica(getApplicationContext());

            // ...la actualiza...
            try {
                mDBHelper.actualizarBaseDatos();
            } catch (IOException mIOException) {
                throw new Error("Incapaz de Actualizar Base de Datos");
            }

            // ...y coge su referencia para escritura.
            try {
                mDb = mDBHelper.getWritableDatabase();
            } catch (SQLException mSQLException) {
                Log.d("Base de Datos: ", mSQLException.toString());
                throw mSQLException;
            }
            contexto = getApplicationContext();
    }

    @NonNull
    @Override
    public Result doWork() {
        new Handler(Looper.getMainLooper()).post(() -> {

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                Log.d("ValorScrapping", "Paso 1");
                browser.evaluateJavascript("obtenerEstacionesPorMunicipio('131','46')",null);
            }, 20000);

            final Handler handler2 = new Handler();
            handler2.postDelayed(() -> {
                Log.d("ValorScrapping", "Paso 2");
                browser.evaluateJavascript("document.getElementById('idEstacionMunicipio').onclick(this)", null);
            }, 40000);

            final Handler handler3 = new Handler();
            handler3.postDelayed(() -> {
                Log.d("ValorScrapping", "Paso 3");
                browser.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementById('tablaDatosOnlineMagnitudes').children[1].lastChild.cells[1].innerHTML);");
                browser.destroy();
            }, 60000); });
        return Result.success();
    }
}
/* An instance of this class will be registered as a JavaScript interface */
class MyJavaScriptInterface
{
    @JavascriptInterface
    @SuppressWarnings("unused")
    public void processHTML(String html)
    {

        // process the html as needed by the app
        Log.d("ValorScrapping", html);
        DateTimeFormatter dtfDia = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime nowDia = LocalDateTime.now();

        String dia =  dtfDia.format(nowDia);

        DateTimeFormatter dtfHora = DateTimeFormatter.ofPattern("HH");
        LocalDateTime nowHora = LocalDateTime.now();

        String hora =  dtfHora.format(nowHora);
        ScrappingWorker.mDBHelper.guardarLecturaEstacion(dia, hora,"38.9688889 - -0.1902778", html, "SO2");
    }
}