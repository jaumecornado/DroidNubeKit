package net.moddity.droidnubekit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.moddity.droidnubekit.annotations.RecordType;
import net.moddity.droidnubekit.errors.DNKErrorHandler;
import net.moddity.droidnubekit.errors.DNKException;
import net.moddity.droidnubekit.interfaces.CloudKitService;
import net.moddity.droidnubekit.interfaces.CloudKitWebViewRedirectHandler;
import net.moddity.droidnubekit.interfaces.DNKCloudKitAuth;
import net.moddity.droidnubekit.objects.DNKObject;
import net.moddity.droidnubekit.requests.DNKCallback;
import net.moddity.droidnubekit.requests.DNKObjectProcessingCallback;
import net.moddity.droidnubekit.requests.DNKRecordLookupRequest;
import net.moddity.droidnubekit.requests.DNKRecordModifyRequest;
import net.moddity.droidnubekit.requests.DNKRecordQueryRequest;

import net.moddity.droidnubekit.responsemodels.DNKRecord;
import net.moddity.droidnubekit.responsemodels.DNKRecordField;
import net.moddity.droidnubekit.responsemodels.DNKRecordsResponse;
import net.moddity.droidnubekit.responsemodels.DNKUser;
import net.moddity.droidnubekit.responsemodels.DNKZone;
import net.moddity.droidnubekit.ui.DNKWebViewAuthActivity;
import net.moddity.droidnubekit.utils.DNKOperationType;
import net.moddity.droidnubekit.utils.DNKRecordFieldDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dalvik.system.DexFile;
import retrofit.Callback;

import retrofit.RequestInterceptor;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

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

    private DNKUser currentUser;

    public Set<Class<?>> modelClasses = new HashSet<>();


    /**
     * Initializes the CloudKit Service
     */
    private DroidNubeKit() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, DNKRecordField>>() {}.getType(), new DNKRecordFieldDeserializer());

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
                .setConverter(new GsonConverter(gsonBuilder.create()))
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
        DroidNubeKit.getInstance().checkForSession();

        try {
            DroidNubeKit.getInstance().modelClasses = DroidNubeKit.getInstance().getClasspathClasses();
        } catch (Exception e) {
            //Todo throw inizialization exception
            e.printStackTrace();
        }
    }

    /**
     * Fetch records using a query
     * @param queryRequest The query request object
     * @param environmentType development / production environment
     * @param callback callback to process the result
     */
    public static <T> void fetchRecordsByQuery(DNKRecordQueryRequest queryRequest, DroidNubeKitConstants.kDatabaseType environmentType, final DNKCallback<List<T>> callback) {
        DroidNubeKit.getInstance().cloudKitService.queryRecords(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                environmentType.toString(),
                queryRequest,
                DroidNubeKit.getInstance().apiToken,
                new DNKObjectProcessingCallback<DNKRecordsResponse, T>() {
                    @Override
                    public void success(DNKRecordsResponse dnkRecordsResponse, Response response) {
                        super.success(dnkRecordsResponse, response);
                        callback.success(getResponseObjects());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                        callback.failure(error.getCause());
                    }
                }
        );
    }

    public static <T> void modifyRecord(T object, DNKOperationType operationType,  DroidNubeKitConstants.kDatabaseType environmentType, final DNKCallback<List<T>> callback) {
        if(!(object instanceof DNKObject)) {
            callback.failure(new Exception("Object it's not instance of DNKRecord: " + object.toString()));
            return;
        }

        List<T> objects = new ArrayList<>();
        objects.add(object);

        modifyRecord(objects, operationType, environmentType, callback);
    }

    /**
     * Modify a current record
     * @param objects The record to modify
     * @param operationType Operation type. More info at: https://developer.apple.com/library/prerelease/ios/documentation/DataManagement/Conceptual/CloutKitWebServicesReference
     * @param environmentType public / private
     * @param callback callback to process the result
     */
    public static <T> void modifyRecord(List<T> objects, DNKOperationType operationType, DroidNubeKitConstants.kDatabaseType environmentType, final DNKCallback<List<T>> callback) {

        Map<String, DNKRecord> records = new HashMap<>();

        for(T object : objects) {
            if (!(object instanceof DNKObject)) {
                callback.failure(new Exception("Object it's not instance of DNKRecord: " + object.toString()));
                return;
            }

            DNKObject recordObject = (DNKObject)object;

            records.put(recordObject.toRecord().getRecordName(), recordObject.toRecord());

            for(DNKRecord descendingRecords : recordObject.getDescendingRecords()) {
                records.put(descendingRecords.getRecordName(), descendingRecords);
            }
        }

        DNKRecordModifyRequest request = DNKRecordModifyRequest.createRequest(new ArrayList<>(records.values()), operationType);

        DroidNubeKit.getInstance().cloudKitService.modifyRecords(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                environmentType.toString(),
                request,
                DroidNubeKit.getInstance().apiToken,
                new DNKObjectProcessingCallback<DNKRecordsResponse, T>() {
                    @Override
                    public void success(DNKRecordsResponse dnkRecordsResponse, Response response) {
                        super.success(dnkRecordsResponse, response);
                        callback.success(getResponseObjects());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                        callback.failure(error.getCause());
                    }
                }
        );
    }

    public static void getCurrentUser(final DNKCallback<DNKUser> callback) {
        DroidNubeKit.getInstance().cloudKitService.getCurrentUser(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                DroidNubeKit.getInstance().apiToken,
                new Callback<DNKUser>() {
                    @Override
                    public void success(DNKUser user, Response response) {
                        callback.success(user);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.failure(error.getCause());
                    }
                }
        );
    }

    public static <T> void getObjects(List<T> objects, DroidNubeKitConstants.kDatabaseType databaseType, final DNKCallback<List<T>> callback) {

        if(objects == null || objects.size() == 0)
            return;

        List<DNKRecord> records = new ArrayList<>();
        for(Object o : objects) {
            if (o instanceof DNKObject) {
                DNKObject dnkObject = (DNKObject)o;
                records.add(dnkObject.toRecord());
            }
        }

        getRecords(records, databaseType, callback);
    }

    /**
     * Fetch multiple records from record objects
     * @param records
     * @param databaseType
     * @param callback
     */
    public static <T> void getRecords(List<DNKRecord> records, DroidNubeKitConstants.kDatabaseType databaseType, final DNKCallback<List<T>> callback) {
        List<String> recordNames = new ArrayList<>();
        for(DNKRecord record : records) {
            recordNames.add(record.getRecordName());
        }
        getRecordByName(recordNames, databaseType, callback);
    }

    /**
     * Fetch multiple record by record name
     * @param recordNames
     * @param databaseType
     * @param callback
     */
    public static <T> void getRecordByName(List<String> recordNames, DroidNubeKitConstants.kDatabaseType databaseType, final DNKCallback<List<T>> callback) {
        DNKRecordLookupRequest request = DNKRecordLookupRequest.createMultipleRecordRequest(recordNames);

        DroidNubeKit.getInstance().cloudKitService.lookupRecords(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                databaseType.toString(),
                request,
                DroidNubeKit.getInstance().apiToken,
                new DNKObjectProcessingCallback<DNKRecordsResponse, T>() {
                    @Override
                    public void success(DNKRecordsResponse dnkRecordsResponse, Response response) {
                        super.success(dnkRecordsResponse, response);
                        callback.success(getResponseObjects());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                        callback.failure(error.getCause());
                    }
                }
        );
    }

    /**
     * Fetch a single record by record name
     * @param recordName
     * @param databaseType
     * @param callback
     */
    public static void getRecordByName(String recordName, DroidNubeKitConstants.kDatabaseType databaseType, final DNKCallback<DNKRecordsResponse> callback) {
        DNKRecordLookupRequest request = DNKRecordLookupRequest.createSingleRecordRequest(recordName);

        DroidNubeKit.getInstance().cloudKitService.lookupRecords(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                databaseType.toString(),
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
    public static void getZones(DroidNubeKitConstants.kDatabaseType environmentType, final DNKCallback<List<DNKZone>> callback) {
        DroidNubeKit.getInstance().cloudKitService.getZones(
                DroidNubeKitConstants.PROTOCOL,
                DroidNubeKit.getInstance().appContainerIdentifier,
                DroidNubeKit.getInstance().environmentType.toString(),
                environmentType.toString(),
                DroidNubeKit.getInstance().apiToken,
                new Callback<List<DNKZone>>() {
                    @Override
                    public void success(List<DNKZone> zones, Response response) {
                        callback.success(zones);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.failure(error.getCause());
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                saveckSession(ckSession);
            }
        }
    }

    private void checkForSession() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(DroidNubeKitConstants.CLOUDKIT_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String ckSession = sharedPreferences.getString(DroidNubeKitConstants.CLOUDKIT_SESSION_KEY, "");
        if(ckSession != null && ckSession.length() > 0) {
            DroidNubeKit.getInstance().ckSession = ckSession;
            checkSessionAlive();
        }
    }

    private void saveckSession(String ckSession) {
        DroidNubeKit.getInstance().ckSession = ckSession;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(DroidNubeKitConstants.CLOUDKIT_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DroidNubeKitConstants.CLOUDKIT_SESSION_KEY, ckSession);
        editor.commit();
        checkSessionAlive();
    }

    private void checkSessionAlive() {
        getCurrentUser(new DNKCallback<DNKUser>() {
            @Override
            public void success(DNKUser dnkUser) {
                if (DroidNubeKit.getInstance().cloudKitAuthHandler != null)
                    DroidNubeKit.getInstance().cloudKitAuthHandler.onAuthSucceed();
                currentUser = dnkUser;
            }

            @Override
            public void failure(Throwable exception) {

            }
        });
    }

    //http://bravenewgeek.com/implementing-spring-like-classpath-scanning-in-android/
    private Set<Class<?>> getClasspathClasses() throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        DexFile dex = new DexFile(getContext().getApplicationInfo().sourceDir);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<String> entries = dex.entries();
        while (entries.hasMoreElements()) {
            String entry = entries.nextElement();
            if (entry.toLowerCase().startsWith(getContext().getPackageName().toLowerCase())) {

                Class<?> clazz = classLoader.loadClass(entry);
                if(clazz.isAnnotationPresent(RecordType.class)) {
                    modelClasses.add(clazz);
                }
                classes.add(clazz);
            }
        }
        return classes;
    }
}
