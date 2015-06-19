package net.moddity.droidnubekit;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import net.moddity.droidnubekit.errors.DNKErrorHandler;
import net.moddity.droidnubekit.errors.DNKException;
import net.moddity.droidnubekit.interfaces.CloudKitService;
import net.moddity.droidnubekit.interfaces.CloudKitWebViewRedirectHandler;
import net.moddity.droidnubekit.interfaces.DNKCloudKitAuth;
import net.moddity.droidnubekit.requests.DNKCallback;
import net.moddity.droidnubekit.requests.DNKRecordQueryRequest;
import net.moddity.droidnubekit.responsemodels.DNKRecordsResponse;
import net.moddity.droidnubekit.responsemodels.DNKZone;
import net.moddity.droidnubekit.ui.DNKWebViewAuthActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jaume Cornadó on 11/6/15.
 */
public class DroidNubeKit implements CloudKitWebViewRedirectHandler {

    /** The singleton instance */
    private static DroidNubeKit instance;

    /** The CloudKit Service reference */
    private CloudKitService cloudKitService;

    /** The API Token */
    private String apiToken;

    /** Session on auth */
    private String ckSession;

    /** If it's production or development */
    private DroidNubeKitConstants.kEnvironmentType environmentType;

    private String appContainerIdentifier;

    private Context context;

    /** External redirect handler to control authentication */
    private DNKCloudKitAuth cloudKitAuthHandler;

    /**
     * Initializes the CloudKit Service
     */
    private DroidNubeKit() {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                if(ckSession != null)
                    request.addQueryParam("ckSession", ckSession);

            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(DroidNubeKitConstants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor)
                .setErrorHandler(new DNKErrorHandler())
                .build();

        cloudKitService = restAdapter.create(CloudKitService.class);
    }

    /**
     * Singleton instance
     * @return the instance
     */
    public static DroidNubeKit getInstance() {
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

    public static void fetchRecordsByQuery(DNKRecordQueryRequest queryRequest, DroidNubeKitConstants.kDatabaseType databaseType, final DNKCallback<DNKRecordsResponse> callback) {
        DroidNubeKit.getInstance().cloudKitService.queryRecords(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                databaseType.toString(),
                queryRequest,
                DroidNubeKit.getInstance().apiToken,
                new Callback<DNKRecordsResponse>() {
                    @Override
                    public void success(DNKRecordsResponse recordsResponse, Response response) {
                        callback.success(recordsResponse);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.failure((DNKException)error.getCause());
                    }
                }
        );
    }

    public static void getZones(DroidNubeKitConstants.kDatabaseType databaseType, final DNKCallback<List<DNKZone>> callback) {
        DroidNubeKit.getInstance().cloudKitService.getZones(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                databaseType.toString(),
                DroidNubeKit.getInstance().apiToken,
                new Callback<List<DNKZone>>() {
                    @Override
                    public void success(List<DNKZone> dnkZones, Response response) {
                        callback.success(dnkZones);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.failure((DNKException) error.getCause());
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
        intent.putExtra(DroidNubeKitConstants.WEBVIEW_REDIRECT_PATTERN_EXTRA, DroidNubeKitConstants.WEBVIEW_REDIRECT_URL_PREFIX+DroidNubeKit.getInstance().appContainerIdentifier.toLowerCase());
        DroidNubeKit.getInstance().getContext().startActivity(intent);
    }

    /**
     * Defines an Auth Handler interface to control the state of CloudKit authentication
     * @param authHandler
     */
    public static void setCloudKitAuthHandler(DNKCloudKitAuth authHandler) {
        DroidNubeKit.getInstance().cloudKitAuthHandler = authHandler;
    }

    @Override
    public void onRedirectFound(Uri redirectUri) {
        if(DroidNubeKitConstants.WEBVIEW_REDIRECT_LOGIN_ENDPOINT.equals(redirectUri.getHost())) {
            String ckSession = redirectUri.getQueryParameter("ckSession");
            if(ckSession != null) {
                DroidNubeKit.getInstance().ckSession = ckSession;

                if(DroidNubeKit.getInstance().cloudKitAuthHandler != null)
                    DroidNubeKit.getInstance().cloudKitAuthHandler.onAuthSucceed();
            }
        }
    }
}
