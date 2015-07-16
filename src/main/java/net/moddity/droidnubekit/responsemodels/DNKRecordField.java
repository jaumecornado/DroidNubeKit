package net.moddity.droidnubekit.responsemodels;

import net.moddity.droidnubekit.utils.DNKFieldTypes;

/**
 * Created by jaume on 13/7/15.
 */
public class DNKRecordField<T> {
    private T value;
    private DNKFieldTypes type;

    public DNKRecordField(DNKFieldTypes type, T value) {
        this.type = type;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public DNKFieldTypes getType() {
        return type;
    }

    public void setType(DNKFieldTypes type) {
        this.type = type;
    }

}
