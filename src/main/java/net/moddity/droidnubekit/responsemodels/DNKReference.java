package net.moddity.droidnubekit.responsemodels;

import java.io.Serializable;

/**
 * Created by jaume on 15/7/15.
 */
public class DNKReference implements Serializable {

    private String recordName;
    private DNKZone zoneID;
    private String action;

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public DNKZone getZoneID() {
        return zoneID;
    }

    public void setZoneID(DNKZone zoneID) {
        this.zoneID = zoneID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
