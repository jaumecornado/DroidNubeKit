package net.moddity.droidnubekit.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.moddity.droidnubekit.DroidNubeKitConstants;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKWebViewAuthActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final WebView webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("CK", "Starting to load :"+url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("CK", "Finishing to load: "+url);
            }
        });
        String redirectURL = getIntent().getExtras().getString(DroidNubeKitConstants.WEBVIEW_REDIRECT_URL_EXTRA);
        webView.loadUrl(redirectURL);
    }

    public void loadRedirectURL(String redirectURL) {

    }

}
