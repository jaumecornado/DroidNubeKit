package net.moddity.droidnubekit;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.moddity.droidnubekit.errors.DNKErrorHandler;
import net.moddity.droidnubekit.interfaces.CloudKitService;
import net.moddity.droidnubekit.requests.DNKCallback;
import net.moddity.droidnubekit.requests.DNKRecordQueryRequest;
import net.moddity.droidnubekit.responsemodels.DNKRecord;
import net.moddity.droidnubekit.responsemodels.DNKRecordsResponse;
import net.moddity.droidnubekit.responsemodels.DNKZone;
import net.moddity.droidnubekit.ui.DNKWebViewAuthActivity;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jaume Cornadó on 11/6/15.
 */
public class DroidNubeKit {

    /** The singleton instance */
    private static DroidNubeKit instance;

    /** The CloudKit Service reference */
    private CloudKitService cloudKitService;

    /** The API Token */
    private String apiToken;

    /** If it's production or development */
    private DroidNubeKitConstants.kEnvironmentType environmentType;

    private String appContainerIdentifier;

    private Context context;

    /**
     * Initializes the CloudKit Service
     */
    private DroidNubeKit() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(DroidNubeKitConstants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setErrorHandler(new DNKErrorHandler())
                .build();

        cloudKitService = restAdapter.create(CloudKitService.class);
    }

    private static DroidNubeKit getInstance() {
        if(instance == null)
            instance = new DroidNubeKit();
        return instance;
    }

    //----------------------
    // Public methods
    //----------------------

    /**
     * The method to initialize the library
     * @param apiToken Api Token obtained on CloudKit dashboard
     * @param appContainerIdentifier Your App Container identifier iCloud.net.moddity.yourapp (similar to this)
     * @param environmentType development / production environment
     * @param context Pass a context to the lib
     */
    public static void initNube(String apiToken, String appContainerIdentifier, DroidNubeKitConstants.kEnvironmentType environmentType, Context context) {
        DroidNubeKit.getInstance().apiToken = apiToken;
        DroidNubeKit.getInstance().environmentType = environmentType;
        DroidNubeKit.getInstance().appContainerIdentifier = appContainerIdentifier;
        DroidNubeKit.getInstance().context = context;
    }

    public static void fetchRecordsByQuery(DNKRecordQueryRequest queryRequest, DroidNubeKitConstants.kDatabaseType databaseType) {
        DroidNubeKit.getInstance().cloudKitService.queryRecords(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                databaseType.toString(),
                queryRequest,
                DroidNubeKit.getInstance().apiToken,
                new DNKCallback<DNKRecordsResponse>() {
                    @Override
                    public void success(DNKRecordsResponse recordsResponse, Response response) {
                        super.success(recordsResponse, response);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                    }
                }
        );
    }

    public static void getZones(DroidNubeKitConstants.kDatabaseType databaseType) {
        DroidNubeKit.getInstance().cloudKitService.getZones(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                databaseType.toString(),
                DroidNubeKit.getInstance().apiToken,
                new DNKCallback<List<DNKZone>>() {
                    @Override
                    public void success(List<DNKZone> zones, Response response) {
                        super.success(zones, response);
                        Log.d("CK", zones.toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                        Log.d("CK", "Error: "+error.getLocalizedMessage());
                    }
                }
        );
    }

    /**
     * Gets the current context of the library
     * @return A reference to the context passed to the library
     */
    public Context getContext() {
        return context;
    }

    /**
     * You must declare DNKWebViewAuthActivity on the AndroidManifest.xml of your application
     * @param redirectURL redirect url provided by CloudKit
     */
    public static void showAuthDialog(String redirectURL) {
        Intent intent = new Intent(DroidNubeKit.getInstance().getContext(), DNKWebViewAuthActivity.class);
        intent.putExtra(DroidNubeKitConstants.WEBVIEW_REDIRECT_URL_EXTRA, redirectURL);
        DroidNubeKit.getInstance().getContext().startActivity(intent);
    }
}
