package net.moddity.droidnubekit.requests;

import net.moddity.droidnubekit.responsemodels.DNKRecord;
import net.moddity.droidnubekit.responsemodels.DNKZone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaume on 14/7/15.
 */
public class DNKRecordLookupRequest {
    private List<DNKLookupRecord> records;
    private DNKZone zoneID;
    private List<String> desiredKeys;

    public List<DNKLookupRecord> getRecords() {
        return records;
    }

    public void setRecords(List<DNKLookupRecord> records) {
        this.records = records;
    }

    public DNKZone getZoneID() {
        return zoneID;
    }

    public void setZoneID(DNKZone zoneID) {
        this.zoneID = zoneID;
    }

    public List<String> getDesiredKeys() {
        return desiredKeys;
    }

    public void setDesiredKeys(List<String> desiredKeys) {
        this.desiredKeys = desiredKeys;
    }

    public static DNKRecordLookupRequest createSingleRecordRequest(String recordName) {
        DNKLookupRecord lookupRecord = new DNKLookupRecord();
        lookupRecord.setRecordName(recordName);

        DNKRecordLookupRequest request = new DNKRecordLookupRequest();

        List<DNKLookupRecord> records = new ArrayList<>();
        records.add(lookupRecord);

        request.setRecords(records);

        return request;
    }
}
