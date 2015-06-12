package moddity.net.droidnubekit.interfaces;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by jaume on 11/6/15.
 */
public interface CloudKitService {
    @GET("/database/{version}/{container}/[environment]/[database]/zones/list")
    List<Zone> listRepos(@Path("user") String user);
}
