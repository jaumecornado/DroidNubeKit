package net.moddity.droidnubekit.errors;

import net.moddity.droidnubekit.responsemodels.DNKUnauthorizedResponse;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKAuthenticationRequiredException extends DNKException {

    private DNKUnauthorizedResponse unauthorizedResponse;

    public DNKAuthenticationRequiredException(int errorCode, Throwable exception, DNKUnauthorizedResponse unauthorizedResponse) {
        super(errorCode, exception);
        this.unauthorizedResponse = unauthorizedResponse;
    }

    public String getRedirectURL() {
        return unauthorizedResponse.getRedirectUrl();
    }

}
