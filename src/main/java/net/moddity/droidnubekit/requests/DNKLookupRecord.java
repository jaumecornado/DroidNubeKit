package net.moddity.droidnubekit.requests;

import java.util.List;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKLookupRecord {
    private String recordName;
    private List<String> desiredKeys;

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public List<String> getDesiredKeys() {
        return desiredKeys;
    }

    public void setDesiredKeys(List<String> desiredKeys) {
        this.desiredKeys = desiredKeys;
    }
}
