package net.moddity.droidnubekit.errors;

import net.moddity.droidnubekit.responsemodels.DNKUnauthorizedResponse;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKAuthenticationRequiredException extends DNKException {

    private DNKUnauthorizedResponse unauthorizedResponse;

    public DNKAuthenticationRequiredException(int errorCode, DNKUnauthorizedResponse unauthorizedResponse) {
        super(errorCode);
        this.unauthorizedResponse = unauthorizedResponse;
    }

    public String getRedirectURL() {
        return unauthorizedResponse.getRedirectUrl();
    }

}
