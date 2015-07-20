package net.moddity.droidnubekit.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.moddity.droidnubekit.DroidNubeKit;
import net.moddity.droidnubekit.DroidNubeKitConstants;
import net.moddity.droidnubekit.interfaces.CloudKitWebViewRedirectHandler;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKWebViewAuthActivity extends Activity {

    private String urlRedirectPattern;

    private CloudKitWebViewRedirectHandler redirectHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        redirectHandler = DroidNubeKit.getInstance();

        final WebView webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(url != null && url.startsWith(urlRedirectPattern)) {
                    Log.d("CK", "Redirect found");
                    Uri redirectUri = Uri.parse(url);
                    redirectHandler.onRedirectFound(redirectUri);
                    finish();
                    return;
                }

                Log.d("CK", "Starting to load :"+url+" - "+getUrlRedirectPattern());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("CK", "Finishing to load: "+url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith(urlRedirectPattern)) {
                    Uri redirectUri = Uri.parse(url);
                    redirectHandler.onRedirectFound(redirectUri);
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        String redirectURL = getIntent().getExtras().getString(DroidNubeKitConstants.WEBVIEW_REDIRECT_URL_EXTRA);
        setUrlRedirectPattern(getIntent().getExtras().getString(DroidNubeKitConstants.WEBVIEW_REDIRECT_PATTERN_EXTRA));
        webView.loadUrl(redirectURL);
    }

    public String getUrlRedirectPattern() {
        return urlRedirectPattern;
    }

    public void setUrlRedirectPattern(String urlRedirectPattern) {
        this.urlRedirectPattern = urlRedirectPattern;
    }
}
