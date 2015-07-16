package net.moddity.droidnubekit.responsemodels;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaume on 12/6/15.
 */
public class DNKRecord {
    
    private String recordName;
    private String recordType;
    private String recordChangeTag;

    @SerializedName("fields")
    private Map<String, DNKRecordField> fields = new HashMap<>();

    private Boolean deleted;

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getRecordChangeTag() {
        return recordChangeTag;
    }

    public void setRecordChangeTag(String recordChangeTag) {
        this.recordChangeTag = recordChangeTag;
    }

    public Map<String, DNKRecordField> getFields() {
        return fields;
    }

    public void setFields(Map<String, DNKRecordField> fields) {
        this.fields = fields;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
