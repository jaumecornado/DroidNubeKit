package net.moddity.droidnubekit.requests;

import net.moddity.droidnubekit.DroidNubeKit;
import net.moddity.droidnubekit.errors.DNKAuthenticationRequiredException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jaume on 12/6/15.
 */
public abstract class DNKCallback<T> implements Callback<T> {
    @Override
    public void success(T t, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {
        if(error.getCause().getClass().equals(DNKAuthenticationRequiredException.class)) {
            DNKAuthenticationRequiredException authenticationRequiredException = (DNKAuthenticationRequiredException)error.getCause();
            DroidNubeKit.showAuthDialog(authenticationRequiredException.getRedirectURL());
        }
    }
}
