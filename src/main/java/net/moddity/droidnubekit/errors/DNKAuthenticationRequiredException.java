package net.moddity.droidnubekit.errors;

import net.moddity.droidnubekit.responsemodels.DNKUnauthorizedResponse;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKAuthenticationRequiredException extends Exception {

    private DNKUnauthorizedResponse unauthorizedResponse;

    public DNKAuthenticationRequiredException(DNKUnauthorizedResponse unauthorizedResponse) {
        this.unauthorizedResponse = unauthorizedResponse;
    }

    public String getRedirectURL() {
        return unauthorizedResponse.getRedirectUrl();
    }

}
