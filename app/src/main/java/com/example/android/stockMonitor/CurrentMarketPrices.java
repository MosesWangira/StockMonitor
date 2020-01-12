package com.example.android.stockMonitor;

import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/*
 *created by moses
 */
public class CurrentMarketPrices extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_market_prices);

        webView = (WebView)findViewById(R.id.market_prices);
        //WebViewClient enable launching about page within the app

        webView.setWebViewClient(new WebViewClient() {
            //loads html file if no internet
            public void onReceivedError(WebView webview, int errorCode, String description, String failingUrl) {
                webview.loadUrl("file:///android_asset/error.html");
            }
            public void onReceivedSslError (WebView webview, SslErrorHandler handler, SslError error){
                handler.proceed();
            }
        });

        webView.loadUrl("https://ke.priceroundup.com/mobile");
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    //enables going back instead of quitting the app
    public void onBackPressed() {
        //canGoBack checks if webView can go back in the history or if opened a different site
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}

