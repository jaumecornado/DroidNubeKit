package net.moddity.droidnubekit.objects;

import net.moddity.droidnubekit.annotations.CKReference;
import net.moddity.droidnubekit.annotations.RecordName;
import net.moddity.droidnubekit.errors.DNKRecordConversionException;
import net.moddity.droidnubekit.responsemodels.DNKRecord;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jaume on 16/7/15.
 */
public abstract class DNKObject implements Serializable {

    private DNKRecord record;

    public DNKObject() {
        record = new DNKRecord();
        setDefaultUUID();
    }

    public DNKRecord toRecord() {
        DNKAnnotationParser annotationParser = new DNKAnnotationParser();
        try {
            annotationParser.parseToRecord(this, record);
        } catch (DNKRecordConversionException e) {
            e.printStackTrace();
            return null;
        }
        return record;
    }

    public List<DNKRecord> getDescendingRecords() {
        List<DNKRecord> records = new ArrayList<>();
        for(Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.isAnnotationPresent(CKReference.class)) {
                try {

                    List<DNKObject> references = (List<DNKObject>)field.get(this);

                    for(DNKObject object : references) {
                        records.add(object.toRecord());
                        List<DNKRecord> descendants = object.getDescendingRecords();
                        if(descendants.size() > 0)
                            records.addAll(descendants);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return records;
    }

    private void setDefaultUUID() {
        for(Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.isAnnotationPresent(RecordName.class)) {
                try {
                    field.set(this, UUID.randomUUID().toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
