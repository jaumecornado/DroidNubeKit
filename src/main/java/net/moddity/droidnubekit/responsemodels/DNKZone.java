package net.moddity.droidnubekit.responsemodels;

import java.io.Serializable;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKZone implements Serializable {
    private String zoneName = "_defaultZone";

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
