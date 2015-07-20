package net.moddity.droidnubekit.requests;

import net.moddity.droidnubekit.responsemodels.DNKRecord;

import java.util.List;

/**
 * Created by jaume on 14/7/15.
 */
public class DNKRecordOperation {
    private String operationType;
    private DNKRecord record;
    private List<String> desiredKeys;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public DNKRecord getRecord() {
        return record;
    }

    public void setRecord(DNKRecord record) {
        this.record = record;
    }

    public List<String> getDesiredKeys() {
        return desiredKeys;
    }

    public void setDesiredKeys(List<String> desiredKeys) {
        this.desiredKeys = desiredKeys;
    }
}
