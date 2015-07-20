package net.moddity.droidnubekit.objects;

import android.content.Context;

import junit.framework.Test;

import net.moddity.droidnubekit.DroidNubeKit;
import net.moddity.droidnubekit.annotations.CKField;
import net.moddity.droidnubekit.annotations.CKReference;
import net.moddity.droidnubekit.annotations.RecordName;
import net.moddity.droidnubekit.annotations.RecordType;
import net.moddity.droidnubekit.errors.DNKRecordConversionException;
import net.moddity.droidnubekit.responsemodels.DNKRecord;
import net.moddity.droidnubekit.responsemodels.DNKRecordField;
import net.moddity.droidnubekit.responsemodels.DNKReference;
import net.moddity.droidnubekit.utils.DNKFieldTypes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dalvik.system.DexFile;

/**
 * Created by jaume on 16/7/15.
 */
public class DNKAnnotationParser {
    public void parseToRecord(Object owner, DNKRecord record) throws DNKRecordConversionException {
        Class clazz = owner.getClass();

        if(clazz.isAnnotationPresent(RecordType.class)) {
            RecordType recordType = (RecordType)clazz.getAnnotation(RecordType.class);
            record.setRecordType(recordType.value());
        }

        for(Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if(field.isAnnotationPresent(RecordName.class)) {
                try {
                    record.setRecordName((String) field.get(owner));
                } catch(IllegalAccessException e) {
                    DNKRecordConversionException exception = new DNKRecordConversionException("Error accessing recordName field "+field.getName());
                    throw exception;
                }
            }

            if(field.isAnnotationPresent(CKField.class)) {
                try {
                    CKField ckField = field.getAnnotation(CKField.class);

                    DNKRecordField<?> recordField = null;

                    switch (ckField.value()) {
                        case STRING:
                            recordField = new DNKRecordField<String>(DNKFieldTypes.STRING, (String) field.get(owner));
                            break;
                        case REFERENCE:
                            DNKObject target = (DNKObject) field.get(owner);
                            if(target == null)
                                continue;
                            String recordName = getRecordNameFromAnnotation(target);
                            DNKReference reference = new DNKReference();
                            reference.setRecordName(recordName);
                            reference.setAction("NONE");
                            recordField = new DNKRecordField<DNKReference>(DNKFieldTypes.REFERENCE, reference);
                            break;
                        case REFERENCE_LIST:
                            recordField = new DNKRecordField<>(DNKFieldTypes.REFERENCE_LIST, (List<DNKReference>) field.get(owner));
                            break;
                    }

                    record.getFields().put(field.getName(), recordField);

                } catch(IllegalAccessException e) {
                    DNKRecordConversionException exception = new DNKRecordConversionException("Error accessing record field "+field.getName());
                    throw exception;
                }
            }

            if(field.isAnnotationPresent(CKReference.class)) {

                List<DNKReference> references = new ArrayList<>();

                try {
                    List<Object> objects = (List<Object>)field.get(owner);

                    for(Object target : objects) {
                        DNKReference newReference = new DNKReference();
                        newReference.setRecordName(getRecordNameFromAnnotation(target));
                        newReference.setAction("NONE");
                        references.add(newReference);
                    }

                    DNKRecordField<List<DNKReference>> referenceField = new DNKRecordField<>(DNKFieldTypes.REFERENCE_LIST, references);

                    record.getFields().put(field.getName(), referenceField);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public <T> T recordToObject(DNKRecord record) throws DNKRecordConversionException {
        try {

            T newObject = null;

            Class<?> clazz = null;

            for(Class<?> cl : DroidNubeKit.getInstance().modelClasses) {
                if(cl.isAnnotationPresent(RecordType.class)) {
                    RecordType recordType = (RecordType)cl.getAnnotation(RecordType.class);
                    if(record.getRecordType().equals(recordType.value())) {
                        newObject = (T)cl.newInstance();
                        clazz = cl;
                    }
                }
            }

            if(clazz == null)
                throw new DNKRecordConversionException("No model class found for this record: "+record.getRecordType());

            for(Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(RecordName.class)) {
                    try {
                        field.set(newObject, record.getRecordName());
                    } catch (IllegalAccessException e) {
                        DNKRecordConversionException exception = new DNKRecordConversionException("Error accessing recordName field " + field.getName());
                        throw exception;
                    }
                }
            }

            for(String fieldName : record.getFields().keySet()) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    DNKRecordField recordField = record.getFields().get(fieldName);

                    if(recordField == null)
                        continue;

                    switch (recordField.getType()) {
                        case STRING:
                            field.set(newObject, (String)recordField.getValue());
                            break;
                        case REFERENCE_LIST:
                            CKReference ckReference = field.getAnnotation(CKReference.class);

                            List referencedObjects = new ArrayList<>();

                            for(DNKReference reference : (List<DNKReference>)recordField.getValue()) {
                                Object newRelatedObject = ckReference.value().newInstance();
                                setRecordName(reference.getRecordName(), newRelatedObject);
                                referencedObjects.add(newRelatedObject);
                            }

                            field.set(newObject, referencedObjects);

                            break;
                    }

                } catch(NoSuchFieldException nsf) {

                }
            }


            return newObject;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getRecordNameFromAnnotation(Object target) throws DNKRecordConversionException {
        Class clazz = target.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(RecordName.class)) {
                try {
                    return (String) field.get(target);
                } catch (IllegalAccessException e) {
                    DNKRecordConversionException exception = new DNKRecordConversionException("Error accessing recordName field " + field.getName());
                    throw exception;
                }
            }
        }
        return null;
    }


    private void setRecordName(String recordName, Object target) throws DNKRecordConversionException {
        Class clazz = target.getClass();
        for(Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(RecordName.class)) {
                try {
                    field.set(target, recordName);
                } catch (IllegalAccessException e) {
                    DNKRecordConversionException exception = new DNKRecordConversionException("Error accessing recordName field " + field.getName());
                    throw exception;
                }
            }
        }
    }


}
