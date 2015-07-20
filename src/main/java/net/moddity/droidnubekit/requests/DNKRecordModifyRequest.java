package net.moddity.droidnubekit.requests;

import net.moddity.droidnubekit.objects.DNKObject;
import net.moddity.droidnubekit.responsemodels.DNKRecord;
import net.moddity.droidnubekit.responsemodels.DNKZone;
import net.moddity.droidnubekit.utils.DNKOperationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaume on 14/7/15.
 */
public class DNKRecordModifyRequest {
    private List<DNKRecordOperation> operations;
    private DNKZone zoneID;
    private Boolean atomic;
    private List<String> desiredKeys;

    public List<DNKRecordOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<DNKRecordOperation> operations) {
        this.operations = operations;
    }

    public DNKZone getZoneID() {
        return zoneID;
    }

    public void setZoneID(DNKZone zoneID) {
        this.zoneID = zoneID;
    }

    public Boolean getAtomic() {
        return atomic;
    }

    public void setAtomic(Boolean atomic) {
        this.atomic = atomic;
    }

    public List<String> getDesiredKeys() {
        return desiredKeys;
    }

    public void setDesiredKeys(List<String> desiredKeys) {
        this.desiredKeys = desiredKeys;
    }


    public static DNKRecordModifyRequest createRequest(DNKObject record, DNKOperationType operationType) {
        List<DNKRecord> records = new ArrayList<>();
        records.add(record.toRecord());
        return createRequest(records, operationType);
    }


    public static DNKRecordModifyRequest createRequest(List<DNKRecord> records, DNKOperationType operationType) {

        List<DNKRecordOperation> operations = new ArrayList<>();

        for(DNKRecord record : records) {
            DNKRecordOperation recordOperation = new DNKRecordOperation();
            recordOperation.setOperationType(operationType.toString());
            recordOperation.setRecord(record);
            operations.add(recordOperation);
        }

        DNKRecordModifyRequest request = new DNKRecordModifyRequest();

        request.setOperations(operations);

        return request;
    }

}
