package net.moddity.droidnubekit.requests;

import net.moddity.droidnubekit.responsemodels.DNKZone;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKRecordQueryRequest {

    private DNKZone zoneID;
    private String resultsLimit;
    private DNKQuery query;
    //TODO continuationMarker
    //TODO desiredKeys
    //TODO zoneWide


    public DNKZone getZoneID() {
        return zoneID;
    }

    public void setZoneID(DNKZone zoneID) {
        this.zoneID = zoneID;
    }

    public String getResultsLimit() {
        return resultsLimit;
    }

    public void setResultsLimit(String resultsLimit) {
        this.resultsLimit = resultsLimit;
    }

    public DNKQuery getQuery() {
        return query;
    }

    public void setQuery(DNKQuery query) {
        this.query = query;
    }

    public static DNKRecordQueryRequest queryWithRecordName(String recordType) {
        DNKQuery query = new DNKQuery();
        query.setRecordType(recordType);

        DNKRecordQueryRequest recordQueryRequest = new DNKRecordQueryRequest();
        recordQueryRequest.setZoneID(new DNKZone());
        recordQueryRequest.setQuery(query);
        return recordQueryRequest;
    }
}
