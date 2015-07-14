package net.moddity.droidnubekit.interfaces;

import net.moddity.droidnubekit.requests.DNKCallback;
import net.moddity.droidnubekit.requests.DNKRecordModifyRequest;
import net.moddity.droidnubekit.requests.DNKRecordQueryRequest;
import net.moddity.droidnubekit.responsemodels.DNKRecord;
import net.moddity.droidnubekit.responsemodels.DNKRecordsResponse;
import net.moddity.droidnubekit.responsemodels.DNKZone;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jaume on 11/6/15.
 */
public interface CloudKitService {
    @GET("/database/{version}/{container}/{environment}/{database}/zones/list")
    void getZones(@Path("version") String version, @Path("container") String container, @Path("environment") String environment, @Path("database") String database, @Query("ckAPIToken") String ckAPIToken, Callback<List<DNKZone>> callback);
    @POST("/database/{version}/{container}/{environment}/{database}/records/query")
    void queryRecords(@Path("version") String version, @Path("container") String container, @Path("environment") String environment, @Path("database") String database, @Body DNKRecordQueryRequest recordQueryRequest, @Query("ckAPIToken") String ckAPIToken, Callback<DNKRecordsResponse> callback);
    @POST("/database/{version}/{container}/{environment}/{database}/records/modify")
    void modifyRecords(@Path("version") String version, @Path("container") String container, @Path("environment") String environment, @Path("database") String database, @Body DNKRecordModifyRequest recordModifyRequest, @Query("ckAPIToken") String ckAPIToken, Callback<DNKRecordsResponse> callback);

}
