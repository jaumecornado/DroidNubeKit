package net.moddity.droidnubekit.responsemodels;

import net.moddity.droidnubekit.utils.DNKFieldTypes;

/**
 * Created by jaume on 13/7/15.
 */
public class DNKRecordField<T> {
    private Object value;
    private DNKFieldTypes type;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DNKFieldTypes getType() {
        return type;
    }

    public void setType(DNKFieldTypes type) {
        this.type = type;
    }

}
