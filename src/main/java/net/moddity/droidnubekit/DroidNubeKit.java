package net.moddity.droidnubekit;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.moddity.droidnubekit.errors.DNKErrorHandler;
import net.moddity.droidnubekit.interfaces.CloudKitService;
import net.moddity.droidnubekit.requests.DNKCallback;
import net.moddity.droidnubekit.requests.DNKRecordModifyRequest;
import net.moddity.droidnubekit.requests.DNKRecordQueryRequest;
import net.moddity.droidnubekit.responsemodels.DNKRecord;
import net.moddity.droidnubekit.responsemodels.DNKRecordField;
import net.moddity.droidnubekit.responsemodels.DNKRecordsResponse;
import net.moddity.droidnubekit.responsemodels.DNKZone;
import net.moddity.droidnubekit.ui.DNKWebViewAuthActivity;
import net.moddity.droidnubekit.utils.DNKOperationType;
import net.moddity.droidnubekit.utils.DNKRecordFieldDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

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

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, DNKRecordField>>() {}.getType(), new DNKRecordFieldDeserializer());

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(DroidNubeKitConstants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gsonBuilder.create()))
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

    /**
     * Fetch records using a query
     * @param queryRequest The query request object
     * @param environmentType development / production environment
     * @param callback callback to process the result
     */
    public static void fetchRecordsByQuery(DNKRecordQueryRequest queryRequest, DroidNubeKitConstants.kDatabaseType environmentType, final DNKCallback<DNKRecordsResponse> callback) {
        DroidNubeKit.getInstance().cloudKitService.queryRecords(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                environmentType.toString(),
                queryRequest,
                DroidNubeKit.getInstance().apiToken,
                new Callback<DNKRecordsResponse>() {
                    @Override
                    public void success(DNKRecordsResponse recordsResponse, Response response) {
                        callback.success(recordsResponse);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.failure(error.getCause());
                    }
                }
        );
    }

    public static void createOrUpdateRecord(DNKRecord record, DroidNubeKitConstants.kDatabaseType environmentType, final DNKCallback<DNKRecordsResponse> callback) {

        DNKRecordModifyRequest request = DNKRecordModifyRequest.createRequest(record, DNKOperationType.FORCE_UPDATE);

        DroidNubeKit.getInstance().cloudKitService.modifyRecords(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                environmentType.toString(),
                request,
                DroidNubeKit.getInstance().apiToken,
                new Callback<DNKRecordsResponse>() {
                    @Override
                    public void success(DNKRecordsResponse dnkRecordsResponse, Response response) {
                        callback.success(dnkRecordsResponse);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.failure(error.getCause());
                    }
                }
        );
    }

    /**
     * Fetches all the zones and their sync tokens in the specified database
     * @param environmentType development / production environment
     */
    public static void getZones(DroidNubeKitConstants.kDatabaseType environmentType) {
        DroidNubeKit.getInstance().cloudKitService.getZones(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                environmentType.toString(),
                DroidNubeKit.getInstance().apiToken,
                new Callback<List<DNKZone>>() {
                    @Override
                    public void success(List<DNKZone> zones, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

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
