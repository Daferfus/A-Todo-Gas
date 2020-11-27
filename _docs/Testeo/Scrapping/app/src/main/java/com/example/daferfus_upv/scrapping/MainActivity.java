package com.example.daferfus_upv.scrapping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        LinearLayout view = new LinearLayout(this);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        WebView browser = new WebView(this);
        /* JavaScript must be enabled if you want it to work, obviously */
        browser.getSettings().setJavaScriptEnabled(true);


        /* Register a new JavaScript interface called HTMLOUT */
        browser.clearHistory();
        browser.clearCache(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setUseWideViewPort(false);
        browser.getSettings().setLoadWithOverviewMode(false);
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        /* WebViewClient must be set BEFORE calling loadUrl! */
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                /* This call inject JavaScript into the page which just finished loading. */
                //browser.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });
        /* load a web page */
        browser.loadUrl("https://webcat-web.gva.es/webcat_web/datosOnlineRvvcca/cargarDatosOnlineRvvcca");
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            browser.evaluateJavascript("obtenerEstacionesPorMunicipio('131','46')",null);
        }, 20000);

        final Handler handler2 = new Handler();
        handler2.postDelayed(() -> {
            browser.evaluateJavascript("document.getElementById('idEstacionMunicipio').onclick(this)", null);
        }, 40000);

        final Handler handler3 = new Handler();
        handler3.postDelayed(() -> {
            browser.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementById('tablaDatosOnlineMagnitudes').children[1].lastChild.cells[1].innerHTML);");
        }, 60000);

        browser.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        view.addView(browser);
        windowManager.addView(view, params);
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
        Log.d("TAG", html);
    }
}