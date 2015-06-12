package net.moddity.droidnubekit.interfaces;

import net.moddity.droidnubekit.responsemodels.Zone;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by jaume on 11/6/15.
 */
public interface CloudKitService {
    @GET("/database/{version}/{container}/{environment}/{database}/zones/list")
    List<Zone> getZones(@Path("version") String version, @Path("container") String container, @Path("environment") String environment, @Path("database") String database);
}
