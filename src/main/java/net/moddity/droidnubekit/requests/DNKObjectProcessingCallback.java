package net.moddity.droidnubekit.requests;

import net.moddity.droidnubekit.objects.DNKAnnotationParser;
import net.moddity.droidnubekit.responsemodels.DNKRecord;
import net.moddity.droidnubekit.responsemodels.DNKRecordsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jaume on 16/7/15.
 */
public class DNKObjectProcessingCallback<T, K> implements Callback<T> {

    private List<K> responseObjects;

    @Override
    public void success(T t, Response response) {
        if(t.getClass().equals(DNKRecordsResponse.class)) {
            try {
                DNKRecordsResponse recordsResponse = (DNKRecordsResponse) t;

                responseObjects = new ArrayList<>();

                DNKAnnotationParser annotationParser = new DNKAnnotationParser();

                for (DNKRecord record : recordsResponse.getRecords()) {
                    K destinationObject = annotationParser.recordToObject(record);
                    responseObjects.add(destinationObject);
                }

            } catch(Exception e) {
                failure(null);
            }
        }
    }

    @Override
    public void failure(RetrofitError error) {

    }

    public List<K> getResponseObjects() {
        return responseObjects;
    }
}
