package net.moddity.droidnubekit.requests;

import net.moddity.droidnubekit.DroidNubeKit;
import net.moddity.droidnubekit.errors.DNKAuthenticationRequiredException;
import net.moddity.droidnubekit.errors.DNKException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jaume on 12/6/15.
 */
public interface DNKCallback<T> {
     void success(T t);

     void failure(DNKException error);
}
