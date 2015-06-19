package net.moddity.droidnubekit.interfaces;

import android.net.Uri;

/**
 * Created by jaume on 18/6/15.
 */
public interface CloudKitWebViewRedirectHandler {
    void onRedirectFound(Uri redirectUri);
}
